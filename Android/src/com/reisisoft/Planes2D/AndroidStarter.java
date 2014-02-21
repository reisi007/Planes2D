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

    @Override
    public MovementContainer Input() {
        return Input;
    }

    @Override
    public void Setup() {

    }
}