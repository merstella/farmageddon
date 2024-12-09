package io.github.farmageddon.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.farmageddon.*;

//import io.github.farmageddon.Crops.Crop;
import io.github.farmageddon.Crops.Crop;
import io.github.farmageddon.Crops.Land;
import io.github.farmageddon.Crops.LandManager;
//import io.github.farmageddon.markets.Market;
import io.github.farmageddon.entities.*;
import io.github.farmageddon.Market;
import io.github.farmageddon.ultilites.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static com.badlogic.gdx.graphics.Color.WHITE;
import static java.lang.Math.clamp;
//import static io.github.farmageddon.Player.maxEqipInventorySize;
//import static io.github.farmageddon.Player.slotCursor;


public class GameScreen implements Screen, InputProcessor{

    private final Main game;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Player player; // Your player class
    public static TiledMap map; // Your game map
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Vector3 touchPosition = new Vector3();
    private final BitmapFont font;
    public static ShapeRenderer shapeRenderer;

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
    public Animator.Activity currentActivity;
    private Animator animation;


    // Items Texture
    public Texture CoinTexture;
    public Texture item1Texture;
    public Texture item2Texture;
    public Texture item3Texture;
    public Texture item4Texture;
    public Texture FishTexture;
    public Texture defaultTexture;
    public Items items;
    public static Items Fish;
    public static Items Default;

    //fishing minigame
    public boolean isHoldingTorch;
    public FishingMinigame minigame;
    //    public TorchLightHandler torchLightHandler;
    private RayHandler rayHandler;
    private PointLight torchLight;
    private World world;

    // torch
//    private TorchLightHandler torch;
//    private boolean isTorchHeld = true;
    private float playerX;
    private float playerY;


//    private final Music music;

    private ArrayList<Animal> animals;
    private final Stage stage;

    private final Vector2 selectedCell;
    int currentDays;

    //demo crops
//    Crop riceCrop;
//    Crop tomatoCrop;

    // demo house
    Array<Entity> houses;

    // demo land
    private final LandManager landManager;
//    private Land singleLand;

    public static Array<DroppedItem> droppedItems;

    private float elapsedTime = 0f;
//    private Queue<Pair<Animal, Animal>> breedingQueue = new LinkedList<>();

    private Array<Monster> monsters;
    private Array<ProtectPlant> plants;
    private Array<Entity> entities;
    private Array<Projectile> projectiles;
    private LogicalEntities logic;
//    private Array<Monster> zombies;
//    private static Array<Plant> plants;
    private PathFinder pathFinder;

    public GameScreen(Main game) {

        this.game = game;
        logic = new LogicalEntities();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Main.GAME_WIDTH, Main.GAME_HEIGHT, camera);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        camera.zoom = 0.25f;
        camera.update();
        map = new TmxMapLoader().load("mapok.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        stage = new Stage(viewport);
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font.setColor(WHITE);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        selectedCell = new Vector2();

        timer = new Timer_();
        timer.setTimeRatio(20000);
        timer.StartNew(120, true, true);
        timer.setStartTime(0, 12, 30, 0);
        clock = new GameTimeClock(timer);
        time = timer.getFormattedTimeofDay();
        currentDays = 0;
        daysLeft = 30;
//        initHouses();
        landManager = new LandManager(Main.GAME_HEIGHT/16,Main.GAME_WIDTH/16);
        player = new Player(640, 300, 100f);

        create();
//        monsters.add(new Monster(0, 0, 30, 600, 400, 1000));
//        monsters.add(new Monster(0, 70, 40, 600, 400, 2000));
        plants.add(new ProtectPlant(400, 400, 1000));

//        market = new Market(100, 100, 200);
//        initPlayerInv();
        initMarket();
        initPlayerInv();

//        market = new Market(100, 100, 200);
        initAnimal();
//        zombies = new Array<Monster>();
//        plants = new Array<Plant>();
        pathFinder = new PathFinder(Main.GAME_WIDTH, Main.GAME_HEIGHT, 16, 16);
        logic.setPathFinder(pathFinder);
        // Initialize zombies and plants
//        zombies.add(new Monster(322, 143, 30,100));
//        zombies.add(new Monster(120, 455, 30,100));
//        zombies.add(new Monster(178, 456, 30,100));
//
//
//        plants.add(new Plant(600, 300, 100));
//        plants.add(new Plant(300, 400, 100));
//        plants.add(new Plant(100, 100, 100));
//        plants.add(new Plant(700, 100, 100));

        loadCollisionLayer();

//        specialPlant = new Plants();
        droppedItems = new Array<>();
        initDebug();
//        music = Main.manager.get("Sound/music.mp3", Music.class);
//        music.setLooping(true);
//        music.setVolume(0.2f);
//        music.play();
//        music = Main.manager.get("Sound/music.mp3", Music.class);
//        music.setLooping(true);
//        music.setVolume(0.2f);
//        music.play();
    }


