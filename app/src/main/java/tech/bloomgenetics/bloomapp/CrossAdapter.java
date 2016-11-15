package tech.bloomgenetics.bloomapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mdric on 11.15.2016.
 */

public class CrossAdapter extends ArrayAdapter<Cross> {
    public CrossAdapter(Context ctx, ArrayList<Cross> ps) {
        super(ctx,0,ps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Cross c = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cross_list_object, parent, false);
        }
        // Lookup view for data population
        TextView cName = (TextView) convertView.findViewById(R.id.preview_cross_name);
        TextView cId = (TextView) convertView.findViewById(R.id.preview_cross_id);
        // Populate the data into the template view using the data object
        cName.setText(c.getName());
        cId.setText(String.valueOf(c.getId()));
        // Return the completed view to render on screen
        return convertView;
    }
}