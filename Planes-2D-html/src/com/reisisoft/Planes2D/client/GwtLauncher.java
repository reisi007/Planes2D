package com.reisisoft.Planes2D.client;

import com.reisisoft.Planes2D.INative;
import com.reisisoft.Planes2D.MovementContainer;
import com.reisisoft.Planes2D.Planes2D;
import com.reisisoft.Planes2D.Planes2D.Resolutions;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication implements INative {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(1280,720);
		return cfg;
	}
	public ApplicationListener getApplicationListener () {
		return new Planes2D(this);
	}

	public MovementContainer Input() {
		// TODO Auto-generated method stub
		return null;
	}

	public void letQuit() {
		// TODO Auto-generated method stub
		
	}

	public void Setup() {
		// TODO Auto-generated method stub
		
	}

	public boolean ContinueStagesWorkflow() {
		// TODO Auto-generated method stub
		return false;
	}

	public float speedX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float speedY() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Resolutions prefferredResolution(Planes2D game) {
		// TODO Auto-generated method stub
		return null;
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