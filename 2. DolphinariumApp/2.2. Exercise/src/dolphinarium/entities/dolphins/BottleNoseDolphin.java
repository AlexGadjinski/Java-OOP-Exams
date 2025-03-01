package dolphinarium.entities.dolphins;

import dolphinarium.entities.foods.Food;

public class BottleNoseDolphin extends BaseDolphin {
    public BottleNoseDolphin(String name, int energy) {
        super(name,energy);
    }

    @Override
    public void jump() {
        decreaseEnergy(200);
    }
}
