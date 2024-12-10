package io.github.farmageddon.ultilites;

import io.github.farmageddon.entities.Monster;

public class GameDifficulty {
    private int maxEnemies;
    private float statFactor;
    private Monster baseMonster;

    public GameDifficulty () {
        maxEnemies = 10;
        baseMonster = new Monster(0, 0, 30, 1000);
    }

}
