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
                        Auctions auctions = new Auctions(objet.getString("owner"),objet.getInt("prixU"),objet.getInt("bid"),obj.getInt("count"),objet.getInt("quantity"));
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

String  description ="";

        if (jsonArray != null) {
            for (int i=0;i<jsonArray.length();i++){
                description ="";
                try {
                    JSONObject obj = new JSONObject(jsonArray.getString(i));
                if (obj.getInt("itemClass") == 0 || obj.getInt("itemClass") == 8 || obj.getInt("itemClass") == 13 || obj.getInt("itemClass") == 16|| obj.getInt("itemClass") == 17) {
                    JSONArray Spells = null;
                    try {
                        Spells = new JSONArray(obj.getString("itemSpells"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int j=0;j<Spells.length();j++){
                        JSONObject itemSpells = new JSONObject(Spells.getString(j));
                    JSONObject spell = new JSONObject(itemSpells.getString("spell"));
                    description += spell.getString("description")+"\n";
                    }
                }else{
                    description = obj.getString("description");
                    }

                    Item items = new Item(obj.getString("name"),obj.getInt("id"),obj.getString("icon"),description,obj.getInt("itemLevel"),obj.getInt("stackable"),obj.getInt("sellPrice"));
                    Items.add(items);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }





        return Items;
    }

}
