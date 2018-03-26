package com.example.utilisateur.wowapi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.utilisateur.wowapi.AdapterListView.ItemsRecyclerAdapter;
import com.example.utilisateur.wowapi.ConverterJSON.JSONConverter;
import com.example.utilisateur.wowapi.SQLite.DatabaseHandler;
import com.example.utilisateur.wowapi.R;
import com.example.utilisateur.wowapi.RecyclerViewClickListener;
import com.example.utilisateur.wowapi.RecyclerViewTouchListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.example.utilisateur.wowapi.entity.Item;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressebar ;EditText nameItem;
    TextView TextServeur;
    RecyclerView listView1;
    List<Item> Items  = new ArrayList<>();
    List<Item> ItemsFav  = new ArrayList<>();
    DatabaseHandler db = null;
    ItemsRecyclerAdapter adapters = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameItem = (EditText) findViewById(R.id.NameItem);
        listView1 = (RecyclerView) findViewById(R.id.listview);
        TextServeur = (TextView) findViewById(R.id.TextServeur);
        progressebar = (ProgressBar)  findViewById(R.id.ProgresseBar);
        progressebar.setVisibility(View.GONE);
        db = new DatabaseHandler(this);

        ImageButton button= (ImageButton) findViewById(R.id.buttonChangerServeur);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListeRealms.class);
                startActivity(intent);
            }
        });
        nameItem.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence s, int start, int before,int count) {
                String NameItem  = s.toString();
                int tailleNameItem = NameItem.length();
                String NameItemRecherche  = s.toString();
                if (tailleNameItem > 0){
                    NameItem= NameItem.substring(0,1);
                    NameItem=NameItem.toUpperCase();
                }


                if (tailleNameItem > 1){
                    NameItem=  NameItem.concat(NameItemRecherche.substring(1));
                }
                ArrayList<Item> myQuoteList = new ArrayList<Item>();
                ItemsRecyclerAdapter adapter = new ItemsRecyclerAdapter(MainActivity.this, R.layout.item_item, myQuoteList);
                listView1.setAdapter(adapter);
                if (!s.toString().equals("")) {
                    progressebar.setVisibility(View.VISIBLE);
                    AfficheRechercheNom(NameItem);

                }else{

                  AffichePreferenceNameItem();

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {



            }


        });

        listView1.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), listView1, new RecyclerViewClickListener() {

            @Override
            public void onClick(View view, int position) {

                String idItem = Integer.toString(Items.get(position).getId());
                Item Item =db.getItemId(Items.get(position).getId());
                if(Item == null){
                    db.addItem(new Item(Items.get(position).getName(), Items.get(position).getId(),Items.get(position).getImage()));

                }

                Intent intent = new Intent(MainActivity.this, ListeAuctions.class);
                intent.putExtra("idItem",idItem);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
                showItemDescription(position);


            }
        }));
        VerifServeurName();
        AffichePreferenceNameItem();








    }
