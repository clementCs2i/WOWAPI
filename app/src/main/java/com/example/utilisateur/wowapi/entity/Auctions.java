package com.example.utilisateur.wowapi.entity;

/**
 * Taux d'une devise
 * Created by Hervé
 */

public class Auctions {

    // ---
    // CHAMPS
    // ---

    private String code;
    private double value;

    // ---
    // CONSTRUCTEURS
    // ---
    public Auctions(String code, double value) {
        this.code = code;
        this.value = value;
    }

    // ---
    // GETTERS/SETTERS
    // ---

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    // ---
    // DIVERS
    // ---

    @Override
    public String toString() {
        return String.format("%s = %s", getCode(), getValue());
    }
}
