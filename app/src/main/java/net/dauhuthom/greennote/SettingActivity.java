package net.dauhuthom.greennote;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    SettingDBHelper settingDBHelper;
    NoteDBHelper noteDBHelper;
    Calendar calendar;

    Button btnSave, btnSendEmail;
    EditText etEmail;
    RadioButton radioButtonLastMonth, radioButtonddmmyyyy, radioButtonmmddyyyy, radioButtonyyyymmdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        addControls();
        addEvents();
        initUI();
    }

    private void addControls() {
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSendEmail = (Button) findViewById(R.id.btnSendEmail);
        etEmail = (EditText) findViewById(R.id.etEmail);
        radioButtonLastMonth = (RadioButton) findViewById(R.id.radioButtonLastMonth);
        radioButtonddmmyyyy = (RadioButton) findViewById(R.id.radioButtonddmmyyyy);
        radioButtonmmddyyyy = (RadioButton) findViewById(R.id.radioButtonmmddyyyy);
        radioButtonyyyymmdd = (RadioButton) findViewById(R.id.radioButtonyyyymmdd);
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingDBHelper = new SettingDBHelper(getApplicationContext());
                Cursor cursor = settingDBHelper.getAll();

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String key = cursor.getString(cursor.getColumnIndex("key"));

                    //key = email
                    if (key.equals("email")) {
                        String email_key = "email";
                        String email_value = etEmail.getText().toString();
                        String email_default_value = null;
                        String email_description = null;
                        settingDBHelper.update(id, email_key, email_value, email_default_value, email_description);
                    }
                    //key = format_date
                    if (key.equals("format_date")) {
                        String format_date_key = "format_date";
                        String format_date_value = "ddmmyyyy";
                        if (radioButtonddmmyyyy.isChecked()) {
                            format_date_value = "ddmmyyyy";
                        } else if (radioButtonmmddyyyy.isChecked()) {
                            format_date_value = "mmddyyyy";
                        } else if (radioButtonyyyymmdd.isChecked()) {
                            format_date_value = "yyyymmdd";
                        }
                        String format_date_default_value = null;
                        String format_date_description = null;
                        settingDBHelper.update(id, format_date_key, format_date_value, format_date_default_value, format_date_description);
                    }
                }

                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
            }
        });
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    sendEmail();
                }
            }
        });
    }

    private void initUI() {
        settingDBHelper = new SettingDBHelper(getApplicationContext());
        Cursor cursor = settingDBHelper.getAll();

        while (cursor.moveToNext()) {
            String key = cursor.getString(cursor.getColumnIndex("key"));
            String value = cursor.getString(cursor.getColumnIndex("value"));
            //key = email
            if (key.equals("email")) {
                etEmail.setText(value);
            }
            //format_date
            if (key.equals("format_date")) {
                Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
                if (value.equals("ddmmyyyy")) {
                    radioButtonddmmyyyy.setChecked(true);
                } else if (value.equals("mmddyyyy")) {
                    radioButtonmmddyyyy.setChecked(true);
                } else if (value.equals("yyyymmdd")) {
                    radioButtonyyyymmdd.setChecked(true);
                }
            }
        }
    }

    private void sendEmail() {
        //get data body email
        noteDBHelper = new NoteDBHelper(this);
        Cursor cursor;
        cursor = noteDBHelper.getAllJoinNow();
        String emailTo = etEmail.getText().toString();

        calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        if (radioButtonLastMonth.isChecked()) {
            currentMonth = currentMonth - 1;
            cursor = noteDBHelper.getAllJoinLastMonth();
        }

        ArrayList tmp = new ArrayList();
        String str = "";
//        String str = "<table border='1'>";
        while (cursor.moveToNext()) {
            String date = new Function().formatDate(cursor.getString(cursor.getColumnIndex("date")), "yyyy-mm-dd", "mm-dd-yyyy");
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String price = new Function().formatDecimal(cursor.getDouble(cursor.getColumnIndex("price")), "###,###,###,###,###", Locale.GERMANY) + " VND";
            String description = cursor.getString(cursor.getColumnIndex("description"));
            if (description.length() > 0) {
                description = " (" + description + ")";
            }
//            str += "<tr>";
            if (tmp.contains(date)) {
                str += name + " (" + price + ") " + description + "<br />";
//                str += "<td>"  + cursor.getString(cursor.getColumnIndex("date")) + "</td>";
//                str += "<td>"  + cursor.getString(cursor.getColumnIndex("name")) + "</td>";
//                str += "<td>"  + new Function().formatDecimal(cursor.getDouble(cursor.getColumnIndex("price")), "###,###,###,###,###", Locale.GERMANY) + " VND" + ") " + "</td>";
//                str += "<td>"  + cursor.getString(cursor.getColumnIndex("description")) + "</td>";
            } else {
                tmp.add(date);
                str += "<br /><b>[" + date + "]</b><br />";
                str += name + " (" + price + ") " + description + "<br />";
//                str += "<td>"  + cursor.getString(cursor.getColumnIndex("date")) + "</td>";
//                str += "<td>"  + cursor.getString(cursor.getColumnIndex("name")) + "</td>";
//                str += "<td>"  + new Function().formatDecimal(cursor.getDouble(cursor.getColumnIndex("price")), "###,###,###,###,###", Locale.GERMANY) + " VND" + ") " + "</td>";
//                str += "<td>"  + cursor.getString(cursor.getColumnIndex("description")) + "</td>";
            }
//            str += "</tr>";
        }
//        str += "</table>";

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/html"); //text/plain -- message/rfc822 -- text/html
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        i.putExtra(Intent.EXTRA_SUBJECT, "Báo cáo chi tiêu tháng " + currentMonth);
        i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(str));
        try {
            startActivity(Intent.createChooser(i, "Chọn ứng dụng gửi email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SettingActivity.this, "Không thể gửi email vì không có ứng dụng gửi email đã cài đặt.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            etEmail.setError("Vui lòng nhập email!");
            return false;
        }

        return true;
    }
}
