package medibase.gaurav.com.healthcareforseniorcitizen;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CalculateDistance extends AppCompatActivity {

    ImageButton SOS;
    String API2;
    String user;
    long phones[];
    double lat[],lon[], userlat, userlon;
    long selected[];
    SharedPreferences sp;
    String friendAPI = "";

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_call);

        SOS = findViewById(R.id.SOS);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this,"Cannot access location",Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        userlat = (location.getLatitude());
                        userlon = (location.getLongitude());

                    }
                });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp = getSharedPreferences("key",0);
                user = sp.getString("Username", "");
                FriendList friendList = new FriendList();
                friendList.execute(friendAPI);
            }
        });
    }

    public class FriendList extends AsyncTask<String,Void,String> {

        String result="";
        URL url;
        HttpURLConnection httpURLConnection;

        @Override
        protected String doInBackground(String... urls) {
            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                Log.e("message",String.valueOf(url));

                int data = reader.read();

                while(data!=-1)
                {
                    char chardata = (char)data;
                    result += chardata;

                    data = reader.read();
                }
                return result;

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            try{
                JSONObject json = new JSONObject(result);
                if(json!=null)
                {
                    JSONArray  rows = json.getJSONArray("Phone");
                    phones = new long[rows.length()];
                    lat = new double[rows.length()];
                    lon = new double[rows.length()];

                    for(int i = 0; i<rows.length() ; i++)
                    {
                        phones[i] = json.getJSONArray("Phone").getInt(i);
                        lat[i] = json.getJSONArray("lat").getDouble(i);
                        lon[i] = json.getJSONArray("lon").getDouble(i);

                    }

                    DistanceFinder distanceFinder = new DistanceFinder(lat,lon,userlat,userlon,phones);
                    selected = distanceFinder.rangeOfDistance(userlat,userlon);
                }

            }
            catch (Exception e){
            }
            return;
        }
    }



}
