package com.example.utilisateur.wowapi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.utilisateur.wowapi.AdapterListView.AuctionsRecyclerAdapter;
import com.example.utilisateur.wowapi.ConverterJSON.JSONConverter;
import com.example.utilisateur.wowapi.HorizontalLineItemDecoration;
import com.example.utilisateur.wowapi.R;
import com.example.utilisateur.wowapi.entity.Auctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Utilisateur on 14/02/2018.
 */

public class ListeAuctions extends AppCompatActivity {
    ProgressBar progressebar ;
    RecyclerView ListAuction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auctions);
        final Intent intent = getIntent();

        progressebar = (ProgressBar)  findViewById(R.id.ProgresseBar);
        progressebar.setVisibility(View.VISIBLE);

        String idItem = intent.getStringExtra("idItem");
        ListAuction = (RecyclerView) findViewById(R.id.rate_list);
        SharedPreferences prefsd = PreferenceManager.getDefaultSharedPreferences(ListeAuctions.this);
        String ServeurName = prefsd.getString("Realm", "");
        AfficheAutoComplete(ServeurName,idItem);

    }
    public void AfficheAutoComplete(String s,String i){



        OkHttpClient client = new OkHttpClient();
        client.dispatcher().cancelAll();
        try {
            s = URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url("http://91.160.186.17:3000/auctions")
                .get()
                .addHeader("server", s)
                .addHeader("item", i)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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

                        List<Auctions> auctionses = JSONConverter.convertAuctions(text);

                        // Adapter et ListView
                        AuctionsRecyclerAdapter adapter = new AuctionsRecyclerAdapter(ListeAuctions.this, R.layout.item_auctions, auctionses);
                        ListAuction.setAdapter(adapter);

                        // Définir le layout de RecyclerView
                        // rateList.setLayoutManager(new LinearLayoutManager(this));
                        ListAuction.setLayoutManager(new GridLayoutManager(ListeAuctions.this, 1));
                        ListAuction.addItemDecoration(new HorizontalLineItemDecoration(Color.GRAY));
                        progressebar.setVisibility(View.GONE);

                    }
                });

                // you code to handle response
            }}
        );
    }
    public String RechercheItem(String s){


        final String[] idItem = {""};
        OkHttpClient client = new OkHttpClient();
        client.dispatcher().cancelAll();
        try {
            s = URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url("http://91.160.186.17:3000/item")
                .get()
                .addHeader("item", s)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(jsonArray.getString(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            idItem[0] =  obj.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                // you code to handle response
            }}
        );
        return idItem[0];
    }

}
