package io.github.farmageddon.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.farmageddon.*;

//import io.github.farmageddon.Crops.Crop;
import io.github.farmageddon.Crops.Crop;
import io.github.farmageddon.Crops.Land;
import io.github.farmageddon.Crops.LandManager;
//import io.github.farmageddon.markets.Market;
import io.github.farmageddon.animals.*;
import io.github.farmageddon.Crops.Seeds;
import io.github.farmageddon.animals.Animal;
import io.github.farmageddon.entities.*;
import io.github.farmageddon.Market;
import io.github.farmageddon.ultilites.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static io.github.farmageddon.ultilites.GameTimeClock.DARKCOLOR;
import static io.github.farmageddon.ultilites.GameTimeClock.LIGHTCOLOR;
import static io.github.farmageddon.Crops.Land.deadPlantTexture;
import static java.lang.Math.clamp;
import static java.lang.ref.Cleaner.create;
//import static io.github.farmageddon.Player.maxEqipInventorySize;
//import static io.github.farmageddon.Player.slotCursor;

public class GameScreen implements Screen, InputProcessor {
    private final Main game;
    public static final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport;
    public static final Player player = new Player(640, 300, 100f); // Your player class
    public static TiledMap map; // Your game map
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Vector3 touchPosition = new Vector3();
    private final BitmapFont font;
    public static ShapeRenderer shapeRenderer;
    public static ShapeRenderer shapeRenderer1;
    private InputMultiplexer inputMultiplexer;
    private final GameTimeClock clock;
    private final Timer_ timer;
    private final boolean showDebugInfo = true;
    private String time;
    private static Label timeLabel, timeStringLabel, daysLeftLabel, daysLeftNum;
    private static Label scoreLabel;
    private static Label scoreStringLabel;
    private static int daysLeft;

    private Texture equipmentTexture;
    private InventoryUI inventoryUI;
    public Market market;
    private InventoryScreen inventoryScreen;
    private MarketScreen marketScreen;
    private boolean isMarketVisible = false;
    private boolean isInventoryVisible = false;
    public static boolean isFishingVisible = false;
    private boolean FishingVisible = false;
    public static boolean cursorRight = false;
    public static boolean cursorLeft = false;
    private UI ui;
    private OptionScreen optionScreen;
    private ItemList itemList;
    public static boolean nightTime;

    public FishingMinigame minigame;
    //    public TorchLightHandler torchLightHandler;
    private RayHandler rayHandler;
    private PointLight torchLight;
    private World world;

    private float playerX;
    private float playerY;
    public static Music music1;
    public static Music music2;
    public static Music CowSound;
    public static Music NoodSound;
    private ArrayList<Animal> animals;
    public final Stage stage;
    private Vector2 selectedCell;
    public static int currentDays;
    public static boolean isPaused;

    Array<Entity> houses;
    House house1;
    private final LandManager landManager;
    public static Array<DroppedItem> droppedItems;
    private float elapsedTime = 0f;

    private Array<Monster> monsters;

    private Array<HouseEntity> entities;
    private Array<Projectile> projectiles;
    private LogicalEntities logic;
    private PrepareMonsters spawner;
    private PathFinder pathFinder;
    public static float stateTime;

    private boolean isPlacing;
    private Array<ProtectPlant> plants;
    private final Vector2 invalidPlacementCell = new Vector2(-1, -1);

    private ZoneManager zoneManager;
    Stage stage1 = new Stage();
//    private HouseEntity houseEntity;

    public GameScreen(Main game) {
        this.game = game;
        itemList = new ItemList();
        stateTime = 0f;
        logic = new LogicalEntities();
        spawner = new PrepareMonsters();
        logic.setSpawner(spawner);

        viewport = new FitViewport(Main.GAME_WIDTH, Main.GAME_HEIGHT, camera);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        camera.zoom = .25f;
        camera.update();
        map = new TmxMapLoader().load("mapok.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        stage = new Stage(viewport);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(WHITE);
        inputMultiplexer = new InputMultiplexer();
        selectedCell = new Vector2();
        timer = new Timer_();
        timer.setTimeRatio(20000);
        timer.StartNew(120, true, true);
        timer.setStartTime(0, 12, 30, 0);
        clock = new GameTimeClock(timer);
        time = timer.getFormattedTimeofDay();
        currentDays = 0;
        daysLeft = 30;

        initHouses();
        landManager = new LandManager(Main.GAME_HEIGHT/16,Main.GAME_WIDTH/16);
        create();

        market = new Market(100, 100, 200);
        initMarket();
        initPlayerInv();
        initAnimal();

        pathFinder = new PathFinder(Main.GAME_WIDTH, Main.GAME_HEIGHT, 16, 16);
        logic.setPathFinder(pathFinder);

        loadCollisionLayer();

        droppedItems = new Array<>();
        initDebug();

        initProtectedPlant();
        slotCursorHandler = new SlotCursorHandler(player, monsters);
        zoneManager = new ZoneManager();
    }

    private void initProtectedPlant() {
        isPlacing = false;
        plants = new Array<>();
    }

    private void loadCollisionLayer() {
        // Example: Assuming you have a list of collision objects
        for (MapObject object : CollisionHandling.collisionLayer) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                pathFinder.addCollisionObject(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }
        }
    }

