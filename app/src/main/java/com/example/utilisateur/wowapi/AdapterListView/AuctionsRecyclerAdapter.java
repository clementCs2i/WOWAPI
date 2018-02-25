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
        holder.codeView.setText(auctions.getCode());
        holder.valueView.setText(String.valueOf(auctions.getValue()));
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
        TextView codeView;
        //@BindView(R.id.rate_value)
        TextView valueView;

        public ViewHolder(View itemView) {
            super(itemView);

            // Instancier les 2 TextViews : version classique
             codeView = (TextView)itemView.findViewById(R.id.rate_code);
             valueView = (TextView)itemView.findViewById(R.id.rate_value);

            // Instancier les 2 TextViews : version ButterKnife
           // ButterKnife.bind(this, itemView);
        }
    }
}
