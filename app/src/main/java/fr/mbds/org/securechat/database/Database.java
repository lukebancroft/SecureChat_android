package fr.mbds.org.securechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import fr.mbds.org.securechat.database.entities.Contact;
import fr.mbds.org.securechat.database.helpers.ContactHelper;

import static fr.mbds.org.securechat.database.Database.ContactContract.FeedContact.TABLE_NAME;

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

    public void createContact(String uid, String username, String email)
    {
        SQLiteDatabase db = contactHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactContract.FeedContact.COLUMN_NAME_UID, uid);
        values.put(ContactContract.FeedContact.COLUMN_NAME_USERNAME, username);
        values.put(ContactContract.FeedContact.COLUMN_NAME_EMAIL, email);

        db.insert(TABLE_NAME, null, values);
    }

    public List<Contact> getContacts() {
        SQLiteDatabase db = contactHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContactContract.FeedContact.COLUMN_NAME_UID,
                ContactContract.FeedContact.COLUMN_NAME_USERNAME,
                ContactContract.FeedContact.COLUMN_NAME_EMAIL
        };


        String selection = "";
        String[] selectionArgs = null;

        String sortOrder = ContactContract.FeedContact.COLUMN_NAME_USERNAME + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,     // The table to query
                projection,                                 // The array of columns to return (pass null to get all)
                selection,                                  // The columns for the WHERE clause
                selectionArgs,                              // The values for the WHERE clause
                null,                              // don't group the rows
                null,                               // don't filter by row groups
                sortOrder                                  // The sort order
        );

        List persons = new ArrayList<Contact>();
        while(cursor.moveToNext())
        {
            //cursor.getLong(cursor.getColumnIndexOrThrow(ContactContract.FeedContact._ID));
            String uid = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_UID));
            String username = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_USERNAME));
            String email = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_EMAIL));
            persons.add(new Contact(uid, username, email));
        }
        cursor.close();

        return persons;
    }

    public void deleteAll() {
        SQLiteDatabase db = contactHelper.getReadableDatabase();
        db.execSQL("DELETE FROM " + ContactContract.FeedContact.TABLE_NAME +
                " WHERE " + ContactContract.FeedContact.COLUMN_NAME_USERNAME + " LIKE '%'");

        //db.execSQL("DROP TABLE "+TABLE_NAME);
    }

    public final class ContactContract{

        private ContactContract() {

        }

        public class FeedContact implements BaseColumns
        {
            public static final String TABLE_NAME = "Contact";
            public static final String COLUMN_NAME_UID = "UID";
            public static final String COLUMN_NAME_USERNAME = "UserName";
            public static final String COLUMN_NAME_EMAIL = "Email";
        }
    }


}
