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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewCross extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CrossCreateTask createTask;
    private CandidateGetTask candGet;
    public JSONArray candidates = null;
    public List<String> cands_in_project = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cross);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button mCreateProjectButton = (Button) findViewById(R.id.new_cross_create_button);
        mCreateProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goCurrentProject();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //candGet = new CandidateGetTask();
        //candGet.execute((Void) null);

        /*addItemsToSpinner1();
        addListenerOnSpinner1ItemSelection();

        addItemsToSpinner2();
        addListenerOnSpinner2ItemSelection();*/

        View hView = navigationView.getHeaderView(0);
        String name = UserAuth.getInstance().getUsername();
        TextView user_field = (TextView) hView.findViewById(R.id.nav_menu_name);
        user_field.setText(name);
    }

    /*public void addItemsToSpinner1() {
        Spinner spinner = (Spinner) findViewById(R.id.new_cross_spinner_parent1);

        List<String> parents = new ArrayList<String>();
        int i;
        parents.add("Parent #1 (Optional)");
        for(i=0; i<cands_in_project.size(); i++){
            parents.add("Candidate #" + i);
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, parents);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    public void addListenerOnSpinner1ItemSelection() {
        Spinner spinner = (Spinner) findViewById(R.id.new_cross_spinner_parent1);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }*/

    public void goCurrentProject() {

        String cross_name = ((EditText) findViewById(R.id.new_cross_name)).getText().toString();
        int cross_p1 = 0;
        int cross_p2 = 0;
        String cross_description = ((EditText) findViewById(R.id.new_cross_description)).getText().toString();

        if(!((EditText) findViewById(R.id.new_cross_parent1)).getText().toString().equals("")) {
            cross_p1 = Integer.parseInt(((EditText) findViewById(R.id.new_cross_parent1)).getText().toString());
        }
        if(!((EditText) findViewById(R.id.new_cross_parent2)).getText().toString().equals("")) {
            cross_p2 = Integer.parseInt(((EditText) findViewById(R.id.new_cross_parent2)).getText().toString());
        }

        if(cross_name.equals("")){
            EditText cErrorField = (EditText) findViewById(R.id.new_cross_name);
            cErrorField.setError("Cross name required!");
        }
        else {
            createTask = new CrossCreateTask(cross_name, cross_p1, cross_p2, cross_description);
            createTask.execute((Void) null);
/*
            Intent intent = new Intent(this.getBaseContext(), MainPage.class);
            startActivity(intent);
            */
        }

    }

    public void goMainPage  () {
        Intent intent = new Intent(NewCross.this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goMessages() {
        Intent intent = new Intent(NewCross.this, Messages.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goProfile() {
        Intent intent = new Intent(NewCross.this, Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goSettings() {
        Intent intent = new Intent(NewCross.this, Settings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

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

    public class CandidateGetTask extends AsyncTask<Void, Void, Boolean> {

        CandidateGetTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            UserAuth user = UserAuth.getInstance();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = "";
            try {

                Bundle bundle = getIntent().getExtras();
                Log.w("Cross Info", String.valueOf(bundle.getInt("proj_id")));
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/candidates");
                HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                client.setRequestMethod("GET");
                client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                client.addRequestProperty("charset", "utf-8");
                byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                client.setUseCaches(false);
                ip = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ip, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.w("Traits in Project", result);
            } catch (Exception e) {
                Log.w("GetTraits Error", e + "");
            } finally {
            }
            try {
                JSONObject jRes = new JSONObject(result);
                candidates = jRes.getJSONArray("data");
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
            String cand_id = "";

            try {

                JSONObject json = null;
                int i;

                if (candidates != null) {
                    for (i = 0; i < candidates.length(); i++) {
                        json = candidates.getJSONObject(i);

                        cand_id = json.getString("id");

                        cands_in_project.add(cand_id);

                    }
                    Log.w("List of Candidates", "" + cands_in_project);
                }
            } catch (Exception e) {
                Log.w("Error:", e);
            }

        }
    }

    public class CrossCreateTask extends AsyncTask<Void, Void, Boolean> {
        String cross_name;
        int cross_p1;
        int cross_p2;
        String description;

        CrossCreateTask(String n, int p1, int p2, String desc) {
            cross_name = n;
            cross_p1 = p1;
            cross_p2 = p2;
            description = desc;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = null;

            // Sends POST request to server to add new user to database.
            try {
                Bundle bundle = getIntent().getExtras();

                String q = "parent1=" + cross_p1 + "&parent2=" + cross_p2 + "&name=" + cross_name + "&description=" + description;
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/crosses");
                HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                client.setRequestMethod("POST");
                client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                Log.w("Authorization", "Basic " + Base64.encodeToString(ba,0));
                client.addRequestProperty("charset", "utf-8");
                client.setRequestProperty("Content-Length", Integer.toString(q.length()));
                client.setUseCaches(false);
                client.setDoOutput(true);
                DataOutputStream op = new DataOutputStream(client.getOutputStream());
                op.write(q.getBytes());
                Log.w("Cross Creation", cross_name);
                ip = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line+"\n");
                }
                result = sb.toString();
                Log.w("Cross Creation",result);
            } catch (Exception e) {
                Log.w("Cross Creation",e + "");
            } finally {
            }


            // TODO: register the new account here.
            return true;
        }

        // Checks to see if user token is same as last time to avoid having to log in every time.
        private void confirmToken(String token) {
            //URL apiURL = new URL("http://" + mEmail + ":" + token + "@bloomgenetics.tech/api/v1/auth");
        }

        // Upon account creation, user is redirected to Login page to log in using newly created account.
        @Override
        protected void onPostExecute(final Boolean success) {

            Bundle bundle = getIntent().getExtras();

            if (success) {
                finish();
                /*
                Intent intent = new Intent(getBaseContext(), MainPage.class);
                startActivity(intent);
                */
                onBackPressed();
                Intent intent = new Intent(NewCross.this, CurrentProject.class);
                intent.putExtra("proj_id", bundle.getInt("proj_id"));
                intent.putExtra("proj_title", bundle.getString("proj_title"));
                intent.putExtra("proj_description", bundle.getString("proj_description"));
                intent.putExtra("proj_type", bundle.getString("proj_type"));
                intent.putExtra("proj_species", bundle.getString("proj_species"));
                intent.putExtra("proj_location", bundle.getString("proj_location"));
                Log.w("Bundle Info ID", String.valueOf(bundle.getInt("proj_id")));
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            } else {
            }
        }

        @Override
        protected void onCancelled() {
        }


    }


}