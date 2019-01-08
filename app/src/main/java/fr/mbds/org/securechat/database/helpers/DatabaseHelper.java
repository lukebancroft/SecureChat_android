package fr.mbds.org.securechat.database.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.mbds.org.securechat.database.Database;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String DATABASE_NAME = "ContactDb.db";
    public static final int DATABASE_VERSION = 7;

    private static final String CREATE_TABLE_CONTACTS =
            "CREATE TABLE IF NOT EXISTS " + Database.DatabaseContract.FeedContact.CONTACTS_TABLE_NAME + " (" +
                    Database.DatabaseContract.FeedContact._ID + " INTEGER PRIMARY KEY," +
                    Database.DatabaseContract.FeedContact.CONTACT_UID + " TEXT," +
                    Database.DatabaseContract.FeedContact.CONTACT_USERNAME + " TEXT," +
                    Database.DatabaseContract.FeedContact.CONTACT_EMAIL + " TEXT)";

    private static final String CREATE_TABLE_MESSAGES =
            "CREATE TABLE IF NOT EXISTS " + Database.DatabaseContract.FeedContact.MESSAGES_TABLE_NAME + " (" +
                    Database.DatabaseContract.FeedContact._ID + " INTEGER PRIMARY KEY," +
                    Database.DatabaseContract.FeedContact.MESSAGE_SENDER + " TEXT," +
                    Database.DatabaseContract.FeedContact.MESSAGE_BODY + " TEXT," +
                    Database.DatabaseContract.FeedContact.MESSAGE_CHAT_WITH + " TEXT," +
                    Database.DatabaseContract.FeedContact.MESSAGE_TIMESTAMP + " TEXT)";

    private static final String SQL_DELETE_CONTACTS =
            "DROP TABLE IF EXISTS " + Database.DatabaseContract.FeedContact.CONTACTS_TABLE_NAME;

    private static final String SQL_DELETE_MESSAGES =
            "DROP TABLE IF EXISTS " + Database.DatabaseContract.FeedContact.MESSAGES_TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CONTACTS);
        db.execSQL(SQL_DELETE_MESSAGES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
