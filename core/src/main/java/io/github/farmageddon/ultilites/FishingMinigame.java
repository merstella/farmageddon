package io.github.farmageddon.ultilites;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.entities.Player;

import static io.github.farmageddon.screens.GameScreen.droppedItems;
//import static io.github.farmageddon.entities.Player.inventory;

public class FishingMinigame extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private Player player;
    private float barWidth = 300; // Chiều dài của thanh bar
    private float barHeight = 20; // Chiều cao của thanh bar

    private float barX, barY; // Vị trí thanh bar
    private float cursorRadius = 10; // Kích thước con trỏ
    private float elapsedTime = 0;   // Thời gian trôi qua

    private float greenWidth = barWidth/3; // Chiều rộng của vùng xanh
    private float GreenX;          // Tọa độ x của vùng xanh
    private int level = 1;

    public int success = 0;  // Trạng thái nhấn phím chính xác
    public static boolean gameOver = false;
    public static boolean cursorGameOver = false;

    public FishingMinigame(Player player) {
        shapeRenderer = new ShapeRenderer();
        this.player = player;
    }

    @Override
    public void create() {
        // Tính toán vị trí thanh bar
        barX = Gdx.graphics.getWidth() / 2f + 50;
        barY = Gdx.graphics.getHeight() / 2f + 150;
        setGreen();
    }

    @Override
    public void render() {
        if (gameOver) {
            System.out.println("Game Over!"); // In thông báo kết thúc trò chơi
            GameScreen.isFishingVisible = false;
            if (success == 3){
                boolean hadFish = false;
                for (int i = 0; i < 25; i++){
                    if (player.inventory.get(i).getItem() == Items.Item.FISH){
                        player.inventory.get(i).add(player.inventory.get(i));
                        hadFish=true ;
                        cursorGameOver = true;
                        break;
                    }
                }
                if (!hadFish){
                    for (int i = 0; i < 25; i++) {
                        if (player.inventory.get(i).getItem() == Items.Item.DEFAULT) {
                            player.setItem(ItemList.Fish, i);
                            cursorGameOver = true;
                            break;
                        }
                    }
                }
            }
            gameOver = true;
            level = 1;
            success = 0;
            return;
        }

        elapsedTime += Gdx.graphics.getDeltaTime(); // Tăng thời gian theo khung hình
        // Tính toán vị trí con trỏ
        float cursorX = barX - barWidth / 2 + (float) Math.abs(Math.cos(elapsedTime) * barWidth);
        // Kiểm tra khi người chơi nhấn phím
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            // Kiểm tra con trỏ có nằm trong vùng xanh không
            if (cursorX >= GreenX && cursorX <= GreenX + greenWidth) {
                success ++;
                System.out.println("ok!");
                nextLevel();
            } else {
                System.out.println("Skill issues!");
                gameOver = true;
                cursorGameOver = true;
            }
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Vẽ thanh bar
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
        shapeRenderer.rect(barX - barWidth / 2, barY - barHeight / 2, barWidth, barHeight);

        // Vẽ vùng xanh
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(GreenX, barY - barHeight / 2, greenWidth, barHeight);

        // Vẽ con trỏ
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.circle(cursorX, barY, cursorRadius);

        shapeRenderer.end();
    }

    private void nextLevel() {
        level++;
        if (level > 3) {
            gameOver = true; // Kết thúc trò chơi nếu vượt quá số level tối đa
        } else {
            greenWidth = barWidth / (3 + level); // Giảm kích thước vùng xanh theo level
            elapsedTime = 0; // Reset thời gian để bắt đầu level mới
        }
    }

    private void setGreen(){
        // Xác định vị trí vùng xanh ngẫu nhiên trong thanh bar
        //GreenX = barX - barWidth/2 + (float) Math.random() * (barWidth - greenWidth);
        GreenX = MathUtils.random(barX - barWidth / 2, barX - barWidth / 2 + (barWidth - greenWidth));
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}

