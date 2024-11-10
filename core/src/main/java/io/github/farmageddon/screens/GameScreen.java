package io.github.farmageddon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.farmageddon.Crops.Crop;
import io.github.farmageddon.Crops.Items;
import io.github.farmageddon.Main;
import io.github.farmageddon.markets.Market;
import io.github.farmageddon.Player;

public class GameScreen implements Screen {
    private final Main game;
    private TiledMap map;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;

    private final Player player;
    private Market market;
    private boolean isMarketVisible = false;

    private Crop testCrop;

    public GameScreen(Main game) {
        this.game = game;
        player = new Player(100, 100, 200);
        market = new Market(game);
        testCrop = new Crop(Items.Item.RICE, 100, 100);
    }

    @Override
    public void show() {
        // Load the map and initialize the camera
        //map = new TmxMapLoader().load("map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600); // Set the camera size (you can adjust these values)
        camera.position.set(player.getX(), player.getY(), 0);
    }

    @Override
    public void render(float delta) {
        // Toggle market visibility with the "M" key
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            isMarketVisible = !isMarketVisible;
            if (isMarketVisible) {
                game.setScreen(market);  // Switch to the market screen
            } else {
                game.setScreen(this);    // Switch back to the game screen
            }
        }

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Update the player and camera
        player.update(delta);
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        // Render the map and player
        //mapRenderer.setView(camera);
        //mapRenderer.render();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch);
        game.batch.draw(testCrop.getCurrentFrame(), testCrop.getFrameSprite().getX(), testCrop.getFrameSprite().getY());
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Update the camera viewport on resize
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        //map.dispose();
        mapRenderer.dispose();
        player.dispose();
    }
}
