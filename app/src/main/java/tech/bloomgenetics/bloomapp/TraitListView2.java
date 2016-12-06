package tech.bloomgenetics.bloomapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by mdric on 10/27/2016.
 */

public class TraitListView2 extends ListView {
    /*
    SimpleAdapter adapter;
    List<Map<String, String>> listItems;

    HashMap<String, String> datum = new HashMap<String, String>();
*/
    ArrayList<Trait> aT = new ArrayList<Trait>();
    TraitAdapter2 tA = new TraitAdapter2(this.getContext(),aT);

    public TraitListView2(Context ctx){

        super(ctx);
        init();
        //setOnItemClickListener(new ClickListener());
    }

    public TraitListView2(Context ctx, AttributeSet attrs){

        super(ctx,attrs);
        init();
        //setOnItemClickListener(new ClickListener());
    }

    public TraitListView2(Context ctx, AttributeSet attrs, int defStyleAttr){

        super(ctx,attrs,defStyleAttr);
        init();
        //setOnItemClickListener(new ClickListener());
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
        setAdapter(tA);
    }

    public void addItem(String name, String f2) {
/*
        projectListObject v = new projectListObject(this.getContext());

        TextView mProjectName = (TextView) v.findViewById(R.id.project_name);
        TextView mProjectInfo = (TextView) v.findViewById(R.id.project_info);
*/
        Trait t = new Trait();
        t.setName(name);
        t.setDR(f2);
        tA.add(t);
    }

    public void addItem2(String name, String dr, String c, String x) {
/*
        projectListObject v = new projectListObject(this.getContext());

        TextView mProjectName = (TextView) v.findViewById(R.id.project_name);
        TextView mProjectInfo = (TextView) v.findViewById(R.id.project_info);
*/
        Trait t = new Trait();
        t.setName(name);
        t.setDR(dr);
        t.setCarrier(c);
        t.setShower(x);
        tA.add(t);
    }

/*    class ClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.w("Clicked","" + id);

            Intent intent = new Intent(getContext(), CurrentCross.class);
            //startActivity(intent);
        }
    }   */
}