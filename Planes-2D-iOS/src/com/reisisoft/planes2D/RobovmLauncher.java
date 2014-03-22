package com.reisisoft.planes2d;

import org.robovm.cocoatouch.foundation.NSAutoreleasePool;
import org.robovm.cocoatouch.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.reisisoft.planes2d.Planes2D.Resolutions;

public class RobovmLauncher extends IOSApplication.Delegate implements INative {
	@Override
	protected IOSApplication createApplication() {
		IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		config.orientationLandscape = true;
		config.orientationPortrait = false;
		return new IOSApplication(new Planes2D(this), config);
	}

	public static void main(String[] argv) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(argv, null, RobovmLauncher.class);
		pool.drain();
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

	public boolean continueStagesWorkflow() {
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

	public boolean firstTouch() {
		// TODO Auto-generated method stub
		return false;
	}
}
