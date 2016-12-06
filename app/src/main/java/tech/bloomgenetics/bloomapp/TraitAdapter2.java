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

public class TraitAdapter2 extends ArrayAdapter<Trait> {
    public TraitAdapter2(Context ctx, ArrayList<Trait> ps) {
        super(ctx, 0, ps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Trait t = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trait_list_object2, parent, false);
        }
        // Lookup view for data population
        TextView tName = (TextView) convertView.findViewById(R.id.trait_name);
        TextView tDominance = (TextView) convertView.findViewById(R.id.trait_dominance);
        TextView tCarry = (TextView) convertView.findViewById(R.id.trait_carry);
        TextView tExpress = (TextView) convertView.findViewById(R.id.trait_express);
        // Populate the data into the template view using the data object
        tName.setText(t.getName());
        tDominance.setText(String.valueOf(t.getDR()));
        tCarry.setText(String.valueOf(t.getCarrier()));
        tExpress.setText(String.valueOf(t.getShower()));
        // Return the completed view to render on screen
        return convertView;
    }
}
