package com.reisisoft.Planes2D.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.reisisoft.Planes2D.Helper;
import com.reisisoft.Planes2D.INative;
import com.reisisoft.Planes2D.MovementContainer;
import com.reisisoft.Planes2D.Planes2D;
import com.reisisoft.Planes2D.Planes2D.Resolutions;

public class GwtLauncher extends GwtApplication implements INative {
	private MovementContainer Input = new MovementContainer();
	private Storage preferences = Storage.getLocalStorageIfSupported();

	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(
				(int) (0.95d * Window.getClientWidth()),
				(int) (0.95d * Window.getClientHeight()));
		return cfg;
	}

	public ApplicationListener getApplicationListener() {
		return new Planes2D(this);
	}

	public MovementContainer Input() {
		Input.doShoot = Gdx.input
				.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT)
				|| Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP))
			Input.Movement = 1f;
		else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN))
			Input.Movement = -1f;
		else
			Input.Movement = 0f;
		return Input;
	}

	public void letQuit() {
		// Not needed
	}

	public void Setup() {
		// Not needed
	}

	public boolean ContinueStagesWorkflow() {
		return Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT)
				|| Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
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
		StringBuilder sb = new StringBuilder("Welcome to Planes 2D");
		sb.append("\nPress <SPACE> to start the game");
		sb.append("\nUse <ARROW_UP> and <ARROW_DOWN> to move");
		sb.append("\nUse <SPACE> to shoot");
		return sb.toString();
	}

	public String GameOverMessage(int score, boolean newHighScore) {
		StringBuilder sb = new StringBuilder("Game Over!");
		if (newHighScore)
			sb.append("\nNEW HIGHSCORE!!!");
		sb.append("\nYour score is:\n");
		sb.append(score);
		sb.append("\nPress <SPACE> to retry!");
		return sb.toString();
	}

	private int HIGHSCORE;
	private final String SCORE = "Score", HASH = "Hash";

	public void saveScore(int score) {
		if (preferences == null) {
			System.out.println("Highscore could not be saved!");
		}// Save highscore
		else if (score > HIGHSCORE) {
			HIGHSCORE = score;
			preferences.setItem(SCORE, Integer.toString(HIGHSCORE)); // Score
			preferences.setItem(HASH, Helper.sha256(HIGHSCORE));
		}

	}

	public int getHighScore() {
		if (preferences == null) {
			System.out.println("Highscore could not be loaded!");
			return 0;
		}// Load score and hash
		int score;
		try {
			score = Integer.parseInt(preferences.getItem(SCORE));
		} catch (NumberFormatException nfE) {
			score = 0;
		}
		if (!Helper.sha256(score).equals(preferences.getItem(HASH))) {
			score = 0;
		}
		return HIGHSCORE = score;
	}

	public boolean continueStagesWorkflow() {
		return Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
	}

	public boolean firstTouch() {
		return continueStagesWorkflow();
	}
}