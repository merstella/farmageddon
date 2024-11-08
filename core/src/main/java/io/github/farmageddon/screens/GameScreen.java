package io.github.farmageddon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.farmageddon.Main;
import io.github.farmageddon.Market;
import io.github.farmageddon.Player;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    private final Main game;
    private TiledMap map;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;

    private final Player player;
    private Market market;
    private boolean isMarketVisible = false;
    public GameScreen(Main game) {
        this.game = game;
        player = new Player(100, 100, 200);
        market = new Market(game);
    }

    @Override
    public void show() {
        //map = new TmxMapLoader().load("map.tmx");
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            // Toggle market visibility
            isMarketVisible = !isMarketVisible;

            if (isMarketVisible) {
                game.setScreen(market);  // Switch to the market screen
            } else {
                game.setScreen(this);    // Switch back to the current game screen (if you have one)
            }
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        player.update(delta);
        game.batch.begin();
        player.render(game.batch);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        player.dispose();

    }
}

