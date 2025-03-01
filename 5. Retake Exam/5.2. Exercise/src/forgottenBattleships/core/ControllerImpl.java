package forgottenBattleships.core;

import forgottenBattleships.entities.battleship.Battleship;
import forgottenBattleships.entities.battleship.PirateBattleship;
import forgottenBattleships.entities.battleship.RoyalBattleship;
import forgottenBattleships.entities.battlezone.BattleZone;
import forgottenBattleships.entities.battlezone.BattleZoneImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static forgottenBattleships.common.ConstantMessages.*;
import static forgottenBattleships.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {
    private Collection<BattleZone> battleZones;
    public ControllerImpl() {
        this.battleZones = new ArrayList<>();
    }

    @Override
    public String addBattleZone(String battleZoneName, int capacity) {
        if (battleZones.stream().anyMatch(b -> b.getName().equals(battleZoneName))) {
            throw new IllegalArgumentException(BATTLE_ZONE_EXISTS);
        }

        battleZones.add(new BattleZoneImpl(battleZoneName, capacity));
        return SUCCESSFULLY_ADDED_BATTLE_ZONE.formatted(battleZoneName);
    }

    @Override
    public BattleZone getBattleZoneByName(String battleZoneName) {
        // може да върне null
        return battleZones.stream()
                .filter(b -> b.getName().equals(battleZoneName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String addBattleshipToBattleZone(String battleZoneName, String shipType, String shipName, int health) {
        BattleZone battleZone = getBattleZoneByName(battleZoneName);
        if (battleZone == null) {
            throw new NullPointerException(BATTLE_ZONE_DOES_NOT_EXISTS);
        }
        Battleship battleship = switch (shipType) {
            case "RoyalBattleship" -> new RoyalBattleship(shipName, health);
            case "PirateBattleship" -> new PirateBattleship(shipName, health);
            default -> throw new IllegalArgumentException(INVALID_SHIP_TYPE);
        };
        if (battleZone.getShips().stream().anyMatch(b -> b.getName().equals(shipName))) {
            throw new IllegalArgumentException(SHIP_EXISTS);
        }

        battleZone.addBattleship(battleship);
        return SUCCESSFULLY_ADDED_SHIP.formatted(shipType, shipName, battleZoneName);
    }

    @Override
    public String startBattle(String battleZoneName, String attackingShip, String shipUnderAttack) {
        BattleZone battleZone = getBattleZoneByName(battleZoneName);
        Battleship attackerShip = battleZone.getBattleshipByName(attackingShip);
        Battleship underAttackShip = battleZone.getBattleshipByName(shipUnderAttack);

        if (attackerShip == null || underAttackShip == null) {
            throw new IllegalArgumentException(INSUFFICIENT_COUNT);
        }

        if (shipCanAttack(attackerShip.getClass().getSimpleName(), underAttackShip.getClass().getSimpleName())) {
            attackerShip.attack(underAttackShip);
        }
        if (underAttackShip.getHealth() <= 0) {
            battleZone.removeBattleShip(underAttackShip);
        }

        String ships = battleZone.getShips().stream()
                .map(Battleship::getName)
                .collect(Collectors.joining(", "));
        return BATTLE_CONTINUES.formatted(battleZoneName) + ships;
    }

    private boolean shipCanAttack(String attackerName, String defenderName) {
        return !(attackerName.equals("PirateBattleship") && defenderName.equals("PirateBattleship"));
    }

    @Override
    public String getStatistics() {
        return battleZones.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
    }
}
