package com.ebatuque.poc_ebatuque_android;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MusicService extends Service {

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
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Music Service Started",Toast.LENGTH_SHORT);
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy(){
        Toast.makeText(this, "Music Service Stopped",Toast.LENGTH_SHORT);
        mediaPlayer.release();
    }


}
