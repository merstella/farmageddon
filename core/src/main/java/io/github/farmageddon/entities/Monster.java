package io.github.farmageddon.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.MonsterAnimation;

public class Monster extends Entity{
    private float targetX, targetY;
    private final Animator animation;
    public Animator.MonsterActivity currentActivity;

    public float getSpeed() { return speed; }

    public float getHealth() { return health; }

    public void setHealth(float health) { this.health = health; }

    public float getTargetX() { return targetX; }

    public float getTargetY() { return targetY; }

    public Monster(float x, float y, float speed, float targetX, float targetY, int maxHealth) {
        super(x, y, speed, true, maxHealth);
        this.targetX = targetX;
        this.targetY = targetY;

        animation = new Animator();
        currentActivity = Animator.MonsterActivity.IDLE_DOWN;
    }
    @Override
    public void update(float delta) {
        super.update(delta);
        Vector2 mov = new Vector2(0, 0);
        mov.x = targetX - position.x;
        mov.y = targetY - position.y;
        float base = (float) Math.sqrt(mov.x * mov.x + mov.y * mov.y);
        if (base > 0) {
            mov.x /= base; // Chuẩn hóa vector
            mov.y /= base;
        }

        position.x += mov.x * speed * delta;
        position.y += mov.y * speed * delta;
    }


    public void render(SpriteBatch batch) {
        batch.begin();
        batch.end();
    }

    public void dispose() {
        animation.dispose();
    }
}
