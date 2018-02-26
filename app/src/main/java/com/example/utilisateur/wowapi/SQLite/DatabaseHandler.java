package com.example.utilisateur.wowapi.SQLite;

/**
 * Created by Utilisateur on 26/02/2018.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.utilisateur.wowapi.entity.Item;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "WowApi";

    // Contacts table name
    private static final String TABLE_ITEMS = "Items";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IDITEM = "idItem";
    private static final String KEY_NAME = "name";
    private static final String KEY_URL_IMG = "urlImage";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_NAME + " TEXT,"
                + KEY_IDITEM + " TEXT,"
                + KEY_URL_IMG + " TEXT,"
                + KEY_ID + " INTEGER PRIMARY KEY" + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDITEM, item.getId()); // Contact Phone
        values.put(KEY_NAME, item.getName()); // Contact Name
        values.put(KEY_URL_IMG, item.getImage()); // Contact Phone


        // Inserting Row
        db.insert(TABLE_ITEMS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Item getItemId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Item item =null;
        Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_ID,
                        KEY_IDITEM, KEY_NAME, KEY_URL_IMG }, KEY_IDITEM + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
                if (cursor.moveToFirst()) {

         item = new Item(cursor.getString(2), Integer.parseInt(cursor.getString(1)),cursor.getString(3));
        }}
        // return contact
        return item;
    }

    // Getting All Contacts
    public List<Item> getAllContacts() {
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setName(cursor.getString(0));
                item.setId(Integer.parseInt(cursor.getString(1)));
                item.setImage(cursor.getString(2));
                // Adding contact to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return contact list
        return itemList;
    }

    // Updating single contact
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, item.getId());
        values.put(KEY_NAME, item.getName());
        values.put(KEY_URL_IMG, item.getImage());

        // updating row
        return db.update(TABLE_ITEMS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
    }

    // Deleting single contact
    public void deleteContact(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}