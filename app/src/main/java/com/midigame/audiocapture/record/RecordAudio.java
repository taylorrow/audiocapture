package com.midigame.audiocapture.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecordAudio {

    private MediaRecorder mediaRecorder;
    private String fileName = Environment.getExternalStorageDirectory() + "/record.3gpp";
//    private String fileName = Environment.getExternalStorageDirectory() + "/record.wav";

    private int timer = 0;
    private boolean isRecord = true;

//    Thread threadOnRecord, threatOnPlay;

//    public void recordStart() {
//        threadOnRecord = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    if (!threadOnRecord.isInterrupted()) {
//                        Log.d("myLog", fileName);
//                        timerRecordStart();
//                        Log.d("myLog", "done1");
//                        releaseRecorder();
//                        File outFile = new File(fileName);
//                        if (outFile.exists()) {
//                            outFile.delete();
//                        }
//
//                        mediaRecorder = new MediaRecorder();
//                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                        mediaRecorder.setOutputFile(fileName);
//                        mediaRecorder.prepare();
//                        mediaRecorder.start();
//                        Log.d("myLog", "done");
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        Log.d("myLog", "start");
//        threadOnRecord.start();
//    }

    public void recordStart() {
        Log.d("myLog", fileName);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("myLog", "a = " + isRecord);
                if (!isRecord) {
                    mediaRecorder.stop();
                    return;
                }
                try {
                    Log.d("myLog", "done1");
                    releaseRecorder();
                    Log.d("myLog", "done2");
                    File outFile = new File(fileName);
                    if (outFile.exists()) {
                        outFile.delete();
                    }
                    Log.d("myLog", "done3");
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(fileName);
                    Log.d("myLog", "done4");
                    mediaRecorder.prepare();
                    Log.d("myLog", "done5");
                    mediaRecorder.start();
                    Log.d("myLog", "done6");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                isRecord = false;
                handler.postDelayed(this, 10000);
            }
        };
        handler.post(runnable);
    }

//    private void timerRecordStart() {
//        Log.d("myLog", "RecordStart");
//        final Handler handler = new Handler();
//        Log.d("myLog", "runnable1");
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                Log.d("myLog", "runnable");
//                timer++;
//                if (timer == 10) {
//                    timer = 0;
//                    threadOnRecord.interrupt();
//                    return;
//                }
////                Log.d("myLog", "runnable");
//                handler.postDelayed(this, 1000);
//            }
//        };
//        handler.post(runnable);
//    }

//    public void start() {
//        threatOnPlay = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (threatOnPlay.isInterrupted()) {
//                        timerPlayStart();
//                        releasePlayer();
//                        mediaPlayer = new MediaPlayer();
//                        mediaPlayer.setDataSource(fileName);
//                        mediaPlayer.prepare();
//                        mediaPlayer.start();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        threatOnPlay.start();
//    }

//    private void timerPlayStart() {
//        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                timer++;
//                if (timer == 10) {
//                    timer = 0;
//                    threatOnPlay.interrupt();
//                    return;
//                }
//                handler.postDelayed(this, 1000);
//            }
//        };
//        handler.postDelayed(runnable, 0);
//    }

    private void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
