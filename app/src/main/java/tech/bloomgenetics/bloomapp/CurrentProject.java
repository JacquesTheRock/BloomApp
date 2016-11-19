package tech.bloomgenetics.bloomapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class CurrentProject extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProjectCrossSearch crossSearch;
    CrossListView clv;
    JSONArray crosses;
    int proj_id = 0;
    int parent1 = 0;
    int parent2 = 0;

    // Loads everything that appears on the page when it's loaded.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clv = (CrossListView) findViewById(R.id.cross_list_view);


        TextView proj_title_field = (TextView)findViewById(R.id.current_proj_title);
        TextView proj_description_field = (TextView)findViewById(R.id.current_proj_description);
        TextView proj_type_field = (TextView)findViewById(R.id.current_proj_type);
        TextView proj_species_field = (TextView)findViewById(R.id.current_proj_species);
        TextView proj_location_field = (TextView)findViewById(R.id.current_proj_location);

        proj_title_field.setText(bundle.getString("proj_title"));
        proj_description_field.setText(bundle.getString("proj_description"));
        proj_type_field.setText(bundle.getString("proj_type"));
        proj_species_field.setText(bundle.getString("proj_species"));
        proj_location_field.setText(bundle.getString("proj_location"));

        // Loads the hamburger menu.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        crossSearch = new ProjectCrossSearch();
        crossSearch.execute((Void)null);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        String name = UserAuth.getInstance().getUsername();
        TextView user_field = (TextView) hView.findViewById(R.id.nav_menu_name);
        user_field.setText(name);

        proj_id = bundle.getInt("proj_id");
        Log.w("Project ID: ", String.valueOf(proj_id));

        Button mNewCrossButton = (Button) findViewById(R.id.new_cross_button);
        mNewCrossButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goNewCross();
            }
        });

    }

    // Functionality to take user to main page when button is pressed.
    public void goMainPage() {
        Intent intent = new Intent(CurrentProject.this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goNewCross() {

        Bundle bundle = getIntent().getExtras();

        Intent intent = new Intent(CurrentProject.this, NewCross.class);
        intent.putExtra("proj_id", bundle.getInt("proj_id"));
        intent.putExtra("proj_title", bundle.getString("proj_title"));
        intent.putExtra("proj_description", bundle.getString("proj_description"));
        intent.putExtra("proj_type", bundle.getString("proj_type"));
        intent.putExtra("proj_species", bundle.getString("proj_species"));
        intent.putExtra("proj_location", bundle.getString("proj_location"));
        Log.w("Project ID Passed: ", String.valueOf(bundle.getInt("proj_id")));
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goMessages() {
        Intent intent = new Intent(CurrentProject.this, Messages.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goProfile() {
        Intent intent = new Intent(CurrentProject.this, Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goSettings() {
        Intent intent = new Intent(CurrentProject.this, Settings.class);
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
            goProfile();
        } else if (id == R.id.nav_projects) {
            goMainPage();
        } else if (id == R.id.nav_messages) {
            goMessages();
        } else if (id == R.id.nav_settings) {
            goSettings();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class ProjectGetCross extends AsyncTask<Void,Void,Boolean> {
        private int projID;
        private int crossID;
        public ProjectGetCross(int p, int c) {
            projID = p;
            crossID = c;

        }
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            UserAuth user = UserAuth.getInstance();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result2 = "";
            JSONObject cross;
            try {

                Bundle bundle = getIntent().getExtras();
                Log.w("Project Info",user.getUsername());
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + projID + "/crosses/" + crossID);
                Log.w("Project ID for Cross:", String.valueOf(projID));
                Log.w("Cross ID for Cross:", String.valueOf(crossID));
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
                result2 = sb.toString();
                JSONObject outter = new JSONObject(result2);
                cross = outter.getJSONObject("data");
                Log.w("Look Here for Parents",result2);

                parent1 = cross.getInt("parent1");
                parent2 = cross.getInt("parent2");
            } catch (Exception e) {
                Log.w("Cross Specific Info", "" + crossID);
                Log.w("Cross Specific Info", "" + projID);
                Log.w("Cross Specific Info",e + "");
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Bundle bundle = getIntent().getExtras();
            if(success) {
                Intent mainIntent = new Intent(CurrentProject.this, CurrentCross.class);
                JSONObject selected = null;
                int k;

                for (k = 0; k < crosses.length(); k++) {
                    try {
                        selected = crosses.getJSONObject(k);

                        if (selected.getInt("id") == crossID) {
                            mainIntent.putExtra("proj_id", bundle.getInt("proj_id"));
                            mainIntent.putExtra("proj_title", bundle.getString("proj_title"));
                            mainIntent.putExtra("proj_description", bundle.getString("proj_description"));
                            mainIntent.putExtra("proj_type", bundle.getString("proj_type"));
                            mainIntent.putExtra("proj_species", bundle.getString("proj_species"));
                            mainIntent.putExtra("proj_location", bundle.getString("proj_location"));
                            mainIntent.putExtra("cross_id", selected.getInt("id"));
                            mainIntent.putExtra("cross_name", selected.getString("name"));
                            mainIntent.putExtra("cross_p1", parent1);
                            mainIntent.putExtra("cross_p2", parent2);

                            Log.w("Passing Parent1 ID:", String.valueOf(parent1));
                        }

                    } catch (Exception e) {
                        Log.w("Error: ", e);
                    }
                }
                startActivity(mainIntent);
            }

        }
    }
    public class ProjectCrossSearch extends AsyncTask<Void, Void, Boolean> {

        Bundle bundle = getIntent().getExtras();
        JSONObject cross = null;

        ProjectCrossSearch() {
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

                Log.w("Cross Info", String.valueOf(bundle.getInt("proj_id")));
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/crosses");
                HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                client.setRequestMethod("GET");
                client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                client.addRequestProperty("charset", "utf-8");
                byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba, 0));
                client.setUseCaches(false);
                ip = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ip, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.w("Cross Info", result);
            } catch (Exception e) {
                Log.w("Cross Info", e + "");
            } finally {
            }
            try {
                JSONObject jRes = new JSONObject(result);
                crosses = jRes.getJSONArray("data");
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
            int cross_id = 0;
            int i;


            Log.w("Cross List: ", crosses.toString());

            try {

                JSONObject json = null;

                if(crosses.length() == 0) {

                    name = "None";
                    cross_id = -1;

                    clv.AddItem(name, cross_id);

                }

                for (i = 0; i < crosses.length(); i++) {
                    json = crosses.getJSONObject(i);

                    cross_id = json.getInt("id");
                    if (json.getString("name").equals("")) {
                        name = "Cross #" + cross_id;
                    } else {
                        name = json.getString("name");
                    }

                    clv.AddItem(name, cross_id);
                    clv.setOnItemClickListener(new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                            int cid = clv.aC.get(i).getId();
                            int pid = bundle.getInt("proj_id");
                            ProjectGetCross crossThread = new ProjectGetCross(pid,cid);
                            crossThread.execute((Void) null);
                        }
                    });

                }

            } catch (Exception e) {
                Log.w("Error:", e);
            }

        }

    }
}
