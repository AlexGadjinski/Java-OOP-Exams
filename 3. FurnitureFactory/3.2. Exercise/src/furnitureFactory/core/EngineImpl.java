package furnitureFactory.core;

import furnitureFactory.common.Command;
import furnitureFactory.entities.factories.Factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class EngineImpl implements Engine {
    private Controller controller;
    private BufferedReader reader;


    public EngineImpl() {
        this.controller = new ControllerImpl();
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        while (true) {
            String result;
            try {
                result = processInput();

                if (result.equals("Exit")) {
                    break;
                }
            } catch (NullPointerException | IllegalArgumentException | IllegalStateException | IOException e) {
                result = e.getMessage();
            }

            System.out.println(result);
        }
    }

    private String processInput() throws IOException {
        String input = this.reader.readLine();
        String[] tokens = input.split("\\s+");

        Command command = Command.valueOf(tokens[0]);
        String result = null;
        String[] data = Arrays.stream(tokens).skip(1).toArray(String[]::new);

        result = switch (command) {
            case BuildFactory -> buildFactory(data);
            case GetFactoryByName -> String.valueOf(getFactoryByName(data));
            case BuildWorkshop -> buildWorkshop(data);
            case AddWorkshopToFactory -> addWorkshopToFactory(data);
            case ProduceFurniture -> produceFurniture(data);
            case BuyWoodForFactory -> buyWoodForFactory(data);
            case AddWoodToWorkshop -> addWoodToWorkshop(data);
            case GetReport -> getReport();
            case Exit -> Command.Exit.name();
        };
        return result;
    }

    private String buildFactory(String[] data) {
        return controller.buildFactory(data[0], data[1]);
    }

    private Factory getFactoryByName(String[] data) {
        return controller.getFactoryByName(data[0]);
    }

    private String buildWorkshop(String[] data) {
        return controller.buildWorkshop(data[0], Integer.parseInt(data[1]));
    }

    private String addWorkshopToFactory(String[] data) {
        return controller.addWorkshopToFactory(data[0], data[1]);
    }

    private String produceFurniture(String[] data) {
        return controller.produceFurniture(data[0]);
    }

    private String buyWoodForFactory(String[] data) {
        return controller.buyWoodForFactory(data[0]);
    }

    private String addWoodToWorkshop(String[] data) {
        return controller.addWoodToWorkshop(data[0], data[1], data[2]);
    }

    private String getReport() {
        return controller.getReport();
    }
}
