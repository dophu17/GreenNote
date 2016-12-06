package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    NoteDBHelper noteDBHelper;
    ServiceDBHelper serviceDBHelper;
    ArrayList<Note> list;
    AdapterNote adapter;
    Calendar calendar;
    Date date;
    String currentDate;
    Double totalToday = 0.0;

    ListView lvNote;
    TextView tvDate, tvTotal;
    FloatingActionButton floatingActionButtonAdd;
    Button btnNote, btnStatistical, btnService, btnOther, btnNextDate, btnPrevDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        addControls();
        addEvents();
        readData();
        insertServiceSimple();
    }

    private void addControls() {
        //date current
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
        currentDate = new Function().formatDate(strDate, "mm-dd-yyyy", "yyyy-mm-dd");

        lvNote = (ListView) findViewById(R.id.lvNote);
        list = new ArrayList<>();
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        adapter = new AdapterNote(this, list, currentDate, tvTotal);
        lvNote.setAdapter(adapter);
        tvDate = (TextView) findViewById(R.id.tvDate);
        floatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.floatingActionButtonAdd);
        btnNote = (Button) findViewById(R.id.btnNote);
        btnStatistical = (Button) findViewById(R.id.btnStatistical);
        btnService = (Button) findViewById(R.id.btnService);
        btnOther = (Button) findViewById(R.id.btnOther);
        btnNextDate = (Button) findViewById(R.id.btnNextDate);
        btnPrevDate = (Button) findViewById(R.id.btnPrevDate);
        tvDate.setText(strDate);
    }

    private void addEvents() {
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });
        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnStatistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatisticalActivity.class);
                startActivity(intent);
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
        btnNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, 1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
                tvDate.setText(simpleDateFormat.format(calendar.getTime()));
                currentDate = new Function().formatDate(simpleDateFormat.format(calendar.getTime()), "mm-dd-yyyy", "yyyy-mm-dd");

                readData();
            }
        });
        btnPrevDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
                tvDate.setText(simpleDateFormat.format(calendar.getTime()));
                currentDate = new Function().formatDate(simpleDateFormat.format(calendar.getTime()), "mm-dd-yyyy", "yyyy-mm-dd");

                readData();
            }
        });
    }

    public void readData() {
        noteDBHelper = new NoteDBHelper(this);
        Cursor cursor = noteDBHelper.getAllJoinByDate(currentDate);

        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int service_id = cursor.getInt(cursor.getColumnIndex("service_id"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String service_name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(new Note(id, service_id, price, date, description, service_name));
        }
        adapter.notifyDataSetChanged();

        noteDBHelper = new NoteDBHelper(this);
        totalToday = noteDBHelper.getSumToday(currentDate);
        tvTotal.setText(new Function().formatDecimal(adapter.getTotal(), "###,###,###,###,###", Locale.GERMANY) + " VND");
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