    public void initMarket() {
        while (player.eqipInventory.size() <= player.maxEqipInventorySize) {
            player.eqipInventory.add(itemList.Default);
        }
        while (player.inventory.size() <= player.maxInventorySize) {
            player.inventory.add(itemList.Default);
        }
    }

    private void initAnimal() {
        animals = new ArrayList<>();
        animals.add(new Chicken(580, 570, stage, this));
        animals.add(new Chicken(605, 580, stage, this));
        animals.add(new Pig(658, 190, stage, this));
        animals.add(new Pig(672, 170, stage, this));
        animals.add(new Cow(820, 538, stage, this));
        animals.add(new Cow(800, 530, stage, this));
        animals.add(new Sheep(430, 480, stage, this));
        animals.add(new Sheep(400, 500, stage, this));
    }

    private void initHouses() {
        houses = new Array<>();
        MapObjects objects = map.getLayers().get("Nhà").getObjects();
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                float x = rect.x;
                float y = rect.y;
                Entity house = new Entity(x, y, 0, true, 100);
                houses.add(house);
            }
        }
    }

    public void initPlayerInv() {
//        // set Item
        player.setEquipItem(itemList.Shovel,0);
        player.setEquipItem(itemList.Hoe, 1);
        player.setEquipItem(itemList.Bucket, 2);
        player.setEquipItem(itemList.Torch, 3);
        player.setEquipItem(itemList.Carrot, 4);
//        player.setItem(itemList.OneGu,0);
        player.setItem(itemList.Tomato,1);
        player.setItem(itemList.Rice,2);
        player.setItem(itemList.Carrot,3);
        player.setItem(itemList.OneGunIcon,4);

        market.addMarketItem(itemList.RadishSeed);
        market.addMarketItem(itemList.RiceSeed);
        market.addMarketItem(itemList.CarrotSeed);
        market.addMarketItem(itemList.TomatoSeed);
        market.addMarketItem(itemList.OneGunIcon);
        market.addMarketItem(itemList.SmallChicken);
        market.addMarketItem(itemList.SmallCow);
        market.addMarketItem(itemList.SmallSheep);
        market.addMarketItem(itemList.SmallPig);
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
        int titleSize = 32;
        inventoryUI = new InventoryUI(titleSize);
        marketScreen = new MarketScreen(titleSize,market, player);
        inventoryScreen = new InventoryScreen(titleSize, player);
        optionScreen = new OptionScreen(player);
        ui = new UI(player);
        minigame = new FishingMinigame(player);
        minigame.create();
        boolean isHoldingTorch = false;
        if (player.eqipInventory.get(player.slotCursor).getItem() == Items.Item.COIN){
            isHoldingTorch = true;
        } else {
            isHoldingTorch = false;
        }

        inputMultiplexer.addProcessor(ui.stage);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        music1 = Gdx.audio.newMusic(Gdx.files.internal("Sound\\music.mp3"));
        music1.setLooping(true);
        music1.setVolume(0.2f);
        music2 = Gdx.audio.newMusic(Gdx.files.internal("Sound\\nhac nen ban dem - Copy.mp3"));
        music2.setLooping(true);
        music2.setVolume(0.1f);
        CowSound = Gdx.audio.newMusic(Gdx.files.internal("Sound\\bo.mp3"));
        CowSound.setLooping(false);
        CowSound.setVolume(0.2f);
        NoodSound = Gdx.audio.newMusic(Gdx.files.internal("Sound\\ga.mp3"));
        NoodSound.setLooping(false);
        NoodSound.setVolume(0.2f);
    }
    boolean isNewDay = false;
    @Override
    public void render(float delta) {
        checkZonePosition(zoneManager,player.getPosition().x,player.getPosition().y);
        if (nightTime) {
            if(!music1.isPlaying()) {
                music2.stop();
                music1.play();
            }
        } else {
            if (!music2.isPlaying()) {
                music1.stop();
                music2.play();
            }
        }

        stateTime += delta;
        if (highlightTimer > 0) {
            highlightTimer -= delta;
        }
        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.update(delta);
//        System.out.println(player.getPosition());
        checkItemPickup(player);
        camera.position.set(
            MathUtils.clamp(player.getPosition().x + 16, Main.GAME_WIDTH * camera.zoom /2, Main.GAME_WIDTH *  (1 - camera.zoom/2)),
            MathUtils.clamp(player.getPosition().y + 16, Main.GAME_HEIGHT * camera.zoom /2, Main.GAME_HEIGHT *  (1 - camera.zoom/2)),
            0
        );
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);
        mapRenderer.render();
        isNewDay = false;
        handleDayPassed();
        clock.act(delta);
        checkBreeding();

        time = timer.getFormattedTimeofDay();
        timeLabel.setText(time);
        shapeRenderer.setProjectionMatrix(camera.combined);

        slotCursorHandler.update();
