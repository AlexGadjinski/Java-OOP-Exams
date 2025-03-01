package furnitureFactory.entities.workshops;

public class TableWorkshop extends BaseWorkshop {
    private int producedFurnitureCount = 0;
    private static final int TABLE_WOOD_QUANTITY_REDUCE_FACTOR = 50;

    public TableWorkshop(int woodQuantity) {
        super(woodQuantity, TABLE_WOOD_QUANTITY_REDUCE_FACTOR);
    }

    @Override
    public int getProducedFurnitureCount() {
        return producedFurnitureCount;
    }

    @Override
    public void produce() {
        producedFurnitureCount++;
        setWoodQuantity(getWoodQuantity() - TABLE_WOOD_QUANTITY_REDUCE_FACTOR);
    }
}
