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

public class ServiceDBHelper extends DBhelper {

    public ServiceDBHelper(Context context) {
        super(context);
    }

    public long insert (String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SERVICE_NAME, name);
        long id = sqLiteDatabase.insert(TABLE_SERVICE_NAME, null, contentValues);
        return id;
    }

    public boolean update (int id, String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SERVICE_NAME, name);
        sqLiteDatabase.update(TABLE_SERVICE_NAME, contentValues, COLUMN_SERVICE_ID + " = ? ", new String[] { id + "" } );
        return true;
    }

    public int delete (int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_SERVICE_NAME, COLUMN_SERVICE_ID + " = ? ", new String[] { Integer.toString(id) });
    }

    public Cursor get(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_SERVICE_NAME + " WHERE " + COLUMN_SERVICE_ID + " = " + id + "", null);
        return cursor;
    }

    public Cursor getAll() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_SERVICE_NAME, null);
        return cursor;
    }
}
