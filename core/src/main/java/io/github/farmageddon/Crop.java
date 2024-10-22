package io.github.farmageddon;

public class Crop {
    private String type;
    private int growthStage;
    private boolean isHarvestable;

    public Crop(String type) {
        this.type = type;
        this.growthStage = 0;  // Initial growth stage
        this.isHarvestable = false;
    }

    public void grow() {
        growthStage++;
        if (growthStage >= 5) {
            isHarvestable = true;
        }
    }

    public boolean isHarvestable() {
        return isHarvestable;
    }

    public String getType() {
        return type;
    }

    public void harvest() {
        if (isHarvestable) {
            System.out.println("Harvesting " + type);
            growthStage = 0;  // Reset growth after harvest
            isHarvestable = false;
        } else {
            System.out.println(type + " is not ready to harvest yet.");
        }
    }
}
