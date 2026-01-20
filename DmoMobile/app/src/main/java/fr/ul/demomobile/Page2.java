package fr.ul.demomobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Page2 extends BaseActivity {
    private TextView tv;
    private TextView tv2;
    private FusedLocationProviderClient clientPosition;
    private TextView tv3;
    private double lat;
    private double lon;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page2);

        //Ajout du menu
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Zone de texte
        tv = findViewById(R.id.tv_page2);
        tv2 = findViewById(R.id.tv_localisatioin);
        tv3 = findViewById(R.id.tv_meteo);

        //affichage des infos reçus de l'actv principale
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String info = extras.getString("info");
            int val = extras.getInt("val");
            tv.setText(info + " " + val);
        }

        //initiliasation de la localisation
        clientPosition = LocationServices.getFusedLocationProviderClient(this);
    }



    //fct qui permet de retourner à l'activité principale
    public void rerourPage2(View view){
        Intent resultIntent = new Intent();
        Date dateCourante = Calendar.getInstance().getTime();
        resultIntent.putExtra("date_retour", dateCourante.toString());

        setResult(2, resultIntent);
        finish();
    }


    //fct d'action au bouton localisation
    public void locate(View view){
        //demande des droits
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        //get la position
        clientPosition.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location loc) {
                if (loc != null) {
                    // La position est définie
                    String result = "Lat: " + loc.getLatitude() + "\nLon: " + loc.getLongitude();
                    tv2.setText(result);
                    lat = loc.getLatitude();
                    lon = loc.getLongitude();

                    //affichage de la météo
                    printMeteo();

                } else {
                    tv2.setText(getString(R.string.meteo));
                }
            }
        });
    }


    private void printMeteo(){
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + lat +
                "&longitude=" + lon +
                "&current=temperature_2m,precipitation_probability,cloud_cover,wind_speed_10m,wind_direction_10m";

        //mise en thread pour pas surchargé l'appli
        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";

                JSONObject jsonResponse = new JSONObject(response);
                JSONObject current = jsonResponse.getJSONObject("current");
                JSONObject units = jsonResponse.getJSONObject("current_units");

                String temp = current.getDouble("temperature_2m") + " " + units.getString("temperature_2m");
                String rain = current.getInt("precipitation_probability") + " " + units.getString("precipitation_probability");
                String clouds = current.getInt("cloud_cover") + " " + units.getString("cloud_cover");
                String wind = current.getDouble("wind_speed_10m") + " " + units.getString("wind_speed_10m");

                String finalResult = "Temp : " + temp + "\n"+getString(R.string.rain)  +" : " + rain +
                        "\n"+getString(R.string.cloud)+" : " + clouds + "\n"+getString(R.string.wind)+" : " + wind;

                //Affiche et met fin au thread
                runOnUiThread(() -> {
                    tv3.setText(finalResult);
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> tv3.setText("Erreur lors de la récupération météo"));
            }
        }).start();
    }

}