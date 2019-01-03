package fr.mbds.org.securechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import fr.mbds.org.securechat.database.entities.User;
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

    public void createUser(String username, String email, String password)
    {
        SQLiteDatabase db = contactHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactContract.FeedContact.COLUMN_NAME_USERNAME, username);
        values.put(ContactContract.FeedContact.COLUMN_NAME_EMAIL, email);
        values.put(ContactContract.FeedContact.COLUMN_NAME_PASSWORD, password);

        db.insert(TABLE_NAME, null, values);
    }

    public List<User> getUsers() {
        SQLiteDatabase db = contactHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContactContract.FeedContact.COLUMN_NAME_USERNAME,
                ContactContract.FeedContact.COLUMN_NAME_EMAIL,
                ContactContract.FeedContact.COLUMN_NAME_PASSWORD
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

        List persons = new ArrayList<User>();
        while(cursor.moveToNext())
        {
            //cursor.getLong(cursor.getColumnIndexOrThrow(ContactContract.FeedContact._ID));
            String username = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_USERNAME));
            String email = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_PASSWORD));
            persons.add(new User(username, email, password));
        }
        cursor.close();

        return persons;
    }

    public boolean checkUser(String email, String password) {
        String[] columns = {
                BaseColumns._ID
        };
        SQLiteDatabase db = contactHelper.getReadableDatabase();
        String selection = ContactContract.FeedContact.COLUMN_NAME_EMAIL + " = ?" + " AND " + ContactContract.FeedContact.COLUMN_NAME_PASSWORD + " = ?";

        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(
                TABLE_NAME, //Table to query
                columns,                                //columns to return
                selection,                              //columns for the WHERE clause
                selectionArgs,                          //The values for the WHERE clause
                null,                          //group the rows
                null,                           //filter by row groups
                null);                         //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
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
            public static final String COLUMN_NAME_USERNAME = "UserName";
            public static final String COLUMN_NAME_EMAIL = "Email";
            public static final String COLUMN_NAME_PASSWORD = "Password";
        }
    }


}
