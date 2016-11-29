package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dhtp on 11/26/2016.
 */

public class NoteDBHelper extends DBhelper {

    public NoteDBHelper(Context context) {
        super(context);
    }

    public long insert (int service_id, double price, String date, String description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTE_SERVICE_ID, service_id);
        contentValues.put(COLUMN_NOTE_PRICE, price);
        contentValues.put(COLUMN_NOTE_DATE, date);
        contentValues.put(COLUMN_NOTE_DESCRIPTION, description);
        long id = sqLiteDatabase.insert(TABLE_NOTE_NAME, null, contentValues);
        return id;
    }

    public boolean update (int id, int service_id, double price, String date, String description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTE_SERVICE_ID, service_id);
        contentValues.put(COLUMN_NOTE_PRICE, price);
        contentValues.put(COLUMN_NOTE_DATE, date);
        contentValues.put(COLUMN_NOTE_DESCRIPTION, description);
        sqLiteDatabase.update(TABLE_NOTE_NAME, contentValues, COLUMN_NOTE_ID + " = ? ", new String[] { id + "" } );
        return true;
    }

    public int delete (int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NOTE_NAME, COLUMN_NOTE_ID + " = ? ", new String[] { Integer.toString(id) });
    }

    public Cursor get(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NOTE_NAME + " WHERE " + COLUMN_NOTE_ID + " = " + id + "", null);
        return cursor;
    }

    public Cursor getAll() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NOTE_NAME, null);
        return cursor;
    }

    public Cursor getAllJoin() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NOTE_NAME + " LEFT JOIN " + TABLE_SERVICE_NAME + " ON notes.service_id = services.id", null);
        return cursor;
    }

    public Cursor getAllJoinNow() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NOTE_NAME + " LEFT JOIN " + TABLE_SERVICE_NAME + " ON notes.service_id = services.id WHERE notes.date = date('now', 'localtime')", null);
        return cursor;
    }

    public Cursor getSumToday(String currentDate) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date = date('" + currentDate + "')", null);
        return cursor;
    }

    public Cursor getSumThisMonth(String currentDate) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('" + currentDate + "', 'start of month') AND date('" + currentDate + "', 'start of month' , '+1 month', '-1 days')", null);
        return cursor;
    }

    public Cursor getSumYesterday(String currentDate) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('" + currentDate + "', '-2 days') AND date('" + currentDate + "', '-1 days')", null);
        return cursor;
    }

    public Cursor getSumLastMonth(String currentDate) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('" + currentDate + "', 'start of month', '-1 month') AND date('" + currentDate + "', 'start of month', '-1 days')", null);
        return cursor;
    }
}
