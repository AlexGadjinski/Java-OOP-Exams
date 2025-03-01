package dolphinarium.core;

import dolphinarium.entities.dolphins.BottleNoseDolphin;
import dolphinarium.entities.dolphins.Dolphin;
import dolphinarium.entities.dolphins.SpinnerDolphin;
import dolphinarium.entities.dolphins.SpottedDolphin;
import dolphinarium.entities.foods.Food;
import dolphinarium.entities.foods.Herring;
import dolphinarium.entities.foods.Mackerel;
import dolphinarium.entities.foods.Squid;
import dolphinarium.entities.pools.DeepWaterPool;
import dolphinarium.entities.pools.Pool;
import dolphinarium.entities.pools.ShallowWaterPool;
import dolphinarium.repositories.FoodRepository;
import dolphinarium.repositories.FoodRepositoryImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static dolphinarium.common.ConstantMessages.*;
import static dolphinarium.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {
    private FoodRepository foodRepository;
    private Map<String, Pool> pools;

    public ControllerImpl() {
        this.foodRepository = new FoodRepositoryImpl();
        this.pools = new LinkedHashMap<>();
    }

    @Override
    public String addPool(String poolType, String poolName) {
        Pool pool = switch (poolType) {
            case "DeepWaterPool" -> new DeepWaterPool(poolName);
            case "ShallowWaterPool" -> new ShallowWaterPool(poolName);
            default -> throw new NullPointerException(INVALID_POOL_TYPE);
        };

        if (pools.containsKey(poolName)) {
            throw new NullPointerException(POOL_EXISTS);
        }
        pools.put(poolName, pool);
        return SUCCESSFULLY_ADDED_POOL_TYPE.formatted(poolType, poolName);
    }

    @Override
    public String buyFood(String foodType) {
        Food food = switch (foodType) {
            case "Squid" -> new Squid();
            case "Herring" -> new Herring();
            case "Mackerel" -> new Mackerel();
            default -> throw new IllegalArgumentException(INVALID_FOOD_TYPE);
        };
        foodRepository.add(food);
        return SUCCESSFULLY_BOUGHT_FOOD_TYPE.formatted(foodType);
    }

    @Override
    public String addFoodToPool(String poolName, String foodType) {
        Food food = foodRepository.findByType(foodType);
        if (food == null) {
            throw new IllegalArgumentException(NO_FOOD_FOUND.formatted(foodType));
        }

        foodRepository.remove(food);
        pools.get(poolName).addFood(food);
        return SUCCESSFULLY_ADDED_FOOD_IN_POOL.formatted(foodType, poolName);
    }

    @Override
    public String addDolphin(String poolName, String dolphinType, String dolphinName, int energy) {
        Dolphin dolphin = switch (dolphinType) {
            case "BottleNoseDolphin" -> new BottleNoseDolphin(dolphinName, energy);
            case "SpottedDolphin" -> new SpottedDolphin(dolphinName, energy);
            case "SpinnerDolphin" -> new SpinnerDolphin(dolphinName, energy);
            default -> throw new IllegalArgumentException(INVALID_DOLPHIN_TYPE);
        };
        Pool pool = pools.get(poolName);
        if (pool.getDolphins().stream().anyMatch(d -> d.getName().equals(dolphinName))) {
            throw new IllegalArgumentException(DOLPHIN_EXISTS);
        }

        String poolType = pool.getClass().getSimpleName();
        if (canSwimInPool(dolphinType, poolType)) {
            pool.addDolphin(dolphin);
            return SUCCESSFULLY_ADDED_DOLPHIN_IN_POOL.formatted(dolphinType, dolphinName, poolName);
        }
        return POOL_NOT_SUITABLE;
    }

    private boolean canSwimInPool(String dolphinType, String poolType) {
        if (dolphinType.equals("BottleNoseDolphin") && poolType.equals("DeepWaterPool")) {
            return true;
        } else if (dolphinType.equals("SpinnerDolphin") && poolType.equals("ShallowWaterPool")) {
            return true;
        } else return dolphinType.equals("SpottedDolphin");
    }

    @Override
    public String feedDolphins(String poolName, String foodType) {
        Pool pool = pools.get(poolName);
        Food food = pool.getFoods().stream()
                .filter(f -> f.getClass().getSimpleName().equals(foodType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NO_FOOD_OF_TYPE_ADDED_TO_POOL));

        pool.getDolphins().forEach(d -> d.eat(food));
        pool.getFoods().remove(food);
        return DOLPHINS_FED.formatted(pool.getDolphins().size(), poolName);
    }

    @Override
    public String playWithDolphins(String poolName) {
        Pool pool = pools.get(poolName);
        Collection<Dolphin> dolphins = pool.getDolphins();
        if (dolphins.isEmpty()) {
            throw new IllegalArgumentException(NO_DOLPHINS);
        }

        ArrayList<Dolphin> removedDolphins = new ArrayList<>();
        for (Dolphin dolphin : dolphins) {
            dolphin.jump();
            if (dolphin.getEnergy() <= 0) {
                removedDolphins.add(dolphin);
            }
        }
        removedDolphins.forEach(pool::removeDolphin);

        return DOLPHINS_PLAY.formatted(poolName, removedDolphins.size());
    }

    @Override
    public String getStatistics() {
        return pools.values().stream()
                .map(Pool::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
