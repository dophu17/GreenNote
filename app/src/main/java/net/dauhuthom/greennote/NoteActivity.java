package net.dauhuthom.greennote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

    Button btnAddNote, btnBack;
    ListView lvNote;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        addControls();
        addEvents();
        readData();
    }

    private void addControls() {
        btnAddNote = (Button) findViewById(R.id.btnAddNote);
        btnBack = (Button) findViewById(R.id.btnBack);
        lvNote = (ListView) findViewById(R.id.lvNote);
        list = new ArrayList<>();
        adapter = new AdapterNote(this, list);
        lvNote.setAdapter(adapter);
        tvDate = (TextView) findViewById(R.id.tvDate);

        //date current
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
        tvDate.setText(strDate);
    }

    private void addEvents() {
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void readData() {
//        database = Database.initDatabase(this, DATABASE_NAME);
//        database.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , service_id INTEGER, price DOUBLE, date VARCHAR DATETIME, description TEXT)");
//        database.execSQL("CREATE TABLE IF NOT EXISTS services(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , name TEXT)");
        //Cursor cursor = database.rawQuery("SELECT notes.*, services.name FROM notes LEFT JOIN services ON notes.service_id = services.id WHERE date = date('now', 'localtime')", null);
        noteDBHelper = new NoteDBHelper(this);
        serviceDBHelper = new ServiceDBHelper(this);
        //Cursor cursor = database.rawQuery("SELECT notes.*, services.name FROM notes LEFT JOIN services ON notes.service_id = services.id", null);
        Cursor cursor = noteDBHelper.getAllJoinNow();

        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int service_id = cursor.getInt(cursor.getColumnIndex("service_id"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String date = cursor.getString(3);
            String description = cursor.getString(4);
            String service_name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(new Note(id, service_id, price, date, description, service_name));
        }
        adapter.notifyDataSetChanged();
    }
}
