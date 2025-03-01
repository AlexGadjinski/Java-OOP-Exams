package dolphinarium.entities.pools;

import dolphinarium.entities.dolphins.Dolphin;
import dolphinarium.entities.foods.Food;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static dolphinarium.common.ConstantMessages.*;
import static dolphinarium.common.ExceptionMessages.*;

public abstract class BasePool implements Pool {
    private String name;
    private int capacity;
    private Collection<Food> foods;
    private Collection<Dolphin> dolphins;

    protected BasePool(String name, int capacity) {
        setName(name);
        this.capacity = capacity;
        this.foods = new ArrayList<>();
        this.dolphins = new ArrayList<>();
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new NullPointerException(POOL_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
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
        if (dolphins.size() == capacity) {
            throw new IllegalStateException(NOT_ENOUGH_CAPACITY);
        }

        // веднага след като сме създали делфин, ако искаме да го добавим в басейна
        if (dolphin.getEnergy() <= 0) {
            throw new IllegalArgumentException(DOLPHIN_ENERGY_BELOW_OR_EQUAL_ZERO);
        }
        dolphins.add(dolphin);
    }

    @Override
    public void removeDolphin(Dolphin dolphin) {
//        dolphins.removeIf(d -> d.getName().equals(dolphin.getName()));
        dolphins.remove(dolphin);
    }

    @Override
    public void addFood(Food food) {
        foods.add(food);
    }

    @Override
    public String toString() {
        String dolphinsInfo = dolphins.isEmpty()
                ? NONE
                : dolphins.stream().map(d -> "%s - %s".formatted(d.getName(), d.getEnergy()))
                .collect(Collectors.joining(DELIMITER));
        return DOLPHINS_FINAL.formatted(name) + System.lineSeparator() + dolphinsInfo;
    }
}
