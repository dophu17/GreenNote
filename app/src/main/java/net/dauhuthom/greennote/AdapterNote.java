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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by phu on 11/7/2016.
 */

public class AdapterNote extends BaseAdapter {

    Activity context;
    ArrayList<Note> list;

    public AdapterNote(Activity context, ArrayList<Note> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.lv_row_note, null);
        TextView tvName = (TextView) row.findViewById(R.id.tvName);
        TextView tvPrice = (TextView) row.findViewById(R.id.tvPrice);
        Button btnEdit = (Button) row.findViewById(R.id.btnEdit);
        Button btnDelete = (Button) row.findViewById(R.id.btnDelete);

        final Note note = list.get(i);
        tvName.setText(note.service_name);
        tvPrice.setText(formatDecimal(note.price, "###,###,###,###,###", Locale.GERMANY) + " VND");

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditNoteActivity.class);
                intent.putExtra("id", note.id);
                context.startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure delete this note?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
                        noteDBHelper.delete(note.id);

                        Cursor cursor = noteDBHelper.getAll();
                        list.clear();
                        while (cursor.moveToNext()) {
                            int id = cursor.getInt(0);
                            int service_id = cursor.getInt(1);
                            double price = cursor.getDouble(2);
                            String date = cursor.getString(3);
                            String description = cursor.getString(4);
                            String service_name = cursor.getString(5);
                            list.add(new Note(id, service_id, price, date, description, service_name));
                        }
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    private String formatDecimal(double number, String format, Locale locale) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        DecimalFormat formatter = new DecimalFormat (format, otherSymbols);
        return formatter.format(number);
    }
}
