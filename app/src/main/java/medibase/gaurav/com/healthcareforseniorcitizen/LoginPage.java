package medibase.gaurav.com.healthcareforseniorcitizen;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LoginPage extends AppCompatActivity {

    EditText username, password;
    Button login, signup;
    SharedPreferences sp;
    String loginStatus;
    String uname, pass;
    String API = " ";
    RequestQueue MyRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sp = getSharedPreferences("key", 0);
        loginStatus = sp.getString("Login", "");

        username = findViewById(R.id.usernameedit);
        password = findViewById(R.id.passwordedit);

        try {
            if (loginStatus.equals("true")) {
                uname = sp.getString("Username", "");
                pass = sp.getString("Password", "");
                HttpsCalls();
            }
        } catch (Exception e) {
        }

        login = findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpsCalls();
            }
        });
    }


    void changeIntent(String status) {
        if (status == "true") {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Login", "true");
            editor.putString("Username", username.getText().toString());
            editor.putString("Password", password.getText().toString());
            editor.apply();
            Intent intent = new Intent(LoginPage.this, CalculateDistance.class);
            startActivity(intent);
            finish();
        } else
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
    }

    void HttpsCalls() {
        MyRequestQueue = Volley.newRequestQueue(this);

        String url = "https://damp-harbor-71564.herokuapp.com/login";


        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                if(json!=null) {
                    String auth = (json.getString("auth"));
                    changeIntent(auth);

                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("Username", uname);
                MyData.put("Password",pass);//Add the data you'd like to send to the server.
                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }
}