    private void loadCollisionLayer() {
        // Example: Assuming you have a list of collision objects
        for (MapObject object : CollisionHandling.collisionLayer) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                pathFinder.addCollisionObject(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }
        }
//        music = Main.manager.get("Sound/music.mp3", Music.class);
//        music.setLooping(true);
//        music.setVolume(0.2f);
//        music.play();

        // torch
//        torch = new TorchLightHandler(world);
    }

    public void initMarket() {
        Texture defaultTexture = new Texture(Gdx.files.internal("default.png"));
        Default = new Items(defaultTexture,Items.ItemType.DEFAULT,Items.Item.DEFAULT, 0, 0);
        while (player.eqipInventory.size() <= player.maxEqipInventorySize) {
            player.eqipInventory.add(Default);
        }
    }


    private void initAnimal() {
//        animal = new Animal(680, 230, "Chicken", stage);
//        animal.setBound(620, 690, 160, 260);
        animals = new ArrayList<>();
        Animal chicken1 = new Animal(650, 180, "Chicken", stage);
        chicken1.setBound(620, 690, 160, 260);

        Animal chicken2 = new Animal(670, 200, "Chicken", stage);
        chicken2.setBound(620, 690, 160, 260);
        animals.add(chicken1);
        animals.add(chicken2);

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
            defaultTexture = new Texture(Gdx.files.internal("default.png"));
            Default = new Items(defaultTexture, Items.ItemType.NULL , Items.Item.NULL ,0,0);
        while (player.eqipInventory.size() <= player.maxEqipInventorySize){
            player.eqipInventory.add(Default);
        }

        FishTexture = new Texture(Gdx.files.internal("Animals\\Bee\\Bee_Hive.png"));
        Fish = new Items(FishTexture, Items.ItemType.FOOD, Items.Item.FISH,10,1);
        player.setItem(Fish);

        CoinTexture = new Texture(Gdx.files.internal("Coin_Icon.png"));
        items = new Items(CoinTexture, Items.ItemType.OTHER, Items.Item.COIN, 10,1);
        player.setEquipItem(items,0);


        item4Texture = new Texture(Gdx.files.internal("Well.png"));
        items = new Items(item4Texture, Items.ItemType.OTHER, Items.Item.CARROT,10, 0);
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
        // fishing minigame
        minigame = new FishingMinigame();
        minigame.create();


        boolean isHoldingTorch = false;
        if (player.eqipInventory.get(player.slotCursor).getItem() == Items.Item.COIN){
            isHoldingTorch = true;
        } else {
            isHoldingTorch = false;
        }
//        updateTorchLight(isHoldingTorch, player.getX(), player.getY());
//        if (player.eqipInventory.get(player.slotCursor).getItemName() == "Coin"){
//            isHoldingTorch = true;
//        } else {
//            isHoldingTorch = false;
//        }
//        torchLightHandler.updateTorchLight(isHoldingTorch, player.getX(), player.getY());
    }

    boolean isNewDay;
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        player.update(delta);
        System.out.println(player.getPosition());

//        updateZombies(delta);
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
        stage.act(delta);
        stage.draw();

        playerX = player.getPosition().x;
        playerY = player.getPosition().y;

