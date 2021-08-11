package com.ebatuque.poc_ebatuque_android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.DefaultAudioSink;
import com.google.android.exoplayer2.audio.ForwardingAudioSink;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;

public class AudioServiceExoPlayer extends Service {

    ChannelMappingAudioProcessor channelMappingAudioProcessor;

    int[] outputChannels = {1, 1};
    float[] outputChannelsVolume = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    String loopFile;
    String RES_RAW = "raw";
    SimpleExoPlayer player;
    final int sampleRate = 44100;
    final int bufferSize = 50000;

    public AudioServiceExoPlayer(Context context, String loopFile, float speed, float volume){
        this.loopFile = loopFile;

        DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(context)
        {
            @Nullable
            @Override
            protected AudioSink buildAudioSink(Context context,
                                               boolean enableFloatOutput,
                                               boolean enableAudioTrackPlaybackParams,
                                               boolean enableOffload) {

                AudioSink audioSink = new DefaultAudioSink(AudioCapabilities.getCapabilities(context),
                        new DefaultAudioSink.DefaultAudioProcessorChain(buildAudioProcessors()),
                        enableFloatOutput,
                        enableAudioTrackPlaybackParams,
                        DefaultAudioSink.OFFLOAD_MODE_DISABLED);

                ForwardingAudioSink forwardingAudioSink = new ForwardingAudioSink(audioSink);

                try {
                    Format format = new Format.Builder().build().buildUpon()
                            .setChannelCount(outputChannelsVolume.length)
                            .setPcmEncoding(C.ENCODING_PCM_16BIT)
                            .setSampleMimeType("audio/vorbis")
                            .setSampleRate(sampleRate).build();

                    forwardingAudioSink.configure(format, bufferSize, outputChannels);
                } catch (AudioSink.ConfigurationException e) {
                    e.printStackTrace();
                }

                return forwardingAudioSink;
            }


        };

        player = new SimpleExoPlayer.Builder(context,defaultRenderersFactory).build();
        final int resID = context.getResources().getIdentifier(loopFile,
                RES_RAW, context.getPackageName());
        Uri uri = RawResourceDataSource.buildRawResourceUri(resID);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.setVolume(volume);
        player.setRepeatMode(player.REPEAT_MODE_ALL);
        setPlayBackParameters(speed);
        player.prepare();

//      Each channel must have its volume adjusted
//      It is based on the last saved value (or the initial value when played for first time)
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
            float volumeLimeter = 0.5f;
            float instrumentChannelLeft = (float) instrVolumeArray[instrumentIndex]/100*volumeLimeter;
            float instrumentChannelRight = (float) instrVolumeArray[instrumentIndex]/100*volumeLimeter;
            float adjustmentFactor = 1;
            int centralPosition = 50;
            int maxVolume = 100;
            int leftChannelIdx = instrumentIndex*2;
            int rightChannelIdx = instrumentIndex*2+1;

            if(instrPanLeftArray[instrumentIndex] > centralPosition){
                int instPanLeft = instrPanLeftArray[instrumentIndex];
                instPanLeft = Math.min(instPanLeft, maxVolume);
                adjustmentFactor = (float) (maxVolume - ((instPanLeft - centralPosition)*2))/100;
                instrumentChannelRight = instrumentChannelRight*adjustmentFactor;
            }else if(instrPanLeftArray[instrumentIndex] < centralPosition){
                int instPanRight = instrPanLeftArray[instrumentIndex];
                instPanRight = Math.max(instPanRight, 0);
                adjustmentFactor = (float) (maxVolume - ((centralPosition - instPanRight)*2))/100;
                instrumentChannelLeft = instrumentChannelLeft*adjustmentFactor;
            }
            outputChannelsVolume[leftChannelIdx] = instrumentChannelLeft;
            outputChannelsVolume[rightChannelIdx] = instrumentChannelRight;

            channelMappingAudioProcessor.setChannelMap(outputChannels, outputChannelsVolume);
        }catch (Exception e){
            //none
        }
    }


    public void setPlayBackParameters(float playRate){
        player.setPlaybackParameters(new PlaybackParameters(playRate));
    }

    public void play(){

        player.play();


    }

    public void stop(){
        player.stop(true);
//        simpleExoPlayers.stop();
    }



    private AudioProcessor[] buildAudioProcessors() {

//                 a modified version of ChannelMappingAudioProcessor is being used
                channelMappingAudioProcessor = new ChannelMappingAudioProcessor();
                try {
                    channelMappingAudioProcessor.initialize(sampleRate,
                            outputChannels.length, C.ENCODING_PCM_16BIT);
                } catch (AudioProcessor.UnhandledAudioFormatException e) {
                    System.out.println(e.getMessage());
                }
                channelMappingAudioProcessor.setChannelMap(outputChannels, outputChannelsVolume);
                return new AudioProcessor[] {channelMappingAudioProcessor};
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
