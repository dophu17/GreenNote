package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by phu on 11/29/2016.
 */

public class SettingDBHelper extends DBhelper {
    public SettingDBHelper(Context context) {
        super(context);
    }

    public long insert (String key, String value, String default_value, String description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SETTING_KEY, key);
        contentValues.put(COLUMN_SETTING_VALUE, value);
        contentValues.put(COLUMN_SETTING_DEFAULT_VALUE, default_value);
        contentValues.put(COLUMN_SETTING_DESCRIPTION, description);
        long id = sqLiteDatabase.insert(TABLE_SETTING_NAME, null, contentValues);
        return id;
    }

    public boolean update (int id, String key, String value, String default_value, String description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SETTING_KEY, key);
        contentValues.put(COLUMN_SETTING_VALUE, value);
        contentValues.put(COLUMN_SETTING_DEFAULT_VALUE, default_value);
        contentValues.put(COLUMN_SETTING_DESCRIPTION, description);
        sqLiteDatabase.update(TABLE_SETTING_NAME, contentValues, COLUMN_SETTING_ID + " = ? ", new String[] { id + "" } );
        return true;
    }

    public int delete (int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_SETTING_NAME, COLUMN_SETTING_ID + " = ? ", new String[] { Integer.toString(id) });
    }

    public Cursor get(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_SETTING_NAME + " WHERE " + COLUMN_SETTING_ID + " = " + id + "", null);
        return cursor;
    }

//    public Cursor getByKey(String key) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_SETTING_NAME + " WHERE " + COLUMN_SETTING_KEY + " = '" + key + "'", null);
//        cursor.moveToFirst();
//        return cursor;
//    }

    public Cursor getAll() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_SETTING_NAME, null);
        return cursor;
    }
}
