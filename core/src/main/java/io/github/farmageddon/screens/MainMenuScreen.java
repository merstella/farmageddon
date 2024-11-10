package io.github.farmageddon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.farmageddon.Main;

public class MainMenuScreen implements Screen {
    // constants for button dimensions
    private static final int PLAY_BUTTON_WIDTH = 200;
    private static final int PLAY_BUTTON_HEIGHT = 100;
    private static final int EXIT_BUTTON_WIDTH = 200;
    private static final int EXIT_BUTTON_HEIGHT = 100;

    final private Main game;
    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;

    private Viewport viewport;
    private Camera camera;

    public MainMenuScreen(Main game) {
        camera = new PerspectiveCamera();
        viewport = new FitViewport(800, 480, camera); // FitViewport for screen size adjustments
        this.game = game;
        playButtonActive = new Texture(Gdx.files.internal("playButtonActive.png"));
        playButtonInactive = new Texture(Gdx.files.internal("playButtonInactive.png"));
        exitButtonActive = new Texture(Gdx.files.internal("exitButtonActive.png"));
        exitButtonInactive = new Texture(Gdx.files.internal("exitButtonInactive.png"));
    }

    @Override
    public void show() {
        // No initialization required here
    }

    @Override
    public void render(float delta) {
        // Clear the screen with black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Get the screen width and height dynamically
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // Calculate button positions to center them on the screen
        int playButtonX = (screenWidth - PLAY_BUTTON_WIDTH) / 2;
        int playButtonY = screenHeight / 2 + PLAY_BUTTON_HEIGHT; // position the play button above the center
        int exitButtonX = (screenWidth - EXIT_BUTTON_WIDTH) / 2;
        int exitButtonY = screenHeight / 2 - EXIT_BUTTON_HEIGHT; // position the exit button below the center

        // Begin rendering the buttons
        game.batch.begin();

        // Draw the play button (check if it is hovered and clicked)
        if (Gdx.input.getX() < playButtonX + PLAY_BUTTON_WIDTH && Gdx.input.getX() > playButtonX
            && screenHeight - Gdx.input.getY() < playButtonY + PLAY_BUTTON_HEIGHT
            && screenHeight - Gdx.input.getY() > playButtonY) {
            game.batch.draw(playButtonActive, playButtonX, playButtonY, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                this.dispose();
                game.setScreen(new GameScreen(game)); // Switch to the game screen
            }
        } else {
            game.batch.draw(playButtonInactive, playButtonX, playButtonY, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        // Draw the exit button (check if it is hovered and clicked)
        if (Gdx.input.getX() < exitButtonX + EXIT_BUTTON_WIDTH && Gdx.input.getX() > exitButtonX
            && screenHeight - Gdx.input.getY() < exitButtonY + EXIT_BUTTON_HEIGHT
            && screenHeight - Gdx.input.getY() > exitButtonY) {
            game.batch.draw(exitButtonActive, exitButtonX, exitButtonY, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                Gdx.app.exit(); // Exit the application
            }
        } else {
            game.batch.draw(exitButtonInactive, exitButtonX, exitButtonY, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height); // Update the viewport on resizing
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        playButtonActive.dispose();
        playButtonInactive.dispose();
        exitButtonActive.dispose();
        exitButtonInactive.dispose();
    }
}
