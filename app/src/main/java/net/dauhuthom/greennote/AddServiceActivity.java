package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddServiceActivity extends AppCompatActivity {

    ServiceDBHelper serviceDBHelper;

    EditText etServiceName;
    Button btnSaveService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        addControls();
        addEvents();
    }

    private void addEvents() {
        //insert
        btnSaveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    serviceDBHelper = new ServiceDBHelper(getApplicationContext());
                    String name = etServiceName.getText().toString();
                    long id = serviceDBHelper.insert(name);

                    Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void addControls() {
        etServiceName = (EditText) findViewById(R.id.etServiceName);
        btnSaveService = (Button) findViewById(R.id.btnSaveService);
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(etServiceName.getText().toString().trim())) {
            etServiceName.setError("Vui lòng nhập tên danh mục chi tiêu!");
            return false;
        }

        return true;
    }
}
