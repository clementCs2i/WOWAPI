package com.example.utilisateur.wowapi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.io.UnsupportedEncodingException;
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


                ArrayList<Item> myQuoteList = new ArrayList<Item>();
                ItemsRecyclerAdapter adapter = new ItemsRecyclerAdapter(MainActivity.this, R.layout.item_item, myQuoteList);
                listView1.setAdapter(adapter);
                if (!s.toString().equals("")) {
                    progressebar.setVisibility(View.VISIBLE);
                    AfficheRechercheNom(s.toString());

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
                Boolean ItemPresent = false;
                String idItem = Integer.toString(Items.get(position).getId());
                Item Item =db.getItemId(Items.get(position).getId());
                if(Item ==null){
                    db.addItem(new Item(Items.get(position).getName(), Items.get(position).getId(),Items.get(position).getImage()));

                }

                Intent intent = new Intent(MainActivity.this, ListeAuctions.class);
                intent.putExtra("idItem",idItem);
                startActivity(intent);





            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getApplicationContext(),  " is long pressed!", Toast.LENGTH_SHORT).show();

            }
        }));
        VerifServeurName();
        AffichePreferenceNameItem();


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

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params){
        OkHttpClient client = new OkHttpClient();
        // client.dispatcher().cancelAll();

        Request request = new Request.Builder()
                .url("http://91.160.186.17:3000/itemName")
                .get()
                .addHeader("item", "R")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String text = response.body().string();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();




                    }
                });

                // you code to handle response
            }}
        );

        return "";
    }
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}}
