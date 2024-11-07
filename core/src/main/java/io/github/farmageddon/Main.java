package io.github.farmageddon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 720;
    public SpriteBatch batch;
    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }

}
