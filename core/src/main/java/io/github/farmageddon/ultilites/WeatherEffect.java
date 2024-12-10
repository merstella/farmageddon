package io.github.farmageddon.ultilites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class WeatherEffect {
    private ParticleEffect rainEffect;
    private ParticleEffect fogEffect;
    private ParticleEffect stormEffect;
    public WeatherEffect() {
        // Load the particle effects (you can also create these programmatically)
        rainEffect = new ParticleEffect();
        fogEffect = new ParticleEffect();
        stormEffect = new ParticleEffect();
        rainEffect.load(Gdx.files.internal("rain.p"), Gdx.files.internal("effects"));
//        fogEffect.load(Gdx.files.internal("fog.p"), Gdx.files.internal("effects"));
//        stormEffect.load(Gdx.files.internal("storm.p"), Gdx.files.internal("effects"));
    }

    public void update(float delta, WeatherManager.Weather currentWeather) {
        if (currentWeather == WeatherManager.Weather.RAIN) {
            rainEffect.update(delta);
        } else if (currentWeather == WeatherManager.Weather.FOG) {
            fogEffect.update(delta);
        } else if (currentWeather == WeatherManager.Weather.STORM) {
            stormEffect.update(delta);
        }
    }

    public void render(SpriteBatch batch, WeatherManager.Weather currentWeather) {
        if (currentWeather == WeatherManager.Weather.RAIN) {
            rainEffect.draw(batch);
        } else if (currentWeather == WeatherManager.Weather.FOG) {
            fogEffect.draw(batch);
        } else if (currentWeather == WeatherManager.Weather.STORM) {
            stormEffect.draw(batch);
        }
    }
}
