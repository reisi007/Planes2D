package com.reisisoft.planes2D;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter implements INative {
	private static final boolean DEBUG = false;
	public static void main(String[] args) {
		DesktopStarter ds = new DesktopStarter();
		ds.Start();
	}
	private LwjglApplication application;
	private LwjglApplicationConfiguration cfg;
	private boolean goneFullscreen = false;
	// Loading and saving score
	private int HIGHSCORE;

	private MovementContainer Input;

	private int maxH;

	private String url = ClassLoader.getSystemClassLoader().getResource(".").getPath() + "/planes2d.score";

	public DesktopStarter() {
		cfg = new LwjglApplicationConfiguration();
		cfg.title = "Plane 2D";
		if (DEBUG) {
			cfg.width = 1280;
			cfg.height = 720;
			cfg.fullscreen = false;
		} else {
			cfg.width = Toolkit.getDefaultToolkit().getScreenSize().width;
			cfg.height = Toolkit.getDefaultToolkit().getScreenSize().height;
			goneFullscreen = cfg.fullscreen = true;
		}
		maxH = cfg.height;
		cfg.resizable = false;
		cfg.initialBackgroundColor = Helper.CornFlowerBlue;
		cfg.foregroundFPS = 60;
		cfg.backgroundFPS = 0;
		System.out.println("Highscore saved @" + url + "\nWidth: " + cfg.width + "\tHeight: " + cfg.height + "\n");
		Input = new MovementContainer(cfg.height / 2f, false);
	}

	@Override
	public boolean continueStagesWorkflow() {
		return Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
	}

	@Override
	public boolean firstTouch() {
		return continueStagesWorkflow();
	}

	@Override
	public String GameOverMessage(int score, boolean newHighScore) {
		StringBuilder sb = new StringBuilder("Game Over!");
		if (newHighScore)
			sb.append("\nNEW HIGHSCORE!!!");
		sb.append("\nYour score is:\n");
		sb.append(score);
		sb.append("\nPress <SPACE> to retry!");
		return sb.toString();
	}

	@Override
	public int getHighScore() {
		ArrayList<String> tmp = new ArrayList<>();
		try {
			FileInputStream fileInputStream = new FileInputStream(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(fileInputStream));
			try {
				String s;
				while ((s = in.readLine()) != null)
					tmp.add(s);
			} catch (IOException ioE) {
				System.out.println("Lines: " + tmp.size() + "\nIOException:" + ioE.getMessage());
				if (tmp.size() != 2)
					tmp.clear();
			}
			// In the first line, there should be the score, in the second the SHA-256 hash
			// If the Score is valid Hash(0) == 1
			if (tmp.size() != 2)
				HIGHSCORE = 0;
			else if (Helper.hash(tmp.get(0)).equals(tmp.get(1)))
				try {
					HIGHSCORE = Integer.parseInt(tmp.get(0));
				} catch (NumberFormatException nfE) {
					HIGHSCORE = 0;
				}
			else
				HIGHSCORE = 0;

		} catch (FileNotFoundException fnfE) {
			tryCreateFile(fnfE);
		}
		if (tmp.isEmpty())
			HIGHSCORE = 0;
		return HIGHSCORE > 0 ? HIGHSCORE : 0;
	}

	@Override
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

	@Override
	public void letQuit() {
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE))
			application.exit();
	}

	@Override
	public Planes2D.Resolutions prefferredResolution(Planes2D game) {
		if (game.getCurH() > 500f)
			return Planes2D.Resolutions.HiRes;
		return Planes2D.Resolutions.LowRes;
	}

	@Override
	public void saveScore(int score) {
		HIGHSCORE = score < 0 ? 0 : score;
		PrintWriter file = null;
		try {
			file = new PrintWriter(url);
			file.println(score);
			file.println(Helper.hash(score));
		} catch (FileNotFoundException fnfE) {
			tryCreateFile(fnfE);
		} finally {
			if (file != null)
				file.close();
		}
	}

	@Override
	public void Setup() {
		if (goneFullscreen)
			Gdx.input.setCursorCatched(true);
	}
	@Override
	public float speedX() {
		return 1;
	}

	@Override
	public float speedY() {
		return 1;
	}

	public void Start() {
		application = new LwjglApplication(new Planes2D(this), cfg);
	}

	private void tryCreateFile(FileNotFoundException fnfE) {
		File file = new File(url);
		try {
			file.createNewFile();
		} catch (IOException ioE) {
			// Can't save score this time
			System.out.println("Score can not be saved.\nFileNotFound:\t" + fnfE.getMessage() + "\nIO:\t" + ioE.getMessage());
		}
	}

	@Override
	public String WelcomeMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Welcome to Planes 2D\n");
		sb.append("Press <SPACE> to start the game\n");
		sb.append("Use <ARROW_UP> and <ARROW_DOWN> to move\n");
		sb.append("Use <SPACE> to shoot");
		return sb.toString();
	}
}
