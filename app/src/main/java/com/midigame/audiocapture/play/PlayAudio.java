package com.midigame.audiocapture.play;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.midigame.audiocapture.coverter.ConvertAudio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PlayAudio {

    private MediaPlayer mediaPlayer;
    private String fileName = Environment.getExternalStorageDirectory() + "/record.3gpp";
    //    private String fileName = Environment.getExternalStorageDirectory() + "/record.wav";
    private boolean isPlay = true;
    Context context;

    public PlayAudio(Context context) {
        this.context = context;
    }

    public void getCon() {
        int q = 0;
        int w = 0;
        int j = 0;
        byte[] audioByte = new ConvertAudio(context).converter();
        byte[] audioByteNew = new byte[audioByte.length - q - w];
        byte[] audioByteNew2 = new byte[audioByteNew.length];
        Log.d("myLog", "audioByte1=" + audioByte.length);
        for (int i = q; i < audioByte.length - w; i++) {
            audioByteNew[j] = audioByte[i];
            j++;
        }
        j = 0;
        for (int i = audioByteNew.length - 1; i <= 0; i--) {
            audioByteNew2[j] = audioByteNew[i];
            j++;
        }
//        for (int i = q; i < audioByteNew2.length; i++) {
//            audioByte[i] = audioByteNew2[i];
//        }
        Log.d("myLog", "audioByte2=" + audioByte.length);
        playConvertAudio(audioByte);
    }

    public void playConvertAudio(byte[] soundBytes) {
        File path = new File(fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            fos.write(soundBytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        releasePlayer();
        mediaPlayer = new MediaPlayer();

        try {

            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playStart() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!isPlay) {
                    mediaPlayer.stop();
                    return;
                }
                try {
                    releasePlayer();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(fileName);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isPlay = false;
                handler.postDelayed(this, 10000);
            }
        };
        handler.post(runnable);
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