//        renderGridDebug();
        renderLand(delta);

        renderPlayer();
        stage.act(delta);
        stage.draw();
        for (int i = animals.size() - 1; i >= 0; --i) {
            if (animals.get(i).isDead()) {
                animals.remove(i);
            }
        }

        renderSelectedCell();
        house1.render(game.batch);

        updateProtectPlant(delta);
        renderProtectPlant();
        renderInvalidCell();
        renderDebugInfo();

        CollisionHandling.renderCollision();
        logic.increaseTimerLogic(delta);
        logic.updateEntities(monsters, plants, projectiles, entities, player, delta);
        logic.renderEntities(monsters, plants, projectiles, entities, game.batch);
        game.batch.begin();
//        zoneManager.render(game.batch);
//        System.out.println("Drop size" + droppedItems.size);
        for (DroppedItem item : droppedItems) {
//            System.out.println(item.getItemType());
            item.render(game.batch);
        }
        game.batch.end();

        updateProtectPlant(delta);
        renderProtectPlant();
        CollisionHandling.renderCollision();
        renderAmbientLighting();
        handleKeyDown(delta);
        ui.render();
        isOptionScreen(delta);

//        if (player.getHealth() == 0 || House.getCurrentHealth() == 0){
//            Texture UI_Buttons = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\UI_Buttons.png"));
//            isPaused = true;
//            inputMultiplexer.clear();
//            ui.batch.begin();
//            font.getData().setScale(5f); // Phóng to gấp 2 lần
//            font.draw(ui.batch,"GAME OVER",(Gdx.graphics.getWidth() - 400)/2,(Gdx.graphics.getHeight())/2);
//            font.getData().setScale(3f); // Phóng to gấp 2 lần
//            font.draw(ui.batch,"Day passed: " + currentDays,(Gdx.graphics.getWidth()-220)/2,(Gdx.graphics.getHeight()-200)/2);
//            ui.batch.end();
//            Button menuButton = createButton(new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 29, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19)),
//                new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 28, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19)),"Menu");
//            menuButton.setSize(70,70);
//            menuButton.setPosition((Gdx.graphics.getWidth())/2,(Gdx.graphics.getHeight()-400)/2);
//            stage1.addActor(menuButton);
//            Gdx.input.setInputProcessor(stage1);
//            inputMultiplexer.addProcessor(stage1);
//
//        }
//        stage1.act(delta);
//        stage1.draw();
    }
    private void renderInvalidCell() {
        if (invalidPlacementCell.x != -1 && invalidPlacementCell.y != -1) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 0, 0, 1); // Red color
            shapeRenderer.rect(invalidPlacementCell.x * 16, invalidPlacementCell.y * 16, 16, 16);
            shapeRenderer.end();
        }
    }

    public void checkZonePosition(ZoneManager zoneManager,float playerX, float playerY) {
        if (zoneManager.chickenZone.contains(playerX, playerY)) {
            NoodSound.play();
        } else if (zoneManager.pigZone.contains(playerX, playerY)) {

        } else if (zoneManager.cowZone.contains(playerX, playerY)) {
            CowSound.play();
        } else if (zoneManager.sheepZone.contains(playerX, playerY)) {

        }
    }

    private Button createButton(TextureRegion active, TextureRegion inactive, String action) {
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(inactive); // Trạng thái không được nhấn
        buttonStyle.down = new TextureRegionDrawable(active); // Trạng thái được nhấn
        Button button = new Button(buttonStyle);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (action.equals("Menu")) {
                    game.setScreen(new MainMenuScreen(game));
                }
                UI.buttonMusic.play();
            }
        });
        return button;
    }


    private void isOptionScreen(float delta) {
        if (ui.isOptionVisible ==  true){
            optionScreen.show();
        }
        else { optionScreen.hide(); }
        if (ui.isOptionVisible){
            optionScreen.render(delta);
        }
    }
    private void updateProtectPlant(float delta) {
        for (ProtectPlant plant : plants) {
            plant.update(delta);
        }
    }

    private void renderProtectPlant() {
        for (ProtectPlant plant : plants) {
            plant.render(game.batch);
        }
    }

    private void renderGridDebug() {
        // Set up the ShapeRenderer

        // Draw the grid (you can call this method)
        pathFinder.drawGrid(shapeRenderer);
        for (Monster zombie : monsters)
            pathFinder.drawPath(shapeRenderer, zombie.getPath());
    }

    private void checkBreeding() {
        // Create a copy of the current stage actors to avoid concurrent modification
        List<Animal> stageAnimals = new ArrayList<>();
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Animal) {
                stageAnimals.add((Animal) actor);
            }
        }

        for (int i = 0; i < stageAnimals.size(); i++) {
            Animal animalA = stageAnimals.get(i);
            if (!animalA.getTypeCanBreed()) continue;
            for (int j = i + 1; j < stageAnimals.size(); j++) {
                Animal animalB = stageAnimals.get(j);
                if (!animalB.getTypeCanBreed()) continue;
                float distance = animalA.getPosition().dst(animalB.getPosition());

                if (animalA.isEligibleForBreeding(animalB) && distance < 50) {
                    Animal offspring = animalA.breed(animalB, stage);
                    if (offspring != null) {
//                        System.out.println("Breeding "+ "Offspring Created: " + offspring.getType());
//                        System.out.println("Breeding "+ "Offspring Position: x=" + offspring.getX() + ", y=" + offspring.getY());

                        // Add offspring to both the stage and your list
                        animals.add(offspring);

                        // Update and render the stage immediately
                        stage.act(Gdx.graphics.getDeltaTime());
                        stage.draw();
                    }
                }
            }
        }
    }
    private void renderLand(float delta) {

        landManager.update(delta);
        game.batch.begin();
        for (int i = 0; i < Main.GAME_HEIGHT/16; i++) {
            for (int j = 0; j < Main.GAME_WIDTH/16; j++) {
                Land land = landManager.getLand(i, j);
                Land.LandState currentState = land.getCurrentState();
                Crop crop = land.getCrop();

                if (crop != null && isNewDay) {
                    crop.addDay();
                }

                if (currentState != Land.LandState.PLAIN) {
                    Texture texture = Land.getTextureForState(currentState);
                    game.batch.draw(texture,j*16 , i * 16, 16, 16);
                }

//                if (land.isHasDeadPlant()) {
//                    game.batch.draw(deadPlantTexture, j * 16, i * 16);
//                }
                if (crop != null) {
                    game.batch.draw(crop.getCurrentFrame(), j * 16, i * 16); // Render crop at grid position
                }

            }
        }
        game.batch.end();
    }

    private void renderHouse() {
        for (Entity house : houses) {
            house.render(game.batch);
        }
    }

    private void handleDayPassed() {
        if(timer.getDaysPassed() != currentDays) {
            currentDays = timer.getDaysPassed();
            isNewDay =true;
            if (currentDays % 2 == 0) {
                for (Animal animal : animals) {
                    animal.incrementAge();
                    if (animal.getAge() % 2 == 0 && animal.getHealth() > 80 && animal.getState() < 3) {
                        animal.incState();
                    }
                }
            }
        }
    }


    private void handleKeyDown(float delta) {
        if (!isPaused) {
// bấm m để hiện lên cửa sổ market
            if (!isInventoryVisible) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
//            Gdx.input.setInputProcessor(marketScreen.stage);
                    isMarketVisible = !isMarketVisible;
                    System.out.println("marketScreen");
                    if (isMarketVisible) {
                        inputMultiplexer.clear();
                        inputMultiplexer.addProcessor(marketScreen);
                        inputMultiplexer.addProcessor(marketScreen.stage);
                        marketScreen.show();
                    } else {
                        inputMultiplexer.clear();
                        inputMultiplexer.addProcessor(ui.stage);
                        inputMultiplexer.addProcessor(stage);
                        inputMultiplexer.addProcessor(this);
                        marketScreen.hide();
                    }
                }
            }
            if (isMarketVisible) {
                marketScreen.render(delta);
            }
            //bấm B để hiện lên cửa sổ inventory
            if (!isMarketVisible) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                    isInventoryVisible = !isInventoryVisible;
                    System.out.println("inventoryScreen");
                    if (isInventoryVisible) {
                        inputMultiplexer.clear();
                        inputMultiplexer.addProcessor(inventoryScreen);
                        inputMultiplexer.addProcessor(inventoryScreen.stage);
                        inventoryScreen.show();
                    } else {
                        inputMultiplexer.clear();
                        inputMultiplexer.addProcessor(ui.stage);
                        inputMultiplexer.addProcessor(stage);
                        inputMultiplexer.addProcessor(this);
                        inventoryScreen.hide();
                    }
                }
            }
            if (isInventoryVisible) {
                inventoryScreen.render(delta);
            }
            //inventory Eqip UI
            inventoryUI.drawInventory(player);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                inventoryUI.slotCol = 0;
                player.slotCursor = 0;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                inventoryUI.slotCol = 1;
                player.slotCursor = 1;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                inventoryUI.slotCol = 2;
                player.slotCursor = 2;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                inventoryUI.slotCol = 3;
                player.slotCursor = 3;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
                inventoryUI.slotCol = 4;
                player.slotCursor = 4;
            }

            //    bấm F để bắt đầu câu cá, nếu ItemName trà về tại vị trí player.slotCursor của equipInventory == "Coin"
            if (player.eqipInventory.get(player.slotCursor).getItem() == (Items.Item.FISHINGROD)) {
                if ((player.getPosition().x < 896f && player.getPosition().x > 887f && player.getPosition().y < 500f && player.getPosition().y > 480f) ||
                    (player.getPosition().x < 881f && player.getPosition().x > 872f && player.getPosition().y < 556f && player.getPosition().y > 518f) ||
                    (player.getPosition().x < 898f && player.getPosition().x > 892f && player.getPosition().y < 570f && player.getPosition().y > 562f)) {
                    if (!FishingVisible) {
                        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                            cursorRight = !cursorRight;
                            float time = Gdx.graphics.getDeltaTime();
                            elapsedTime += time;
                        }
                    }
                } else if ((player.getPosition().x < 1028f && player.getPosition().x > 1026f && player.getPosition().y < 587f && player.getPosition().y > 576f) ||
                    (player.getPosition().x < 1045f && player.getPosition().x > 1039f && player.getPosition().y < 572f && player.getPosition().y > 563f) ||
                    (player.getPosition().x < 1064f && player.getPosition().x > 1060f && player.getPosition().y < 549f && player.getPosition().y > 516f) ||
                    (player.getPosition().x < 1041f && player.getPosition().x > 1039f && player.getPosition().y < 514f && player.getPosition().y > 480f) ||
                    (player.getPosition().x < 1049f && player.getPosition().x > 1044f && player.getPosition().y < 498f && player.getPosition().y > 465f)) {
                    if (!FishingVisible) {
                        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                            cursorLeft = !cursorLeft;
                            float time = Gdx.graphics.getDeltaTime();
                            elapsedTime += time;
                        }
                    }
                }
                if (cursorRight) {
                    isFishingVisible = !isFishingVisible;
                    FishingVisible = !FishingVisible;
                    player.currentActivity = Animator.Activity.START_FISHING_RIGHT;
                    player.updateFishingAnimation();
                    cursorRight = false;
                }
                if (cursorLeft) {
                    isFishingVisible = !isFishingVisible;
                    FishingVisible = !FishingVisible;
                    player.currentActivity = Animator.Activity.START_FISHING_LEFT;
                    player.updateFishingAnimation();
                    cursorLeft = false;
                }
                if (FishingVisible) {
                    switch (player.currentActivity) {
                        case START_FISHING_RIGHT:
                            // Kiểm tra nếu hoạt ảnh đã chạy xong
                            if (Player.animation.actionAnimations[player.currentActivity.ordinal()].isAnimationFinished(stateTime)) {
                                player.currentActivity = Animator.Activity.WAIT_FISHING_RIGHT;
                            }
                            break;
                        case START_FISHING_LEFT:
                            // Kiểm tra nếu hoạt ảnh đã chạy xong
                            if (Player.animation.actionAnimations[player.currentActivity.ordinal()].isAnimationFinished(stateTime)) {
                                player.currentActivity = Animator.Activity.WAIT_FISHING_LEFT;
                            }
                            break;

                        case WAIT_FISHING_LEFT:
                            minigame.render();
                            // Kiểm tra nếu minigame đã kết thúc
                            if (FishingMinigame.cursorGameOver) {
                                player.currentActivity = Animator.Activity.DONE_FISHING_LEFT;
                            }
                            break;
                        case WAIT_FISHING_RIGHT:
                            minigame.render();
                            // Kiểm tra nếu minigame đã kết thúc
                            if (FishingMinigame.cursorGameOver) {
                                player.currentActivity = Animator.Activity.DONE_FISHING_RIGHT;
                            }
                            break;
                        case DONE_FISHING_RIGHT:
                            // Chạy hoạt ảnh DONE_FISHING (có thể thêm logic nếu cần)
                            if (Player.animation.actionAnimations[player.currentActivity.ordinal()].isAnimationFinished(stateTime)) {
                                player.currentActivity = Animator.Activity.NONE;
                                FishingVisible = false;
                                Player.hasStartedFishing = false;
                                FishingMinigame.gameOver = false;
                                FishingMinigame.cursorGameOver = false;
                                elapsedTime = 0f;
                                System.out.println(player.currentActivity);
                            }
                            break;
                        case DONE_FISHING_LEFT:
                            // Chạy hoạt ảnh DONE_FISHING (có thể thêm logic nếu cần)
                            if (Player.animation.actionAnimations[player.currentActivity.ordinal()].isAnimationFinished(stateTime)) {
                                System.out.println("Fishing process completed!");
                                player.currentActivity = Animator.Activity.NONE;
                                FishingVisible = false;
                                isFishingVisible = false;
                                Player.hasStartedFishing = false;
                                FishingMinigame.gameOver = false;
                                FishingMinigame.cursorGameOver = false;
                                elapsedTime = 0f;
                            }
                            break;

                        default:
                            break;
                    }
                }
            }
        }
    }
    private float highlightTimer = 0; // Timer for rendering the highlight
    private final float HIGHLIGHT_DURATION = 0.16f; // Duration in seconds
    private void renderSelectedCell() {
        if (highlightTimer > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 0, 0.3f); // Semi-transparent yellow

            shapeRenderer.rect(
                selectedCell.x * 16,
                selectedCell.y * 16,
                16,
                16
            );

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

    }
    private void renderAmbientLighting() {
        if (player.eqipInventory.get(player.slotCursor).getItem() == Items.Item.TORCH) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            float circleCenterX = playerX + 16; // Tọa độ X trung tâm của player
            float circleCenterY = playerY + 16; // Tọa độ Y trung tâm của player
            float circleRadius = 50;            // Bán kính hình tròn

            // Bật stencil buffer
            Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
            Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);
            Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF);
            Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_REPLACE);
            // Vẽ hình tròn vào stencil buffer
            Gdx.gl.glColorMask(false, false, false, false); // Không vẽ vào màn hình
            Gdx.gl.glDepthMask(false);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 1, 1); // Màu bất kỳ (không quan trọng vì không vẽ lên màn hình)
            shapeRenderer.circle(circleCenterX, circleCenterY, circleRadius);
            shapeRenderer.end();

            Gdx.gl.glColorMask(true, true, true, true); // Kích hoạt lại màu sắc
            Gdx.gl.glDepthMask(true);
            Gdx.gl.glStencilFunc(GL20.GL_NOTEQUAL, 1, 0xFF); // Chỉ vẽ ngoài hình tròn

            // Vẽ ánh sáng toàn màn hình
            Gdx.gl20.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (clock.worldTime.getElapsedInHours() >= (clock.NIGHT_TIME - 0.8) || clock.worldTime.getElapsedInHours() <= (clock.DAY_TIME - 1.0) ) {
                // Vẽ hình tròn lớn màu xám
                nightTime = false;
                shapeRenderer.setColor(0f, 0f, 0f, 0f); // Màu xám
                shapeRenderer.circle(circleCenterX, circleCenterY, 55);
                if (clock.worldTime.getElapsedInHours() >= (clock.NIGHT_TIME - 0.7) || clock.worldTime.getElapsedInHours() <= (clock.DAY_TIME - 1.2) ) {
                    shapeRenderer.setColor(0f, 0f, 0f, 0.1f);
                    shapeRenderer.circle(circleCenterX, circleCenterY, 60);
                    if (clock.worldTime.getElapsedInHours() >= (clock.NIGHT_TIME - 0.6) || clock.worldTime.getElapsedInHours() <= (clock.DAY_TIME - 1.3)) {
                        shapeRenderer.setColor(0f, 0f, 0f, 0.2f);
                        shapeRenderer.circle(circleCenterX, circleCenterY, 65);
                        if (clock.worldTime.getElapsedInHours() >= (clock.NIGHT_TIME - 0.5) || clock.worldTime.getElapsedInHours() <= (clock.DAY_TIME - 1.4)) {
                            shapeRenderer.setColor(0f, 0f, 0f, 0.3f);
                            shapeRenderer.circle(circleCenterX, circleCenterY, 70);
                            if (clock.worldTime.getElapsedInHours() >= (clock.NIGHT_TIME - 0.4) || clock.worldTime.getElapsedInHours() <= (clock.DAY_TIME - 1.5)) {
                                shapeRenderer.setColor(0f, 0f, 0f, 0.4f);
                                shapeRenderer.circle(circleCenterX, circleCenterY, 75);
                                if (clock.worldTime.getElapsedInHours() >= (clock.NIGHT_TIME - 0.3) || clock.worldTime.getElapsedInHours() <= (clock.DAY_TIME - 1.6)) {
                                    shapeRenderer.setColor(0f, 0f, 0f, 0.5f);
                                    shapeRenderer.circle(circleCenterX, circleCenterY, 80);
                                    System.out.println("torch");
                                    nightTime = true;
                                }
                            }
                        }
                    }
                }
            }
            shapeRenderer.setColor(clock.getAmbientLighting()); // Màu ánh sáng nền
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl20.glDisable(GL20.GL_BLEND);
            Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);
        }
        else {
            Gdx.gl20.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(clock.getAmbientLighting());
            Matrix4 mat = camera.combined.cpy();
            shapeRenderer.setProjectionMatrix(mat);
            mat.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.rect(camera.position.x - viewport.getWorldWidth() / 2,
            camera.position.y - viewport.getWorldHeight() / 2,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl20.glDisable(GL20.GL_BLEND);
        }
    }
    private void renderPlayer() {
        player.render(game.batch);
    }

    private void renderDebugInfo() {
        if (!showDebugInfo) return;
        game.batch.begin();
        font.draw(game.batch, String.format("Time: %s", timer.getFormattedTimeofDay()), player.getPosition().x, player.getPosition().y);
        font.draw(game.batch, String.format("Day passed: %s", timer.getDaysPassed()), player.getBounds().x, player.getPosition().y - 30);
        font.draw(game.batch, String.format("Day: %s", currentDays),43,Gdx.graphics.getHeight() - 98);
        Color ambient = clock.getAmbientLighting();
//        font.draw(game.batch, String.format("Ambient: R%.2f G%.2f B%.2f A%.2f", ambient.r, ambient.g, ambient.b, ambient.a), 10, 430);
        game.batch.end();
    }

    public Timer_ getTimer() {
        return timer;
    }


    public OrthographicCamera getCamera() {
        return camera;
    }

    private void checkItemPickup(Player player) {
        for (int i = 0; i < droppedItems.size; i++) {
            DroppedItem item = droppedItems.get(i);
            if (player.getBounds().overlaps(item.getBounds())) { // Check collision
                System.out.println("Picked up: " + item.getItemType());
                player.setItem(new Items(item.getTexture(), item.getType(), item.getItemType(), 10, 1),1); // Add to player's inventory
                droppedItems.removeIndex(i); // Remove the item from the list
//                item.dispose(); // Dispose the item's texture
                break;
            }

        }
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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

    private SlotCursorHandler slotCursorHandler;
    public SlotCursorHandler getSlotCursorHandler() {
        return slotCursorHandler;
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    private void disposeArbitraryPlant() {
//        if (plants.size > 0) {
//            // You can dispose of the first plant or any other plant based on your logic
//            Plant plantToDispose = plants.get(0); // Example: Get the first plant
//
//            // Mark the plant for removal
//            plantToDispose.markForRemoval(); // Mark it as disposed
//
//            // Optionally, remove it from the list of plants if it is no longer needed
//            plants.removeValue(plantToDispose, true);
//
//            // Now, check if any zombie has this plant as a target and set it to null
//            for (Monster zombie : zombies) {
//                if (zombie.getTargetPlant() == plantToDispose) {
//                    zombie.setTargetPlant(null); // Set the zombie's target to null
//                }
//            }
//
//            System.out.println("Disposed of plant: " + plantToDispose);
//        }
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
            // Convert screen coordinates to world coordinates
            touchPosition.set(screenX, screenY, 0);
            camera.unproject(touchPosition);
            int cellX = (int) touchPosition.x / 16;
            int cellY = (int) touchPosition.y / 16;
            selectedCell.set(cellX, cellY);
            highlightTimer = HIGHLIGHT_DURATION;

            Vector2 touchPosition2D = new Vector2(touchPosition.x, touchPosition.y);
            System.out.println(touchPosition2D);
            System.out.println(cellX + ";" + cellY);
            Land land = landManager.getLand(cellY, cellX);
//            System.out.prinln(land.isHasDeadPlant());
            // Handle farming/planting actions if valid farming position
            if (zoneManager.isValidFarmingPosition(touchPosition2D)) {
                System.out.println("CHECK");
                if (!land.isEmpty()) {
//                    if (land.setHasDeadPlant(false);
                    DroppedItem droppedItem = land.harvestCrop();
                    if (droppedItem != null) {
                        droppedItems.add(droppedItem);
                    }
                }

                // Plant a seed if the player has one selected
                if (slotCursorHandler.getSeed() != null) {
                    if (land.isEmpty()) {
                        System.out.println("flag2");
                        System.out.println(slotCursorHandler.getSeed());
                        Crop crop = new Crop(slotCursorHandler.getSeed(), cellX * 16, cellY * 16); // Example crop
                        land.plantCrop(crop);
                    }
                }
            }
            if (slotCursorHandler.getAction() != null) {
                // Check if the action is a tool like "hoe" or "bucket"
                if (Objects.equals(slotCursorHandler.getAction(), "hoe") || Objects.equals(slotCursorHandler.getAction(), "bucket")) {
                    if (!zoneManager.isValidFarmingPosition(touchPosition2D)) {
                        return true; // If not a valid farming position, do nothing
                    }
                }
                player.updateActivityAnimation(slotCursorHandler.getAction(), touchPosition2D);
                slotCursorHandler.startAction(land);

            }
            if (slotCursorHandler.getMutatedPlant() != null) {
                // Handle mutated plant placement
                isPlacing = true;
                GridNode gridNode = pathFinder.getGridNodeForEntity(touchPosition2D);
                if (gridNode != null && gridNode.getGridType() != GridNode.GridType.UNPASSABLE) {
                    ProtectPlant newPlant = new ProtectPlant(1,cellX * 16, cellY * 16, 1000);
                    plants.add(newPlant);
                } else {
                    invalidPlacementCell.set(cellX, cellY);
                }
            }
            if (slotCursorHandler.getAnimal() != null) {
                Animal newAnimal = slotCursorHandler.getAnimal(cellX * 16, cellY * 16, stage, this);
                animals.add(newAnimal);
            }

            return true;
        }
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            player.stopActivity();
            if (slotCursorHandler.getMutatedPlant() == null) return true;
            if (isPlacing) {
                if (!plants.isEmpty()) {
                    ProtectPlant plantToConfirm = plants.peek();
                    if (invalidPlacementCell.x == -1 && invalidPlacementCell.y == -1) {
                        plantToConfirm.setOpacity(1f);
                        plantToConfirm.setPlanted(true);
                    } else {
                        plants.pop();
                    }
                }
                isPlacing = false;
                invalidPlacementCell.set(-1, -1);
            }
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
        if (slotCursorHandler.getMutatedPlant() == null) return false;
        if (isPlacing && !plants.isEmpty()) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            int cellX = (int) touchPosition.x / 16;
            int cellY = (int) touchPosition.y / 16;

            ProtectPlant lastPlant = plants.peek();
            lastPlant.setPosition(new Vector2(cellX * 16, cellY * 16));
            lastPlant.incrementOpacity(Gdx.graphics.getDeltaTime() * 0.5f);

            // Check if the new position is valid
            Vector2 touchPosition2D = new Vector2(touchPosition.x, touchPosition.y);
            GridNode gridNode = pathFinder.getGridNodeForEntity(touchPosition2D);

            if (gridNode != null && gridNode.getGridType() == GridNode.GridType.UNPASSABLE) {
                invalidPlacementCell.set(cellX, cellY); // Mark invalid cell for red border
            } else {
                invalidPlacementCell.set(-1, -1); // Reset invalid cell
            }
        }
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

    private void create () {
        monsters = new Array<>();
        plants = new Array<>();
        projectiles = new Array<>();
        entities = new Array<>();


        house1 = new House(598, 446);
        HouseEntity houseentity1 = new HouseEntity(567, 405, 0, house1);
        HouseEntity houseentity2 = new HouseEntity(634, 393, 0, house1);
        HouseEntity houseentity3 = new HouseEntity(597, 474, 0, house1);
        HouseEntity houseentity4 = new HouseEntity(644, 456, 0, house1);
        HouseEntity houseentity5 = new HouseEntity(564, 443, 0, house1);
        HouseEntity houseentity6 = new HouseEntity(576, 458, 0, house1);
        HouseEntity houseentity7 = new HouseEntity(576, 458, 0, house1);
        HouseEntity houseentity8 = new HouseEntity(621, 469, 0, house1);
        house1.addEntity(houseentity1);
        house1.addEntity(houseentity2);
        house1.addEntity(houseentity3);
        house1.addEntity(houseentity4);
        house1.addEntity(houseentity5);
        house1.addEntity(houseentity6);
        house1.addEntity(houseentity7);
        house1.addEntity(houseentity8);

        for (HouseEntity entity : house1.getEntitiesOnBorder()) {
            entities.add(entity);
        }
    }

}
