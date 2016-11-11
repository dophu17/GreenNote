package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
        //Service service2 = (Service) spinnerServiceID.getItemAtPosition(1);
        //Toast.makeText(this, cursor.getInt(cursor.getColumnIndex("service_id")) + "--" + service2.id, Toast.LENGTH_LONG).show();
        for (int i = 0; i < spinnerServiceID.getCount(); i++) {
            Service service = (Service) spinnerServiceID.getItemAtPosition(1);
            Toast.makeText(this, cursor.getInt(cursor.getColumnIndex("service_id")) + "--" + service.id , Toast.LENGTH_LONG).show();
            String serviceIdNote = cursor.getInt(cursor.getColumnIndex("service_id")) + "";
            String serviceIdList = service.id + "";
            if (serviceIdNote.equals(serviceIdList)) {
                spinnerServiceID.setSelection(i);
                //Toast.makeText(this, spinnerServiceID.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, service.id + "--" + cursor.getInt(cursor.getColumnIndex("service_id")), Toast.LENGTH_LONG).show();
                //break;
            }
        }
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
        //insert
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    private void update() {
        String price = etPrice.getText().toString();
        String description = etDescription.getText().toString();
        String date = etDate.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("price", price);
        contentValues.put("description", description);
        contentValues.put("date", date);
        contentValues.put("service_id", ServiceID);

        database = Database.initDatabase(this, DATABASE_NAME);
        database.update("notes", contentValues, "id = ?", new String[]{id + ""});
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
