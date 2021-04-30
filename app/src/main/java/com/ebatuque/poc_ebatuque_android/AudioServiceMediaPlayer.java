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

    private LoopMediaPlayer channel1, channel2, channel3, channel4;

    public AudioServiceMediaPlayer(Context context, String loopFile, float volume, float rate) {

        final int resID = context.getResources().getIdentifier(loopFile,
                RES_RAW, context.getPackageName());
        channel1 = LoopMediaPlayer.create(context,resID,volume,volume,rate);
//        channel2 = LoopMediaPlayer.create(context, R.raw.reco,volume,volume,rate);
//        channel3 = LoopMediaPlayer.create(context,R.raw.caixeta,volume,volume,rate);
//        channel4 = LoopMediaPlayer.create(context,R.raw.pandeiro,volume,volume,rate);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void start() throws IllegalStateException {
        channel1.start();
//        channel2.start();
//        channel3.start();
//        channel4.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        channel1.release();
//        channel2.release();
//        channel3.release();
//        channel4.release();
    }
}
