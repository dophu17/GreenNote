package net.dauhuthom.greennote;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatisticalActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;
    Calendar calendar;
    Date date;
    String currentDate = null;
    double totalToday = 0, totalYesterday = 0, totalThisWeek = 0, totalLastWeek = 0, totalThisMonth = 0, totalLastMonth = 0;

    TextView tvToday, tvThisWeek, tvThisMonth, tvYesterday, tvLastWeek, tvLastMonth, tvChangeDate, tvWarningDay, tvWarningMonth, tvWarningWeek;
    Button btnChangeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);

        addControls();
        readData();
        addEvents();
    }

    private void addControls() {
        tvToday = (TextView) findViewById(R.id.tvToday);
        tvThisWeek = (TextView) findViewById(R.id.tvThisWeek);
        tvThisMonth = (TextView) findViewById(R.id.tvThisMonth);
        tvYesterday = (TextView) findViewById(R.id.tvYesterday);
        tvLastWeek = (TextView) findViewById(R.id.tvLastWeek);
        tvLastMonth = (TextView) findViewById(R.id.tvLastMonth);
        tvWarningDay = (TextView) findViewById(R.id.tvWarningDay);
        tvWarningMonth = (TextView) findViewById(R.id.tvWarningMonth);
        tvWarningWeek = (TextView) findViewById(R.id.tvWarningWeek);
        tvChangeDate = (TextView) findViewById(R.id.tvChangeDate);
        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);

        //date current
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
        tvChangeDate.setText(strDate);

        //set current date for sql
        SimpleDateFormat simpleDateFormatCurrent = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = simpleDateFormatCurrent.format(calendar.getTime());
    }

    public void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , service_id INTEGER, price DOUBLE, date VARCHAR DEFAULT (CURRENT_DATE) , description TEXT)");

        //today
        Cursor cursor = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date = date('" + currentDate + "', 'localtime')", null);
        if (cursor.moveToFirst()) {
            tvToday.setText(formatDecimal(cursor.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalToday = cursor.getDouble(0);
        }
        //this week
        Cursor cursorThisWeek = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('" + currentDate + "', '-7 days') AND datetime('" + currentDate + "', 'localtime')", null);
        if (cursorThisWeek.moveToFirst()) {
            tvThisWeek.setText(formatDecimal(cursorThisWeek.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalThisWeek = cursorThisWeek.getDouble(0);
        }
        //this month
        Cursor cursorThisMonth = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN datetime('" + currentDate + "', 'start of month') AND datetime('" + currentDate + "', 'localtime')", null);
        if (cursorThisMonth.moveToFirst()) {
            tvThisMonth.setText(formatDecimal(cursorThisMonth.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalThisMonth = cursorThisMonth.getDouble(0);
        }
        //yesterday
        Cursor cursorYesterday = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('" + currentDate + "', '-2 days') AND datetime('" + currentDate + "', '-1 days')", null);
        if (cursorYesterday.moveToFirst()) {
            tvYesterday.setText(formatDecimal(cursorYesterday.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalYesterday = cursorYesterday.getDouble(0);
        }
        //last week
        Cursor cursorLastWeek = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('" + currentDate + "', '-14 days') AND datetime('" + currentDate + "', '-7 days')", null);
        if (cursorLastWeek.moveToFirst()) {
            tvLastWeek.setText(formatDecimal(cursorLastWeek.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalLastWeek = cursorLastWeek.getDouble(0);
        }
        //last month
        Cursor cursorLastMonth = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN datetime('" + currentDate + "', 'start of month', '-1 month') AND datetime('" + currentDate + "', 'start of month')", null);
        if (cursorLastMonth.moveToFirst()) {
            tvLastMonth.setText(formatDecimal(cursorLastMonth.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalLastMonth = cursorLastMonth.getDouble(0);
        }

        //warning
        if (totalToday <= totalYesterday) {
            tvWarningDay.setText("Great! Save than yesterday " + formatDecimal(totalYesterday - totalToday, "###,###,###,###,###", Locale.GERMANY) + " VND");
            tvWarningDay.setTextColor(ContextCompat.getColor(this, R.color.colorBackground));
        } else {
            tvWarningDay.setText("Bad! Waste than yesterday " + formatDecimal(totalToday - totalYesterday, "###,###,###,###,###", Locale.GERMANY) + " VND");
            tvWarningDay.setTextColor(ContextCompat.getColor(this, R.color.colorWarning));
        }
        if (totalThisWeek <= totalLastWeek) {
            tvWarningWeek.setText("Great! Save than last week " + formatDecimal(totalLastWeek - totalThisWeek, "###,###,###,###,###", Locale.GERMANY) + " VND");
            tvWarningWeek.setTextColor(ContextCompat.getColor(this, R.color.colorBackground));
        } else {
            tvWarningWeek.setText("Bad! Waste than last week " + formatDecimal(totalThisWeek - totalLastWeek, "###,###,###,###,###", Locale.GERMANY) + " VND");
            tvWarningWeek.setTextColor(ContextCompat.getColor(this, R.color.colorWarning));
        }
        if (totalThisMonth <= totalLastMonth) {
            tvWarningMonth.setText("Great! Save than last month " + formatDecimal(totalLastMonth - totalThisMonth, "###,###,###,###,###", Locale.GERMANY) + " VND");
            tvWarningMonth.setTextColor(ContextCompat.getColor(this, R.color.colorBackground));
        } else {
            tvWarningMonth.setText("Bad! Waste than last month " + formatDecimal(totalThisMonth - totalLastMonth, "###,###,###,###,###", Locale.GERMANY) + " VND");
            tvWarningMonth.setTextColor(ContextCompat.getColor(this, R.color.colorWarning));
        }
    }

    private String formatDecimal(double number, String format, Locale locale) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        DecimalFormat formatter = new DecimalFormat (format, otherSymbols);
        return formatter.format(number);
    }

    private void addEvents() {
        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        tvChangeDate.setText((i1 + 1) + "-" + i2 + "-" + i);
                        calendar.set(i, i1, i2);
                        date = calendar.getTime();

                        currentDate = i + "-" + (i1 + 1) + "-" + i2;
                        readData();
                    }
                };
                String string = tvChangeDate.getText() + "";
                //Lấy ra chuỗi của textView Date
                String strArrtmp[] = string.split("-");
                int day = Integer.parseInt(strArrtmp[1]);
                int month = Integer.parseInt(strArrtmp[0]) - 1;
                int year = Integer.parseInt(strArrtmp[2]);
                //Hiển thị ra Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticalActivity.this,
                        onDateSetListener, year, month, day);
                datePickerDialog.setTitle("Change date");
                datePickerDialog.show();
            }
        });
    }
}
