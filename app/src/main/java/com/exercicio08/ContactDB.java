package com.exercicio08;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ContactDB {
    private SQLiteDatabase db;

    public ContactDB(Context context) {
        db = new ContactDBHelper(context).getWritableDatabase();
    }

    public void addContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("phone", contact.getPhone());
        values.put("email", contact.getEmail());
        long rowId = db.insert("contacts", null, values);
        if (rowId != -1) {
            Log.d("ContactDB", "Contact added successfully!");
        } else {
            Log.e("ContactDB", "Error adding contact!");
        }
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = db.query("contacts", new String[]{"id", "name", "phone", "email"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            String email = cursor.getString(3);
            contacts.add(new Contact(id, name, phone, email));
        }
        cursor.close();
        return contacts;
    }

    public Contact getContact(int id) {
        Cursor cursor = db.query("contacts", new String[]{"id", "name", "phone", "email"}, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToNext()) {
            int contactId = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            String email = cursor.getString(3);
            return new Contact(contactId, name, phone, email);
        }
        return null;
    }

    public void updateContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("phone", contact.getPhone());
        values.put("email", contact.getEmail());
        db.update("contacts", values, "id = ?", new String[]{String.valueOf(contact.getId())});
    }

    public void deleteContact(int id) {
        db.beginTransaction();
        try {
            db.delete("contacts", "id =?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private class ContactDBHelper extends SQLiteOpenHelper {
        public ContactDBHelper(Context context) {
            super(context, "contacts.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE contacts (id INTEGER PRIMARY KEY, name TEXT, phone TEXT, email TEXT)");
            Log.d("ContactDB", "Database created successfully!");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
}