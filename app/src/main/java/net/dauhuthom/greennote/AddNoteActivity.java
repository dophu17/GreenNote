package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.attr.name;

public class AddNoteActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;
    ArrayList<Service> listService;
    AdapterSpinnerService adapter;
    int ServiceID = -1;

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
        spinnerServiceID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = (Service) adapterView.getItemAtPosition(i);
                ServiceID = service.id;
                Toast.makeText(adapterView.getContext(), service.name + "-" + service.id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });
    }

    private void addControls() {
        etCost = (EditText) findViewById(R.id.etCost);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnSave = (Button) findViewById(R.id.btnSave);
        spinnerServiceID = (Spinner) findViewById(R.id.spinnerServiceID);
        listService = new ArrayList<>();

        //load service
        database = Database.initDatabase(this, DATABASE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS services(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , name TEXT)");
        Cursor cursor = database.rawQuery("SELECT * FROM services", null);
        listService.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            listService.add(new Service(id, name));
        }
        //end load service

        adapter = new AdapterSpinnerService(this, listService);
        spinnerServiceID.setAdapter(adapter);
    }

    private void insert() {
        String price = etCost.getText().toString();
        int service_id = ServiceID;
        String description = etDescription.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("price", price);
        contentValues.put("service_id", service_id);
        contentValues.put("description", description);
        contentValues.put("date", getDateTime());

        database = Database.initDatabase(this, DATABASE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , service_id INTEGER, price DOUBLE, date VARCHAR DEFAULT (CURRENT_DATE) , description TEXT)");
        database.insert("notes", null, contentValues);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateNow = new Date();
        return dateFormat.format(dateNow);
    }
}


//http://android-er.blogspot.com/2014/04/spinner-with-different-displat-text-and.html
//http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
