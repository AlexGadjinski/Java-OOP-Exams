package furnitureFactory.entities.workshops;

public class DeckingWorkshop extends BaseWorkshop {
    private int producedFurnitureCount = 0;
    private static final int DECKING_WOOD_QUANTITY_REDUCE_FACTOR = 150;

    public DeckingWorkshop(int woodQuantity) {
        super(woodQuantity, DECKING_WOOD_QUANTITY_REDUCE_FACTOR);
    }

    @Override
    public int getProducedFurnitureCount() {
        return producedFurnitureCount;
    }

    @Override
    public void produce() {
        producedFurnitureCount++;
        setWoodQuantity(getWoodQuantity() - DECKING_WOOD_QUANTITY_REDUCE_FACTOR);
    }
}
