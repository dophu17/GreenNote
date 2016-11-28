package net.dauhuthom.greennote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    ServiceDBHelper serviceDBHelper;

    Button btnToService, btnStatistical, btnToNote, btnAbout, btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
        insertServiceSimple();
    }

    private void addControls() {
        btnToService = (Button) findViewById(R.id.btnToService);
        btnToNote = (Button) findViewById(R.id.btnToNote);
        btnStatistical = (Button) findViewById(R.id.btnStatistical);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnSetting = (Button) findViewById(R.id.btnSetting);
    }

    private void addEvents() {
        btnToService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                startActivity(intent);
            }
        });
        btnToNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
            }
        });
        btnStatistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatisticalActivity.class);
                startActivity(intent);
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void insertServiceSimple() {
        serviceDBHelper = new ServiceDBHelper(this);
        Cursor cursor = serviceDBHelper.getAll();
        if (cursor.getCount() == 0) {
            String[] serviceName = {
                    "Ăn uống",
                    "Xem phim",
                    "Mua sắm",
                    "Cafe"
            };
            for (int i = 0; i < serviceName.length; i++) {
                ContentValues contentValues = new ContentValues();
                serviceDBHelper = new ServiceDBHelper(this);
                serviceDBHelper.insert(serviceName[i]);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
