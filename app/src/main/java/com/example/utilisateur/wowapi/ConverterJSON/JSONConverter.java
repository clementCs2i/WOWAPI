package com.example.utilisateur.wowapi.ConverterJSON;

import com.example.utilisateur.wowapi.entity.Auctions;
import com.example.utilisateur.wowapi.entity.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertisseur Taux JSON => Java
 * Created by Hervé
 */

public class JSONConverter {
    /**
     * Conversion de taux JSON sous forme de liste de taux Java
     * @param jsonString    Chaîne JSON contenant les taux OpenExchange
     * @return              Liste de taux
     */
    public static List<Auctions> convertAuctions(String jsonString) {
        List<Auctions> auctionses = new ArrayList<>();



            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            if (jsonArray != null) {
                for (int i=0;i<jsonArray.length();i++){
                    try {
                        JSONObject obj = new JSONObject(jsonArray.getString(i));
                        JSONObject objet = obj.getJSONObject("_id");
                        Auctions auctions = new Auctions(objet.getString("owner"), objet.getInt("prixU"));
                        auctionses.add(auctions);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }





        return auctionses;
    }
    public static List<Item> convertItems(String jsonString) {
        List<Item> Items = new ArrayList<>();



        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        if (jsonArray != null) {
            for (int i=0;i<jsonArray.length();i++){
                try {
                    JSONObject obj = new JSONObject(jsonArray.getString(i));


                    Item items = new Item(obj.getString("name"),obj.getInt("id"),obj.getString("icon"));
                    Items.add(items);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }





        return Items;
    }
    public static Item convertItem(String jsonString) {
        Item Items =null;

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        if (jsonArray != null) {

                try {
                    JSONObject obj = new JSONObject(jsonArray.getString(0));


                     Items = new Item(obj.getString("name"),obj.getInt("id"),obj.getString("icon"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }





        return Items;
    }
}
