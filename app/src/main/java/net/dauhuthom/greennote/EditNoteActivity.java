package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class EditNoteActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;
    ArrayList<Service> listService;
    AdapterSpinnerService adapter;
    int id = -1;
    int ServiceID = -1;

    EditText etPrice, etDescription, etDate;
    Button btnSave;
    Spinner spinnerServiceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        addControls();
        initUI();
        addEvents();
    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM notes WHERE id = ?", new String[]{id + ""});
        cursor.moveToFirst();
        String price = cursor.getString(cursor.getColumnIndex("price"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        etPrice.setText(price);
        etDescription.setText(description);
        etDate.setText(date);

        //get service

    }

    private void addEvents() {
        //insert
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    private void addControls() {
        etPrice = (EditText) findViewById(R.id.etCost);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etDate = (EditText) findViewById(R.id.etDate);
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

    private void update() {
        String price = etPrice.getText().toString();
        String description = etDescription.getText().toString();
        String date = etDate.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("price", price);
        contentValues.put("description", description);
        contentValues.put("date", date);

        database = Database.initDatabase(this, DATABASE_NAME);
        database.update("notes", contentValues, "id = ?", new String[]{id + ""});
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
