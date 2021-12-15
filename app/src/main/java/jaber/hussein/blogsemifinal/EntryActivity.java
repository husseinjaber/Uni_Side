package jaber.hussein.blogsemifinal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class EntryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        if(isInternetWorking()) {
            Toast.makeText(EntryActivity.this, "working", Toast.LENGTH_SHORT).show();
            new CountDownTimer(3000, 1000) {
                public void onFinish() {
                    // When timer is finished
                    // Execute your code here

                    Intent toMain = new Intent(EntryActivity.this,MainActivity.class);
                    startActivity(toMain);
                    finish();

                }

                public void onTick(long millisUntilFinished) {
                    // millisUntilFinished    The amount of time until finished.
                }
            }.start();
        }
        else
        {
            Toast.makeText(this, "Not working", Toast.LENGTH_SHORT).show();
        }


    }
    public boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }






}
