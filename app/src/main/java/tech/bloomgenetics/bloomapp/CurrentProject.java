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

public class CurrentProject extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //UserProjectSearch projTask;
    ProjectListView plv;
    JSONArray projects;
    int proj_id = 0;

    // Loads everything that appears on the page when it's loaded.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        TextView proj_title_field = (TextView)findViewById(R.id.current_proj_title);
        TextView proj_description_field = (TextView)findViewById(R.id.current_proj_description);
        TextView proj_type_field = (TextView)findViewById(R.id.current_proj_type);
        TextView proj_species_field = (TextView)findViewById(R.id.current_proj_species);
        TextView proj_location_field = (TextView)findViewById(R.id.current_proj_location);

        proj_title_field.setText(bundle.getString("proj_name"));
        proj_description_field.setText(bundle.getString("proj_desc"));
        proj_type_field.setText(bundle.getString("proj_type"));
        proj_species_field.setText(bundle.getString("proj_species"));
        proj_location_field.setText(bundle.getString("proj_location"));

        // Loads the hamburger menu.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //projTask = new UserProjectSearch();
        //projTask.execute((Void)null);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        String name = UserAuth.getInstance().getUsername();
        TextView user_field = (TextView) hView.findViewById(R.id.nav_menu_name);
        user_field.setText(name);

        proj_id = bundle.getInt("proj_id");
        Log.w("Project ID: ", String.valueOf(proj_id));

    }

    // Functionality to take user to main page when button is pressed.
    public void goMainPage() {
        Intent intent = new Intent(CurrentProject.this, MainPage.class);
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

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
