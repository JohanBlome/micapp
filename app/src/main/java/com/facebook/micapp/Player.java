package com.facebook.micapp;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class Player {
    final static String TAG = "micapp.play";

    AudioTrack mPlayer = null;
    byte[] mSound = null;
    Context mContext;
    public Player(Context context) {
        mContext = context;
    }

    void playSound() {
        int usage = AudioAttributes.USAGE_VOICE_COMMUNICATION;
        int type = AudioAttributes.CONTENT_TYPE_SPEECH;
        int samplerate = 48000;
        int buffersize = AudioTrack.getMinBufferSize(
                samplerate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (mSound == null) {
            InputStream is = mContext.getResources().openRawResource(R.raw.voices_48khz_s16pcm);
            int size;
            int read = 0;
            try {
                size = is.available();
                mSound = new byte[size];
                read = is.read(mSound);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                final AudioTrack player =
                        new AudioTrack.Builder()
                                .setAudioAttributes(new AudioAttributes.Builder()
                                        .setUsage(usage)
                                        .setContentType(type)
                                        .build())
                                .setAudioFormat(new AudioFormat.Builder()
                                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                        .setSampleRate(samplerate)
                                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                        .build())
                                .setTransferMode(AudioTrack.MODE_STREAM)
                                .setBufferSizeInBytes(buffersize)
                                .setPerformanceMode(AudioTrack.PERFORMANCE_MODE_LOW_LATENCY)
                                .build();
                int played = 0;
                player.play();

                int contentType = -1;
                AudioAttributes ats = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    ats = player.getAudioAttributes();

                    contentType = ats.getContentType();

                    int usage = ats.getUsage();
                    int flags = ats.getFlags();
                    Log.d(TAG, "Player:\nusage: " + usage + "\nContent type = " + contentType + "\nflags: " + flags);
                }


                while (played < mSound.length) {
                    int written = player.write(mSound, played, buffersize);
                    if (written > 0) {
                        played += written;
                    } else {
                        Log.d(TAG, "Failed to write to player: " + written);
                        break;
                    }
                }


                player.release();
            }
        });
        t.start();


    }
}
