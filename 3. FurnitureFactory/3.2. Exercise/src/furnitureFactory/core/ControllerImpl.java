package furnitureFactory.core;

import furnitureFactory.entities.factories.AdvancedFactory;
import furnitureFactory.entities.factories.Factory;
import furnitureFactory.entities.factories.OrdinaryFactory;
import furnitureFactory.entities.wood.OakWood;
import furnitureFactory.entities.wood.Wood;
import furnitureFactory.entities.workshops.DeckingWorkshop;
import furnitureFactory.entities.workshops.TableWorkshop;
import furnitureFactory.entities.workshops.Workshop;
import furnitureFactory.repositories.WoodRepository;
import furnitureFactory.repositories.WoodRepositoryImpl;
import furnitureFactory.repositories.WorkshopRepository;
import furnitureFactory.repositories.WorkshopRepositoryImpl;

import java.util.ArrayList;
import java.util.Collection;

import static furnitureFactory.common.ConstantMessages.*;
import static furnitureFactory.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {
    private WoodRepository woodRepository;
    private WorkshopRepository workshopRepository;
    private Collection<Factory> factories;

    public ControllerImpl() {
        this.woodRepository = new WoodRepositoryImpl();
        this.workshopRepository = new WorkshopRepositoryImpl();
        this.factories = new ArrayList<>();
    }

    @Override
    public String buildFactory(String factoryType, String factoryName) {
        Factory factory;
        switch (factoryType) {
            case "OrdinaryFactory" -> factory = new OrdinaryFactory(factoryName);
            case "AdvancedFactory" -> factory = new AdvancedFactory(factoryName);
            default -> throw new IllegalArgumentException(INVALID_FACTORY_TYPE);
        };
        if (factories.stream().anyMatch(f -> f.getName().equals(factoryName))) {
            throw new NullPointerException(FACTORY_EXISTS);
        }
        factories.add(factory);
        return SUCCESSFULLY_BUILD_FACTORY_TYPE.formatted(factoryType, factoryName);
    }

    @Override
    public Factory getFactoryByName(String factoryName) {
        // throws null!
        return factories.stream()
                .filter(f -> f.getName().equals(factoryName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String buildWorkshop(String workshopType, int woodCapacity) {
        Workshop workshop;
        switch (workshopType) {
            case "TableWorkshop" -> workshop = new TableWorkshop(woodCapacity);
            case "DeckingWorkshop" -> workshop = new DeckingWorkshop(woodCapacity);
            default -> throw new IllegalArgumentException(INVALID_WORKSHOP_TYPE);
        };
        workshopRepository.add(workshop);
        return SUCCESSFULLY_BUILD_WORKSHOP_TYPE.formatted(workshopType);
    }

    @Override
    public String addWorkshopToFactory(String factoryName, String workshopType) {
        Workshop workshop = workshopRepository.findByType(workshopType);
        if (workshop == null) {
            throw new NullPointerException(NO_WORKSHOP_FOUND.formatted(workshopType));
        }

        Factory factory = getFactoryByName(factoryName);
        // factory null!

        if (factory.getWorkshops().stream().anyMatch(w -> w.getClass().getSimpleName().equals(workshopType))) {
            throw new IllegalArgumentException(WORKSHOP_EXISTS);
        }

        String factoryType = factory.getClass().getSimpleName();

        if ((factory instanceof OrdinaryFactory && workshop instanceof DeckingWorkshop) ||
                factory instanceof AdvancedFactory && workshop instanceof TableWorkshop) {
            return NON_SUPPORTED_WORKSHOP;
        }

        factory.addWorkshop(workshop);
        return SUCCESSFULLY_ADDED_WORKSHOP_IN_FACTORY.formatted(workshopType, factoryName);
    }

    @Override
    public String buyWoodForFactory(String woodType) {
        if (!woodType.equals("OakWood")) {
            throw new IllegalArgumentException(INVALID_WOOD_TYPE);
        }
        woodRepository.add(new OakWood());
        return SUCCESSFULLY_BOUGHT_WOOD_FOR_FACTORY.formatted(woodType);
    }

    @Override
    public String addWoodToWorkshop(String factoryName, String workshopType, String woodType) {
        Factory factory = getFactoryByName(factoryName);
        if (factory.getWorkshops().stream().noneMatch(w -> w.getClass().getSimpleName().equals(workshopType))) {
            throw new NullPointerException(NO_WORKSHOP_ADDED);
        }
        Wood wood = woodRepository.findByType(woodType);
        if (wood == null) {
            throw new NullPointerException(NO_WOOD_FOUND.formatted(woodType));
        }

        Workshop workshop = factory.getWorkshops().stream()
                .filter(w -> w.getClass().getSimpleName().equals(workshopType))
                .findFirst()
                .orElse(null);

        workshop.addWood(wood);
        woodRepository.remove(wood);

        return SUCCESSFULLY_ADDED_WOOD_IN_WORKSHOP.formatted(woodType, workshopType);
    }

    @Override
    public String produceFurniture(String factoryName) {
        Factory factory = getFactoryByName(factoryName);
        if (factory.getWorkshops().isEmpty()) {
            throw new NullPointerException(THERE_ARE_NO_WORKSHOPS.formatted(factoryName));
        }

        boolean produced = false;

        StringBuilder sb = new StringBuilder();
        Collection<Workshop> workshops = factory.getWorkshops();

        for (Workshop workshop : workshops) {
            if (workshop.getWoodQuantity() < workshop.getWoodQuantityReduceFactor() && workshop.getWoodQuantity() > 0) {
                sb.append(INSUFFICIENT_WOOD).append(System.lineSeparator());
            } else if (workshop.getWoodQuantity() <= 0) {
                workshops.remove(workshop);
                workshopRepository.remove(workshop);
                factory.getRemovedWorkshops().add(workshop);
                sb.append(WORKSHOP_STOPPED_WORKING.formatted(workshop.getClass().getSimpleName()));
                sb.append(System.lineSeparator());
            } else {
                sb.append(String.format("%d piece of furniture was produced in the %s factory.", workshop.getProducedFurnitureCount(), factoryName));
                sb.append(System.lineSeparator());
                produced = true;
            }
        }
        if (produced) {
            sb.append(String.format("This time %s factory did not produce anything.", factoryName));
        }
        return sb.toString().trim();
    }

    @Override
    public String getReport() {
        StringBuilder sb = new StringBuilder();

        for (Factory factory : factories) {
            sb.append(String.format("Production by %s factory:\n", factory.getName()));
            for (Workshop removedWorkshop : factory.getRemovedWorkshops()) {
                sb.append(String.format("%s %d furniture produced\n", removedWorkshop.getClass().getSimpleName(), removedWorkshop.getProducedFurnitureCount()));
            }
            if (factory.getRemovedWorkshops().isEmpty()) {
                sb.append("No workshops were added to produce furniture.\n");
            }
        }
        return sb.toString().trim();
    }
}
