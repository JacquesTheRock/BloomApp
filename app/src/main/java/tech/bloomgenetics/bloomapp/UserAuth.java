package tech.bloomgenetics.bloomapp;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mdric on 10/18/2016.
 */

class UserAuth {

    private static UserAuth authObject;
    public static UserAuth getInstance() {
        if(authObject == null)
            authObject = new UserAuth();
        return authObject;
    }
    private String user, login_token ;

    private UserAuth() {
    }
    public void setUsername(String username) {
        user = username;
        login_token = "";
    }
    public String getAuthorization() {
        return user + ":" + login_token;
    }
    public Boolean Login(String password) {

        InputStream ip = null;
        String result = null;
        try {
            String q = "user=" + user + "&password=" + password;
            URL apiURL = new URL("http://bloomgenetics.tech/api/v1/auth");
            HttpURLConnection client = (HttpURLConnection) apiURL.openConnection();
            client.setRequestMethod("POST");
            client.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
            client.addRequestProperty("charset", "utf-8");
            client.setRequestProperty("Content-Length", Integer.toString(q.length()));
            client.setUseCaches(false);
            client.setDoOutput(true);
            DataOutputStream op = new DataOutputStream(client.getOutputStream());
            op.write(q.getBytes());
            Log.w("Auth",user);
            ip = new BufferedInputStream(client.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(ip,"UTF-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line+"\n");
            }
            result = sb.toString();
            Log.w("Auth",result);
        } catch (Exception e) {
            Log.w("Auth",e.getMessage());
        } finally {
        }
        JSONObject json = null;
        JSONObject json2 = null;

        try {
            json = new JSONObject(result);
            json2 = new JSONObject(json.getString("data"));
            user = json2.getString("id");
            login_token = json2.getString("token");
        } catch (Exception e) {

        }
        Log.w("Auth", "token: " + login_token);
        return !login_token.equals("");
    }

}
