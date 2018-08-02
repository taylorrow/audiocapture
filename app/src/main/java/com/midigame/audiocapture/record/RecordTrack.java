package com.midigame.audiocapture.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RecordTrack {

    private String fileName = Environment.getExternalStorageDirectory() + "/test.pcm";
    private AudioRecord audioRecord;
    private short[] audioData;
    private int minBufferSize;
    private DataOutputStream dataOutputStream;
    private Boolean recording = true;
    private boolean isRecord = true;

    public void start() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!isRecord) {
                    audioRecord.stop();
                    recording = false;
                    return;
                }
                try {
                    File file = new File(fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    OutputStream outputStream = new FileOutputStream(file);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                    dataOutputStream = new DataOutputStream(bufferedOutputStream);
                    minBufferSize = AudioRecord.getMinBufferSize(8000,
                            AudioFormat.CHANNEL_CONFIGURATION_MONO,
                            AudioFormat.ENCODING_PCM_16BIT);
                    audioData = new short[minBufferSize];
                    audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            8000,
                            AudioFormat.CHANNEL_CONFIGURATION_MONO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            minBufferSize);
                    audioRecord.startRecording();
                    bufferRead();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isRecord = false;
                handler.postDelayed(this, 10000);
            }
        };
        handler.post(runnable);
    }

    private void bufferRead() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (recording) {
                        int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);
                        for (int i = 0; i < numberOfShort; i++) {
                            dataOutputStream.writeShort(audioData[i]);
                        }
                    }
                    dataOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
