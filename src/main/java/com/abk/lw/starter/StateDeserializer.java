package com.abk.lw.starter;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import timber.log.Timber;

/**
 * A sample deserializer that reads state as strings to a file.
 *
 * Created by kgilmer on 4/11/15.
 */
public class StateDeserializer implements Runnable {
    private Context context;
    private UserConfiguration uc;

    public StateDeserializer(Context context, UserConfiguration uc) {
        this.context = context;
        this.uc = uc;
    }

    @Override
    public void run() {
        File store = new File(context.getFilesDir(), "state.bin");
        if (store.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(store));
                String val = br.readLine();
                br.close();

                if (val != null && val.trim().length() > 0) {
                    try {
                        uc.setValue(Integer.parseInt(val));
                        Timber.i("Loaded state: " + val);
                        return;
                    } catch (NumberFormatException e) {
                        Timber.e("Failed to parse state.", e);
                    }
                } else {
                    Timber.e("Empty state file.");
                }
            } catch (IOException e) {
                Timber.e("Failed to load state.", e);
            }
        } else {
            Timber.e("Save file doesn't exist.");
        }
        Timber.i("Unable to restore state.");
    }
}
