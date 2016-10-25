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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateAccount extends AppCompatActivity {

    private AccountCreateTask createTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button mLoginButton = (Button) findViewById(R.id.login_redirect_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLogin();
            }
        });
    }

    /* Redirects user to Login when login button is clicked */
    public void goLogin() {
        String email = ((EditText) findViewById(R.id.create_email)).getText().toString();
        String uid = ((EditText) findViewById(R.id.create_user_name)).getText().toString();
        String name = ((EditText) findViewById(R.id.create_name)).getText().toString();
        String password  = ((EditText) findViewById(R.id.create_password)).getText().toString();
        String cPassword  = ((EditText) findViewById(R.id.create_confirm_password)).getText().toString();
        String address = ((EditText) findViewById(R.id.create_address)).getText().toString();
        createTask = new AccountCreateTask(email, password, uid, name, address);
        createTask.execute((Void) null);
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

        AccountCreateTask(String e, String p, String u, String n, String a) {
            email = e;
            password = p;
            uid = u;
            name = n;
            address = a;
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
            try {
                String q = "username=" + uid + "&email=" + email + "&password=" + password + "&name=" + name;
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
