package io.github.farmageddon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.farmageddon.screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 768;
    public SpriteBatch batch;
    public static AssetManager manager;
    public Texture blank;


    @Override
    public void create() {
        batch = new SpriteBatch();
        blank = new Texture(Gdx.files.internal("farm.png"));

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
