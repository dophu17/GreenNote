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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class ServiceActivity extends AppCompatActivity {

    ServiceDBHelper serviceDBHelper;
    ArrayList<Service> list;
    AdapterService adapter;

    Button btnNote, btnStatistical, btnService, btnOther;
    FloatingActionButton floatingActionButtonAdd;
    ListView lvService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        addControls();
        addEvents();
        readData();
        insertServiceSimple();
    }

    private void addControls() {
        lvService = (ListView) findViewById(R.id.lvService);
        list = new ArrayList<>();
        adapter = new AdapterService(this, list);
        lvService.setAdapter(adapter);
        floatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.floatingActionButtonAdd);
        btnNote = (Button) findViewById(R.id.btnNote);
        btnStatistical = (Button) findViewById(R.id.btnStatistical);
        btnService = (Button) findViewById(R.id.btnService);
        btnOther = (Button) findViewById(R.id.btnOther);
    }

    private void addEvents() {
        //add
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddServiceActivity.class);
                startActivity(intent);
            }
        });
        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
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

            }
        });
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OtherActivity.class);
                startActivity(intent);
            }
        });

        //update
        lvService.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = lvService.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    public void readData() {
        serviceDBHelper = new ServiceDBHelper(this);
        Cursor cursor = serviceDBHelper.getAll();
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(new Service(id, name));
        }
        adapter.notifyDataSetChanged();
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
                ContentValues contentValues = new ContentValues();
                serviceDBHelper = new ServiceDBHelper(this);
                serviceDBHelper.insert(serviceName[i]);
            }
        }
    }
}
