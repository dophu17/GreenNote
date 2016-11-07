package net.dauhuthom.greennote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;
    ArrayList<Service> list;
    AdapterService adapter;

    Button btnAddService, btnBack;
    ListView lvService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        addControls();
        addEvents();
        readData();
    }

    private void addEvents() {
        //add
        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddServiceActivity.class);
                startActivity(intent);
            }
        });
        //back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    private void addControls() {
        btnAddService = (Button) findViewById(R.id.btnAddService);
        btnBack = (Button) findViewById(R.id.btnBack);
        lvService = (ListView) findViewById(R.id.lvService);
        list = new ArrayList<>();
        adapter = new AdapterService(this, list);
        lvService.setAdapter(adapter);
    }

    public void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM services", null);
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            list.add(new Service(id, name));
        }
        adapter.notifyDataSetChanged();
    }
}
