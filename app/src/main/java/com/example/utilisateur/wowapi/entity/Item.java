package com.example.utilisateur.wowapi.entity;

/**
 * Taux d'une devise
 * Created by Hervé
 */

public class Item {

    // ---
    // CHAMPS
    // ---

    private String Name;
    private int Id;
    private String Image;

    // ---
    // CONSTRUCTEURS
    // ---
    public Item(String Name, int Id,String Image) {
        this.Name = Name;
        this.Id = Id;
        this.Image = Image;
    }

    // ---
    // GETTERS/SETTERS
    // ---

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


    // ---
    // DIVERS
    // ---

    @Override
    public String toString() {
        return String.format("%s = %s", getName(), getId(),getImage());
    }
}
