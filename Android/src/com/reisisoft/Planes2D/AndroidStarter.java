package com.reisisoft.Planes2D;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidStarter extends AndroidApplication implements INative {
    private MovementContainer Input = new MovementContainer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = false;
        cfg.useCompass = true;
        cfg.useWakelock = true;
        cfg.useGL20 = true;
        cfg.hideStatusBar = true;
        initialize(new Planes2D(this), cfg);
    }

    int i = 0;

    @Override
    public MovementContainer Input() {
        i++;
        if (i % 20 == 0)
            System.out.println("Movement:\t");
        return Input;
    }

    @Override
    public void letQuit() {
        //Not needed for Android
    }

    @Override
    public void Setup() {

    }
}