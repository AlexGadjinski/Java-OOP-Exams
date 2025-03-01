package furnitureFactory.repositories;

import furnitureFactory.entities.wood.Wood;

import java.util.ArrayList;
import java.util.Collection;

import static furnitureFactory.common.ExceptionMessages.NO_WOOD_FOUND;

public class WoodRepositoryImpl implements WoodRepository {
    private Collection<Wood> woodTypes;

    public WoodRepositoryImpl() {
        this.woodTypes = new ArrayList<>();
    }

    @Override
    public void add(Wood wood) {
        woodTypes.add(wood);
    }

    @Override
    public boolean remove(Wood wood) {
        return woodTypes.remove(wood);
    }

    @Override
    public Wood findByType(String type) {
        return woodTypes.stream()
                .filter(w -> w.getClass().getSimpleName().equals(type))
                .findFirst()
                .orElseThrow(() -> new NullPointerException(NO_WOOD_FOUND.formatted(type)));
    }
}
