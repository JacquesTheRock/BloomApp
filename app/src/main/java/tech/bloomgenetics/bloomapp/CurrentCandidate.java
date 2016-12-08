package tech.bloomgenetics.bloomapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class CurrentCandidate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CandidateTraitSearch traitTask;
    PictureSearch picTask;
    TraitListView tlv;
    JSONObject result_data;
    JSONArray traits = null;
    JSONObject picture_object = null;
    byte[] decodedString = null;
    Bitmap image = null;

    // Loads everything that appears on the page when it's loaded.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_candidate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tlv = (TraitListView) findViewById(R.id.candidate_traits);

        // Loads the hamburger menu.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        Log.w("Candidate Image ID", ""+bundle.getInt("candidate_iid"));
        traitTask = new CandidateTraitSearch();
        traitTask.execute((Void) null);
        picTask = new PictureSearch();
        picTask.execute((Void)null);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        String name = UserAuth.getInstance().getUsername();
        TextView user_field = (TextView) hView.findViewById(R.id.nav_menu_name);
        user_field.setText(name);

        // Button loaded and made functional.
/*        Button mNewProjectButton = (Button) findViewById(R.id.new_project_button);
        mNewProjectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goNewProject();
            }
        });
*/
        TextView candidate_name = (TextView) findViewById(R.id.current_candidate_title);
        ImageView cand_img = (ImageView)findViewById(R.id.candidate_picture);

        candidate_name.setText(String.valueOf(bundle.getString("candidate_name")));

        Log.w("Candidate ID", "" + bundle.getString("candidate_id"));

    }

    // Functionality to take user to main page when button is pressed.
    public void goMainPage() {
        Intent intent = new Intent(CurrentCandidate.this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    // Functionality to take user to new project when button is pressed.
    public void goNewProject() {
        Intent intent = new Intent(CurrentCandidate.this, NewProject.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void goMessages() {
        Intent intent = new Intent(CurrentCandidate.this, Messages.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void goProfile() {
        Intent intent = new Intent(CurrentCandidate.this, Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void goSettings() {
        Intent intent = new Intent(CurrentCandidate.this, Settings.class);
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
    public class CandidateTraitSearch extends AsyncTask<Void, Void, Boolean> {

        CandidateTraitSearch() {
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
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/crosses/" + bundle.getInt("cross_id") + "/candidates/" + Integer.parseInt(bundle.getString("candidate_id")));
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
            } catch (Exception e) {
                Log.w("Candidate Specific Info", e + "");
            } finally {
            }
            try {
                JSONObject jRes = new JSONObject(result);
                Log.w("jRes", "" + jRes);
                result_data = jRes.getJSONObject("data");
                Log.w("Candidate Data Array", "" + result_data);
                if(result_data != null) {
                    traits = result_data.getJSONArray("traits");
                    Log.w("Cndidate Traits Array", "" + traits);
                }
                //traits = jRes2.getJSONArray("traits");
                //Log.w("Trait Array", "" + traits);
            } catch (Exception e) {
                Log.w("Trait Error", "" + e);
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
            String trait_weight = "";
            int i;

            try {

                JSONObject json = null;

                if(traits != null) {

                    for (i = 0; i < traits.length(); i++) {
                        json = traits.getJSONObject(i);

                        trait_name = json.getString("name");
                        String weight = json.getString("type");
                        trait_weight = weight.substring(0, 1).toUpperCase() + weight.substring(1);
                        //traits = json.get("traits");
                        tlv.addItem(trait_name, trait_weight);

                    }
                }

            } catch (Exception e) {
                Log.w("Error:", e);
            }

        }
    }

    public class PictureSearch extends AsyncTask<Void, Void, Boolean> {

        PictureSearch() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            UserAuth user = UserAuth.getInstance();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.w("Error in PicSearch", "");
                return false;
            }
            InputStream ip = null;
            String result = null;
            try {

                Bundle bundle = getIntent().getExtras();
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/images/" + bundle.getInt("candidate_iid"));
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
                Log.w("Candidate Pic Result",result);
            } catch (Exception e) {
                Log.w("Pic Error #741",e + "");
            } finally {
            }
            try {
                JSONObject jRes = new JSONObject(result);
                Log.w("Image jRes", ""+jRes);
                picture_object = jRes.getJSONObject("data");
                Log.w("Picture_Object", ""+picture_object);
                String image_data = picture_object.getString("data");
                Log.w("Image_Data", ""+image_data);
                decodedString = Base64.decode(image_data.getBytes(), Base64.URL_SAFE);
                Log.w("Decoded String", ""+decodedString);
                image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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

            Log.w("Bitmap", ""+image);
            ImageView cand_img = (ImageView)findViewById(R.id.candidate_picture);
            cand_img.setImageBitmap(image);
        }

    }
}