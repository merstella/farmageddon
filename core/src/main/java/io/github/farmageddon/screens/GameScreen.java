package io.github.farmageddon.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.farmageddon.*;

import io.github.farmageddon.markets.Items;
import io.github.farmageddon.markets.Market;
import io.github.farmageddon.ultilites.GameTimeClock;
import io.github.farmageddon.ultilites.Timer_;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.badlogic.gdx.graphics.Color.WHITE;

public class GameScreen implements Screen, InputProcessor {
    private final Main game;

    private OrthographicCamera camera;
    private Viewport gameView;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private GameTimeClock clock;
    private Timer_ timer;
    private boolean showDebugInfo = true;
    private String time;
    private static Label timeLabel, timeStringLabel, daysLeftLabel, daysLeftNum;
    private static Label scoreLabel;
    private static Label scoreStringLabel;
    private static int daysLeft;

    private final Player player;
    private Texture equipmentTexture;
    private InventoryUI inventoryUI;
    public Market market;
    private InventoryScreen inventoryScreen;
    private MarketScreen marketScreen;
    private boolean isMarketVisible = false;
    private boolean isInventoryVisible = false;

    // Items Texture
    public Texture CoinTexture;
    public Texture item1Texture;
    public Texture item2Texture;
    public Texture item3Texture;
    public Texture item4Texture;
    public Items items;

    private Music music;

    // tiled map
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Animal animal;
    private Stage stage;

    private static Vector2 selectedCell;

