package furnitureFactory.repositories;

import furnitureFactory.entities.workshops.Workshop;

import java.util.ArrayList;
import java.util.Collection;

import static furnitureFactory.common.ExceptionMessages.*;

public class WorkshopRepositoryImpl implements WorkshopRepository {
    private Collection<Workshop> workshops;

    public WorkshopRepositoryImpl() {
        this.workshops = new ArrayList<>();
    }

    @Override
    public void add(Workshop workshop) {
        String workshopType = workshop.getClass().getSimpleName();
        if (workshops.stream().anyMatch(w -> w.getClass().getSimpleName().equals(workshopType))) {
            throw new IllegalArgumentException(WORKSHOP_EXISTS_IN_REPOSITORY);
        } else if (workshop.getWoodQuantity() <= 0) {
            throw new IllegalArgumentException(WORKSHOP_WOOD_QUANTITY_BELOW_OR_EQUAL_ZERO);
        }
        workshops.add(workshop);
    }

    @Override
    public boolean remove(Workshop workshop) {
        return workshops.remove(workshop);
    }

    @Override
    public Workshop findByType(String type) {
        return workshops.stream()
                .filter(w -> w.getClass().getSimpleName().equals(type))
                .findFirst()
                .orElseThrow(() -> new NullPointerException(NO_WORKSHOP_FOUND.formatted(type)));
    }
}
