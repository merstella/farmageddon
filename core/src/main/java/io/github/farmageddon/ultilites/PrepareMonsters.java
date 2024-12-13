package io.github.farmageddon.ultilites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.entities.Monster;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class PrepareMonsters {
    private int maxEnemies;
    private int numEnemiesSpawnToday;
    private float statFactor, curDiffMulti;
    private Vector2 posMin, posMax;
    private Monster baseMonster;
    private Array<Monster> preparedMonsters = null;
    private Array<Float> timeSummon = null;
    public Random rand;

    public PrepareMonsters () {
        maxEnemies = 50;
        curDiffMulti = 1;
        rand = new Random();
        posMax = new Vector2(400, 400);
        posMin = new Vector2(0, 0);
        baseMonster = new Monster(0, 0, 30, 100);
    }
    public boolean isArrayNull () {return preparedMonsters == null && timeSummon == null;}
    public void prepareArrays () {
        preparedMonsters = new Array<>();
        timeSummon = new Array<>();
        for(int i = 0; i < maxEnemies; i++){
            preparedMonsters.add(new Monster(0, 0, 0, 100));
            timeSummon.add(0f);
        }
    }

    public boolean timeGreaterSummon (float time, int summonIndex) {
        return (time >= timeSummon.get(summonIndex));
    }

    public int random (int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }
    public float random (float min, float max) {return rand.nextInt((int)(max - min + 1)) + min;}

    public Vector2 randomPosition () {
        Vector2 position = new Vector2(0, 0);
        int positionCase = 0;
        positionCase = random(1, 4);
        position.x = random(posMin.x, posMax.x);
        position.y = random(posMin.y, posMax.y);
        switch (positionCase) {
            case 1:
                position.x = posMin.x;
                break;
            case 2:
                position.y = posMin.y;
                break;
            case 3:
                position.x = posMax.x;
                break;
            case 4:
                position.y = posMax.x;
                break;
        }
        return position;
    }
    public float randomTimer (float minTime, float maxTime) {
        return (float)random((int)(minTime) + 1, (int)maxTime);
    }
//    public void addMonsterToMonsters (int index, Array<Monster> monsters) {
//        monsters.add(preparedMonsters.get(index));
//    }

    public void addMonstersToMonsters (int numMonsterNeedSpawn, Array<Monster> monsters) {
        for(int i = 0; i < numMonsterNeedSpawn; i++) {
            monsters.add(preparedMonsters.get(i));

            Monster monster = preparedMonsters.get(i);
//            System.out.print(monsters.get(monsters.size-1).getHealth() + " " + monsters.get(monsters.size-1).getSpeed() + "\n");
        }
    }

    public void releaseOrderMonster (int index) {
        preparedMonsters.get(index).setExist(true);
    }

    public void prepareMonsters (int totalMonsterRequired, float timeStartSpawn, float timeFinishSpawn) {
        for (int i = 0; i < totalMonsterRequired; i++) {
            Monster monster = preparedMonsters.get(i);
            monster.setDamagePoint(baseMonster.getDamagePoint() * curDiffMulti);
            monster.setHealth(baseMonster.getHealth() * curDiffMulti);
            monster.setMaxHealth(baseMonster.getHealth() * curDiffMulti);
            monster.setSpeed(baseMonster.getSpeed() * curDiffMulti);
            monster.setPosition(randomPosition());
            monster.setTypeTarget(-1);
            monster.setExist(false);
            timeSummon.set(i, randomTimer(timeStartSpawn, timeFinishSpawn));
//            System.out.println("prepare monster " + i + " " + timeSummon.get(i));
        }
        for(int i = 0; i < totalMonsterRequired; i++) {
            for(int j = i + 1; j < totalMonsterRequired; j++) {
                if(timeSummon.get(i) > timeSummon.get(j)){
                    float mid = timeSummon.get(i);
                    timeSummon.set(i, timeSummon.get(j));
                    timeSummon.set(j, mid);
                }
            }
        }
    }

    public int getMaxEnemies() { return maxEnemies; }
    public void setMaxEnemies(int maxEnemies) { this.maxEnemies = maxEnemies; }

    public int getNumEnemiesSpawnToday() { return numEnemiesSpawnToday; }
    public void setNumEnemiesSpawnToday(int numEnemiesSpawnToday) { this.numEnemiesSpawnToday = numEnemiesSpawnToday; }

    public float getStatFactor() { return statFactor; }
    public void setStatFactor(float statFactor) { this.statFactor = statFactor; }

    public float getCurDiffMulti() { return curDiffMulti; }
    public void setCurDiffMulti(float curDiffMulti) { this.curDiffMulti = curDiffMulti; }

    public Vector2 getPosMin() { return posMin; }
    public void setPosMin(Vector2 posMin) { this.posMin = posMin; }

    public Vector2 getPosMax() { return posMax; }
    public void setPosMax(Vector2 posMax) { this.posMax = posMax; }

}
