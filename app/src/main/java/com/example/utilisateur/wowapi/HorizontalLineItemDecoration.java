package com.example.utilisateur.wowapi;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Classe de "décoration" utilisée pour le tracé de séparateurs d'éléments sur RecyclerView.
 * Created by Hervé.
 */

public class HorizontalLineItemDecoration extends RecyclerView.ItemDecoration {

    // ---
    // CHAMPS
    // ---

    private Paint mPaint;

    // ---
    // CONSTRUCTEURS
    // ---

    public HorizontalLineItemDecoration(int color) {
        // Un objet Paint
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        // Récupérer les coordonnées gauche et droite du tracé
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for(int i = 0; i < parent.getChildCount(); i++)
        {
            // Récupérer la coordonnée bas de chaque bande horizontale
            View child = parent.getChildAt(i);
            int bottom = child.getBottom();

            // Tracer la ligne de séparation
            c.drawLine(left, bottom, right, bottom, mPaint);
        }
    }
}
