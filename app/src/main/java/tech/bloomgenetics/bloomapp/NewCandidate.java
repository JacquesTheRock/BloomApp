package tech.bloomgenetics.bloomapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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

public class NewCandidate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CandidateCreateTask createTask;
    private TraitCreateTask traitCreate;
    private GetTraitSearch getTraits;
    private GetTraitSearch2 getTraits2;
    private TraitsToId ttID;
    public List<Integer> traits_insert = new ArrayList<>();
    public int[] traitIDs;
    private JSONArray traits = null;
    public List<String> traits_in_project = new ArrayList<>();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_candidate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button mCreateProjectButton = (Button) findViewById(R.id.new_candidate_create_button);
        mCreateProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goCurrentCross();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        String name = UserAuth.getInstance().getUsername();
        TextView user_field = (TextView) hView.findViewById(R.id.nav_menu_name);
        user_field.setText(name);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void goCurrentCross() {

        int pool1;
        int pool2;
        int pool3;
        int pool4;

        String trait1 = ((EditText) findViewById(R.id.new_cand_t1)).getText().toString();
        if(!((EditText) findViewById(R.id.t1_pool)).getText().toString().equals("")){
            pool1 = Integer.parseInt(((EditText) findViewById(R.id.t1_pool)).getText().toString());
        }
        else{
            pool1 = 0;
        }
        RadioGroup dom_rec_rg1 = (RadioGroup) findViewById(R.id.dom_rec_1);
        int selectedId1 = dom_rec_rg1.getCheckedRadioButtonId();
        RadioButton dom_rec_rb1 = (RadioButton) findViewById(selectedId1);
        String dom_rec1 = String.valueOf(dom_rec_rb1.getText());

        String trait2 = ((EditText) findViewById(R.id.new_cand_t2)).getText().toString();
        if(!((EditText) findViewById(R.id.t2_pool)).getText().toString().equals("")){
            pool2 = Integer.parseInt(((EditText) findViewById(R.id.t2_pool)).getText().toString());
        }
        else{
            pool2 = 0;
        }
        RadioGroup dom_rec_rg2 = (RadioGroup) findViewById(R.id.dom_rec_2);
        int selectedId2 = dom_rec_rg2.getCheckedRadioButtonId();
        RadioButton dom_rec_rb2 = (RadioButton) findViewById(selectedId2);
        String dom_rec2 = String.valueOf(dom_rec_rb2.getText());

        String trait3 = ((EditText) findViewById(R.id.new_cand_t3)).getText().toString();
        if(!((EditText) findViewById(R.id.t3_pool)).getText().toString().equals("")){
            pool3 = Integer.parseInt(((EditText) findViewById(R.id.t3_pool)).getText().toString());
        }
        else{
            pool3 = 0;
        }
        RadioGroup dom_rec_rg3 = (RadioGroup) findViewById(R.id.dom_rec_3);
        int selectedId3 = dom_rec_rg3.getCheckedRadioButtonId();
        RadioButton dom_rec_rb3 = (RadioButton) findViewById(selectedId3);
        String dom_rec3 = String.valueOf(dom_rec_rb3.getText());

        String trait4 = ((EditText) findViewById(R.id.new_cand_t4)).getText().toString();
        if(!((EditText) findViewById(R.id.t4_pool)).getText().toString().equals("")){
            pool4 = Integer.parseInt(((EditText) findViewById(R.id.t4_pool)).getText().toString());
        }
        else{
            pool4 = 0;
        }
        RadioGroup dom_rec_rg4 = (RadioGroup) findViewById(R.id.dom_rec_4);
        int selectedId4 = dom_rec_rg4.getCheckedRadioButtonId();
        RadioButton dom_rec_rb4 = (RadioButton) findViewById(selectedId4);
        String dom_rec4 = String.valueOf(dom_rec_rb4.getText());

        Log.w("Pool values", pool1 + " "+ pool2 + " " + pool3 + " " + pool4);
        getTraits = new GetTraitSearch(trait1, dom_rec1, pool1, trait2, dom_rec2, pool2, trait3, dom_rec3, pool3, trait4, dom_rec4, pool4);
        getTraits.execute((Void) null);

        //Intent intent = new Intent(this.getBaseContext(), MainPage.class);
        //startActivity(intent);
    }


    public void goMainPage() {
        Intent intent = new Intent(NewCandidate.this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void goMessages() {
        Intent intent = new Intent(NewCandidate.this, Messages.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void goProfile() {
        Intent intent = new Intent(NewCandidate.this, Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void goSettings() {
        Intent intent = new Intent(NewCandidate.this, Settings.class);
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("NewCandidate Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public class CandidateCreateTask extends AsyncTask<Void, Void, Boolean> {
        int[] traits;

        CandidateCreateTask(int[] t) {
            traits = t;
            Log.w("Trait Array", ""+traits);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = null;

            // Sends POST request to server to add new user to database.
            try {
                Bundle bundle = getIntent().getExtras();
                //JSONArray traits = new JSONArray();

                /*
                String q = "traits=" + traits[0];
                for (int i = 1; i < traits.length; i++)
                    q += "," + traits[i];
                */
                Candidate c = new Candidate();
                c.setTraits(traits);
                String q = c.toString();
                Log.w("Create Candidate Data",q);
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/crosses/" + bundle.getInt("cross_id") + "/candidates");
                HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                client.setRequestMethod("POST");
                client.addRequestProperty("Content-type", "application/json");
                byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba, 0));
                Log.w("Authorization", "Basic " + Base64.encodeToString(ba, 0));
                client.addRequestProperty("charset", "utf-8");
                client.setRequestProperty("Content-Length", Integer.toString(q.length()));
                client.setUseCaches(false);
                client.setDoOutput(true);
                DataOutputStream op = new DataOutputStream(client.getOutputStream());
                op.write(q.getBytes());
                ip = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ip, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.w("Cross Creation", result);
            } catch (Exception e) {
                Log.w("Cross Creation", e + "");
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
                Intent intent = new Intent(NewCandidate.this, CurrentCross.class);
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
            } else {
            }
        }

        @Override
        protected void onCancelled() {
        }


    }

    public class GetTraitSearch extends AsyncTask<Void, Void, Boolean> {

        String t1 = "";
        String dr1 = "";
        int p1 = 0;
        String t2 = "";
        String dr2 = "";
        int p2 = 0;
        String t3 = "";
        String dr3 = "";
        int p3 = 0;
        String t4 = "";
        String dr4 = "";
        int p4 = 0;

        GetTraitSearch(String trt1, String dmrc1, int pl1, String trt2, String dmrc2, int pl2, String trt3, String dmrc3, int pl3, String trt4, String dmrc4, int pl4) {
            t1 = trt1;
            dr1 = dmrc1;
            p1 = pl1;
            t2 = trt2;
            dr2 = dmrc2;
            p2 = pl2;
            t3 = trt3;
            dr3 = dmrc3;
            p3 = pl3;
            t4 = trt4;
            dr4 = dmrc4;
            p4 = pl4;
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
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/traits");
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
                traits = jRes.getJSONArray("data");
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
            String trait_name = "";

            try {

                JSONObject json = null;
                int i;

                if (traits != null) {
                    for (i = 0; i < traits.length(); i++) {
                        json = traits.getJSONObject(i);

                        trait_name = json.getString("name");

                        traits_in_project.add(trait_name);

                    }
                    Log.w("List of Traits", "" + traits_in_project);
                    Log.w("Get Traits", "Complete");
                }
            } catch (Exception e) {
                Log.w("Error:", e);
            }

            Log.w("Pool values", p1 + " "+ p2 + " " + p3 + " " + p4);
            traitCreate = new TraitCreateTask(t1, dr1, p1, t2, dr2, p2, t3, dr3, p3, t4, dr4, p4);
            traitCreate.execute((Void) null);

        }
    }

    public class GetTraitSearch2 extends AsyncTask<Void, Void, Boolean> {

        String t1 = "";
        String dr1 = "";
        int p1 = 0;
        String t2 = "";
        String dr2 = "";
        int p2 = 0;
        String t3 = "";
        String dr3 = "";
        int p3 = 0;
        String t4 = "";
        String dr4 = "";
        int p4 = 0;

        GetTraitSearch2(String trt1, String dmrc1, int pl1, String trt2, String dmrc2, int pl2, String trt3, String dmrc3, int pl3, String trt4, String dmrc4, int pl4) {
            t1 = trt1;
            dr1 = dmrc1;
            p1 = pl1;
            t2 = trt2;
            dr2 = dmrc2;
            p2 = pl2;
            t3 = trt3;
            dr3 = dmrc3;
            p3 = pl3;
            t4 = trt4;
            dr4 = dmrc4;
            p4 = pl4;
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
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/traits");
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
                traits = jRes.getJSONArray("data");
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
            String trait_name = "";

            try {

                JSONObject json = null;
                int i;

                if (traits != null) {
                    for (i = 0; i < traits.length(); i++) {
                        json = traits.getJSONObject(i);

                        trait_name = json.getString("name");

                        traits_in_project.add(trait_name);

                    }
                    Log.w("List of Traits", "" + traits_in_project);
                    Log.w("Get Traits", "Complete");
                }
            } catch (Exception e) {
                Log.w("Error:", e);
            }

            ttID = new TraitsToId(t1, t2, t3, t4);
            ttID.execute((Void) null);

        }
    }

    public class TraitsToId extends AsyncTask<Void, Void, Boolean> {

        String t1;
        String t2;
        String t3;
        String t4;

        TraitsToId(String tr1, String tr2, String tr3, String tr4) {
            t1 = tr1;
            t2 = tr2;
            t3 = tr3;
            t4 = tr4;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            UserAuth user = UserAuth.getInstance();
            /*try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = "";
            try {

                Bundle bundle = getIntent().getExtras();
                Log.w("Cross Info", String.valueOf(bundle.getInt("proj_id")));
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/traits");
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
                Log.w("Traits to IDs", "Complete");
            } catch (Exception e) {
                Log.w("GetTraits Error", e + "");
            } finally {
            }
            try {
                JSONObject jRes = new JSONObject(result);
                traits = jRes.getJSONArray("data");
            } catch (Exception e) {

            } */

            return true;
        }

        // Checks to see if user token is same as last time to avoid having to log in every time.
        private void confirmToken(String token) {
            //URL apiURL = new URL("http://" + mEmail + ":" + token + "@bloomgenetics.tech/api/v1/auth");
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            String trait_name = "";

            try {

                JSONObject json = null;
                int i;

                if (traits != null) {
                    for (i = 0; i < traits.length(); i++) {
                        json = traits.getJSONObject(i);
                        trait_name = json.getString("name");

                        if(trait_name.equals(t1) || trait_name.equals(t2) || trait_name.equals(t3) || trait_name.equals(t4)){
                            traits_insert.add(json.getInt("id"));
                        }

                    }
                    Log.w("List of Traits", "" + traits_in_project);
                }
            } catch (Exception e) {
                Log.w("Error:", e);
            }

            int i;
            traitIDs = new int[traits_insert.size()];
            for(i=0; i<traits_insert.size(); i++){
                traitIDs[i] = traits_insert.get(i);
            }

            createTask = new CandidateCreateTask(traitIDs);
            createTask.execute((Void) null);

        }
    }

    public class TraitCreateTask extends AsyncTask<Void, Void, Boolean> {
        String t1 = "";
        String dr1 = "";
        int p1 = 0;
        String t2 = "";
        String dr2 = "";
        int p2 = 0;
        String t3 = "";
        String dr3 = "";
        int p3 = 0;
        String t4 = "";
        String dr4 = "";
        int p4 = 0;

        TraitCreateTask(String trt1, String dmrc1, int pl1, String trt2, String dmrc2, int pl2, String trt3, String dmrc3, int pl3, String trt4, String dmrc4, int pl4) {
            t1 = trt1;
            dr1 = dmrc1;
            p1 = pl1;
            t2 = trt2;
            dr2 = dmrc2;
            p2 = pl2;
            t3 = trt3;
            dr3 = dmrc3;
            p3 = pl3;
            t4 = trt4;
            dr4 = dmrc4;
            p4 = pl4;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = null;

            try {
                Bundle bundle = getIntent().getExtras();
                JSONArray traits = new JSONArray();
                int weight = 0;

                if (!t1.equals("") && !traits_in_project.contains(t1)) {

                    if (dr1.equals("Dominant")) {
                        weight = 2;
                    } else if (dr1.equals("Recessive")) {
                        weight = 1;
                    } else {
                        weight = 3;
                    }
                    String q = "name=" + t1 + "&type_id=" + weight + "&pool=" + p1;
                    URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/traits");
                    HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                    client.setRequestMethod("POST");
                    client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                    byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                    client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                    Log.w("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                    client.addRequestProperty("charset", "utf-8");
                    client.setRequestProperty("Content-Length", Integer.toString(q.length()));
                    client.setUseCaches(false);
                    client.setDoOutput(true);
                    DataOutputStream op = new DataOutputStream(client.getOutputStream());
                    op.write(q.getBytes());
                    Log.w("Trait Creation","Name - " + t1);
                    ip = new BufferedInputStream(client.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    Log.w("Flag","76");
                    while ((line = reader.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    result = sb.toString();
                    Log.w("Trait Created!", "");
                }
                if (!t2.equals("") && !traits_in_project.contains(t2)) {

                    if (dr2.equals("Dominant")) {
                        weight = 2;
                    } else if (dr2.equals("Recessive")) {
                        weight = 1;
                    } else {
                        weight = 3;
                    }
                    String q = "name=" + t2 + "&type_id=" + weight + "&pool=" + p2;
                    URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/traits");
                    HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                    client.setRequestMethod("POST");
                    client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                    byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                    client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                    Log.w("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                    client.addRequestProperty("charset", "utf-8");
                    client.setRequestProperty("Content-Length", Integer.toString(q.length()));
                    client.setUseCaches(false);
                    client.setDoOutput(true);
                    DataOutputStream op = new DataOutputStream(client.getOutputStream());
                    op.write(q.getBytes());
                    Log.w("Trait Creation","Name - " + t2);
                    ip = new BufferedInputStream(client.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    result = sb.toString();
                    Log.w("Trait Created!", "");
                }
                if (!t3.equals("") && !traits_in_project.contains(t3)) {

                    if (dr3.equals("Dominant")) {
                        weight = 2;
                    } else if (dr3.equals("Recessive")) {
                        weight = 1;
                    } else {
                        weight = 3;
                    }
                    String q = "name=" + t3 + "&type_id=" + weight + "&pool=" + p3;
                    URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/traits");
                    HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                    client.setRequestMethod("POST");
                    client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                    byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                    client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                    Log.w("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                    client.addRequestProperty("charset", "utf-8");
                    client.setRequestProperty("Content-Length", Integer.toString(q.length()));
                    client.setUseCaches(false);
                    client.setDoOutput(true);
                    DataOutputStream op = new DataOutputStream(client.getOutputStream());
                    op.write(q.getBytes());
                    Log.w("Trait Creation","Name - " + t3);
                    ip = new BufferedInputStream(client.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    Log.w("Flag","76");
                    while ((line = reader.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    result = sb.toString();
                    Log.w("Trait Created!", "");
                }
                if (!t4.equals("") && !traits_in_project.contains(t4)) {

                    if (dr4.equals("Dominant")) {
                        weight = 2;
                    } else if (dr4.equals("Recessive")) {
                        weight = 1;
                    } else {
                        weight = 3;
                    }
                    String q = "name=" + t4 + "&type_id=" + weight + "&pool=" + p4;
                    URL apiURL = new URL("http://bloomgenetics.tech/api/v1/projects/" + bundle.getInt("proj_id") + "/traits");
                    HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                    client.setRequestMethod("POST");
                    client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                    byte[] ba = UserAuth.getInstance().getAuthorization().getBytes();
                    client.addRequestProperty("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                    Log.w("Authorization", "Basic " + Base64.encodeToString(ba,Base64.NO_WRAP));
                    client.addRequestProperty("charset", "utf-8");
                    client.setRequestProperty("Content-Length", Integer.toString(q.length()));
                    client.setUseCaches(false);
                    client.setDoOutput(true);
                    DataOutputStream op = new DataOutputStream(client.getOutputStream());
                    op.write(q.getBytes());
                    Log.w("Trait Creation","Name - " + t4);
                    ip = new BufferedInputStream(client.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    Log.w("Flag","76");
                    while ((line = reader.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    result = sb.toString();
                    Log.w("Trait Created!", "");
                }
                Log.w("Trait Creation", "Complete");
            } catch (Exception e) {
                Log.w("Trait Creation Error", e + "");
            } finally {
            }


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            getTraits2 = new GetTraitSearch2(t1, dr1, p1, t2, dr2, p2, t3, dr3, p3, t4, dr4, p4);
            getTraits2.execute((Void) null);
        }
    }

}