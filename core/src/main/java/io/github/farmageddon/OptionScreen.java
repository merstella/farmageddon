package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.UI;

public class OptionScreen implements Screen {
    private UI ui;
    private Texture BoxFrame;
    private Texture UI_Frames;
    private Texture UI_Buttons;
    private TextureRegion exitButtonActive;
    private TextureRegion exitButtonInactive;
    private TextureRegion UpbuttonTextureActive;
    private TextureRegion UpbuttonTextureInactive;
    private TextureRegion DownbuttonTextureActive;
    private TextureRegion DownbuttonTextureInactive;
    private TextureRegion BarFrame;
    private GameScreen gameScreen;
    private Main game;
    private SpriteBatch batch;
    public Stage stage1;
//    public boolean isOptionVisible = false;

    private BitmapFont font;
    private float volume;


    public OptionScreen() {
        this.game = new Main();
        this.gameScreen = new GameScreen(game);
//        ui = new UI();
        this.UI_Buttons = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\UI_Buttons.png"));
        this.BoxFrame = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\BoxFrame.png"));
        BarFrame = new TextureRegion(UI_Buttons, 0, (UI_Buttons.getHeight() / 19), 32, 14);
        UpbuttonTextureActive = new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 35, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19));
        UpbuttonTextureInactive = new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 34, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19));
        DownbuttonTextureActive = new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 38, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19));
        DownbuttonTextureInactive = new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 37, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19));
        exitButtonActive = new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 29, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19));
        exitButtonInactive = new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 28, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19));

        Button exitButton = createButton(exitButtonActive,exitButtonInactive,"exit");
        exitButton.setPosition(Gdx.graphics.getWidth() / 2 + 160, Gdx.graphics.getHeight() / 2 );
        exitButton.setSize(exitButton.getWidth() * 3, exitButton.getHeight() * 3);

        Button upButton = createButton(UpbuttonTextureActive, UpbuttonTextureInactive, "volume louder");
        upButton.setPosition(Gdx.graphics.getWidth() / 2 - 110, Gdx.graphics.getHeight() / 2 + 10);
        upButton.setSize(upButton.getWidth() * 3, upButton.getHeight() * 3);

        Button downButton = createButton(DownbuttonTextureActive, DownbuttonTextureInactive, "volume quite");
        downButton.setPosition(Gdx.graphics.getWidth() / 2 - 110, Gdx.graphics.getHeight() / 2 + 80);
        downButton.setSize(downButton.getWidth() * 3, downButton.getHeight() * 3);

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2f);
        volume = 2f;

        this.stage1 = new Stage(new ScreenViewport());
        this.batch = new SpriteBatch();
        // Thêm các nút vào stage
        stage1.addActor(upButton);
        stage1.addActor(downButton);
        stage1.addActor(exitButton);
        System.out.println(volume);
    }

    private Button createButton(TextureRegion active, TextureRegion inactive, String action) {
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(inactive); // Trạng thái không được nhấn
        buttonStyle.down = new TextureRegionDrawable(active); // Trạng thái được nhấn
        Button button = new Button(buttonStyle);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (action.equals("volume quite")) {
                    volume = Math.min(volume + 1, 10);
                    gameScreen.music.setVolume(volume/10);

                } else if (action.equals("volume louder")) {
                    volume = Math.max(volume - 1, 0);
                    gameScreen.music.setVolume(volume/10);
                } else if (action.equals("exit")) {
                    ui = new UI();
                    ui.isOptionVisible = false;
                    System.out.println("exit");
                }
            }
        });
        return button;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage1);
    }

    @Override
    public void render(float v) {
        Gdx.input.setInputProcessor(stage1);
        float delta = Gdx.graphics.getDeltaTime();
        batch.begin();
        batch.draw(BoxFrame, (Gdx.graphics.getWidth() - (BoxFrame.getWidth()) * 8) / 2, (Gdx.graphics.getHeight() - BoxFrame.getHeight() * 8) / 2, BoxFrame.getWidth() * 8, BoxFrame.getHeight() * 8);
        batch.draw(BarFrame, (Gdx.graphics.getWidth() - (BarFrame.getRegionWidth()) * 3) / 2 + 20, (Gdx.graphics.getHeight() - BarFrame.getRegionHeight() * 3) / 2 + 70, BarFrame.getRegionWidth() * 4, BarFrame.getRegionHeight() * 4);
        font.draw(batch, String.valueOf(volume), (Gdx.graphics.getWidth() - (BarFrame.getRegionWidth()) * 3) / 2 + 75, (Gdx.graphics.getHeight() - BarFrame.getRegionHeight() * 3) / 2 + 108);
        // Cập nhật và vẽ Stage (UI)
        stage1.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage1.act(delta);
        stage1.draw();
        batch.end();
        ;
    }

    @Override
    public void resize(int i, int i1) {
        stage1.getViewport().update(i, i1, true);
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
        UI_Buttons.dispose();
        UI_Frames.dispose();
        stage1.dispose();
        batch.dispose();
    }
}
