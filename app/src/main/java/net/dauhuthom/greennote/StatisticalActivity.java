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
import java.text.NumberFormat;
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
    double totalToday = 0, totalYesterday = 0, totalThisMonth = 0, totalLastMonth = 0;

    TextView tvToday, tvThisMonth, tvYesterday, tvLastMonth, tvChangeDate, tvWarningDay, tvWarningMonth, tvWarningWeek;
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
        tvThisMonth = (TextView) findViewById(R.id.tvThisMonth);
        tvYesterday = (TextView) findViewById(R.id.tvYesterday);
        tvLastMonth = (TextView) findViewById(R.id.tvLastMonth);
        tvWarningDay = (TextView) findViewById(R.id.tvWarningDay);
        tvWarningMonth = (TextView) findViewById(R.id.tvWarningMonth);
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
        database.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , service_id INTEGER, price DOUBLE, date DATETIME, description TEXT)");

        //today
        Cursor cursor = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date = date('" + currentDate + "')", null);
        if (cursor.moveToFirst()) {
            tvToday.setText(formatDecimal(cursor.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalToday = cursor.getDouble(0);
        }
        //this month
        Cursor cursorThisMonth = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('" + currentDate + "', 'start of month') AND date('" + currentDate + "', 'start of month' , '+1 month', '-1 days')", null);
        if (cursorThisMonth.moveToFirst()) {
            tvThisMonth.setText(formatDecimal(cursorThisMonth.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalThisMonth = cursorThisMonth.getDouble(0);
        }
        //yesterday
        Cursor cursorYesterday = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('" + currentDate + "', '-2 days') AND date('" + currentDate + "', '-1 days')", null);
        if (cursorYesterday.moveToFirst()) {
            tvYesterday.setText(formatDecimal(cursorYesterday.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalYesterday = cursorYesterday.getDouble(0);
        }
        //last month
        Cursor cursorLastMonth = database.rawQuery("SELECT SUM(price) as total_price FROM notes WHERE date BETWEEN date('" + currentDate + "', 'start of month', '-1 month') AND date('" + currentDate + "', 'start of month', '-1 days')", null);
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
                        NumberFormat numberFormat = new DecimalFormat("00");
                        tvChangeDate.setText((i1 + 1) + "-" + i2 + "-" + i);
                        calendar.set(i, i1, i2);
                        date = calendar.getTime();

                        currentDate = i + "-" + numberFormat.format(i1 + 1) + "-" + numberFormat.format(i2);
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
