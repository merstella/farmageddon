package io.github.farmageddon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import io.github.farmageddon.Main;
import io.github.farmageddon.Player;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    private final Main game;
    private final Player player;
//    private Tile[][] tiles; // Assuming you have a 2D array of tiles
//    private Texture defaultTexture; // Texture for unvisited tiles

    public GameScreen(Main game) {
        this.game = game;
        player = new Player();
//        defaultTexture = new Texture(Gdx.files.internal("farm.png"));
//        initializeTiles(); // Initialize your tile
//        renderBackground(); // Render background elements only once
    }

//    private void initializeTiles() {
//        // Initialize your tiles here (size and contents)
//        tiles = new Tile[10][10]; // Example size, adjust as needed
//        for (int x = 0; x < tiles.length; x++) {
//            for (int y = 0; y < tiles[x].length; y++) {
//                tiles[x][y] = new Tile(defaultTexture, x, y, null); // Initialize each tile
//            }
//        }
//    }
//
//    private void renderBackground() {
//        game.getBatch().begin();
//        for (int x = 0; x < tiles.length; x++) {
//            for (int y = 0; y < tiles[x].length; y++) {
//                game.getBatch().draw(defaultTexture, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
//            }
//        }
//        game.getBatch().end();
//    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
//        defaultTexture.dispose();
    }
}

