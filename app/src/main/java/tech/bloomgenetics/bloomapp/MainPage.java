package tech.bloomgenetics.bloomapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ListAdapter;
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
import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    UserProjectSearch projTask;
    ProjectListView plv;
    JSONArray projects;
    // Loads everything that appears on the page when it's loaded.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        plv = (ProjectListView) findViewById(R.id.project_list_view);


        // Loads the hamburger menu.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        projTask = new UserProjectSearch();
        projTask.execute((Void)null);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Button loaded and made functional.
        Button mNewProjectButton = (Button) findViewById(R.id.new_project_button);
        mNewProjectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goNewProject();
            }
        });
    }

    // Functionality to take user to main page when button is pressed.
    public void goMainPage() {
        Intent intent = new Intent(this.getBaseContext(), MainPage.class);
        startActivity(intent);
    }
    // Functionality to take user to new project when button is pressed.
    public void goNewProject() {
        Intent intent = new Intent(this.getBaseContext(), NewProject.class);
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
        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_projects) {
            goMainPage();
        } else if (id == R.id.nav_messages) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserProjectSearch extends AsyncTask<Void, Void, Boolean> {

        UserProjectSearch() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            UserAuth user = UserAuth.getInstance();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = null;
            try {

                Log.w("Project Info",user.getUsername());
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/users/" + user.getUsername() + "/projects");
                HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                client.setRequestMethod("GET");
                client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                client.addRequestProperty("charset", "utf-8");
                client.setUseCaches(false);
                ip = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line+"\n");
                }
                result = sb.toString();
                Log.w("Project Info",result);
            } catch (Exception e) {
                Log.w("Project Info",e + "");
            } finally {
            }
            try {
                JSONObject jRes = new JSONObject(result);
                projects = jRes.getJSONArray("data");
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
            String title = "";
            String role = "";
            int i;

            for(i=0; i < projects.length(); i++){

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

                    Log.w("Project Title:", title);
                    Log.w("Project Info", role);
                }
                catch (Exception e){
                    Log.w("Error:", e);
                }
                plv.AddItem(title, role);

            }

        }
/*
        @Override
        protected void onCancelled() {
        }
*/

    }


}
