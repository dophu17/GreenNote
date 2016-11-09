package net.dauhuthom.greennote;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phu on 11/8/2016.
 */

public class AdapterSpinnerService extends BaseAdapter {

    Activity context;
    ArrayList<Service> list;

    public AdapterSpinnerService(Activity context, ArrayList<Service> list) {
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
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.spinner_row_service, null);

        TextView tvServiceName = (TextView) row.findViewById(R.id.tvServiceName);

        final Service service = list.get(i);
        tvServiceName.setText(service.name);

        return row;
    }
}
