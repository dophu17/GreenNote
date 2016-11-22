package net.dauhuthom.greennote;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by dhtp on 11/20/2016.
 */

public class PriceTextWatcher implements TextWatcher {

    private final DecimalFormat df;
    private final DecimalFormat dfnd;
    private final EditText et;
    private boolean hasFractionalPart;
    private int trailingZeroCount;
    private Locale locale;
    private String format = "###,###,###,###,###";

    public PriceTextWatcher(EditText editText) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale.GERMANY);

        df = new DecimalFormat(format, otherSymbols);
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat(format, otherSymbols);
        this.et = editText;
        hasFractionalPart = false;

        //DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        //DecimalFormat formatter = new DecimalFormat (format, otherSymbols);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int index = charSequence.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
        trailingZeroCount = 0;
        if (index > -1) {
            for (index++; index < charSequence.length(); index++) {
                if (charSequence.charAt(index) == '0')
                    trailingZeroCount++;
                else {
                    trailingZeroCount = 0;
                }
            }
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        et.removeTextChangedListener(this);

        if (editable != null && !editable.toString().isEmpty()) {
            try {
                int inilen, endlen;
                inilen = et.getText().length();
                //String v = editable.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "").replace("$","");
                String v = editable.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "").replace("","");
                Number n = df.parse(v);
                int cp = et.getSelectionStart();
                if (hasFractionalPart) {
                    StringBuilder trailingZeros = new StringBuilder();
                    while (trailingZeroCount-- > 0)
                        trailingZeros.append('0');
                    et.setText(df.format(n) + trailingZeros.toString());
                } else {
                    et.setText(dfnd.format(n));
                }
                //et.setText("$".concat(et.getText().toString()));
                et.setText("".concat(et.getText().toString()));
                endlen = et.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel < et.getText().length()) {
                    et.setSelection(sel);
                } else if (trailingZeroCount > -1) {
                    //et.setSelection(et.getText().length() - 3);
                    et.setSelection(et.getText().length());
                } else {
                    et.setSelection(et.getText().length());
                }
            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
        }

        et.addTextChangedListener(this);
    }
}

//stackoverflow.com/questions/33319898/android-format-input-currency-with-2-decimal-format
