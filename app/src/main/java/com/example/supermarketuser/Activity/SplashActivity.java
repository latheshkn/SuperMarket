package com.example.supermarketuser.Activity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.supermarketuser.R;
import com.example.supermarketuser.Utils.Config.GpsUtils;
import com.example.supermarketuser.Utils.Config.SharedPrefManager;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    SharedPrefManager prefManager;
    TextView text;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        text=findViewById(R.id.text);

        counter = SharedPrefManager.loadFrompref(this);
        if (counter > 0) {
            text.setText(String.valueOf(counter));
        }

        prefManager = new SharedPrefManager(this);
        final LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGpsDEvice(this)) {
            Toast.makeText(getApplicationContext(), "Gps already enabled", Toast.LENGTH_SHORT).show();
            redirectioScreen();
        }else{
              if (!hasGpsDEvice(this)){

                  Toast.makeText(getApplicationContext(), "Gps not  supported", Toast.LENGTH_SHORT).show();
                  finish();
              }
              if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGpsDEvice(this)){
                  Toast.makeText(getApplicationContext(), "Gps not  enabled", Toast.LENGTH_SHORT).show();
                 new GpsUtils(this).turnGpsOn(new GpsUtils.OnGpsListener() {
                     @Override
                     public void gpsStatus(boolean isGPSEnable) {
                         if (isGPSEnable){
                            redirectioScreen();
                         }
                     }
                 });
              }
        }


    }

    private void redirectioScreen() {

        ScheduledExecutorService work = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = () -> {
            if (prefManager.isLogedIn()) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (prefManager.isSkip()) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        work.schedule(runnable, 3, TimeUnit.SECONDS);
    }


    private boolean hasGpsDEvice(Context context) {
//        we are checking the location manager
        LocationManager mnr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//if the location manager contails null return false
        if (mnr == null) {
            return false;
        } else {

//       else we will store the list location providers in list
            final List<String> provider = mnr.getAllProviders();
            return provider.contains(LocationManager.GPS_PROVIDER);
        }

    }

}