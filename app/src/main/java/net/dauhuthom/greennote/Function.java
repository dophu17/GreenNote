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
        String day = null, month = null, year = null, output = null;
        if (type.length() <= 0) {
            type = "yyyy-mm-dd";
        }

        NumberFormat numberFormat = new DecimalFormat("00");
        String strArrtmp[] = date.split("-");

        //input date
        if (type == "yyyy-mm-dd") {
            day = strArrtmp[2];
            month = strArrtmp[1];
            year = strArrtmp[0];
        } else if (type == "mm-dd-yyyy") {
            day = strArrtmp[1];
            month = strArrtmp[0];
            year = strArrtmp[2];
        } else if (type == "dd-mm-yyyy") {
            day = strArrtmp[0];
            month = strArrtmp[1];
            year = strArrtmp[2];
        }

        //output date
        if (returnType == "yyyy-mm-dd") {
            output = year + "-" + month + "-" + day;
        } else if (returnType == "mm-dd-yyyy") {
            output = month + "-" + day + "-" + year;
        } else if (returnType == "dd-mm-yyyy") {
            output = day + "-" + month + "-" + day;
        }

        return output;
    }

    public String formatDecimal(double number, String format, Locale locale) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        DecimalFormat formatter = new DecimalFormat (format, otherSymbols);
        return formatter.format(number);
    }
}
