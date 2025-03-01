package furnitureFactory.entities.workshops;

import furnitureFactory.entities.wood.Wood;

public abstract class BaseWorkshop implements Workshop {
    private int woodQuantity;
    private int producedFurnitureCount;
    private final int woodQuantityReduceFactor;

    protected BaseWorkshop(int woodQuantity, int woodQuantityReduceFactor) {
        setWoodQuantity(woodQuantity);
        this.producedFurnitureCount = 0;
        this.woodQuantityReduceFactor = woodQuantityReduceFactor;
    }

    private void setWoodQuantity(int woodQuantity) {
        this.woodQuantity = Math.max(0, woodQuantity);
    }

    @Override
    public int getWoodQuantity() {
        return woodQuantity;
    }

    @Override
    public int getProducedFurnitureCount() {
        return producedFurnitureCount;
    }

    @Override
    public int getWoodQuantityReduceFactor() {
        return woodQuantityReduceFactor;
    }

    @Override
    public void addWood(Wood wood) {
        woodQuantity += wood.getWoodQuantity();
    }

    @Override
    public void produce() {
        setWoodQuantity(woodQuantity - woodQuantityReduceFactor);
        producedFurnitureCount++;
    }

    @Override
    public String toString() {
        return "%s: %d furniture produced".formatted(getClass().getSimpleName(), producedFurnitureCount);
    }
}
