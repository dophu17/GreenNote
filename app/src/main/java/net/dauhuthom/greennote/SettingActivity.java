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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    SettingDBHelper settingDBHelper;
    NoteDBHelper noteDBHelper;

    Button btnSave, btnSendEmail;
    EditText etEmail;

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
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingDBHelper = new SettingDBHelper(getApplicationContext());
                Cursor cursor = settingDBHelper.getAll();

                while (cursor.moveToNext()) {
                    String key = cursor.getString(cursor.getColumnIndex("key"));

                    //key = email
                    if (key.equals("email")) {
                        String email_key = "email";
                        String email_value = etEmail.getText().toString();
                        String email_default_value = null;
                        String email_description = null;
                        settingDBHelper.update(cursor.getInt(cursor.getColumnIndex("id")), email_key, email_value, email_default_value, email_description);
                    }
                }

                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
            }
        });
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
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
        }
    }

    private void sendEmail() {
        //get data body email
        noteDBHelper = new NoteDBHelper(this);
        Cursor cursor = noteDBHelper.getAllJoinLastMonth();
        String emailTo = etEmail.getText().toString();
        if (emailTo.length() <= 0) {
            emailTo = "dophu17@gmail.com";
        }

        ArrayList tmp = new ArrayList();
        String str = "";
//        String str = "<table border='1'>";
        while (cursor.moveToNext()) {
            String date = new Function().formatDate(cursor.getString(cursor.getColumnIndex("date")), "mm-dd-yyyy", "yyyy-mm-dd");
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String price = new Function().formatDecimal(cursor.getDouble(cursor.getColumnIndex("price")), "###,###,###,###,###", Locale.GERMANY) + " VND";
            String description = cursor.getString(cursor.getColumnIndex("description"));
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
        i.putExtra(Intent.EXTRA_SUBJECT, "The report is detail statistical last month");
        i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(str));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SettingActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
