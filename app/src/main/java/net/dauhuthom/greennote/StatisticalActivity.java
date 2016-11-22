package net.dauhuthom.greennote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class StatisticalActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;

    TextView tvToday, tvThisWeek, tvThisMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);

        addControls();
        readData();
    }

    private void addControls() {
        tvToday = (TextView) findViewById(R.id.tvToday);
        tvThisWeek = (TextView) findViewById(R.id.tvThisWeek);
        tvThisMonth = (TextView) findViewById(R.id.tvThisMonth);
    }

    public void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , service_id INTEGER, price DOUBLE, date VARCHAR DEFAULT (CURRENT_DATE) , description TEXT)");

        //today
        Cursor cursor = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date >= date('now', 'localtime')", null);
        if (cursor.moveToFirst()) {
            tvToday.setText(formatDecimal(cursor.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
        }
        //this week
        Cursor cursorLastWeek = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('now', '-7 days') AND datetime('now', 'localtime')", null);
        if (cursorLastWeek.moveToFirst()) {
            tvThisWeek.setText(formatDecimal(cursorLastWeek.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
        }
        //this month
        Cursor cursorLastMonth = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime')", null);
        if (cursorLastMonth.moveToFirst()) {
            tvThisMonth.setText(formatDecimal(cursorLastMonth.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
        }

    }

    private String formatDecimal(double number, String format, Locale locale) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        DecimalFormat formatter = new DecimalFormat (format, otherSymbols);
        return formatter.format(number);
    }
}
