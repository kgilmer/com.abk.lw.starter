package com.abk.lw.starter;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import timber.log.Timber;

/**
 * A sample serializer that writes state to a file.
 *
 * Created by kgilmer on 4/11/15.
 */
public class StateSerializer implements Runnable {
    private Context context;
    private UserConfiguration uc;

    public StateSerializer(Context context, UserConfiguration uc) {
        this.context = context;
        this.uc = uc;
    }

    @Override
    public void run() {
        File store = new File(context.getFilesDir(), "state.bin");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(store));
            writer.write(Integer.toString(uc.getValue()));
            writer.newLine();
            writer.close();
            Timber.i("Saved state. " + uc.getValue());
        } catch (IOException e) {
            Timber.e("Failed to save state.", e);
        }
    }
}
