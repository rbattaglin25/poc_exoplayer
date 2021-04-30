package com.ebatuque.poc_ebatuque_android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.util.Log;

import com.google.android.exoplayer2.PlaybackParameters;

import java.io.IOException;
import java.lang.reflect.Parameter;

public class LoopMediaPlayer {


    private static final String TAG = LoopMediaPlayer.class.getName();
    private Context mContext = null;
    private int mResId = 0;
    private String mPath = null;

    private MediaPlayer mCurrentPlayer = null;
    private MediaPlayer mNextPlayer = null;

    private float mLeftVolume = 0.0f;
    private float mRightVolume = 0.0f;
    private float mRate = 0.0f;

    /**
     * Creating instance of the player with given context and raw resource
     *
     * @param context - context
     * @param resId   - raw resource
     * @return new instance
     */
    public static LoopMediaPlayer create(Context context, int resId, float leftVolume, float rightVolume, float rate) {
        return new LoopMediaPlayer(context, resId, leftVolume, rightVolume, rate);
    }


    private LoopMediaPlayer(Context context, int resId, float leftVolume, float rightVolume, float rate) {
        mContext = context;
        mResId = resId;
        mLeftVolume = leftVolume;
        mRightVolume = rightVolume;
        mRate = rate;
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(mResId);
            mCurrentPlayer = new MediaPlayer();
            mCurrentPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNextMediaPlayerRaw() {
        AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(mResId);
        mNextPlayer = new MediaPlayer();
        try {
            mNextPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mNextPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mNextPlayer.seekTo(0);
                    mCurrentPlayer.setNextMediaPlayer(mNextPlayer);
                    mCurrentPlayer.setVolume(mLeftVolume,mRightVolume);
                    PlaybackParams params = mCurrentPlayer.getPlaybackParams();
                    params.setSpeed(mRate);
                    mCurrentPlayer.setPlaybackParams(params);
                    mCurrentPlayer.setOnCompletionListener(onCompletionListener);
                }
            });
            mNextPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private final MediaPlayer.OnCompletionListener onCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mCurrentPlayer = mNextPlayer;
                    createNextMediaPlayerRaw();
                    mediaPlayer.release();
                }
            };


    public boolean isPlaying() throws IllegalStateException {
        if (mCurrentPlayer != null) {
            return mCurrentPlayer.isPlaying();
        } else {
            return false;
        }
    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (mCurrentPlayer != null) {
            mCurrentPlayer.setVolume(leftVolume, rightVolume);
            mLeftVolume = leftVolume;
            mRightVolume = rightVolume;
        } else {
            Log.d(TAG, "setVolume()");
        }

    }

    public void start() throws IllegalStateException {

        if (mCurrentPlayer != null) {
            mCurrentPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.d(TAG, "LoopMediaPlayer Prepared");
                    mCurrentPlayer.setVolume(mLeftVolume,mRightVolume);
                    PlaybackParams params = mCurrentPlayer.getPlaybackParams();
                    params.setSpeed(mRate);
                    mCurrentPlayer.setPlaybackParams(params);
                    mCurrentPlayer.start();
                }
            });
            mCurrentPlayer.prepareAsync();

            createNextMediaPlayerRaw();
        } else {
            Log.d(TAG, "start() | mCurrentPlayer is NULL");
        }

    }

    public void stop() throws IllegalStateException {
        if (mCurrentPlayer != null && mCurrentPlayer.isPlaying()) {
            Log.d(TAG, "stop()");
            mCurrentPlayer.stop();
            mNextPlayer.stop();
        } else {
            Log.d(TAG, "stop() | mCurrentPlayer " +
                    "is NULL or not playing");
        }

    }

    public void pause() throws IllegalStateException {
        if (mCurrentPlayer != null && mCurrentPlayer.isPlaying()) {
            Log.d(TAG, "pause()");
            mCurrentPlayer.pause();
        } else {
            Log.d(TAG, "pause() | mCurrentPlayer " +
                    "is NULL or not playing");
        }

    }

    public void setWakeMode(Context c, int mode) {
        if (mCurrentPlayer != null) {
            mCurrentPlayer.setWakeMode(c, mode);
            Log.d(TAG, "setWakeMode() | ");
        } else {
            Log.d(TAG, "setWakeMode() | " +
                    "mCurrentPlayer is NULL");
        }
    }

    public void setAudioStreamType(int audioStreamType) {
        if (mCurrentPlayer != null) {
            mCurrentPlayer.setAudioStreamType(audioStreamType);
        } else {
            Log.d(TAG, "setAudioStreamType() | " +
                    "mCurrentPlayer is NULL");
        }
    }

    public void release() {
        Log.d(TAG, "release()");
        if (mCurrentPlayer != null)
            mCurrentPlayer.release();
        if (mNextPlayer != null)
            mNextPlayer.release();
    }

    public void reset() {
        if (mCurrentPlayer != null) {
            Log.d(TAG, "reset()");
            mCurrentPlayer.reset();
        } else {
            Log.d(TAG, "reset() | " +
                    "mCurrentPlayer is NULL");
        }

    }

    public void rate(float speed){
        Log.d(TAG, "rate()");
        PlaybackParams params = new PlaybackParams();
        params.setSpeed(speed);

        if (mCurrentPlayer != null)
            mCurrentPlayer.setPlaybackParams(params);
        mRate = speed;
    }

}