private void showItemDescription(int position){
// Création du pupup
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
        View layout = inflater.inflate(R.layout.item_description,null);
        //Get the devices screen density to calculate correct pixel sizes
        float density=MainActivity.this.getResources().getDisplayMetrics().density;
        // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(layout, (int)density*350, (int)density*300, true);

        //Set up touch closing outside of pop-up
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.setOutsideTouchable(true);
        // display the pop-up in the center
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
    // Alimentation de la pupup
    //Affichage de l'image
    ImageView icon = (ImageView) layout.findViewById(R.id.ImageItem);
    String URL = String.valueOf(Items.get(position).getImage());
    icon.setTag(URL);
    AsyncTaskImagePopup AsyncTask=new AsyncTaskImagePopup();
    if(AsyncTask.getStatus().equals(AsyncTaskImagePopup.Status.RUNNING))
    {
        AsyncTask.cancel(true);
    }
    AsyncTask.execute(icon);

    //Affichage des texts
    TextView Name = (TextView) layout.findViewById(R.id.ItemName);
    TextView description = (TextView) layout.findViewById(R.id.description);
    TextView stack = (TextView) layout.findViewById(R.id.Stack);
    TextView prixGold = (TextView) layout.findViewById(R.id.PrixGold);
    TextView prixSilver = (TextView) layout.findViewById(R.id.PrixSilver);

    TextView prixBronze = (TextView) layout.findViewById(R.id.PrixBronze);

    TextView lvl = (TextView) layout.findViewById(R.id.itemLVL);

    Name.setText(Items.get(position).getName());
    description.setText(Items.get(position).getDescription());

    stack.setText("Empilement "+Items.get(position).getStack());


    double prixItem = Items.get(position).getPrix();

    double PrixGold =prixItem/10000;
    int Gold = (int)PrixGold;
    prixGold.setText(Integer.toString(Gold));

    double PrixSilver =(prixItem-Gold*10000)/100;
    int Silver = (int)PrixSilver;
    prixSilver.setText(Integer.toString(Silver));

    double PrixBronze =prixItem-Gold*10000-Silver*100;
    int Bronze = (int)PrixBronze;
    prixBronze.setText(Integer.toString(Bronze));

    lvl.setText("Niveau d'objet "+Items.get(position).getItemLvl());


}

    public void AfficheRechercheNom(String s){
        OkHttpClient client = new OkHttpClient();
        client.dispatcher().cancelAll();

        try {
             s = URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url("http://91.160.186.17:3000/itemName")
                .get()
                .addHeader("item", s)

                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String text = call.toString();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Aucune connexion internet", Toast.LENGTH_LONG).show();
                        progressebar.setVisibility(View.GONE);

                    }
                });


                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String text = response.body().string();

               // Affichereponse(text);
                if (!response.isSuccessful()) {

                    throw new IOException("Unexpected code " + response);

                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        listView1.setAdapter(null);
                        Items = JSONConverter.convertItems(text);
                        adapters = new ItemsRecyclerAdapter(MainActivity.this, R.layout.item_item, Items);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        listView1.setLayoutManager(mLayoutManager);
                        listView1.setItemAnimator(new DefaultItemAnimator());
                        listView1.setAdapter(adapters);
                        progressebar.setVisibility(View.GONE);
                    }
                });

                // you code to handle response
            }}
);
    }
    public void AffichePreferenceNameItem(){

            Items = db.getAllContacts();

            adapters = new ItemsRecyclerAdapter(MainActivity.this, R.layout.item_item, Items);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            listView1.setLayoutManager(mLayoutManager);
            listView1.setItemAnimator(new DefaultItemAnimator());
            listView1.setAdapter(adapters);
        }
    public void VerifServeurName(){
        SharedPreferences prefsd = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String ServeurName = prefsd.getString("Realm", "");
        if(ServeurName.equals("") ) {
            Intent intent = new Intent(MainActivity.this, ListeRealms.class);
            startActivity(intent);
        }else{
            TextServeur.setText("Serveur : "+ServeurName);
        }}





    class AsyncTaskImagePopup extends AsyncTask<ImageView, Void, Bitmap> {
        ImageView imageView = null;
        String urlImg = "";
        public void returnNameImg(){
            this.urlImg = "http://wow.zamimg.com/images/wow/icons/large/"+imageView.getTag().toString()+".jpg";
        }
        @Override
        protected Bitmap doInBackground(ImageView... imageViews){
            this.imageView = imageViews[0];
            returnNameImg();
            HttpURLConnection httpConn = null;
            int resCode = 0;
            Bitmap bitmap = null;
            try {
                URL url = new URL(urlImg);
                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.connect();
                resCode = httpConn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = httpConn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);


                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
        protected void onPostExecute(Bitmap result){

            imageView.setImageBitmap(result);
        }
    }}
