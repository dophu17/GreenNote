package net.dauhuthom.greennote;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by phu on 11/8/2016.
 */

public class AdapterSpinnerService extends ArrayAdapter<Service> {
    public AdapterSpinnerService(Context context, int resource) {
        super(context, resource);
    }

    public AdapterSpinnerService(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public AdapterSpinnerService(Context context, int resource, Service[] objects) {
        super(context, resource, objects);
    }

    public AdapterSpinnerService(Context context, int resource, int textViewResourceId, Service[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public AdapterSpinnerService(Context context, int resource, List<Service> objects) {
        super(context, resource, objects);
    }

    public AdapterSpinnerService(Context context, int resource, int textViewResourceId, List<Service> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
