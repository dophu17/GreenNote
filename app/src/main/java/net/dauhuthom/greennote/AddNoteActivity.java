package net.dauhuthom.greennote;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    Calendar calendar;
    Date date;

    EditText etCost, etDescription, etDate;
    Button btnSave, btnChangeDate;
    Spinner spinnerServiceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        addControls();
        addEvents();
    }

    private void addControls() {
        etCost = (EditText) findViewById(R.id.etCost);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
        etDate = (EditText) findViewById(R.id.etDate);
        spinnerServiceID = (Spinner) findViewById(R.id.spinnerServiceID);
        listService = new ArrayList<>();

        //load img_service
        database = Database.initDatabase(this, DATABASE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS services(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , name TEXT)");
        Cursor cursor = database.rawQuery("SELECT * FROM services", null);
        listService.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            listService.add(new Service(id, name));
        }
        //end load img_service

        adapter = new AdapterSpinnerService(this, listService);
        spinnerServiceID.setAdapter(adapter);

        //date current
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
        etDate.setText(strDate);
    }

    private void addEvents() {
        spinnerServiceID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = (Service) adapterView.getItemAtPosition(i);
                ServiceID = service.id;
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

        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        etDate.setText((i1 + 1) + "-" + i2 + "-" + i);
                        calendar.set(i, i1, i2);
                        date = calendar.getTime();
                    }
                };
                String string = etDate.getText() + "";
                //Lấy ra chuỗi của textView Date
                String strArrtmp[] = string.split("-");
                int day = Integer.parseInt(strArrtmp[1]);
                int month = Integer.parseInt(strArrtmp[0]) - 1;
                int year = Integer.parseInt(strArrtmp[2]);
                //Hiển thị ra Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNoteActivity.this,
                        onDateSetListener, year, month, day);
                datePickerDialog.setTitle("Change date");
                datePickerDialog.show();
            }
        });
    }

    private void insert() {
        String price = etCost.getText().toString();
        int service_id = ServiceID;
        String description = etDescription.getText().toString();
        String changeDate = etDate.getText() + "";

        ContentValues contentValues = new ContentValues();
        contentValues.put("price", price);
        contentValues.put("service_id", service_id);
        contentValues.put("description", description);
        contentValues.put("date", formatDate(changeDate));

        database = Database.initDatabase(this, DATABASE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , service_id INTEGER, price DOUBLE, date VARCHAR DEFAULT (CURRENT_DATE) , description TEXT)");
        database.insert("notes", null, contentValues);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private String formatDate(String date) {
        String strArrtmp[] = date.split("-");
        int day = Integer.parseInt(strArrtmp[1]);
        int month = Integer.parseInt(strArrtmp[0]);
        int year = Integer.parseInt(strArrtmp[2]);
        return year + "-" + month + "-" + day;
    }
}


//http://android-er.blogspot.com/2014/04/spinner-with-different-displat-text-and.html
//http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
