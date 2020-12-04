package com.example.supermarketuser.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.supermarketuser.R;
import com.example.supermarketuser.Utils.Config.SharedPrefManager;

import static com.example.supermarketuser.Utils.Config.SharedPrefManager.PREF_TOTAL_KEY;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    int counter = 0;
    TextView count;
    Button btn, btn_dec;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count = findViewById(R.id.count);
        btn = findViewById(R.id.btn);
        btn_dec = findViewById(R.id.btn_dec);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                SharedPrefManager.SaveTotalKey(getApplicationContext(), counter);
            }
        });

        btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                counter--;
//                SharedPrefManager.SaveTotalKey(getApplicationContext(), counter);
                Intent intent=new Intent(MainActivity.this,SplashActivity.class);
                startActivity(intent);
            }
        });


//            counter = pref.getInt(PREF_TOTAL_KEY, 0);
//            if (counter>0){
//                count.setText(String.valueOf(counter));
//            }
        counter = SharedPrefManager.loadFrompref(this);
        if (counter > 0) {
            count.setText(String.valueOf(counter));

        }


    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREF_TOTAL_KEY)) {
            counter = sharedPreferences.getInt(PREF_TOTAL_KEY, 0);
            count.setText(String.valueOf(counter));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPrefManager.registerPrif(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPrefManager.unregisterPref(this, this);
    }
}