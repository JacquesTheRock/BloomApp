package tech.bloomgenetics.bloomapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CreateAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private AccountCreateTask createTask;
    View focusView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addItemsToSpinner();
        addListenerOnSpinnerItemSelection();

        Button mLoginButton = (Button) findViewById(R.id.login_redirect_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLogin();
            }
        });
    }

    public void addItemsToSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.create_growzone_spinner);

        List<String> growzones = new ArrayList<String>();
        growzones.add("Grow Zone (Optional)");
        growzones.add("1a");
        growzones.add("1b");
        growzones.add("2a");
        growzones.add("2b");
        growzones.add("3a");
        growzones.add("3b");
        growzones.add("4a");
        growzones.add("4b");
        growzones.add("5a");
        growzones.add("5b");
        growzones.add("6a");
        growzones.add("6b");
        growzones.add("7a");
        growzones.add("7b");
        growzones.add("8a");
        growzones.add("8b");
        growzones.add("9a");
        growzones.add("9b");
        growzones.add("10a");
        growzones.add("10b");
        growzones.add("11a");
        growzones.add("11b");
        growzones.add("12a");
        growzones.add("12b");
        growzones.add("13a");
        growzones.add("13b");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, growzones);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        Spinner spinner = (Spinner) findViewById(R.id.create_growzone_spinner);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    /* Redirects user to Login when login button is clicked */
    public void goLogin() {
        String email = ((EditText) findViewById(R.id.create_email)).getText().toString();
        String uid = ((EditText) findViewById(R.id.create_user_name)).getText().toString();
        String name = ((EditText) findViewById(R.id.create_name)).getText().toString();
        String password  = ((EditText) findViewById(R.id.create_password)).getText().toString();
        String cPassword  = ((EditText) findViewById(R.id.create_confirm_password)).getText().toString();
        String address = ((EditText) findViewById(R.id.create_address)).getText().toString();
        String growzone = ((Spinner) findViewById(R.id.create_growzone_spinner)).getSelectedItem().toString();
        if(password.equals(cPassword)){
            createTask = new AccountCreateTask(email, password, uid, name, address, growzone);
            createTask.execute((Void) null);
        }
        else {
            EditText cPasswordField = (EditText) findViewById(R.id.create_confirm_password);
            cPasswordField.setError("Passwords do not match!");
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AccountCreateTask extends AsyncTask<Void, Void, Boolean> {
        String email;// = ((EditText) findViewById(R.id.create_email)).getText().toString();
        String uid;// = ((EditText) findViewById(R.id.create_user_name)).getText().toString();
        String name;// = ((EditText) findViewById(R.id.create_name)).getText().toString();
        String password;//  = ((EditText) findViewById(R.id.create_password)).getText().toString();
        String address;// = ((EditText) findViewById(R.id.create_address)).getText().toString();
        String growzone;

        AccountCreateTask(String e, String p, String u, String n, String a, String g) {
            email = e;
            password = p;
            uid = u;
            name = n;
            address = a;
            growzone = g;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if(address.equals("")){
                address = "Nowhere";
            }
            if(name.equals("Grow Zone (Optional)")){
                growzone = "Not Given";
            }
            if(growzone.equals("Grow Zone (Optional)")){
                growzone = "Undisclosed";
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }
            InputStream ip = null;
            String result = null;

            // Sends POST request to server to add new user to database.
            try {
                String q = "username=" + uid + "&email=" + email + "&password=" + password + "&name=" + name + "&location=" + address + "&growzone=" + growzone + "&season=" + "All" + "&specialty=" + "None" + "&about=" + ". . .";
                URL apiURL = new URL("http://bloomgenetics.tech/api/v1/users");
                HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
                client.setRequestMethod("POST");
                client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                client.addRequestProperty("charset", "utf-8");
                client.setRequestProperty("Content-Length", Integer.toString(q.length()));
                client.setUseCaches(false);
                client.setDoOutput(true);
                DataOutputStream op = new DataOutputStream(client.getOutputStream());
                op.write(q.getBytes());
                Log.w("Account Creation",uid);
                ip = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line+"\n");
                }
                result = sb.toString();
                Log.w("Account Creation",result);
            } catch (Exception e) {
                Log.w("Account Creation",e + "");
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
                Intent intent = new Intent(getBaseContext(), Login.class);
                startActivity(intent);
            } else {
            }
        }

        @Override
        protected void onCancelled() {
        }


    }


}
