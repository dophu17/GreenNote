package net.dauhuthom.greennote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StatisticalActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;

    TextView tvToday, tvLastWeek, tvLastMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);

        addControls();
        readData();
    }

    private void addControls() {
        tvToday = (TextView) findViewById(R.id.tvToday);
        tvLastWeek = (TextView) findViewById(R.id.tvLastWeek);
        tvLastMonth = (TextView) findViewById(R.id.tvLastMonth);
    }

    public void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , service_id INTEGER, price DOUBLE, date VARCHAR DEFAULT (CURRENT_DATE) , description TEXT)");

        //today
        Cursor cursor = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date >= date('now', 'localtime')", null);
        if (cursor.moveToFirst()) {
            tvToday.setText(cursor.getDouble(0) + " VND");
        }
        //last week
        Cursor cursorLastWeek = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('now', '-7 days') AND datetime('now', 'localtime')", null);
        if (cursorLastWeek.moveToFirst()) {
            tvLastWeek.setText(cursorLastWeek.getDouble(0) + " VND");
        }
        //last month
        Cursor cursorLastMonth = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime')", null);
        if (cursorLastMonth.moveToFirst()) {
            tvLastMonth.setText(cursorLastMonth.getDouble(0) + " VND");
        }

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateNow = new Date();
        return dateFormat.format(dateNow);
    }
}