    int currentDays;
    public GameScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        gameView = new FitViewport(1280, 768, camera);
        camera.setToOrtho(false, gameView.getWorldWidth(), gameView.getWorldHeight());
        camera.zoom = 1f;
        camera.update();
        map = new TmxMapLoader().load("mapok.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        stage = new Stage(gameView);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(WHITE);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        selectedCell = new Vector2();

        timer = new Timer_();
        timer.setTimeRatio(9000);
        timer.StartNew(120, true, true);
        timer.setStartTime(0, 12, 30, 0);
        clock = new GameTimeClock(timer);
        time = timer.getFormattedTimeofDay();
        currentDays = 0;
        daysLeft = 30;

        player = new Player(640, 384, 100f);
        market = new Market(100, 100, 200);
        initMarket();
        animal = new Animal(680, 230, "Chicken", stage);
        animal.setBound(620, 690, 160, 260);

        initDebug();
        music = Main.manager.get("Sound/music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
    }
    public void initMarket() {

        // Test items inventory System
        CoinTexture = new Texture(Gdx.files.internal("Coin_Icon.png"));
        items = new Items("Coin", CoinTexture, 10);
        player.setEquipItem(items);// them vao equip

        item1Texture = new Texture(Gdx.files.internal("SellButton.png"));
        items = new Items("Sell", item1Texture, 10);
        player.setEquipItem(items);

        item2Texture = new Texture(Gdx.files.internal("BuyButton.png"));
        items = new Items("Buy", item2Texture, 10);
        player.setEquipItem(items);// them vao inventory

        item3Texture = new Texture(Gdx.files.internal("moneyBar.png"));
        items = new Items("Money", item3Texture, 10);
        player.setEquipItem(items);

        item4Texture = new Texture(Gdx.files.internal("chickencutted/tile000.png"));
        items = new Items("Well", item4Texture, 10);
        player.setItem(items);


        //money
        player.addMoney(200);
    }
    public void initDebug() {
        timeLabel = new Label(time, new Label.LabelStyle(font, WHITE));
        timeStringLabel = new Label("Time", new Label.LabelStyle(font, WHITE));
        timeStringLabel.setFontScale(2);
        timeLabel.setFontScale(2);
        daysLeftLabel = new Label("Days Left", new Label.LabelStyle(font, WHITE));
        daysLeftNum = new Label(String.format("%d", daysLeft), new Label.LabelStyle(font, WHITE));
        daysLeftLabel.setFontScale(2);
        daysLeftNum.setFontScale(2);
    }
    @Override
    public void show() {
        // inventory & market
        int titleSize = 32;
        inventoryUI = new InventoryUI(titleSize);
        marketScreen = new MarketScreen(titleSize,market, player);
        inventoryScreen = new InventoryScreen(titleSize, player);
    }

    @Override
    public void render(float delta) {
        gameView.apply();
        player.update(delta);
        setting();
        if(timer.getDaysPassed() != currentDays){
            currentDays = timer.getDaysPassed();
            if (currentDays % 2 == 0) animal.incState();
            daysLeft--;
            daysLeftNum.setText(String.format("%d", daysLeft));
        }

        clock.act(delta);
        animal.update(delta, currentDays);
        stage.act();
        stage.draw();
        handleKeyDown(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            animal.feed();
        }
        //printAnimalDebugInfo();
        time = timer.getFormattedTimeofDay();
        timeLabel.setText(time);
        shapeRenderer.setProjectionMatrix(camera.combined);
        renderPlayer();
        renderSelectedCell();
        renderAmbientLighting();

        gameView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        renderDebugInfo();
    }

    private void handleKeyDown(float delta) {
        // bấm m để hiện lên cửa sổ market
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            isMarketVisible = !isMarketVisible;
            System.out.println("marketScreen");

            if (isMarketVisible == true) {
                marketScreen.show();
            } else {
                marketScreen.hide();
            }
            //game.setScreen(marketScreen);
        }

        if (isMarketVisible) {
            marketScreen.render(delta);
        }


        // bấm B để hiện lên cửa sổ inventory
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)){
            isInventoryVisible = !isInventoryVisible;
            System.out.println("inventoryScreen");

            if (isInventoryVisible == true) {
                inventoryScreen.show();
            } else {
                inventoryScreen.hide();
            }
            //game.setScreen(marketScreen);
        }

        if (isInventoryVisible) {
            inventoryScreen.render(delta);
        }

        //inventory Eqip UI
        inventoryUI.drawInventory(player);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            inventoryUI.slotCol = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            inventoryUI.slotCol = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            inventoryUI.slotCol = 2;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            inventoryUI.slotCol = 3;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            inventoryUI.slotCol = 4;
        }
    }

    private void setting() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(player.getPosition().x + 16,player.getPosition().y + 16, 0);
        game.batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);
        mapRenderer.render();
    }
    private void renderSelectedCell() {
        if (player.currentActivity != PlayerAnimation.Activity.NONE) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rect(selectedCell.x * 16, selectedCell.y * 16, 16, 16);
            shapeRenderer.end();
        }
    }
    private void renderAmbientLighting() {

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(clock.getAmbientLighting());
        Matrix4 mat = camera.combined.cpy();
        shapeRenderer.setProjectionMatrix(mat);
        mat.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.rect(camera.position.x - gameView.getWorldWidth() / 2,
            camera.position.y - gameView.getWorldHeight() / 2,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);
    }
    private void renderPlayer() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fillCircle(0, 0, 1);
        Texture blueDotTexture = new Texture(pixmap);
        pixmap.dispose();

        player.render(game.batch);
        game.batch.draw(blueDotTexture, player.getPosition().x, player.getPosition().y, 2, 2); // x, y, width, height
        game.batch.end();
    }

    private void renderDebugInfo() {
        if (!showDebugInfo) return;

        game.batch.begin();
        font.draw(game.batch, String.format("Time: %s", timer.getFormattedTimeofDay()), 10, 470);
        font.draw(game.batch, String.format("Day passed: %s", timer.getDaysPassed()), 10, 410);
        font.draw(game.batch, String.format("CurrentDay : %s", currentDays), 10, 390);
        Color ambient = clock.getAmbientLighting();
        font.draw(game.batch, String.format("Ambient: R%.2f G%.2f B%.2f A%.2f", ambient.r, ambient.g, ambient.b, ambient.a), 10, 430);
        game.batch.end();
    }

    public Timer_ getTimer() {
        return timer;
    }


    public int getCurrentDays() {
        return currentDays;
    }

    @Override
    public void resize(int width, int height) {
        gameView.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        player.dispose();
        map.dispose();
        mapRenderer.dispose();
    }

    public void printAnimalDebugInfo() {
        game.batch.begin();
        String debugInfo = "Animal: " + animal.getType() +
            ",\n Hunger: " + animal.getHunger() +
            "\n, Health: " + animal.getHealth() + "\n, State: " + animal.getState()
            + "\n, can be produce?: " + animal.canBeProduced();

        font.draw(game.batch, debugInfo, player.getPosition().x, player.getPosition().y);
        font.draw(game.batch, player.getPosition().x +  " " + player.getPosition().y, player.getPosition().x + 100, player.getPosition().y + 100);
        game.batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            // Convert screen to world coordinates
            Vector3 screenCoords = new Vector3(screenX, screenY, 0);
            Vector3 worldCoords = camera.unproject(screenCoords);

            // Log the coordinates
            System.out.println("Player: " + player.getPosition());
            System.out.printf("Screen Coordinates: (%d, %d)\n", screenX, screenY);
            System.out.printf("World Coordinates: (%.2f, %.2f)\n", worldCoords.x, worldCoords.y);

            // Use world coordinates for any game logic if necessary
            return true;
        }
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            player.stopActivity();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
