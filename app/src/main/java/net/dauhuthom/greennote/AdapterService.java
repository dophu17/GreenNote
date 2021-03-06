package net.dauhuthom.greennote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by DHTP on 11/6/2016.
 */

public class AdapterService extends BaseAdapter {
    Activity context;
    ArrayList<Service> list;

    public AdapterService(Activity context, ArrayList<Service> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.lv_row_service, null);
        TextView tvName = (TextView) row.findViewById(R.id.tvName);
        Button btnEdit = (Button) row.findViewById(R.id.btnEdit);
        Button btnDelete = (Button) row.findViewById(R.id.btnDelete);

        final Service service = list.get(i);
        tvName.setText(service.name);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditServiceActivity.class);
                intent.putExtra("id", service.id);
                context.startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa danh mục chi tiêu này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ServiceDBHelper serviceDBHelper = new ServiceDBHelper(context);
                        serviceDBHelper.delete(service.id);

                        serviceDBHelper = new ServiceDBHelper(context);
                        Cursor cursor = serviceDBHelper.getAll();
                        list.clear();
                        while (cursor.moveToNext()) {
                            int id = cursor.getInt(0);
                            String name = cursor.getString(1);
                            list.add(new Service(id, name));
                        }
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });

        return row;
    }
}
