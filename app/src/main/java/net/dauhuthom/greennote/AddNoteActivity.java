package net.dauhuthom.greennote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;
    List<Service> listService = null;
    ArrayAdapter<Service> arrayAdapter;

    EditText etCost, etDescription;
    Button btnSave;
    Spinner spinnerServiceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        addControls();
        addEvents();
    }

    private void addEvents() {

    }

    private void addControls() {
        etCost = (EditText) findViewById(R.id.etCost);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnSave = (Button) findViewById(R.id.btnSave);
        spinnerServiceID = (Spinner) findViewById(R.id.spinnerServiceID);
        listService = new ArrayList<>();

        //
        database = Database.initDatabase(this, DATABASE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS services(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , name TEXT)");
        Cursor cursor = database.rawQuery("SELECT * FROM services", null);
        listService.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            listService.add(new Service(id, name));
        }
        //arrayAdapter.notifyDataSetChanged();
        //

        arrayAdapter = new ArrayAdapter<Service>(this, android.R.layout.simple_spinner_item, listService);
        spinnerServiceID.setAdapter(arrayAdapter);
    }
}


//http://android-er.blogspot.com/2014/04/spinner-with-different-displat-text-and.html
