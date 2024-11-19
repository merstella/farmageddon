package io.github.farmageddon;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;
import io.github.farmageddon.Crops.Crop;
import io.github.farmageddon.Crops.Items;
import io.github.farmageddon.screens.GameScreen;
import com.badlogic.gdx.audio.Sound;

public class PlayerController implements InputProcessor {
    private Player player;
    private Crop crop;
    private GameScreen screen;
    Vector3 tp;

    public PlayerController(GameScreen screen, Player player){
        this.screen = screen;
        this.player = player;
        tp =  new Vector3();

    }
    @Override
    public boolean keyDown(int keycode) {
        // +/- : Tăng/giảm tốc độ gametime
        if (keycode == Input.Keys.PLUS) {
            screen.getTimer().setTimeRatio(9000);
        }
        if (keycode == Input.Keys.MINUS) {
            screen.getTimer().setTimeRatio(600);
        }
        // 0: tăng thời gian lên 1 ngày và set 7h
        if (keycode == Input.Keys.NUM_0) {
            screen.getTimer().setStartTime(screen.getCurrentDays() + 1, 7, 0, 0);
        }
        // return true để không có event nào đụng vào nữa
        return true;
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
        Vector3 coords = screen.getCamera().unproject(tp.set(screenX, screenY, 0));
        if(Math.abs(player.getPlayerCenterX()- coords.x) < 50 && Math.abs(player.getPlayerCenterY()-coords.y) < 50) {
            for (int i = 0; i < screen.getSeeds().size; i++) {
                if(screen.getSeeds().get(i).getBoundingRect().contains(coords.x, coords.y)){
                    screen.buySeed(screen.getSeeds().get(i));
                }
            }

            for (int i = 0; i < screen.numCrops; i++) {
                if (screen.getCrops().get(i).getFrameSprite().getBoundingRectangle().contains(coords.x, coords.y)) {
                    if (screen.currentItem.getItem() == Items.Item.BUCKET) {
                        screen.getCrops().get(i).setWatered(true);
                        Main.manager.get("ref asset/water.mp3", Sound.class).play(2);
                    }
                    if (screen.getCrops().get(i).getGrowthStage() == 3) {
                        screen.addMoney(screen.getCrops().get(i).getPrice());
                        screen.getCrops().removeIndex(i);
                        screen.numCrops--;
                        Main.manager.get("ref asset/dirt.mp3", Sound.class).play();
                        return false;
                    } else {
                        return false;
                    }
                }
            }

        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
        // Adjust intType based on amountY for vertical scrolling
        screen.intType += (int) amountY;

        // Wrap intType within valid bounds
        if (screen.intType > screen.getItems().size - 1) {
            screen.intType = 0;
        }
        if (screen.intType < 0) {
            screen.intType = screen.getItems().size - 1;
        }

        // Set the selected item based on the updated intType
        screen.setMouseCrop(screen.getItems().get(screen.intType));

        return false; // Change to true if you want to indicate event handling
    }



}
