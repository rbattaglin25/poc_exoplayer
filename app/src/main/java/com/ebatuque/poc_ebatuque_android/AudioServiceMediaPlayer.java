package com.ebatuque.poc_ebatuque_android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class AudioServiceMediaPlayer extends Service {

    public static final String TAG = AudioServiceMediaPlayer.class.getSimpleName();
    String RES_RAW = "raw";

    private LoopMediaPlayer channel1;

    public AudioServiceMediaPlayer(Context context, String loopFile, float volume, float rate) {

        final int resID = context.getResources().getIdentifier(loopFile,
                RES_RAW, context.getPackageName());
        channel1 = LoopMediaPlayer.create(context,resID,volume,volume,rate);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void start() throws IllegalStateException {
        channel1.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        channel1.release();

    }
}
