package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dhtp on 11/26/2016.
 */

public class NoteDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GreenNote.db";
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SERVICE_ID = "service_id";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESCRIPTION = "description";

    public NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public NoteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_SERVICE_ID + " INTEGER," +
                COLUMN_PRICE + " DOUBLE," +
                COLUMN_DATE + " DATETIME," +
                COLUMN_DESCRIPTION + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insert (int service_id, double price, String date, String description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SERVICE_ID, service_id);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_DESCRIPTION, description);
        long id = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return id;
    }

    public boolean update (int id, int service_id, double price, String date, String description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SERVICE_ID, service_id);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_DESCRIPTION, description);
        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + " = ? ", new String[] { id + "" } );
        return true;
    }

    public int delete (int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + " = ? ", new String[] { Integer.toString(id) });
    }

    public Cursor get(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id + "", null);
        return cursor;
    }

    public Cursor getAll() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }

    public Cursor getAllJoin() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " LEFT JOIN services ON notes.service_id = services.id", null);
        return cursor;
    }

    public Cursor getAllJoinNow() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " LEFT JOIN services ON notes.service_id = services.id WHERE date = date('now', 'localtime')", null);
        return cursor;
    }
}
