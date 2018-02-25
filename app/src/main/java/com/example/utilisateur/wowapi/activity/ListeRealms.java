package com.example.utilisateur.wowapi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.utilisateur.wowapi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Utilisateur on 14/02/2018.
 */

public class ListeRealms extends AppCompatActivity {
    ListView listView1;
    ArrayAdapter<String> adapter;
    ArrayList<String> listdata = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listes_realms);
        listView1 = (ListView) findViewById(R.id.listview);



        AfficheAutoComplete();
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                String selectedItem = (String) listView1.getItemAtPosition(position);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListeRealms.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Realm", selectedItem);

                editor.commit();
                Intent intent = new Intent(ListeRealms.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }

    public void AfficheAutoComplete(){
        OkHttpClient client = new OkHttpClient();
        client.dispatcher().cancelAll();


        Request request = new Request.Builder()
                .url("http://91.160.186.17:3000/realms")
                .get()
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
                        listdata.clear();

                        listView1.setAdapter(null);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        if (jsonArray != null) {
                            for (int i=0;i<jsonArray.length();i++){
                                try {
                                    JSONObject obj = new JSONObject(jsonArray.getString(i));


                                    listdata.add(obj.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListeRealms.this,
                                android.R.layout.simple_list_item_1, listdata);
                        listView1.setAdapter(adapter);
                    }
                });

                // you code to handle response
            }}
        );
    }
}
