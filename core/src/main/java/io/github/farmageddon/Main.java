package io.github.farmageddon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.farmageddon.screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 720;
    public SpriteBatch batch;
    public AssetManager manager;
    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("Sound/music.mp3", Music.class);
        manager.finishLoading();
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
    @Override
    public void dispose() {
        manager.dispose();
        batch.dispose();
    }

}
