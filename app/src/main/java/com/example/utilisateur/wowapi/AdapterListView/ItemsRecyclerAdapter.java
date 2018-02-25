package com.example.utilisateur.wowapi.AdapterListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.utilisateur.wowapi.R;
import com.example.utilisateur.wowapi.activity.MainActivity;
import com.example.utilisateur.wowapi.entity.Auctions;
import com.example.utilisateur.wowapi.entity.Item;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Hervé on 03/04/2017.
 */

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder> {

    // ---
    // CHAMPS
    // ---

    private Context mContext;
    private int mItemLayout;
    private List<Item> mItems;


    // ---
    // CONSTRUCTEUR
    // ---

    public ItemsRecyclerAdapter(Context context, int itemLayout, List<Item> items) {
        mContext = context;
        mItemLayout = itemLayout;
        mItems = items;
    }



    // ---
    // METHODES DE L'ADAPTER
    // ---

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Gonfler une vue à partir d'un layout XML
        View view = LayoutInflater.from(mContext).inflate(mItemLayout, parent, false);

        // Renvoyer le ViewHolder basé sur la vue
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Récupérer le taux demandé
        Item Items = mItems.get(position);

        // Transférer les valeurs de taux dans le ViewHolder
        holder.nameView.setText(Items.getName());

        ImageView icon = holder.image;
        String URL = String.valueOf(Items.getImage());
        icon.setTag(URL);
        AsyncTaskRunner calcul=new AsyncTaskRunner();
        if(calcul.getStatus().equals(AsyncTask.Status.RUNNING))
        {
            calcul.cancel(true);
        }
        calcul.execute(icon);



    }
    public void annulAsynctask (){


    }

    /**
     * Nombre d'éléments gérés par l'adapter
     * @return  Nombre de taux
     */

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // -------------
    // Le ViewHolder
    // -------------
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Les vues d'une bande horizontale

        //@BindView(R.id.rate_code)
        TextView nameView;
        //@BindView(R.id.rate_value)


        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            // Instancier les 2 TextViews : version classique
            nameView = (TextView)itemView.findViewById(R.id.item);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            // Instancier les 2 TextViews : version ButterKnife
           // ButterKnife.bind(this, itemView);
        }
    }

}
 class AsyncTaskRunner extends AsyncTask<ImageView, Void, Bitmap> {
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
}

