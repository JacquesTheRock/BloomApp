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

public class Messages extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    UserProjectSearch projTask;
    ProjectListView plv;
    JSONArray projects;

    // Loads everything that appears on the page when it's loaded.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
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

        View hView = navigationView.getHeaderView(0);
        String name = UserAuth.getInstance().getUsername();
        TextView user_field = (TextView) hView.findViewById(R.id.nav_menu_name);
        user_field.setText(name);

        // Button loaded and made functional.
        Button mNewProjectButton = (Button) findViewById(R.id.new_project_button);
        mNewProjectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goMainPage();
            }
        });

    }

    // Functionality to take user to main page when button is pressed.
    public void goMainPage() {
        Intent intent = new Intent(Messages.this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    // Functionality to take user to new project when button is pressed.
    public void goMessages() {
        Intent intent = new Intent(Messages.this, Messages.class);
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
        if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_projects) {
            goMainPage();
        } else if (id == R.id.nav_messages) {
            goMessages();
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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = null;
            try {

                Log.w("Message User",user.getUsername());
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/users/" + user.getUsername() + "/mail");
                HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                client.setRequestMethod("GET");
                client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                client.addRequestProperty("charset", "utf-8");
                byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba,0));
                client.setUseCaches(false);
                ip = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line+"\n");
                }
                result = sb.toString();
                Log.w("Message Info",result);
            } catch (Exception e) {
                Log.w("Message Error",e + "");
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
            String location = "";
            String type = "";
            String species = "";
            String description = "";
            int proj_id = 0;
            int i;

            Log.w("Project List: ", projects.toString());

            try{

                JSONObject json = null;

                for(i=1; i < projects.length(); i++){
                    json = projects.getJSONObject(i);

                    proj_id = json.getInt("id");
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

                    plv.AddItem(title, role, proj_id);
                    plv.setOnItemClickListener(new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                            Intent mainIntent = new Intent(Messages.this, CurrentProject.class);
                            JSONObject selected = null;
                            int j = plv.aP.get(i).getId();
                            int k;

                            Log.w("Project ID: ", String.valueOf(j));
                            for(k=0; k<projects.length(); k++) {
                                try {
                                    selected = projects.getJSONObject(k);

                                    if(selected.getInt("id") == j) {
                                        mainIntent.putExtra("proj_id", selected.getInt("id"));
                                        mainIntent.putExtra("proj_title", selected.getString("name"));
                                        mainIntent.putExtra("proj_description", selected.getString("description"));
                                        mainIntent.putExtra("proj_type", selected.getString("type"));
                                        mainIntent.putExtra("proj_species", selected.getString("species"));
                                        mainIntent.putExtra("proj_location", selected.getString("location"));
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

        }

    }


}
