package tech.bloomgenetics.bloomapp;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mdric on 10/27/2016.
 */

public class ProjectListView extends ListView {
    /*
    SimpleAdapter adapter;
    List<Map<String, String>> listItems;

    HashMap<String, String> datum = new HashMap<String, String>();
*/
    ArrayList<Project> aP = new ArrayList<Project>();
    ProjectAdapter pA = new ProjectAdapter(this.getContext(),aP);

    public ProjectListView (Context ctx){

        super(ctx);
        init();
    }

    public ProjectListView (Context ctx,AttributeSet attrs){

        super(ctx,attrs);
        init();
    }

    public ProjectListView (Context ctx,AttributeSet attrs, int defStyleAttr){

        super(ctx,attrs,defStyleAttr);
        init();
    }


    private void init() {
        /*
        listItems=new ArrayList<>();
        datum.put("First Line", "First line of text");
        datum.put("Second Line","Second line of text");
        listItems.add(datum);
        adapter=new SimpleAdapter(this.getContext(), listItems,
                R.layout.sample_project_list_object,
                new String[] {"First Line", "Second Line", ""},
                new int[] {R.id.project_name, R.id.project_info}
        );
        */



        //this.(R.layout.sample_project_list_object);
        setAdapter(pA);
    }

    public void AddItem(String title, String role) {
/*
        projectListObject v = new projectListObject(this.getContext());

        TextView mProjectName = (TextView) v.findViewById(R.id.project_name);
        TextView mProjectInfo = (TextView) v.findViewById(R.id.project_info);
*/
        Project p = new Project();
        p.setTitle(title);
        p.setInfo(role);
        pA.add(p);
    }
}

// "beansprout123, Member"