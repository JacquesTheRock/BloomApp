package tech.bloomgenetics.bloomapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mdric on 11/21/2016.
 */

public class TraitAdapter extends ArrayAdapter<Trait> {
    public TraitAdapter(Context ctx, ArrayList<Trait> ps) {
        super(ctx, 0, ps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Trait t = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trait_list_object, parent, false);
        }
        // Lookup view for data population
        TextView tName = (TextView) convertView.findViewById(R.id.trait_name);
        TextView tField2 = (TextView) convertView.findViewById(R.id.trait_f2);
        // Populate the data into the template view using the data object
        tName.setText(t.getName());
        tField2.setText(String.valueOf(t.getField2()));
        // Return the completed view to render on screen
        return convertView;
    }
}
