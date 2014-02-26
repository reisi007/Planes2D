package com.reisisoft.Planes2D.client;

import com.reisisoft.Planes2D.INative;
import com.reisisoft.Planes2D.Planes2D;
import com.reisisoft.Planes2D.MovementContainer;
import com.reisisoft.Planes2D.Planes2D.Resolutions;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication implements INative {
	MovementContainer Input = new MovementContainer();
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(1280,720);
		return cfg;
	}
	public ApplicationListener getApplicationListener () {
		return new Planes2D(this);
	}

	public MovementContainer Input() {
		Input.doShoot = Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP))
            Input.Movement = 1f;
        else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN))
            Input.Movement = -1f;
        else
            Input.Movement = 0f;
        return Input;
	}

	public void letQuit() {	
		//Not needed
	}

	public void Setup() {
		// Not needed		
	}

	public boolean ContinueStagesWorkflow() {
		return Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
	}

	public float speedX() {
		return 1;
	}

	public float speedY() {
		return 1;
	}

	public Resolutions prefferredResolution(Planes2D game) {
		return Resolutions.HiRes;
	}

	public String WelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String GameOverMessage(int score, boolean newHighScore) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveScore(int score) {
		// TODO Auto-generated method stub
		
	}

	public int getHighScore() {
		// TODO Auto-generated method stub
		return 0;
	}
}