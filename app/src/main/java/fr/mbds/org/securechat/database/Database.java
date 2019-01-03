package fr.mbds.org.securechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import fr.mbds.org.securechat.database.entities.Person;
import fr.mbds.org.securechat.database.helpers.ContactHelper;

public class Database {

    private static Database dbInstance;

    private static ContactHelper contactHelper;

    private Database() {

    }

    public static  Database getInstance(Context context) {

        if (dbInstance == null) {
            dbInstance = new Database();
            contactHelper = new ContactHelper(context);
        }
        return dbInstance;
    }

    public void addPerson(String firstname,String lastname)
    {
        SQLiteDatabase db = contactHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactContract.FeedContact.COLUMN_NAME_LASTNAME, lastname);
        values.put(ContactContract.FeedContact.COLUMN_NAME_FIRSTNAME, firstname);

        long newRowId = db.insert(ContactContract.FeedContact.TABLE_NAME, null, values);
    }

    public List<Person> readPerson() {
        SQLiteDatabase db = contactHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContactContract.FeedContact.COLUMN_NAME_LASTNAME,
                ContactContract.FeedContact.COLUMN_NAME_FIRSTNAME
        };


        String selection = "";
        String[] selectionArgs = null;

        String sortOrder = ContactContract.FeedContact.COLUMN_NAME_LASTNAME + " DESC";

        Cursor cursor = db.query(
                ContactContract.FeedContact.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List persons = new ArrayList<Person>();
        while(cursor.moveToNext())
        {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactContract.FeedContact._ID));
            String nom = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_LASTNAME));
            String prenom = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_FIRSTNAME));
            persons.add(new Person(nom,prenom));
        }
        cursor.close();

        return persons;
    }

    public void deleteAll() {
        SQLiteDatabase db = contactHelper.getReadableDatabase();
        db.execSQL("DELETE FROM " + ContactContract.FeedContact.TABLE_NAME +
                " WHERE " + ContactContract.FeedContact.COLUMN_NAME_FIRSTNAME + " LIKE '%'");
    }

    public final class ContactContract{

        private ContactContract() {

        }

        public class FeedContact implements BaseColumns
        {
            public static final String TABLE_NAME = "Contact";
            public static final String COLUMN_NAME_LASTNAME = "LastName";
            public static final String COLUMN_NAME_FIRSTNAME = "FirstName";
        }
    }


}
