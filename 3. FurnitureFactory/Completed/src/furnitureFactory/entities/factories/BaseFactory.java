package furnitureFactory.entities.factories;

import furnitureFactory.entities.workshops.Workshop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static furnitureFactory.common.ExceptionMessages.FACTORY_NAME_NULL_OR_EMPTY;

public abstract class BaseFactory implements Factory {
    private String name;
    private Collection<Workshop> workshops;
    private Collection<Workshop> removedWorkshops;

    protected BaseFactory(String name) {
        setName(name);
        this.workshops = new ArrayList<>();
        this.removedWorkshops = new ArrayList<>();
    }

    private void setName(String name) {
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

    @Override
    public String toString() {
        String workshopsInfo = workshops.isEmpty() && removedWorkshops.isEmpty()
                ? "No workshops were added to produce furniture."
                : workshops.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()))
                + System.lineSeparator()
                + removedWorkshops.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));

        return "Production by %s factory:\n".formatted(name) +
                "  %s".formatted(workshopsInfo.trim());
    }
}
