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
import java.util.stream.Collectors;
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
        Factory factory = switch (factoryType) {
            case "OrdinaryFactory" -> new OrdinaryFactory(factoryName);
            case "AdvancedFactory" -> new AdvancedFactory(factoryName);
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
        // може да върне null
        return factories.stream()
                .filter(f -> f.getName().equals(factoryName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String buildWorkshop(String workshopType, int woodCapacity) {
        Workshop workshop = switch (workshopType) {
            case "TableWorkshop" -> new TableWorkshop(woodCapacity);
            case "DeckingWorkshop" -> new DeckingWorkshop(woodCapacity);
            default -> throw new IllegalArgumentException(INVALID_WORKSHOP_TYPE);
        };
        workshopRepository.add(workshop);
        return SUCCESSFULLY_BUILD_WORKSHOP_TYPE.formatted(workshopType);
    }

    @Override
    public String addWorkshopToFactory(String factoryName, String workshopType) {
        Factory factory = getFactoryByName(factoryName);
        // factory може да е null
        Workshop workshop = workshopRepository.findByType(workshopType);

        if (factory.getWorkshops().stream().anyMatch(w -> w.getClass().getSimpleName().equals(workshopType))) {
            throw new IllegalArgumentException(WORKSHOP_EXISTS);
        }

        if (workshopIsSupported(workshopType, factory.getClass().getSimpleName())) {
            factory.addWorkshop(workshop);
            return SUCCESSFULLY_ADDED_WORKSHOP_IN_FACTORY.formatted(workshopType, factoryName);
        }
        return NON_SUPPORTED_WORKSHOP;
    }

    private boolean workshopIsSupported(String workshopType, String factoryType) {
        return switch (workshopType) {
            case "TableWorkshop" -> factoryType.equals("OrdinaryFactory");
            case "DeckingWorkshop" -> factoryType.equals("AdvancedFactory");
            default -> throw new IllegalArgumentException("Never Reached!!");
        };
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
        // factory може да е null
        Workshop workshop = factory.getWorkshops().stream()
                .filter(w -> w.getClass().getSimpleName().equals(workshopType))
                .findFirst()
                .orElseThrow(() -> new NullPointerException(NO_WORKSHOP_ADDED));
        Wood wood = woodRepository.findByType(woodType);

        workshop.addWood(wood);
        woodRepository.remove(wood);
        return SUCCESSFULLY_ADDED_WOOD_IN_WORKSHOP.formatted(woodType, workshopType);
    }

    @Override
    public String produceFurniture(String factoryName) {
        Factory factory = getFactoryByName(factoryName);
        // factory може да е null

        Collection<Workshop> workshops = factory.getWorkshops();
        if (workshops.isEmpty()) {
            throw new NullPointerException(THERE_ARE_NO_WORKSHOPS.formatted(factoryName));
        }

        int producedFurniture = 0;
        for (Workshop workshop : workshops) {
            if (workshop.getWoodQuantity() < workshop.getWoodQuantityReduceFactor() && workshop.getWoodQuantity() > 0) {
                System.out.println(INSUFFICIENT_WOOD);
                continue;
            }
            if (workshop.getWoodQuantity() <= 0) {
                workshopRepository.remove(workshop);
                factory.getRemovedWorkshops().add(workshop);
                System.out.println(WORKSHOP_STOPPED_WORKING.formatted(workshop.getClass().getSimpleName()));
                continue;
            }
            workshop.produce();
            producedFurniture++;
        }
        factory.getWorkshops().removeAll(factory.getRemovedWorkshops());
        return producedFurniture == 0
                ? FACTORY_DO_NOT_PRODUCED.formatted(factoryName)
                : SUCCESSFUL_PRODUCTION.formatted(producedFurniture, factoryName);
    }

    @Override
    public String getReport() {
        return factories.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
    }
}
