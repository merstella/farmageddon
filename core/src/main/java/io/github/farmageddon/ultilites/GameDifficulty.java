package io.github.farmageddon.ultilites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.entities.Monster;

import java.util.Random;

public class GameDifficulty {
    private int maxEnemies;
    private float secondSinceStart, timeSinceLastSpawn, cooldownSpawn, nextTimeSpawn, timeForSpawn;
    private int numEnemiesSpawnToday;
    private float statFactor, curDiffMulti;
    private Monster baseMonster;
    private float minX, maxX;
    private float minY, maxY;
    private Array<Monster> preparedMonsters;
    private Array<Float> timeSummon;
    public Random rand;

//    public void setMonsters (com.badlogic.gdx.utils.Array<Monster> monsters) {
//        this.monsters = monsters;
//    }

    public GameDifficulty () {
        maxEnemies = 50;

        preparedMonsters = new Array<>();
        timeSummon = new Array<>();
        statFactor = 1;
        curDiffMulti = 1;
        secondSinceStart = 0;  // time from start game to now
        cooldownSpawn = 30;    // time from last spawn to next spawn
        timeSinceLastSpawn = 0;// second begin spawning monster last time
        timeForSpawn = 10;     // time for monster spawn

        for (int i = 0; i < maxEnemies; i++) {
            preparedMonsters.add(new Monster(0, 0, 30, 100));
            timeSummon.add(0f);
        }

        numEnemiesSpawnToday = 10;
        baseMonster = new Monster(0, 0, 30, 100);
        minX = 0;
        minY = 0;
        maxX = 1000;
        maxY = 1000;
        rand = new Random();
    }

    public int random (int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }
    public float random (float min, float max) {return rand.nextInt((int)(max - min + 1)) + min;}

    public Vector2 randomPosition () {
        Vector2 position = new Vector2(0, 0);
        int positionCase = 0;
        positionCase = random(1, 4);
        position.x = random(minX, maxX);
        position.y = random(minY, maxY);
        switch (positionCase) {
            case 1:
                position.x = minX;
            case 2:
                position.y = minY;
            case 3:
                position.x = maxX;
            case 4:
                position.y = maxY;
        }
        return position;
    }
    public float randomTimer (float minTime, float maxTime) {
        return (float)random((int)(minTime) + 1, (int)maxTime);
    }
    public boolean isNowSpawn () {
        return (cooldownSpawn <= timeSinceLastSpawn);
    }

    public void addMonsterToMonsters (int index, Array<Monster> monsters) {
        monsters.add(preparedMonsters.get(index));
    }


    public void prepareMonsters (int totalMonsterRequired) {
        for (int i = 0; i < totalMonsterRequired; i++) {
            Monster monster = preparedMonsters.get(i);
            monster.setDamagePoint(monster.getDamagePoint() * curDiffMulti);
            monster.setHealth(monster.getHealth() * curDiffMulti);
            monster.setSpeed(monster.getSpeed() * curDiffMulti);
            monster.setPosition(randomPosition());
            monster.setTypeTarget(-1);
            monster.setExist(false);
            timeSummon.set(i, randomTimer(0f, timeForSpawn));
        }
    }


    public void summonMonsterToGame (Array<Monster> monsters) {
//        secondSinceStart += delta;
//        timeSinceLastSpawn += delta;
        if(cooldownSpawn > timeSinceLastSpawn)return;
        while(timeSinceLastSpawn >= cooldownSpawn) timeSinceLastSpawn -= cooldownSpawn;
        prepareMonsters(numEnemiesSpawnToday);
        while (monsters.size > 0) monsters.removeIndex(0);
        for(int i = 0; i < numEnemiesSpawnToday; i++){
            monsters.add(preparedMonsters.get(i));
        }

    }

    public void update (float delta) {
        secondSinceStart += delta;

//        if(secondSinceStart)

    }


}
