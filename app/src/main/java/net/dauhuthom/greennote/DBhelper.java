package net.dauhuthom.greennote;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by phu on 11/28/2016.
 */

public class DBhelper extends SQLiteOpenHelper {

    //4: add table "setting"
    //6: autosend = "no" default
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "GreenNote.db";

    //table "notes"
    public static final String TABLE_NOTE_NAME = "notes";
    public static final String COLUMN_NOTE_ID = "id";
    public static final String COLUMN_NOTE_SERVICE_ID = "service_id";
    public static final String COLUMN_NOTE_PRICE = "price";
    public static final String COLUMN_NOTE_DATE = "date";
    public static final String COLUMN_NOTE_DESCRIPTION = "description";

    //table "services"
    public static final String TABLE_SERVICE_NAME = "services";
    public static final String COLUMN_SERVICE_ID = "id";
    public static final String COLUMN_SERVICE_NAME = "name";

    //table "settings"
    public static final String TABLE_SETTING_NAME = "settings";
    public static final String COLUMN_SETTING_ID = "id";
    public static final String COLUMN_SETTING_KEY = "key";
    public static final String COLUMN_SETTING_VALUE = "value";
    public static final String COLUMN_SETTING_DEFAULT_VALUE = "default_value";
    public static final String COLUMN_SETTING_DESCRIPTION = "description";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTE_NAME + " (" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_NOTE_SERVICE_ID + " INTEGER," +
                COLUMN_NOTE_PRICE + " DOUBLE," +
                COLUMN_NOTE_DATE + " VARCHAR," +
                COLUMN_NOTE_DESCRIPTION + " TEXT )");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SERVICE_NAME + " (" +
                COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_SERVICE_NAME + " TEXT )");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SETTING_NAME + " (" +
                COLUMN_SETTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_SETTING_KEY + " VARCHAR," +
                COLUMN_SETTING_VALUE + " TEXT," +
                COLUMN_SETTING_DEFAULT_VALUE + " TEXT," +
                COLUMN_SETTING_DESCRIPTION + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING_NAME);
        onCreate(sqLiteDatabase);
    }
}
