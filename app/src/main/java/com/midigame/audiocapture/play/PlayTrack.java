package com.midigame.audiocapture.play;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Environment;
import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PlayTrack {

    private AudioTrack audioTrack;
    private boolean isPlay = true;

    public void start() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!isPlay) {
                    audioTrack.stop();
                    return;
                }
                File file = new File(Environment.getExternalStorageDirectory(), "/test.pcm");
                int shortSizeInBytes = Short.SIZE / Byte.SIZE;
                int bufferSizeInBytes = (int) (file.length() / shortSizeInBytes);
                short[] audioData = new short[bufferSizeInBytes];
                try {
                    InputStream inputStream = new FileInputStream(file);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
                    int i = 0;
                    while (dataInputStream.available() > 0) {
                        audioData[i] = dataInputStream.readShort();
                        i++;
                    }
                    short[] reversAudio = new short[audioData.length];
                    i = 0;
                    for (int w = audioData.length - 1; w >= 0; w--) {
                        reversAudio[i] = audioData[w];
                        i++;
                    }
                    audioData = reversAudio;
                    dataInputStream.close();
                    audioTrack = new AudioTrack(
                            AudioManager.STREAM_MUSIC,
                            8000,
                            AudioFormat.CHANNEL_CONFIGURATION_MONO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            bufferSizeInBytes,
                            AudioTrack.MODE_STREAM);
                    audioTrack.play();
                    audioTrack.write(audioData, 0, bufferSizeInBytes);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isPlay = false;
                handler.postDelayed(this, 10000);
            }
        };
        handler.post(runnable);
    }
}