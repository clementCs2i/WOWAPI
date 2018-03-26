package com.example.utilisateur.wowapi.entity;



public class Item {

    // ---
    // CHAMPS
    // ---

    private String Name;
    private int Id;
    private String Image;
    private String Description;
    private int ItemLvl;
    private int  Stack;
    private int Prix;

    // ---
    // CONSTRUCTEURS
    // ---


    public Item(String name, int id, String image, String description, int itemLvl, int stack, int prix) {
        Name = name;
        Id = id;
        Image = image;
        Description = description;
        ItemLvl = itemLvl;
        Stack = stack;
        Prix = prix;
    }
    public Item(String name, int id, String image) {
        Name = name;
        Id = id;
        Image = image;
    }

    public Item() {

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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getItemLvl() {
        return ItemLvl;
    }

    public void setItemLvl(int itemLvl) {
        ItemLvl = itemLvl;
    }

    public int getStack() {
        return Stack;
    }

    public void setStack(int stack) {
        Stack = stack;
    }

    public int getPrix() {
        return Prix;
    }

    public void setPrix(int prix) {
        Prix = prix;
    }
// ---
    // DIVERS
    // ---

    @Override
    public String toString() {
        return String.format("%s = %s", getName(), getId(),getImage());
    }
}
