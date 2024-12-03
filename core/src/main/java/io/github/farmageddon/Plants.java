
package io.github.farmageddon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Pixmap;
public class Plants {
    private SpriteBatch batch;
    private PlantsAnimation plantsAnimation;
    // Thuộc tính của cây
    private float x; // Vị trí X
    private float y; // Vị trí Y
    private int health; // Sức khỏe


    public Plants(SpriteBatch batch, float x, float y, int health) {
        // Tải sprite sheet
        plantsAnimation = new PlantsAnimation();
        // Đặt vị trí, sức khỏe và sát thương
        this.x = x;
        this.y = y;
        this.health = health;
    }
    // Getter và Setter cho các thuộc tính
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }
    public void render(SpriteBatch batch) {
        batch.begin();
        plantsAnimation.render(x, y,PlantsAnimation.PlantsState.IDLE);
        batch.end();
    }
    public void dispose() {
        plantsAnimation.dispose();
        batch.dispose();
    }

}
