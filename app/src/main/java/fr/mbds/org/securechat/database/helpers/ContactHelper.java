package fr.mbds.org.securechat.database.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.mbds.org.securechat.database.Database;

public class ContactHelper extends SQLiteOpenHelper {

    public ContactHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String DATABASE_NAME = "ContactDb.db";
    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Database.ContactContract.FeedContact.TABLE_NAME + " (" +
                    Database.ContactContract.FeedContact._ID + " INTEGER PRIMARY KEY," +
                    Database.ContactContract.FeedContact.COLUMN_NAME_LASTNAME + " TEXT," +
                    Database.ContactContract.FeedContact.COLUMN_NAME_FIRSTNAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Database.ContactContract.FeedContact.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
