package com.ebatuque.poc_ebatuque_android;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Musicservice extends Service {

    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        Toast.makeText(this, "Music Service Created",Toast.LENGTH_SHORT);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.forro112);
        mediaPlayer.setLooping(true);
    }

    public void onStart(Intent intent, int startId){
        Toast.makeText(this, "Music Service Started",Toast.LENGTH_SHORT);
        mediaPlayer.start();

    }

    public void onDestroy(){
        Toast.makeText(this, "Music Service Stopped",Toast.LENGTH_SHORT);
        mediaPlayer.stop();
    }


}
