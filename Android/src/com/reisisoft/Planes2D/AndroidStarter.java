package com.reisisoft.Planes2D;

import android.os.Bundle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidStarter extends AndroidApplication implements INative {
    private MovementContainer Input = new MovementContainer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = true;
        cfg.useCompass = true;
        cfg.useWakelock = true;
        cfg.useGL20 = true;
        cfg.hideStatusBar = true;
        initialize(new Planes2D(this), cfg);
    }

    int i = 0;
    float max = 45;

    @Override
    public MovementContainer Input() {
        i++;
        Input.doShoot = Gdx.input.isTouched();
        float m = 0f;
        switch (Gdx.input.getRotation()) {
            case (0):
            case (90):
                m = -Gdx.input.getPitch();
                break;
            case (180):
            case (270):
                m = Gdx.input.getPitch();
                break;
        }
        if (m > max)
            m = max;
        if (m < -max)
            m = -max;
        Input.Movement = m / 90f;
       /* if (i % 20 == 0) {
            System.out.println("Movement:(" + Gdx.input.isPeripheralAvailable(com.badlogic.gdx.Input.Peripheral.Accelerometer) + ")" + "\tX:\t" + Gdx.input.getPitch() + "\tY:\t" + Gdx.input.getRoll() + "\tZ:\t" + Gdx.input.getAzimuth());
            System.out.println(Gdx.input.getRotation());
            System.out.println(Input.toString());
        }*/
        return Input;
    }

    @Override
    public void letQuit() {
        //Not needed for Android
    }

    @Override
    public void Setup() {

    }

    @Override
    public boolean ContinueStagesWorkflow() {
        return Gdx.input.isTouched();
    }

    @Override
    public float speedX() {
        return 0.8f;
    }

    @Override
    public float speedY() {
        return 1.3f;
    }

    @Override
    public Planes2D.Resolutions prefferredResolution(Planes2D game) {
        return Planes2D.Resolutions.LowRes; //Dummy implementation
    }
}