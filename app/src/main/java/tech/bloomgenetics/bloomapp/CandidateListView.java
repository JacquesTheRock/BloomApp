package tech.bloomgenetics.bloomapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class CandidateListView extends ListView {
    /*
    SimpleAdapter adapter;
    List<Map<String, String>> listItems;

    HashMap<String, String> datum = new HashMap<String, String>();
*/
    ArrayList<Candidate> aCand = new ArrayList<Candidate>();
    CandidateAdapter candA = new CandidateAdapter(this.getContext(),aCand);

    public CandidateListView (Context ctx){

        super(ctx);
        init();
        setOnItemClickListener(new ClickListener());
    }

    public CandidateListView (Context ctx,AttributeSet attrs){

        super(ctx,attrs);
        init();
        setOnItemClickListener(new ClickListener());
    }

    public CandidateListView (Context ctx,AttributeSet attrs, int defStyleAttr){

        super(ctx,attrs,defStyleAttr);
        init();
        setOnItemClickListener(new ClickListener());
    }


    private void init() {

        setAdapter(candA);

    }

    public void AddItem(String name, int id) {
/*
        projectListObject v = new projectListObject(this.getContext());

        TextView mProjectName = (TextView) v.findViewById(R.id.project_name);
        TextView mProjectInfo = (TextView) v.findViewById(R.id.project_info);
*/
        Candidate c = new Candidate();
        c.setName(name);
        c.setId(id);
        candA.add(c);
    }

    class ClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.w("Clicked","" + id);

            Intent intent = new Intent(getContext(), CurrentCandidate.class);
            //startActivity(intent);
        }
    }
}