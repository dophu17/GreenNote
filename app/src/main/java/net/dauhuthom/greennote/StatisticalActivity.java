package net.dauhuthom.greennote;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatisticalActivity extends AppCompatActivity {

    NoteDBHelper noteDBHelper;
    Calendar calendar;
    Date date;
    String currentDate = null;
    double totalToday = 0, totalYesterday = 0, totalThisMonth = 0, totalLastMonth = 0;

    //private PieChart mChart;


    TextView tvChangeDate;//, tvToday, tvThisMonth, tvYesterday, tvLastMonth, tvWarningDay, tvWarningMonth;
    Button btnChangeDate, btnNote, btnStatistical, btnService, btnOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);

        addControls();
        readData();
        addEvents();
        drawChart();
    }

    private void addControls() {
//        tvToday = (TextView) findViewById(R.id.tvToday);
//        tvThisMonth = (TextView) findViewById(R.id.tvThisMonth);
//        tvYesterday = (TextView) findViewById(R.id.tvYesterday);
//        tvLastMonth = (TextView) findViewById(R.id.tvLastMonth);
//        tvWarningDay = (TextView) findViewById(R.id.tvWarningDay);
//        tvWarningMonth = (TextView) findViewById(R.id.tvWarningMonth);
//        tvChangeDate = (TextView) findViewById(R.id.tvChangeDate);
        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
        btnNote = (Button) findViewById(R.id.btnNote);
        btnStatistical = (Button) findViewById(R.id.btnStatistical);
        btnService = (Button) findViewById(R.id.btnService);
        btnOther = (Button) findViewById(R.id.btnOther);

        //date current
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
//        tvChangeDate.setText(strDate);

        //set current date for sql
        SimpleDateFormat simpleDateFormatCurrent = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = simpleDateFormatCurrent.format(calendar.getTime());
    }

    public void readData() {
        noteDBHelper = new NoteDBHelper(this);
        //today
        Cursor cursor = noteDBHelper.getSumToday(currentDate);
        if (cursor.moveToFirst()) {
//            tvToday.setText(new Function().formatDecimal(cursor.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalToday = cursor.getDouble(0);
        }
        //this month
        Cursor cursorThisMonth = noteDBHelper.getSumThisMonth(currentDate);
        if (cursorThisMonth.moveToFirst()) {
//            tvThisMonth.setText(new Function().formatDecimal(cursorThisMonth.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalThisMonth = cursorThisMonth.getDouble(0);
        }
        //yesterday
        Cursor cursorYesterday = noteDBHelper.getSumYesterday(currentDate);
        if (cursorYesterday.moveToFirst()) {
//            tvYesterday.setText(new Function().formatDecimal(cursorYesterday.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalYesterday = cursorYesterday.getDouble(0);
        }
        //last month
        Cursor cursorLastMonth = noteDBHelper.getSumLastMonth(currentDate);
        if (cursorLastMonth.moveToFirst()) {
//            tvLastMonth.setText(new Function().formatDecimal(cursorLastMonth.getDouble(0), "###,###,###,###,###", Locale.GERMANY) + " VND");
            totalLastMonth = cursorLastMonth.getDouble(0);
        }

        //warning
        if (totalToday <= totalYesterday) {
//            tvWarningDay.setText("Great! Save than yesterday " + new Function().formatDecimal(totalYesterday - totalToday, "###,###,###,###,###", Locale.GERMANY) + " VND");
//            tvWarningDay.setTextColor(ContextCompat.getColor(this, R.color.colorBackground));
        } else {
//            tvWarningDay.setText("Bad! Waste than yesterday " + new Function().formatDecimal(totalToday - totalYesterday, "###,###,###,###,###", Locale.GERMANY) + " VND");
//            tvWarningDay.setTextColor(ContextCompat.getColor(this, R.color.colorWarning));
        }
        if (totalThisMonth <= totalLastMonth) {
//            tvWarningMonth.setText("Great! Save than last month " + new Function().formatDecimal(totalLastMonth - totalThisMonth, "###,###,###,###,###", Locale.GERMANY) + " VND");
//            tvWarningMonth.setTextColor(ContextCompat.getColor(this, R.color.colorBackground));
        } else {
//            tvWarningMonth.setText("Bad! Waste than last month " + new Function().formatDecimal(totalThisMonth - totalLastMonth, "###,###,###,###,###", Locale.GERMANY) + " VND");
//            tvWarningMonth.setTextColor(ContextCompat.getColor(this, R.color.colorWarning));
        }
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
        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
            }
        });
        btnStatistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                startActivity(intent);
            }
        });
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OtherActivity.class);
                startActivity(intent);
            }
        });
    }

    private void drawChart() {
        RelativeLayout mainLayout;

        float[] yData = {5, 10, 15, 30, 40};
        String[] xData = {"Sony", "Xiaomi", "Sumsung", "LG", "Iphone"};
    }
}
