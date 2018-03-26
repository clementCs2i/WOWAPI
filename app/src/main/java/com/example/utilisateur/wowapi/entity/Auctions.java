package com.example.utilisateur.wowapi.entity;

/**
 * Taux d'une devise
 * Created by Hervé
 */

public class Auctions {

    // ---
    // CHAMPS
    // ---

    private String Vendeur;
    private double PrixDirect;
    private double PrixEnchere;
    private int quantite;
    private int nbEnchere;

    // ---
    // CONSTRUCTEURS
    // ---

    public Auctions(String vendeur, double prixDirect, double prixEnchere, int quantite, int nbEnchere) {
        Vendeur = vendeur;
        PrixDirect = prixDirect;
        PrixEnchere = prixEnchere;
        this.quantite = quantite;
        this.nbEnchere = nbEnchere;
    }


    // ---
    // GETTERS/SETTERS
    // ---

    public String getVendeur() {
        return Vendeur;
    }

    public void setVendeur(String vendeur) {
        Vendeur = vendeur;
    }

    public double getPrixDirect() {
        return PrixDirect;
    }

    public void setPrixDirect(double prixDirect) {
        PrixDirect = prixDirect;
    }

    public double getPrixEnchere() {
        return PrixEnchere;
    }

    public void setPrixEnchere(double prixEnchere) {
        PrixEnchere = prixEnchere;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getNbEnchere() {
        return nbEnchere;
    }

    public void setNbEnchere(int nbEnchere) {
        this.nbEnchere = nbEnchere;
    }


    // ---
    // DIVERS
    // ---

    @Override
    public String toString() {
        return String.format("%s = %s", getVendeur(), getPrixDirect());
    }
}
