package net.dauhuthom.greennote;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditNoteActivity extends AppCompatActivity {

    NoteDBHelper noteDBHelper;
    ServiceDBHelper serviceDBHelper;
    ArrayList<Service> listService;
    AdapterSpinnerService adapter;
    int id = -1;
    int ServiceID = -1;
    Calendar calendar;
    Date date;

    EditText etPrice, etDescription, etDate;
    Button btnSave, btnChangeDate;
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
        etPrice = (EditText) findViewById(R.id.etPrice);
        etPrice.addTextChangedListener(new PriceTextWatcher(etPrice));
        etDescription = (EditText) findViewById(R.id.etDescription);
        etDate = (EditText) findViewById(R.id.etDate);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
        spinnerServiceID = (Spinner) findViewById(R.id.spinnerServiceID);
        listService = new ArrayList<>();

        //load img_service
        serviceDBHelper = new ServiceDBHelper(getApplicationContext());
        Cursor cursor = serviceDBHelper.getAll();
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
        simpleDateFormat = new SimpleDateFormat(new Function().getDefaultFormatDate(getBaseContext()), Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
        etDate.setText(new Function().formatDate(strDate, new Function().getDefaultFormatDate(getBaseContext())));
    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        noteDBHelper = new NoteDBHelper(getApplicationContext());
        Cursor cursor = noteDBHelper.get(id);
        cursor.moveToFirst();
        double price = cursor.getDouble(cursor.getColumnIndex("price"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        etPrice.setText(new Function().formatDecimal(price, "###,###,###,###,###", Locale.GERMANY));
        etDescription.setText(description);
        etDate.setText(new Function().formatFromyyyyMMdd(date, new Function().getDefaultFormatDate(getBaseContext())));
//        etDate.setText(date);

        //get img_service
        int serviceIdNote = cursor.getInt(cursor.getColumnIndex("service_id"));
        for (int i = 0; i < spinnerServiceID.getCount(); i++) {
            Service service = (Service) spinnerServiceID.getItemAtPosition(i);
            int serviceIdList = service.id;
            if (serviceIdNote == serviceIdList) {
                spinnerServiceID.setSelection(i);
                break;
            }
        }
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

        //update
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    String strPrice = etPrice.getText().toString();
                    strPrice = strPrice.replace(".", "");
                    double price = Double.parseDouble(strPrice);
                    String description = etDescription.getText().toString();
                    String changeDate = etDate.getText() + "";
                    changeDate = new Function().formatToyyyyMMdd(changeDate, new Function().getDefaultFormatDate(getBaseContext()));
                    noteDBHelper = new NoteDBHelper(getApplicationContext());
                    noteDBHelper.update(id, ServiceID, price, changeDate, description);

                    Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        etDate.setText(new Function().formatFromyyyyMMdd(i + "-" + (i1 + 1) + "-" + i2, new Function().getDefaultFormatDate(getBaseContext())));
                        calendar.set(i, i1, i2);
                        date = calendar.getTime();
                    }
                };
                String string = etDate.getText() + "";
                //Lấy ra chuỗi của textView Date
                int day = new Function().getDay(string, new Function().getDefaultFormatDate(getBaseContext()));
                int month = new Function().getMonth(string, new Function().getDefaultFormatDate(getBaseContext())) - 1;
                int year = new Function().getYear(string, new Function().getDefaultFormatDate(getBaseContext()));
                //Hiển thị ra Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditNoteActivity.this,
                        onDateSetListener, year, month, day);
                datePickerDialog.setTitle("Change date");
                datePickerDialog.show();
            }
        });
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(etPrice.getText().toString().trim())) {
            etPrice.setError("Vui lòng nhập số tiền!");
            return false;
        }

        return true;
    }
}

//http://stackoverflow.com/questions/1739734/using-decimalformat-keeping-extra-zeros
