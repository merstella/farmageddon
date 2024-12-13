package io.github.farmageddon.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import io.github.farmageddon.Main;


public class IntroductionScreen implements Screen {
    private Stage stage;
    private Main game;
    private Texture[] images;
    private int currentIndex;
    private Image displayImage;
    private Texture UI_Buttons;
    public static Music buttonSound;

    public IntroductionScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        this.UI_Buttons = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\UI_Buttons.png"));
        Gdx.input.setInputProcessor(stage);

        // Load images
        images = new Texture[]{
            new Texture(Gdx.files.internal("intro\\intro1.png")),
            new Texture(Gdx.files.internal("intro\\intro2.png")),
            new Texture(Gdx.files.internal("intro\\intro3.png")),
            new Texture(Gdx.files.internal("intro\\intro4.png")),
            new Texture(Gdx.files.internal("intro\\intro5.png")),
            new Texture(Gdx.files.internal("intro\\intro6.png"))
        };
        currentIndex = 0;

        // Display image setup
        displayImage = new Image(images[currentIndex]);
        displayImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        displayImage.setPosition(0, 0);
        stage.addActor(displayImage);

        Button leftButton = createButton(new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 35, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19))
            , new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 34, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19)),"-");
        leftButton.setSize(70, 70);
        leftButton.setPosition(50,(Gdx.graphics.getHeight()) / 2 - 40);
        Button rightButton = createButton(new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 38, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19))
            , new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 37, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19)),"+");
        rightButton.setSize(70, 70);
        rightButton.setPosition(50,(Gdx.graphics.getHeight()) / 2 + 20);
        Button exitButton = createButton( new TextureRegion(UI_Buttons, (UI_Buttons.getWidth() / 39) * 29, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19)),new TextureRegion(UI_Buttons,
            (UI_Buttons.getWidth() / 39) * 28, (UI_Buttons.getHeight() / 19), (UI_Buttons.getWidth() / 39), (UI_Buttons.getHeight() / 19)),"exit");
        exitButton.setSize(70, 70);
        exitButton.setPosition(Gdx.graphics.getWidth() - 90, Gdx.graphics.getHeight()- 90);
        buttonSound = Gdx.audio.newMusic(Gdx.files.internal("Sound/button.mp3"));
        buttonSound.setLooping(false);
        buttonSound.setVolume(1f);

        stage.addActor(leftButton);
        stage.addActor(rightButton);
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);
    }
    private void showPreviousImage() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = images.length - 1;
        }
        updateImage();
    }
    private void showNextImage() {
        currentIndex++;
        if (currentIndex >= images.length) {
            currentIndex = 0;
        }
        updateImage();
    }
    private void updateImage() {
        displayImage.setDrawable(new Image(images[currentIndex]).getDrawable());
    }

    private Button createButton(TextureRegion active, TextureRegion inactive, String action) {
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(inactive); // Trạng thái không được nhấn
        buttonStyle.down = new TextureRegionDrawable(active); // Trạng thái được nhấn
        Button button = new Button(buttonStyle);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (action.equals("-")) {
                    if (currentIndex == 0) {
                    }
                    else {
                        showPreviousImage();
                    }
                } else if (action.equals("exit")) {
                    game.setScreen(new MainMenuScreen(game));
                } else {
                    if (currentIndex == images.length - 1) {
                    }
                    else {
                        showNextImage();
                    }
                }
                buttonSound.stop();
                buttonSound.play();
            }
        });
        return button;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        for (Texture texture : images) {
            texture.dispose();
        }
    }
}
