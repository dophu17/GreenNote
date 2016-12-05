package net.dauhuthom.greennote;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by phu on 11/28/2016.
 */

public class Function {
    public String formatDate(String date, String type, String returnType) {
        int day = 0;
        int month = 0;
        int year = 0;
        String output = null;
        if (type.length() <= 0) {
            type = "yyyy-mm-dd";
        }

        NumberFormat numberFormat = new DecimalFormat("00");
        String strArrtmp[] = date.split("-");

        //input date
        if (type == "yyyy-mm-dd") {
            day = Integer.parseInt(strArrtmp[2]);
            month = Integer.parseInt(strArrtmp[1]);
            year = Integer.parseInt(strArrtmp[0]);
        } else if (type == "mm-dd-yyyy") {
            day = Integer.parseInt(strArrtmp[1]);
            month = Integer.parseInt(strArrtmp[0]);
            year = Integer.parseInt(strArrtmp[2]);
        } else if (type == "dd-mm-yyyy") {
            day = Integer.parseInt(strArrtmp[0]);
            month = Integer.parseInt(strArrtmp[1]);
            year = Integer.parseInt(strArrtmp[2]);
        }

        //output date
        if (returnType == "yyyy-mm-dd") {
            output = year + "-" + numberFormat.format(month) + "-" + numberFormat.format(day);
        } else if (returnType == "mm-dd-yyyy") {
            output = numberFormat.format(month) + "-" + numberFormat.format(day) + "-" + year;
        } else if (returnType == "dd-mm-yyyy") {
            output = day + "-" + numberFormat.format(month) + "-" + numberFormat.format(day);
        }

        return output;
    }

    public String formatDecimal(double number, String format, Locale locale) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        DecimalFormat formatter = new DecimalFormat (format, otherSymbols);
        return formatter.format(number);
    }
}
