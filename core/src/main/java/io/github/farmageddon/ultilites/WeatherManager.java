package io.github.farmageddon.ultilites;

public class WeatherManager {
    private Weather currentWeather;
    private float weatherChangeTimer; // Time until next weather change (in seconds)

    public WeatherManager() {
        this.currentWeather = Weather.CLEAR;
        this.weatherChangeTimer = 10f; // Default weather change interval (10 seconds)
    }

    public void update(float delta) {
        // Update the weather change timer
        weatherChangeTimer -= delta;
        if (weatherChangeTimer <= 0) {
            // Randomly change weather type after a certain interval
            changeWeather();
            weatherChangeTimer = 10f; // Reset the timer
        }
    }

    private void changeWeather() {
        // Randomize weather type (for simplicity, we'll pick between clear, rain, and snow)
        int random = (int) (Math.random() * 1);
        switch (random) {
            case 0:
                currentWeather = Weather.CLEAR;
                break;
            case 1:
                currentWeather = Weather.RAIN;
                break;
            case 2:
                currentWeather = Weather.FOG;
                break;
            case 3:
                currentWeather = Weather.STORM;
        }
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public enum Weather {
        CLEAR, RAIN, FOG, STORM
    }
}
