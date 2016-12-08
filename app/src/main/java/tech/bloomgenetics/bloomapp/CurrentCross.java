package tech.bloomgenetics.bloomapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CurrentCross extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProjectCandidateSearch candTask;
    ParentTraitSearch traitSearch;
    CandidateListView clv;
    TraitListView2 tlv;
    JSONArray candidates;
    JSONArray traits = null;
    JSONObject trait_info = null;
    JSONObject punnet_info = null;

    // Loads everything that appears on the page when it's loaded.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_cross);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clv = (CandidateListView) findViewById(R.id.candidate_list_view);
        tlv = (TraitListView2) findViewById(R.id.trait_list_view);

        // Loads the hamburger menu.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        candTask = new ProjectCandidateSearch();
        candTask.execute((Void)null);
        traitSearch = new ParentTraitSearch();
        traitSearch.execute((Void)null);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        String name = UserAuth.getInstance().getUsername();
        TextView user_field = (TextView) hView.findViewById(R.id.nav_menu_name);
        user_field.setText(name);

        // Button loaded and made functional.
        Button mNewCandidateButton = (Button) findViewById(R.id.new_candidate_button);
        mNewCandidateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goNewCandidate();
            }
        });

        TextView cross_name = (TextView)findViewById(R.id.current_cross_title);
        TextView p1_id = (TextView)findViewById(R.id.cross_p1_id);
        TextView p2_id = (TextView)findViewById(R.id.cross_p2_id);

        cross_name.setText(String.valueOf(bundle.getString("cross_name")));
        p1_id.setText(String.valueOf(bundle.getInt("cross_p1")));
        p2_id.setText(String.valueOf(bundle.getInt("cross_p2")));

    }

    // Functionality to take user to main page when button is pressed.
    public void goMainPage() {
        Intent intent = new Intent(CurrentCross.this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    // Functionality to take user to new project when button is pressed.
    public void goNewCandidate() {
        Bundle bundle = getIntent().getExtras();

        Intent intent = new Intent(CurrentCross.this, NewCandidate.class);
        intent.putExtra("proj_id", bundle.getInt("proj_id"));
        intent.putExtra("proj_title", bundle.getString("proj_title"));
        intent.putExtra("proj_description", bundle.getString("proj_description"));
        intent.putExtra("proj_type", bundle.getString("proj_type"));
        intent.putExtra("proj_species", bundle.getString("proj_species"));
        intent.putExtra("proj_location", bundle.getString("proj_location"));
        intent.putExtra("cross_id", bundle.getInt("cross_id"));
        intent.putExtra("cross_name", bundle.getString("cross_name"));
        intent.putExtra("cross_p1", bundle.getInt("cross_p1"));
        intent.putExtra("cross_p2", bundle.getInt("cross_p2"));
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goMessages() {
        Intent intent = new Intent(CurrentCross.this, Messages.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goProfile() {
        Intent intent = new Intent(CurrentCross.this, Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goSettings() {
        Intent intent = new Intent(CurrentCross.this, Settings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    // Closes hamburger menu when back button is pressed.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    // Option on right-hand side of banner, will be used as Search function
    // SETTINGS FLAG
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Lists out all the items of the hamburger menu. Each redirects to the appropriate page.
        if (id == R.id.nav_projects) {
            goMainPage();
        } /*else if (id == R.id.nav_profile) {
            goProfile();
        } else if (id == R.id.nav_messages) {
            goMessages();
        } else if (id == R.id.nav_settings) {
            goSettings();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ProjectCandidateSearch extends AsyncTask<Void, Void, Boolean> {

        ProjectCandidateSearch() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            UserAuth user = UserAuth.getInstance();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = null;
            try {

                Bundle bundle = getIntent().getExtras();
                Log.w("Project Info", user.getUsername());
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/crosses/" + bundle.getInt("cross_id") + "/candidates");
                Log.w("Project ID for Cross:", String.valueOf(bundle.getInt("proj_id")));
                Log.w("Cross ID for Cross:", String.valueOf(bundle.getInt("cross_id")));
                HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                client.setRequestMethod("GET");
                client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                client.addRequestProperty("charset", "utf-8");
                byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba, Base64.NO_WRAP));
                client.setUseCaches(false);
                ip = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ip, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } catch (Exception e) {
                Log.w("Candidate Specific Info", e + "");
            } finally {
            }
            try {
                JSONObject jRes = new JSONObject(result);
                Log.w("jRes", "" + jRes);
                candidates = jRes.getJSONArray("data");
                Log.w("Candidate Data", "" + candidates);
            } catch (Exception e) {

            }

            return true;
        }

        // Checks to see if user token is same as last time to avoid having to log in every time.
        private void confirmToken(String token) {
            //URL apiURL = new URL("http://" + mEmail + ":" + token + "@bloomgenetics.tech/api/v1/auth");
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            String name = "";
            int cand_id = 0;
            int cand_iid = 0;
            JSONArray traits;
            int i;

            Log.w("Candidate List: ", candidates.toString());

            try {

                JSONObject json = null;

                for (i = 0; i < candidates.length(); i++) {
                    json = candidates.getJSONObject(i);

                    Log.w("Candidate Info", "" + json);

                    cand_id = json.getInt("id");
                    cand_iid = json.getInt("imageId");
                    name = "Candidate #" + cand_id;
                    if (json.get("traits").equals(null)) {
                        traits = new JSONArray();
                    } else {
                        traits = json.getJSONArray("traits");
                    }

                    clv.AddItem(name, cand_id);
                    final String n = name;
                    final int id = cand_id;
                    final String t = traits.toString();
                    Log.w("Candidate Traits", t);
                    clv.setOnItemClickListener(new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                            Bundle bundle = getIntent().getExtras();
                            Intent mainIntent = new Intent(CurrentCross.this, CurrentCandidate.class);
                            JSONObject selected = null;
                            int j = clv.aCand.get(i).getId();
                            int k;

                            Log.w("Project ID: ", String.valueOf(j));
                            for (k = 0; k < candidates.length(); k++) {
                                try {
                                    selected = candidates.getJSONObject(k);

                                    if (selected.getInt("id") == j) {
                                        mainIntent.putExtra("proj_id", bundle.getInt("proj_id"));
                                        mainIntent.putExtra("proj_title", bundle.getString("proj_title"));
                                        mainIntent.putExtra("proj_description", bundle.getString("proj_description"));
                                        mainIntent.putExtra("proj_type", bundle.getString("proj_type"));
                                        mainIntent.putExtra("proj_species", bundle.getString("proj_species"));
                                        mainIntent.putExtra("proj_location", bundle.getString("proj_location"));
                                        mainIntent.putExtra("cross_id", bundle.getInt("cross_id"));
                                        mainIntent.putExtra("cross_name", bundle.getString("cross_name"));
                                        mainIntent.putExtra("cross_p1", bundle.getInt("cross_p1"));
                                        mainIntent.putExtra("cross_p2", bundle.getInt("cross_p2"));
                                        mainIntent.putExtra("candidate_name", "Candidate #" + selected.getInt("id"));
                                        mainIntent.putExtra("candidate_iid", selected.getInt("imageId"));
                                        mainIntent.putExtra("candidate_id", String.valueOf(selected.getInt("id")));
                                        mainIntent.putExtra("candidate_traits", String.valueOf(selected.get("traits")));
                                    }

                                } catch (Exception e) {
                                    Log.w("Error: ", e);
                                }
                            }

                            startActivity(mainIntent);
                        }
                    });

                }

            } catch (Exception e) {
                Log.w("Error:", e);
            }

        }
    }

    public class ParentTraitSearch extends AsyncTask<Void, Void, Boolean> {

        ParentTraitSearch() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            UserAuth user = UserAuth.getInstance();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = null;
            try {

                Bundle bundle = getIntent().getExtras();
                Log.w("Project Info",user.getUsername());
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/crosses/" + bundle.getInt("cross_id") + "/punnet");
                HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                client.setRequestMethod("GET");
                client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                client.addRequestProperty("charset", "utf-8");
                byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                client.setUseCaches(false);
                ip = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line+"\n");
                }
                result = sb.toString();
                Log.w("Cross #694",result);
            } catch (Exception e) {
                Log.w("Cross Error #394",e + "");
            } finally {
            }
            try {
                JSONObject jRes = new JSONObject(result);
                traits = jRes.getJSONArray("data");
            } catch(Exception e) {

            }

            return true;
        }

        // Checks to see if user token is same as last time to avoid having to log in every time.
        private void confirmToken(String token) {
            //URL apiURL = new URL("http://" + mEmail + ":" + token + "@bloomgenetics.tech/api/v1/auth");
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            String trait_name = "";
            String dr = "";
            int trait_carry;
            int trait_show;
            int trait_count;
            String trait_c_odds = "";
            String trait_x_odds = "";
            int i;

            try {

                NumberFormat formatter = new DecimalFormat("#0.0%");
                if(!traits.equals(null)){
                    for (i = 0; i < traits.length(); i++) {


                        punnet_info = traits.getJSONObject(i);
                        trait_info = punnet_info.getJSONObject("trait");

                        trait_name = trait_info.getString("name");
                        dr = trait_info.getString("type").substring(0, 1).toUpperCase() + trait_info.getString("type").substring(1);

                        trait_carry = punnet_info.getInt("carry");
                        trait_show = punnet_info.getInt("show");
                        trait_count = punnet_info.getInt("count");

                        trait_c_odds = formatter.format(((double)trait_carry/(double)trait_count));
                        trait_x_odds = formatter.format(((double)trait_show/(double)trait_count));

                        tlv.addItem2(trait_name, dr, trait_c_odds, trait_x_odds);

                    }
                }

            } catch (Exception e) {
                Log.w("Error:", e);
            }

        }
        /*protected void onPostExecute(final Boolean success) {
            String name = "";
            int cand_id = 0;
            JSONArray traits;
            int i;

            Log.w("Candidate List: ", candidates.toString());

            try{

                JSONObject json = null;

                for(i=1; i < candidates.length(); i++){
                    json = candidates.getJSONObject(i);

                    Log.w("Candidate Info", "" + json);

                    cand_id = json.getInt("id");
                    name = "Candidate #" + cand_id;
                    if(json.get("traits").equals(null)){
                        traits = new JSONArray();
                    }
                    else{
                        traits = json.getJSONArray("traits");
                    }

                    clv.AddItem(name, cand_id);
                    final String n = name;
                    final int id = cand_id;
                    final String t = traits.toString();
                    Log.w("Candidate Traits", t);
                    clv.setOnItemClickListener(new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                            Bundle bundle = getIntent().getExtras();
                            Intent mainIntent = new Intent(CurrentCross.this, CurrentCandidate.class);
                            JSONObject selected = null;
                            int j = clv.aCand.get(i).getId();
                            int k;

                            Log.w("Project ID: ", String.valueOf(j));
                            for(k=0; k<candidates.length(); k++) {
                                try {
                                    selected = candidates.getJSONObject(k);

                                    if(selected.getInt("id") == j) {
                                        mainIntent.putExtra("proj_id", bundle.getInt("proj_id"));
                                        mainIntent.putExtra("proj_title", bundle.getString("proj_title"));
                                        mainIntent.putExtra("proj_description", bundle.getString("proj_description"));
                                        mainIntent.putExtra("proj_type", bundle.getString("proj_type"));
                                        mainIntent.putExtra("proj_species", bundle.getString("proj_species"));
                                        mainIntent.putExtra("proj_location", bundle.getString("proj_location"));
                                        mainIntent.putExtra("cross_id", bundle.getInt("cross_id"));
                                        mainIntent.putExtra("cross_name", bundle.getString("cross_name"));
                                        mainIntent.putExtra("cross_p1", bundle.getInt("cross_p1"));
                                        mainIntent.putExtra("cross_p2", bundle.getInt("cross_p2"));
                                        mainIntent.putExtra("candidate_name", "Candidate #" + selected.getInt("id"));
                                        mainIntent.putExtra("candidate_id", String.valueOf(selected.getInt("id")));
                                        mainIntent.putExtra("candidate_traits", String.valueOf(selected.get("traits")));
                                    }

                                }
                                catch (Exception e) {
                                    Log.w("Error: ", e);
                                }
                            }

                            startActivity(mainIntent);
                        }
                    });

                }

            }
            catch (Exception e){
                Log.w("Error:", e);
            }

        } */
            /*for(i=1; i < projects.length(); i++){

                JSONObject json = null;

                try {
                    json = projects.getJSONObject(i);
                    if (json.getString("name").equals("")) {
                        title = "Project Title";
                    }
                    else {
                        title = json.getString("name");
                    }
                    if (json.getString("role").equals("")) {
                        role = "Member";
                    }
                    else {
                        role = json.getString("role");
                    }
                    proj_id = json.getInt("id");
                    location = json.getString("location");
                    type = json.getString("type");
                    species = json.getString("species");
                    description = json.getString("description");

                    Log.w("Project Title:", title);
                    Log.w("Project Info", role);
                }
                catch (Exception e){
                    Log.w("Error:", e);
                }
                final int finalProj_id = proj_id;
                final String finalTitle = title;
                final String finalLocation = location;
                final String finalType = type;
                final String finalSpecies = species;
                final String finalDescription = description;

                Log.w("Passing Proj ID: ", String.valueOf(finalProj_id));

                plv.AddItem(title, role);
                plv.setOnItemClickListener(new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                        Intent mainIntent = new Intent(MainPage.this, CurrentProject.class);
                        mainIntent.putExtra("proj_id", finalProj_id);
                        mainIntent.putExtra("proj_name", finalTitle);
                        mainIntent.putExtra("proj_location", finalLocation);
                        mainIntent.putExtra("proj_type", finalType);
                        mainIntent.putExtra("proj_species", finalSpecies);
                        mainIntent.putExtra("proj_desc", finalDescription);
                        Log.w("Project: ", finalTitle);

                        startActivity(mainIntent);
                    }
                });

            }

        }

        public class OnClickListenerWithProject {
            private int proj_id;
            private String proj_title;
            private String proj_description;
            private String proj_type;
            private String proj_species;
            private String proj_location;

            public OnClickListenerWithProject(int i, String n, String d, String t, String s, String l) {

                this.proj_id = i;
                this.proj_title = n;
                this.proj_description = d;
                this.proj_type = t;
                this.proj_species = s;
                this.proj_location = l;

            }

            public void OnClick (View v) {

                Intent activityChangeIntent = new Intent(MainPage.this, CurrentProject.class);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activityChangeIntent.putExtra("proj_id", proj_id);
                activityChangeIntent.putExtra("proj_title", proj_title);
                activityChangeIntent.putExtra("proj_description", proj_description);
                activityChangeIntent.putExtra("proj_type", proj_type);
                activityChangeIntent.putExtra("proj_species", proj_species);
                activityChangeIntent.putExtra("proj_location", proj_location);

                MainPage.this.startActivity(activityChangeIntent);

            }

        }
/*
        @Override
        protected void onCancelled() {
        } */

    }


}
