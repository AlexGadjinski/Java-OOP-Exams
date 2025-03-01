package dolphinarium.entities.pools;

import dolphinarium.entities.dolphins.Dolphin;
import dolphinarium.entities.foods.Food;

import java.util.Collection;

public abstract class BasePool implements Pool {
     private String name;
     private int capacity;
     private Collection<Food> foods;
     private Collection<Dolphin> dolphins;

     protected BasePool(String name, int capacity) {

     }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public Collection<Food> getFoods() {
        return foods;
    }

    @Override
    public Collection<Dolphin> getDolphins() {
        return dolphins;
    }

    @Override
    public void addDolphin(Dolphin dolphin) {

    }

    @Override
    public void removeDolphin(Dolphin dolphin) {

    }

    @Override
    public void addFood(Food food) {

    }
}
