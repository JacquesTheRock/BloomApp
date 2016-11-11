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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewProject extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProjectCreateTask createTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button mCreateProjectButton = (Button) findViewById(R.id.login_redirect_button);
        mCreateProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMainPage();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void goMainPage() {

        String proj_name = ((EditText) findViewById(R.id.new_project_name)).getText().toString();
        String proj_description = ((EditText) findViewById(R.id.new_project_description)).getText().toString();
        String proj_type = ((EditText) findViewById(R.id.new_project_type)).getText().toString();
        String proj_species = ((EditText) findViewById(R.id.new_project_species)).getText().toString();
        String proj_location = ((EditText) findViewById(R.id.new_project_location)).getText().toString();

        if(proj_name.equals("")){
            EditText cErrorField = (EditText) findViewById(R.id.new_project_name);
            cErrorField.setError("Project name required!");
        }
        else if(proj_type.equals("")){
            EditText cErrorField = (EditText) findViewById(R.id.new_project_type);
            cErrorField.setError("Project type required!");
        }
        else if(proj_species.equals("")){
            EditText cErrorField = (EditText) findViewById(R.id.new_project_species);
            cErrorField.setError("Species required!");
        }
        else if(proj_location.equals("")){
            EditText cErrorField = (EditText) findViewById(R.id.new_project_location);
            cErrorField.setError("Project location required!");
        }
        else {
            createTask = new ProjectCreateTask(proj_name, proj_description, proj_type, proj_species, proj_location);
            createTask.execute((Void) null);
/*
            Intent intent = new Intent(this.getBaseContext(), MainPage.class);
            startActivity(intent);
            */
        }

    }

    public void goProjectPage  () {
        Intent intent = new Intent(NewProject.this, MainPage.class);
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
        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_projects) {
            goProjectPage();
        } else if (id == R.id.nav_messages) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class ProjectCreateTask extends AsyncTask<Void, Void, Boolean> {
        String proj_name;
        String proj_description;
        String proj_type;
        String proj_species;
        String proj_location;

        ProjectCreateTask(String n, String d, String t, String s, String l) {
            proj_name = n;
            proj_description = d;
            proj_type = t;
            proj_species = s;
            proj_location = l;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = null;

            // Sends POST request to server to add new user to database.
            try {
                String q = "name=" + proj_name + "&description=" + proj_description + "&type=" + proj_type + "&species=" + proj_species + "&location=" + proj_location;
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects");
                HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                client.setRequestMethod("POST");
                client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba,0));
                Log.w("Authorization", "Basic " + Base64.encodeToString(ba,0));
                client.addRequestProperty("charset", "utf-8");
                client.setRequestProperty("Content-Length", Integer.toString(q.length()));
                client.setUseCaches(false);
                client.setDoOutput(true);
                DataOutputStream op = new DataOutputStream(client.getOutputStream());
                op.write(q.getBytes());
                Log.w("Project Creation",proj_name);
                ip = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line+"\n");
                }
                result = sb.toString();
                Log.w("Project Creation",result);
            } catch (Exception e) {
                Log.w("Project Creation",e + "");
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

            if (success) {
                finish();
                /*
                Intent intent = new Intent(getBaseContext(), MainPage.class);
                startActivity(intent);
                */
                onBackPressed();
                Intent intent = new Intent(NewProject.this, MainPage.class);
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