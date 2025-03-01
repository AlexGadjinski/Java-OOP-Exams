package forgottenBattleships.core;

import forgottenBattleships.common.Command;
import forgottenBattleships.entities.battlezone.BattleZone;

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
            case AddBattleZone -> addBattleZone(data);
            case GetBattleZoneByName -> String.valueOf(getBattleZoneByName(data));
            case AddBattleshipToBattleZone -> addBattleshipToBattleZone(data);
            case StartBattle -> startBattle(data);
            case GetStatistics -> getStatistics();
            case Exit -> Command.Exit.name();
        };
        return result;
    }

    private String addBattleZone(String[] data) {
        return controller.addBattleZone(data[0], Integer.parseInt(data[1]));
    }

    private String addBattleshipToBattleZone(String[] data) {
        return controller.addBattleshipToBattleZone(data[0], data[1], data[2], Integer.parseInt(data[3]));
    }

    private BattleZone getBattleZoneByName(String[] data) {
        return controller.getBattleZoneByName(data[0]);

    }

    private String startBattle(String[] data) {
        return controller.startBattle(data[0], data[1], data[2]);
    }

    private String getStatistics() {
        return controller.getStatistics();
    }
}
