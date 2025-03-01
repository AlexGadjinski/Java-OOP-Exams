package furnitureFactory.entities.workshops;

import furnitureFactory.entities.wood.OakWood;
import furnitureFactory.entities.wood.Wood;

public abstract class BaseWorkshop implements Workshop {
    private int woodQuantity;
    private int producedFurnitureCount = 0;
    private int woodQuantityReduceFactor;

    protected BaseWorkshop(int woodQuantity, int woodQuantityReduceFactor) {
        setWoodQuantity(woodQuantity);
        this.woodQuantityReduceFactor = woodQuantityReduceFactor;
    }

    public void setWoodQuantity(int woodQuantity) {
        if (woodQuantity < 0) {
            woodQuantity = 0;
        }
        this.woodQuantity = woodQuantity;
    }

    @Override
    public int getWoodQuantity() {
        return woodQuantity;
    }

    @Override
    public void addWood(Wood wood) {
        if (wood instanceof OakWood) {
            setWoodQuantity(woodQuantity + 200);
        }
    }

    @Override
    public int getWoodQuantityReduceFactor() {
        return woodQuantityReduceFactor;
    }
}
