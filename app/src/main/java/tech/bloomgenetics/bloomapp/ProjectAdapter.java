package tech.bloomgenetics.bloomapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mdric on 10/29/2016.
 */

public class ProjectAdapter extends ArrayAdapter<Project> {
    public ProjectAdapter(Context ctx, ArrayList<Project> ps) {
        super(ctx,0,ps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Project p = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sample_project_list_object, parent, false);
        }
        // Lookup view for data population
        TextView pName = (TextView) convertView.findViewById(R.id.project_name);
        TextView pRole = (TextView) convertView.findViewById(R.id.project_role);
        // Populate the data into the template view using the data object
        pName.setText(p.getTitle());
        pRole.setText(p.getRole());
        // Return the completed view to render on screen
        return convertView;
    }
}
