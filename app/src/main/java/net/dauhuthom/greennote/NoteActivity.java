package net.dauhuthom.greennote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;
    ArrayList<Note> list;
    AdapterNote adapter;

    Button btnAddNote;
    ListView lvNote;

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
        lvNote = (ListView) findViewById(R.id.lvNote);
        list = new ArrayList<>();
        adapter = new AdapterNote(this, list);
        lvNote.setAdapter(adapter);
    }

    private void addEvents() {
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    public void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , service_id INTEGER, price DOUBLE, date VARCHAR DEFAULT (CURRENT_DATE) , description TEXT)");
        database.execSQL("CREATE TABLE IF NOT EXISTS services(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , name TEXT)");
        Cursor cursor = database.rawQuery("SELECT notes.*, services.name FROM notes LEFT JOIN services ON notes.service_id = services.id", null);
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
