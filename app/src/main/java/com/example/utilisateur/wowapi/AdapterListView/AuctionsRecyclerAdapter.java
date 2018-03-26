package com.example.utilisateur.wowapi.AdapterListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.utilisateur.wowapi.entity.Auctions;
import com.example.utilisateur.wowapi.R;

import java.util.List;

//import butterknife.BindView;
//import butterknife.ButterKnife;


/**
 * Created by Hervé on 03/04/2017.
 */

public class AuctionsRecyclerAdapter extends RecyclerView.Adapter<AuctionsRecyclerAdapter.ViewHolder> {

    // ---
    // CHAMPS
    // ---

    private Context mContext;
    private int mItemLayout;
    private List<Auctions> mAuctionses;

    // ---
    // CONSTRUCTEUR
    // ---

    public AuctionsRecyclerAdapter(Context context, int itemLayout, List<Auctions> auctionses) {
        mContext = context;
        mItemLayout = itemLayout;
        mAuctionses = auctionses;
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
        Auctions auctions = mAuctionses.get(position);

        // Transférer les valeurs de taux dans le ViewHolder
        holder.Vendeur.setText(auctions.getVendeur());

        double prixItem = auctions.getPrixDirect();

        double PrixGold =prixItem/10000;

        holder.PrixAchat.setText(Double.toString(round(PrixGold, 2)));

        double prixEnchere = auctions.getPrixEnchere();

        double prixdeEnchere =prixEnchere/10000;
        holder.PrixEnchere.setText(Double.toString(round(prixdeEnchere, 2)));

        holder.quantity.setText(Integer.toString(auctions.getQuantite()));
                holder.nbEnchere.setText(Integer.toString(auctions.getNbEnchere()));
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    /**
     * Nombre d'éléments gérés par l'adapter
     * @return  Nombre de taux
     */
    @Override
    public int getItemCount() {
        return mAuctionses.size();
    }

    // -------------
    // Le ViewHolder
    // -------------
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Les vues d'une bande horizontale

        //@BindView(R.id.rate_code)
        TextView Vendeur;
        //@BindView(R.id.rate_value)

        TextView PrixAchat;
        TextView PrixEnchere;
        TextView quantity;
        TextView nbEnchere;
        public ViewHolder(View itemView) {
            super(itemView);

            // Instancier les 2 TextViews : version classique
             Vendeur = (TextView)itemView.findViewById(R.id.rate_code);
            PrixAchat = (TextView)itemView.findViewById(R.id.PrixAchat);
            PrixEnchere = (TextView)itemView.findViewById(R.id.PrixEnchere);

            quantity = (TextView)itemView.findViewById(R.id.quantity);
            nbEnchere = (TextView)itemView.findViewById(R.id.nbEnchere);


            // Instancier les 2 TextViews : version ButterKnife
           // ButterKnife.bind(this, itemView);
        }
    }
}
