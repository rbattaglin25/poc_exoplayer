package com.ebatuque.poc_ebatuque_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent serviceIntent;
    private Button buttonPlay, buttonPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPause = findViewById(R.id.buttonPause);

        buttonPlay.setOnClickListener(this);
        buttonPause.setOnClickListener(this);

        serviceIntent = new Intent(getApplicationContext(), MusicService.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonPlay:
                startService(serviceIntent);
                break;
            case R.id.buttonPause:
                stopService(serviceIntent);
                break;
        }

    }
}