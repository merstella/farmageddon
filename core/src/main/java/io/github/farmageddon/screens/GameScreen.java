package io.github.farmageddon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.*;
import io.github.farmageddon.Crops.Crop;
//import io.github.farmageddon.markets.InventoryScreen;
//import io.github.farmageddon.markets.Market;
import io.github.farmageddon.ultilites.GameTimeClock;
import io.github.farmageddon.ultilites.Timer_;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.graphics.Color.WHITE;

public class GameScreen implements Screen {
    private final Main game;
    private TiledMap map;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Viewport gameView;

    private final Player player;
    private Texture equipmentTexture;
    private InventoryUI inventoryUI;
    //private Market market;
    private InventoryScreen inventoryScreen;
    private MarketScreen marketScreen;
    private boolean isMarketVisible = false;
    private boolean isInventoryVisible = false;

    public Array<Crop> crops;
    public int numCrops;

    private GameTimeClock clock;
    private Timer_ timer;
    private int currentDays;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    // UI elements
    private String time;
    private static Label timeLabel;
    private static Label timeStringLabel;
    private static Label daysLeftLabel;
    private static Label daysLeftNum;
    private static int daysLeft;
    private Music music;

    private float timeScale = 1.0f; // Controls how fast time passes
    private boolean pauseTime = false;
    private Color debugOverlayColor = new Color(0, 0, 0, 0.5f);
    private boolean showDebugInfo = true;

    //market
    public Market market;

    // Items Texture
    public Texture CoinTexture;
    public Texture item1Texture;
    public Texture item2Texture;
    public Texture item3Texture;
    public Texture item4Texture;
    public Items items;


    public GameScreen(Main game) {
        this.game = game;

        player = new Player(100, 100, 200);

        market = new Market(100, 100, 200);


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

        item4Texture = new Texture(Gdx.files.internal("Well.png"));
        items = new Items("Well", item4Texture, 10);
        player.setItem(items);


        //money
        player.addMoney(200);

        font = new BitmapFont();
        font.setColor(WHITE);

        crops = new Array<>();
        numCrops = 0;

        // Timer initialization
        timer = new Timer_();
        timer.StartNew(60, true, true);
        timer.setStartTime(0, 12, 0, 0);

        // Initialize clock for day transitions
        clock = new GameTimeClock(timer);
        currentDays = 0;
        time = timer.getFormattedTimeofDay();
        daysLeft = 30;

        // Initialize labels for time and days left
        timeLabel = new Label(time, new Label.LabelStyle(new BitmapFont(), WHITE));
        timeStringLabel = new Label("Time", new Label.LabelStyle(new BitmapFont(), WHITE));
        timeStringLabel.setFontScale(2);
        timeLabel.setFontScale(2);
        daysLeftLabel = new Label("Days Left", new Label.LabelStyle(new BitmapFont(), WHITE));
        daysLeftNum = new Label(String.format("%d", daysLeft), new Label.LabelStyle(new BitmapFont(), WHITE));
        daysLeftLabel.setFontScale(2);
        daysLeftNum.setFontScale(2);

        shapeRenderer = new ShapeRenderer();

        music = Main.manager.get("Sound/music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
    }


    @Override
    public void show() {
        // Load the map and initialize camera
        //map = new TmxMapLoader().load("Test.tmx");  // Make sure this path is correct
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();
        gameView = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        camera.setToOrtho(false, gameView.getWorldWidth(), gameView.getWorldHeight());
        camera.position.set(player.getX(), player.getY(), 0);


        // inventory & market
        int titleSize = 32;
        inventoryUI = new InventoryUI(titleSize);
        marketScreen = new MarketScreen(titleSize,market, player);
        inventoryScreen = new InventoryScreen(titleSize, player);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);


        // Toggle market visibility with the "M" key
        //if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            System.out.println("marketScreen");
            game.setScreen(marketScreen);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            System.out.println("inventoryScreen");
            game.setScreen(inventoryScreen);  // Switch to market screen
        }

//        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
//            System.out.println("gameScreen");
//            game.setScreen(this);
//        }


        // Update player and camera
        player.update(delta);
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        // Update the clock and day transitions
        clock.act(delta);
        if (timer.getDaysPassed() != currentDays) {
            for (Crop crop : crops) {
                crop.addDay();
            }
            currentDays = timer.getDaysPassed();
            daysLeft--;
            daysLeftNum.setText(String.format("%d", daysLeft));
        }

        time = timer.getFormattedTimeofDay();
        timeLabel.setText(time);

        // Render map
        mapRenderer.setView(camera);
        //mapRenderer.render();

        // Draw player and UI
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        for (int x = 0; x < gameView.getWorldWidth(); x += 32) {
            for (int y = 0; y < gameView.getWorldHeight(); y += 32) {
                if ((x + y) / 32 % 2 == 0) {
                    game.batch.setColor(0.8f, 0.8f, 0.8f, 1);
                } else {
                    game.batch.setColor(0.6f, 0.6f, 0.6f, 1);
                }
                game.batch.draw(game.blank, x, y, 32, 32);
            }
        }

        player.render(game.batch);
        game.batch.setColor(WHITE);
        game.batch.end();

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

        // Ambient lighting effects
        shapeRenderer.setProjectionMatrix(camera.combined);
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
        renderDebugInfo();
        // Update viewport
        gameView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void renderDebugInfo() {
        if (!showDebugInfo) return;

        game.batch.begin();
        font.draw(game.batch, "Controls:", 10, 590);
        font.draw(game.batch, "SPACE: Pause/Resume time", 10, 570);
        font.draw(game.batch, "LEFT/RIGHT: Adjust time speed", 10, 550);
        font.draw(game.batch, "F3: Toggle debug info", 10, 530);

        font.draw(game.batch, String.format("Time Scale: %.2fx", timeScale), 10, 490);
        font.draw(game.batch, String.format("Time: %s", timer.getFormattedTimeofDay()), 10, 470);
        font.draw(game.batch, String.format("Paused: %s", pauseTime), 10, 450);

        Color ambient = clock.getAmbientLighting();
        font.draw(game.batch, String.format("Ambient: R%.2f G%.2f B%.2f A%.2f",
            ambient.r, ambient.g, ambient.b, ambient.a), 10, 430);
        game.batch.end();
    }
    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        mapRenderer.dispose();
        player.dispose();
    }
}
