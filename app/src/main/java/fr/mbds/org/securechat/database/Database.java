package fr.mbds.org.securechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.mbds.org.securechat.database.entities.Contact;
import fr.mbds.org.securechat.database.entities.Message;
import fr.mbds.org.securechat.database.helpers.DatabaseHelper;

import static fr.mbds.org.securechat.database.Database.DatabaseContract.FeedContact.CONTACTS_TABLE_NAME;
import static fr.mbds.org.securechat.database.Database.DatabaseContract.FeedContact.MESSAGES_TABLE_NAME;

public class Database {

    private static Database dbInstance;

    private static DatabaseHelper databaseHelper;

    private Database() {

    }

    public static  Database getInstance(Context context) {

        if (dbInstance == null) {
            dbInstance = new Database();
            databaseHelper = new DatabaseHelper(context);
        }
        return dbInstance;
    }

    public void createContact(String uid, String username, String email)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.FeedContact.CONTACT_UID, uid);
        values.put(DatabaseContract.FeedContact.CONTACT_USERNAME, username);
        values.put(DatabaseContract.FeedContact.CONTACT_EMAIL, email);

        db.insert(CONTACTS_TABLE_NAME, null, values);
    }

    public void createMessage(String sender, String body, String chatWith, String timestamp)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.FeedContact.MESSAGE_SENDER, sender);
        values.put(DatabaseContract.FeedContact.MESSAGE_BODY, body);
        values.put(DatabaseContract.FeedContact.MESSAGE_CHAT_WITH, chatWith);
        values.put(DatabaseContract.FeedContact.MESSAGE_TIMESTAMP, timestamp);

        db.insert(MESSAGES_TABLE_NAME, null, values);
    }

    public List<Contact> getContacts() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                DatabaseContract.FeedContact.CONTACT_UID,
                DatabaseContract.FeedContact.CONTACT_USERNAME,
                DatabaseContract.FeedContact.CONTACT_EMAIL
        };


        String selection = "";
        String[] selectionArgs = null;

        String sortOrder = DatabaseContract.FeedContact.CONTACT_USERNAME + " DESC";

        Cursor cursor = db.query(
                CONTACTS_TABLE_NAME,     // The table to query
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
            //cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.FeedContact._ID));
            String uid = cursor.getString(cursor.getColumnIndex(DatabaseContract.FeedContact.CONTACT_UID));
            String username = cursor.getString(cursor.getColumnIndex(DatabaseContract.FeedContact.CONTACT_USERNAME));
            String email = cursor.getString(cursor.getColumnIndex(DatabaseContract.FeedContact.CONTACT_EMAIL));

            persons.add(new Contact(uid, username, email));
        }
        cursor.close();

        return persons;
    }

    public List<Message> getMessagesFromContactID(String id) throws ParseException {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                DatabaseContract.FeedContact.MESSAGE_SENDER,
                DatabaseContract.FeedContact.MESSAGE_BODY,
                DatabaseContract.FeedContact.MESSAGE_CHAT_WITH,
                DatabaseContract.FeedContact.MESSAGE_TIMESTAMP
        };


        String selection = DatabaseContract.FeedContact.MESSAGE_CHAT_WITH + " = ?";
        String[] selectionArgs = new String[]{id};

        String sortOrder = DatabaseContract.FeedContact.MESSAGE_TIMESTAMP + " ASC";

        Cursor cursor = db.query(
                MESSAGES_TABLE_NAME,     // The table to query
                projection,                                 // The array of columns to return (pass null to get all)
                selection,                                  // The columns for the WHERE clause
                selectionArgs,                              // The values for the WHERE clause
                null,                              // don't group the rows
                null,                               // don't filter by row groups
                sortOrder                                  // The sort order
        );

        List messages = new ArrayList<Message>();
        SimpleDateFormat timestampFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        while(cursor.moveToNext())
        {
            String body = cursor.getString(cursor.getColumnIndex(DatabaseContract.FeedContact.MESSAGE_BODY));
            String sender = cursor.getString(cursor.getColumnIndex(DatabaseContract.FeedContact.MESSAGE_SENDER));
            String timestamp = cursor.getString(cursor.getColumnIndex(DatabaseContract.FeedContact.MESSAGE_TIMESTAMP));
            messages.add(new Message(body, sender, timestampFormatter.parse(timestamp)));
        }
        cursor.close();

        return messages;
    }

    public void deleteAll() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        //db.execSQL("DELETE FROM " + DatabaseContract.FeedContact.TABLE_NAME +
        //        " WHERE " + DatabaseContract.FeedContact.COLUMN_NAME_USERNAME + " LIKE '%'");

        db.execSQL("DROP TABLE " + CONTACTS_TABLE_NAME);
        db.execSQL("DROP TABLE " + MESSAGES_TABLE_NAME);
    }

    public final class DatabaseContract{

        private DatabaseContract() {

        }

        public class FeedContact implements BaseColumns
        {
            public static final String CONTACTS_TABLE_NAME = "Contact";
            public static final String CONTACT_UID = "UID";
            public static final String CONTACT_USERNAME = "UserName";
            public static final String CONTACT_EMAIL = "Email";

            public static final String MESSAGES_TABLE_NAME = "Message";
            public static final String MESSAGE_SENDER = "Sender";
            public static final String MESSAGE_BODY = "Body";
            public static final String MESSAGE_CHAT_WITH = "ChatWith";
            public static final String MESSAGE_TIMESTAMP = "Timestamp";
        }
    }


}
