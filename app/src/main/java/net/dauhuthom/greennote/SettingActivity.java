package net.dauhuthom.greennote;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    SettingDBHelper settingDBHelper;

    Button btnSave;
    EditText etEmail;
    Switch switchAutoSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        insertSettingSimple();
        addControls();
        addEvents();
        initUI();
    }

    private void insertSettingSimple() {
        settingDBHelper = new SettingDBHelper(this);
        Cursor cursor = settingDBHelper.getAll();
        if (cursor.getCount() == 0) {
            String[] settingsKey = {
                    "email",
                    "autosend"
            };
            for (int i = 0; i < settingsKey.length; i++) {
                ContentValues contentValues = new ContentValues();
                settingDBHelper = new SettingDBHelper(this);
                if (settingsKey[i].equals("autosend")) {
                    settingDBHelper.insert(settingsKey[i], "no", null, null);
                } else {
                    settingDBHelper.insert(settingsKey[i], null, null, null);
                }

            }
        }
//        settingDBHelper = new SettingDBHelper(this);
//        Cursor cursor1 = settingDBHelper.getAll();
//        Toast.makeText(this, cursor1.getCount() + "", Toast.LENGTH_SHORT).show();
    }

    private void addControls() {
        btnSave = (Button) findViewById(R.id.btnSave);
        etEmail = (EditText) findViewById(R.id.etEmail);
        switchAutoSend = (Switch) findViewById(R.id.switchAutoSend);
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

                    //key = autosend
                    if (key.equals("autosend")) {
                        String autosend_key = "autosend";
                        String autosend_value = "no";
                        String autosend_default_value = null;
                        String autosend_description = null;
                        if (switchAutoSend.isChecked()) {
                            autosend_value = "yes";
                        }
                        settingDBHelper.update(cursor.getInt(cursor.getColumnIndex("id")), autosend_key, autosend_value, autosend_default_value, autosend_description);
                    }
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                //send email
                composeEmail();
            }
        });
    }

    private void initUI() {
        settingDBHelper = new SettingDBHelper(getApplicationContext());
        Cursor cursor = settingDBHelper.getAll();

        while (cursor.moveToNext()) {
            String key = cursor.getString(cursor.getColumnIndex("key"));
            String value = cursor.getString(cursor.getColumnIndex("value"));
            //if (key.length() > 0 && value.length() > 0) {
                //key = email
                if (key.equals("email")) {
                    etEmail.setText(value);
                }

                //key = autosend
                if (key.equals("autosend")) {
                        if (value.equals("yes")) {
                            switchAutoSend.setChecked(true);
                        } else {
                            switchAutoSend.setChecked(false);
                        }
                }
           //}

        }
    }

    private void composeEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("message/rfc822");

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
        intent.putExtra(Intent.EXTRA_TEXT,    "Body of email");
        intent.setData(Uri.parse("dophu17@gmail.com")); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        //startActivity(intent);
    }
}
