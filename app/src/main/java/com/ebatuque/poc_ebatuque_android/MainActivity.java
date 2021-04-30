package com.ebatuque.poc_ebatuque_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonPlayEP, buttonPauseEP, buttonPlayMP, buttonPauseMP;
    private AudioServiceExoPlayer audioServiceExoPlayer;
    private AudioServiceMediaPlayer audioServiceMediaPlayer;

    String rhythm = "choro_with_recoreco";
//    String rhythm = "reco_reco";
//    String rhythm = "choro_with_reco_dynamic";
//    String rhythm = "choro_remove_reco";
//    String rhythm = "choro_replace_reco_for_le";
//    String rhythm = "choro_replace_reco_for_xequere";
//    String rhythm = "xequere_agogo";

    float speed = 1.3f;
    float volume = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlayEP = findViewById(R.id.buttonPlay);
        buttonPauseEP = findViewById(R.id.buttonPause);
        buttonPlayMP = findViewById(R.id.playMediaPlayer);
        buttonPauseMP = findViewById(R.id.pauseMediaPlayer);

        buttonPlayEP.setOnClickListener(this);
        buttonPauseEP.setOnClickListener(this);
        buttonPlayMP.setOnClickListener(this);
        buttonPauseMP.setOnClickListener(this);

        audioServiceExoPlayer = new AudioServiceExoPlayer(getApplicationContext(),rhythm, speed,volume);
        audioServiceMediaPlayer = new AudioServiceMediaPlayer(getApplicationContext(),rhythm, volume,speed);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                audioServiceExoPlayer.play();
                break;
            case R.id.buttonPause:
                audioServiceExoPlayer.stop();
                break;
            case R.id.playMediaPlayer:
                audioServiceMediaPlayer.start();
//                audioServiceMediaPlayer.setVolume();
//                audioServiceMediaPlayer.rate();
                break;
            case R.id.pauseMediaPlayer:
                audioServiceMediaPlayer.onDestroy();
                audioServiceMediaPlayer = new AudioServiceMediaPlayer(getApplicationContext(),rhythm,volume,speed);
                break;
        }
    }
}