package com.midigame.audiocapture.coverter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ConvertAudio {

    private String fileName = Environment.getExternalStorageDirectory() + "/record.3gpp";
//    private String fileName = Environment.getExternalStorageDirectory() + "/record.wav";
    Context context;

    public ConvertAudio(Context context) {
        this.context = context;
    }

    public byte[] converter() {
        byte[] soundBytes;

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(Uri.fromFile(new File(fileName)));
            soundBytes = new byte[inputStream.available()];

            soundBytes = toByteArray(inputStream);
            return soundBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1)
                out.write(buffer, 0, read);
        }
        out.close();
        return out.toByteArray();
    }

}
