package net.dauhuthom.greennote;

import android.app.Activity;
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

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;
    ArrayList<Note> list;
    AdapterNote adapter;

    Button btnToService, btnAddNote;
    ListView lvNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
        readData();
    }

    private void addEvents() {
        btnToService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                startActivity(intent);
            }
        });
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        btnToService = (Button) findViewById(R.id.btnToService);
        btnAddNote = (Button) findViewById(R.id.btnAddNote);
        lvNote = (ListView) findViewById(R.id.lvNote);
        list = new ArrayList<>();
        adapter = new AdapterNote(this, list);
        lvNote.setAdapter(adapter);
    }

    public void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , service_id INTEGER, price DOUBLE, date VARCHAR DEFAULT (CURRENT_DATE) , description TEXT)");
        Cursor cursor = database.rawQuery("SELECT * FROM notes", null);
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int service_id = cursor.getInt(1);
            double price = cursor.getDouble(2);
            String date = cursor.getString(3);
            String description = cursor.getString(4);
            list.add(new Note(id, service_id, price, date, description));
        }
        adapter.notifyDataSetChanged();
    }


}
