package net.dauhuthom.greennote;

import android.content.Context;
import android.database.Cursor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by phu on 11/28/2016.
 */

public class Function {
    //format date
    public String formatDate(String date, String returnType) {
        int day = 0;
        int month = 0;
        int year = 0;
        String output = null;
        if (returnType.length() <= 0) {
            returnType = "yyyy-MM-dd";
        }

        NumberFormat numberFormat = new DecimalFormat("00");
        String strArrtmp[] = date.split("-");

        //input date
        if (returnType == "yyyy-MM-dd") {
            day = Integer.parseInt(strArrtmp[2]);
            month = Integer.parseInt(strArrtmp[1]);
            year = Integer.parseInt(strArrtmp[0]);
        } else if (returnType == "MM-dd-yyyy") {
            day = Integer.parseInt(strArrtmp[1]);
            month = Integer.parseInt(strArrtmp[0]);
            year = Integer.parseInt(strArrtmp[2]);
        } else {
            day = Integer.parseInt(strArrtmp[0]);
            month = Integer.parseInt(strArrtmp[1]);
            year = Integer.parseInt(strArrtmp[2]);
        }

        //output date
        if (returnType == "yyyy-MM-dd") {
            output = year + "-" + numberFormat.format(month) + "-" + numberFormat.format(day);
        } else if (returnType == "MM-dd-yyyy") {
            output = numberFormat.format(month) + "-" + numberFormat.format(day) + "-" + year;
        } else {
            output = numberFormat.format(day) + "-" + numberFormat.format(month) + "-" + year;
        }

        return output;
    }

    //format money
    public String formatDecimal(double number, String format, Locale locale) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        DecimalFormat formatter = new DecimalFormat (format, otherSymbols);
        return formatter.format(number);
    }

    //get default format date from database
    public String getDefaultFormatDate(Context context) {
        String result = "ddmmyyyy";
        SettingDBHelper settingDBHelper = new SettingDBHelper(context);
        Cursor cursor = settingDBHelper.getAll();
        while (cursor.moveToNext()) {
            String key = cursor.getString(cursor.getColumnIndex("key"));
            String value = cursor.getString(cursor.getColumnIndex("value"));
            if (key.equals("format_date")) {
                if (value.equals("mmddyyyy")) {
                    result = "MM-dd-yyyy";
                } else if (value.equals("yyyymmdd")) {
                    result = "yyyy-MM-dd";
                } else {
                    result = "dd-MM-yyyy";
                }
            }
        }

        return result;
    }

    public int getDay(String date, String returnType) {
        int day = 0;
        String strArrtmp[] = date.split("-");

        //input date
        if (returnType == "yyyy-MM-dd") {
            day = Integer.parseInt(strArrtmp[2]);
        } else if (returnType == "MM-dd-yyyy") {
            day = Integer.parseInt(strArrtmp[1]);
        } else {
            day = Integer.parseInt(strArrtmp[0]);
        }

        return day;
    }

    public int getMonth(String date, String returnType) {
        int month = 0;
        String strArrtmp[] = date.split("-");

        //input date
        if (returnType == "yyyy-MM-dd") {
            month = Integer.parseInt(strArrtmp[1]);
        } else if (returnType == "MM-dd-yyyy") {
            month = Integer.parseInt(strArrtmp[0]);
        } else {
            month = Integer.parseInt(strArrtmp[1]);
        }

        return month;
    }

    public int getYear(String date, String returnType) {
        int year = 0;
        String strArrtmp[] = date.split("-");

        //input date
        if (returnType == "yyyy-MM-dd") {
            year = Integer.parseInt(strArrtmp[0]);
        } else if (returnType == "MM-dd-yyyy") {
            year = Integer.parseInt(strArrtmp[2]);
        } else {
            year = Integer.parseInt(strArrtmp[2]);
        }

        return year;
    }

    public String formatFromyyyyMMdd(String date, String returnType) {
        int day = 0;
        int month = 0;
        int year = 0;
        String output = null;
        NumberFormat numberFormat = new DecimalFormat("00");
        String strArrtmp[] = date.split("-");

        //input date
        day = Integer.parseInt(strArrtmp[2]);
        month = Integer.parseInt(strArrtmp[1]);
        year = Integer.parseInt(strArrtmp[0]);

        //output date
        if (returnType == "yyyy-MM-dd") {
            output = year + "-" + numberFormat.format(month) + "-" + numberFormat.format(day);
        } else if (returnType == "MM-dd-yyyy") {
            output = numberFormat.format(month) + "-" + numberFormat.format(day) + "-" + year;
        } else {
            output = numberFormat.format(day) + "-" + numberFormat.format(month) + "-" + year;
        }

        return output;
    }

    public String formatToyyyyMMdd(String date, String returnType) {
        int day = 0;
        int month = 0;
        int year = 0;
        String output = null;
        NumberFormat numberFormat = new DecimalFormat("00");
        String strArrtmp[] = date.split("-");

        //input date
        if (returnType == "yyyy-MM-dd") {
            day = Integer.parseInt(strArrtmp[2]);
            month = Integer.parseInt(strArrtmp[1]);
            year = Integer.parseInt(strArrtmp[0]);
        } else if (returnType == "MM-dd-yyyy") {
            day = Integer.parseInt(strArrtmp[1]);
            month = Integer.parseInt(strArrtmp[0]);
            year = Integer.parseInt(strArrtmp[2]);
        } else {
            day = Integer.parseInt(strArrtmp[0]);
            month = Integer.parseInt(strArrtmp[1]);
            year = Integer.parseInt(strArrtmp[2]);
        }

        //output date
        output = year + "-" + numberFormat.format(month) + "-" + numberFormat.format(day);

        return output;
    }
}
