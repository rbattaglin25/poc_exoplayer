package com.ebatuque.poc_ebatuque_android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

public class AudioService extends Service {

    SimpleExoPlayer simpleExoPlayers;
    LoopingMediaSource loopingSource;
    MediaSource mediaSource;
    ChannelMappingAudioProcessor channelMappingAudioProcessor;
    DefaultRenderersFactory defaultRenderersFactory;

    DataSource.Factory dataSourceFactory;
    int[] outputChannels = {1, 1};
    float[] outputChannelsVolume = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};

    String loopFile;

    String RES_RAW = "raw";



    public AudioService(Context context, String loopFile, int rhythmTempoCur,
                        int rhythmTempoStd){

        this.loopFile = loopFile;
        // Sample Rate used for recording the audio file; standard value normally is 44100
        final int sampleRate = 44100;

        defaultRenderersFactory = new DefaultRenderersFactory(context) {

            protected AudioProcessor[] buildAudioProcessors() {

                // a modified version of ChannelMappingAudioProcessor is being used
                channelMappingAudioProcessor = new ChannelMappingAudioProcessor();
                try {
                    channelMappingAudioProcessor.initialize(sampleRate,
                            outputChannels.length, C.ENCODING_PCM_16BIT);
                } catch (AudioProcessor.UnhandledAudioFormatException e) {
                }
                channelMappingAudioProcessor.setChannelMap(outputChannels, outputChannelsVolume);
                return new AudioProcessor[] {channelMappingAudioProcessor};
            }
        };


        final int resID = context.getResources().getIdentifier(loopFile,
                RES_RAW, context.getPackageName());
        Uri uri = RawResourceDataSource.buildRawResourceUri(resID);
        this.dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, context.getString(R.string.app_name)));

        mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

        defaultRenderersFactory.setEnableAudioOffload(true);
        simpleExoPlayers = new SimpleExoPlayer.Builder(context,defaultRenderersFactory).build();
        simpleExoPlayers.experimentalSetOffloadSchedulingEnabled(true);
        loopingSource = new LoopingMediaSource(mediaSource, 100);

        final float playRate = ((float) rhythmTempoCur / (float) rhythmTempoStd);
        simpleExoPlayers.setPlaybackParameters(new PlaybackParameters(playRate, 1.0f));


//         Each channel must have its volume adjusted
//         It is based on the last saved value (or the initial value when played for first time)
        int channelCount  = 4;
        int[] instrPanLeftArray = {50,50,50,50};
        int[] instrVolumeArray = {100,100,100,100};

        for (int i = 0; i < channelCount; i++) {
            setOutputChannelsVolume(i,instrPanLeftArray,instrVolumeArray);
        }

    }

    /**
     * Sets the volume for each channel
     * It includes setting for Pan - right and left balance -  and Volume itself
     * Volume output for each channel is the product of volume times pan
     * Channel numbering uses following rules:
     * 0 - first channel left
     * 1 - first channel right
     * 2 - second channel left
     * 3 - second channel right
     * 4 - third channel left
     * 5 - third channel right
     * 6 - fourth channel left
     * 7 - fourth channel right
     * @param instrumentIndex - number of each pair of channels
     *                        - it varies from 0 to 3
     */
    public void setOutputChannelsVolume(int instrumentIndex,int[] instrPanLeftArray, int[] instrVolumeArray) {
        try {
            float instrumentChannelLeft = ((float) instrVolumeArray[instrumentIndex] *
                    (float) instrPanLeftArray[instrumentIndex]) / 10000;
            instrumentChannelLeft = (instrumentChannelLeft < 0.0f) ? 0.0f : instrumentChannelLeft;
            instrumentChannelLeft = (instrumentChannelLeft > 1.0f) ? 1.0f : instrumentChannelLeft;
            int leftChannelIdx = instrumentIndex*2;
            outputChannelsVolume[leftChannelIdx] = instrumentChannelLeft;

            float instrumentChannelRight = ((float) instrVolumeArray[instrumentIndex] *
                    (float) (100 - instrPanLeftArray[instrumentIndex])) / 10000;
            instrumentChannelRight = (instrumentChannelRight < 0.0f) ? 0.0f : instrumentChannelRight;
            instrumentChannelRight = (instrumentChannelRight > 1.0f) ? 1.0f : instrumentChannelRight;
            int rightChannelIdx = instrumentIndex*2+1;
            outputChannelsVolume[rightChannelIdx] = instrumentChannelRight;

            channelMappingAudioProcessor.setChannelMap(outputChannels, outputChannelsVolume);
        }catch (Exception e){
            //none
        }
    }


//    public void setPlayBackParameters(float playRate){
//        simpleExoPlayers.setPlaybackParameters(new PlaybackParameters(playRate));
//    }

    public void play(){
        simpleExoPlayers.setPlayWhenReady(true);
        simpleExoPlayers.prepare(loopingSource);
    }

    public void stop(){
        simpleExoPlayers.stop();
    }


//    public void release(){
//        simpleExoPlayers.release();
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