//        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
//            animal.feed();
//        }
        //printAnimalDebugInfo();

        time = timer.getFormattedTimeofDay();
        timeLabel.setText(time);
        shapeRenderer.setProjectionMatrix(camera.combined);
//        renderHouse();
        renderLand(delta);



        renderPlayer();
//        for (Plant plant : plants) {
//            plant.render(game.batch);
//        }
//        for (Monster monster : zombies) {
//            monster.render(game.batch);
//
//        }
        renderGridDebug();
        if (currentAct != "attack") renderSelectedCell();
        handleCrop();
        renderCrop();
        //update all the thing before render minimap

        checkItemPickup(player);
        renderAmbientLighting();
        renderDebugInfo();
//        drawDebug();
//        printAnimalDebugInfo();
        CollisionHandling.renderCollision();
        isNewDay = false;

        logic.updateEntities(monsters, plants, projectiles, entities, player, delta);
        logic.renderEntities(monsters, plants, projectiles, game.batch);

//        handleKeyDown(delta);
        handleKeyDown(delta);

    }

//    private void updateZombies(float delta) {
//
//        // Assign paths to all zombies
//        logic.assignPathsToZombies(zombies, plants, pathFinder);
//
//        // Update each zombie's position
//        for (Monster zombie : zombies) {
//            zombie.update(delta); // Move the zombie along the path
//        }
//
//    }






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

            for (int j = i + 1; j < stageAnimals.size(); j++) {
                Animal animalB = stageAnimals.get(j);

                float distance = animalA.getPosition().dst(animalB.getPosition());

                if (animalA.isEligibleForBreeding(animalB) && distance < 50) {
                    Animal offspring = animalA.breed(animalB, stage);
                    if (offspring != null) {
//                        System.out.println("Breeding "+ "Offspring Created: " + offspring.getType());
//                        System.out.println("Breeding "+ "Offspring Position: x=" + offspring.getX() + ", y=" + offspring.getY());

                        // Add offspring to both the stage and your list
                        animals.add(offspring);

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
                if (crop != null) {
                    game.batch.draw(crop.getCurrentFrame(), j * 16, i * 16); // Render crop at grid position
                }
            }
        }
        for (DroppedItem item : droppedItems) {
            item.render(game.batch);
        }
        game.batch.end();
    }

    private void handleDayPassed() {
        if(timer.getDaysPassed() != currentDays) {
            currentDays = timer.getDaysPassed();
            isNewDay = true;
            if (currentDays % 2 == 0) {
                for (Animal animal : animals) {
                    animal.incrementAge(); // Increment the age of each animal daily

                    // Update state if conditions are met (e.g., every 2 days of age)
                    if (animal.getAge() % 2 == 0 && animal.getHealth() > 80 && animal.getState() < 3) {
                        animal.incState();
                    }
                }

            }
        }
    }

    private void handleCrop() {
    }
    private void renderCrop() {
    }

    private void handleKeyDown(float delta) {
        // bấm m để hiện lên cửa sổ market
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            isMarketVisible = !isMarketVisible;
            System.out.println("marketScreen");

            if (isMarketVisible == true) {
                marketScreen.show();
            } else {
                marketScreen.hide();
            }
        }
        if (isMarketVisible) {
            marketScreen.render(delta);
        }


        // bấm B để hiện lên cửa sổ inventory
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            isInventoryVisible = !isInventoryVisible;
            System.out.println("inventoryScreen");
            if (isInventoryVisible) {
                System.out.println("show");
                inventoryScreen.show();
            } else {
                System.out.println("hide");
                inventoryScreen.hide();
            }
        }

        if (isInventoryVisible) {
            inventoryScreen.render(delta);
        }

        //   inventory Eqip UI
        inventoryUI.drawInventory(player);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            inventoryUI.slotCol = 0;
            player.slotCursor = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            inventoryUI.slotCol = 1;
            player.slotCursor = 1;
            System.out.println("num 2");
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
//        if (player.eqipInventory.get(player.slotCursor).getNum() != 0) {
        if (player.eqipInventory.get(player.slotCursor).getItem() == (Items.Item.COIN)) {
            if ((player.getPosition().x < 896f && player.getPosition().x > 887f && player.getPosition().y < 500f && player.getPosition().y > 480f) ||
                (player.getPosition().x < 881f && player.getPosition().x > 872f && player.getPosition().y < 556f && player.getPosition().y > 518f) ||
                (player.getPosition().x < 898f && player.getPosition().x > 892f && player.getPosition().y < 570f && player.getPosition().y > 562f)) {
                if (FishingVisible == false) {
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
                if (FishingVisible == false) {
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
//            if (Player.eqipInventory.get(Player.player.slotCursor).getItem() == Items.Item.COIN) {
                // Bật hoạt ảnh START_FISHING
                player.currentActivity = Animator.Activity.START_FISHING_RIGHT;
                player.updateFishingAnimation();
//            }
                cursorRight = false;
            }
            if (cursorLeft) {
                isFishingVisible = !isFishingVisible;
                FishingVisible = !FishingVisible;
//            if (Player.eqipInventory.get(Player.player.slotCursor).getItem()== Items.Item.COIN) {
                // Bật hoạt ảnh START_FISHING
                player.currentActivity = Animator.Activity.START_FISHING_LEFT;
                player.updateFishingAnimation();
//            }
                cursorLeft = false;
            }
            if (FishingVisible) {
                switch (player.currentActivity) {
                    case START_FISHING_RIGHT:
                        // Kiểm tra nếu hoạt ảnh đã chạy xong
                        if (player.animation.actionAnimations[player.currentActivity.ordinal()].isAnimationFinished(Animator.stateTime)) {
                            player.currentActivity = Animator.Activity.WAIT_FISHING_RIGHT;
                        }
                        break;
                    case START_FISHING_LEFT:
                        // Kiểm tra nếu hoạt ảnh đã chạy xong
                        if (player.animation.actionAnimations[player.currentActivity.ordinal()].isAnimationFinished(Animator.stateTime)) {
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
                        if (player.animation.actionAnimations[player.currentActivity.ordinal()].isAnimationFinished(Animator.stateTime)) {
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
                        if (player.animation.actionAnimations[player.currentActivity.ordinal()].isAnimationFinished(Animator.stateTime)) {
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
//        }
    }

    private void renderSelectedCell() {
        if (player.currentActivity != Animator.Activity.NONE) {
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
        if (selectedCell != null) {

        }
    }

    private void renderAmbientLighting() {
        if (player.eqipInventory.get(player.slotCursor).getItem() == Items.Item.COIN) {
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
    public OrthographicCamera getCamera() {
        return camera;
    }

    public void updateTorchLight( /* boolean isHoldingTorch, */ float x, float y) {
        if (isHoldingTorch) {
            if (torchLight == null) {
                // Tạo ánh sáng mới nếu chưa có
                torchLight = new PointLight(rayHandler, 128, Color.ORANGE, 50, x, y);
                torchLight.setSoftnessLength(10f); // Độ mềm mại của rìa ánh sáng
                torchLight.setDistance(20); // Bán kính tối đa (ánh sáng mạnh nhất tại trung tâm)
            }

            // Cập nhật vị trí ánh sáng theo vị trí người chơi
            torchLight.setPosition(x, y);

            // Giảm độ sáng dần theo khoảng cách
            float playerDistance = 20; // Bán kính tối đa
            float fadeFactor = Math.max(0, 1 - (playerDistance / torchLight.getDistance()));
            torchLight.setColor(new Color(1f, 0.5f, 0.2f, fadeFactor)); // Độ sáng alpha giảm dần
        } else {
            // Xóa ánh sáng nếu không cầm đuốc
            if (torchLight != null) {
                torchLight.remove();
                torchLight = null;
            }
        }
    }

    private void checkItemPickup(Player player) {
        for (int i = 0; i < droppedItems.size; i++) {
            DroppedItem item = droppedItems.get(i);
            if (player.getBounds().overlaps(item.getBounds())) { // Check collision
                System.out.println("Picked up: " + item.getItemType());
//                player.setItem(item.getItemType()); // Add to player's inventory
                droppedItems.removeIndex(i); // Remove the item from the list
                item.dispose(); // Dispose the item's texture
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

    public void printAnimalDebugInfo() {
        game.batch.begin();
        String debugInfo = "Animal: " + animals.get(0).getType() +
            ",\n Hunger: " + animals.get(0).getHunger() +
            "\n, Health: " + animals.get(0).getHealth() + "\n, State: " + animals.get(0).getState()
            + "\n, can be produce?: " + animals.get(0).canBeProduced();

        font.draw(game.batch, debugInfo, player.getPosition().x, player.getPosition().y);
        font.draw(game.batch, player.getPosition().x +  " " + player.getPosition().y, player.getPosition().x + 100, player.getPosition().y + 100);
        game.batch.end();
    }
    private String currentAct = "";
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.H) {
            currentAct = "hoe";
        } else if (keycode == Input.Keys.J) {
            currentAct = "water";
        } else if (keycode == Input.Keys.K) {
            currentAct = "harvest";
        } else if (keycode == Input.Keys.Z) {
            disposeArbitraryPlant();

        } else if (keycode == Input.Keys.L) {
            currentAct = "attack";
        } else if (keycode == Input.Keys.T) {
            Player.itemHolding = "torch";
        }
        return true;
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
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            int cellX = (int) touchPosition.x / 16;
            int cellY = (int) touchPosition.y / 16;
            selectedCell.set(cellX, cellY);

            Vector2 touchPosition2D = new Vector2(touchPosition.x, touchPosition.y);
            if (player.getPosition().dst(touchPosition2D) <= 50) {

                // Update activity and direction based on touch position (for animation purposes)
                player.updateActivityAnimation(currentAct, touchPosition2D);

                // Handle land interactions (hoe, water, harvest)
                Land land = landManager.getLand(cellY, cellX);
                if (Objects.equals(currentAct, "hoe")) {
                    land.hoe();
                    if (land.isEmpty()) {
                        Crop crop = new Crop(Items.Item.RICE, cellX * 16, cellY * 16); // Example crop
                        land.plantCrop(crop);
                    }
                }
                else if (Objects.equals(currentAct, "water")) {
                    land.water();
                    if (!land.isEmpty()) {
                        land.getCrop().setWatered(true);
                    }
                }
                else if (Objects.equals(currentAct, "harvest")) {
                    if (!land.isEmpty()) {
                        DroppedItem droppedItem = land.harvestCrop();
                        if (droppedItem != null) {
                            droppedItems.add(droppedItem);
                        }
                    }
                }

            }

            if (Objects.equals(currentAct, "attack")) {

                // Calculate attack direction based on the touch position
//                    player.attackDirection = touchPosition2D.sub(player.getPosition()).nor(); // Calculate direction vector
                if (Player.timeSinceLastAttack >= Player.attackCooldown && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    Player.timeSinceLastAttack = 0f;  // Reset the attack cooldown
                }

                // Optional: Set the attack animation for the player
                player.updateActivityAnimation(currentAct, touchPosition2D);
                // Additional logic for attacking nearby enemies, if needed.
                // You may want to loop through enemies in the range of the attack and deal damage.
                for (Monster enemy : monsters) {
                    if (player.getBounds().overlaps(enemy.getMonsterBounds())) {
//                            enemy.takeDamage(player.getAttackDamage());
                    }
                }
            }

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

    private void create () {
        monsters = new Array<>();
        plants = new Array<>();
        projectiles = new Array<>();
        entities = new Array<>();
//        monsters.add(new Monster(0, 0, 50, 3000));
//        monsters.add(new Monster(626, 176, 50, 3000));
//        monsters.add(new Monster(761, 650, 50, 2000));
//        monsters.add(new Monster(149, 680, 50, 3000));
//        for(int i = 1; i <= 50; i++){
//            monsters.add(new Monster(0, 0, 50, 3000));
//        }
//        plants.add(new ProtectPlant(500, 500, 7000000));
    }

}
