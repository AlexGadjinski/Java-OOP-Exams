package furnitureFactory.entities.factories;

import furnitureFactory.entities.workshops.Workshop;

import java.util.ArrayList;
import java.util.Collection;

import static furnitureFactory.common.ExceptionMessages.*;

public abstract class BaseFactory implements Factory {
    private String name;
    private Collection<Workshop> workshops;
    private Collection<Workshop> removedWorkshops;

    protected BaseFactory(String name) {
        setName(name);
        this.workshops = new ArrayList<>();
        this.removedWorkshops = new ArrayList<>();
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new NullPointerException(FACTORY_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public void addWorkshop(Workshop workshop) {
        workshops.add(workshop);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<Workshop> getWorkshops() {
        return workshops;
    }

    @Override
    public Collection<Workshop> getRemovedWorkshops() {
        return removedWorkshops;
    }
}
