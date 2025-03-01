package forgottenBattleships.entities.battlezone;

import forgottenBattleships.entities.battleship.Battleship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static forgottenBattleships.common.ConstantMessages.SHIPS_IN_BATTLE_ZONE;
import static forgottenBattleships.common.ConstantMessages.SHIP_WINS;
import static forgottenBattleships.common.ExceptionMessages.*;

public class BattleZoneImpl implements BattleZone {
    private String name;
    private int capacity;
    private Collection<Battleship> ships;

    public BattleZoneImpl(String name, int capacity) {
        setName(name);
        this.capacity = capacity;
        this.ships = new ArrayList<>();
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new NullPointerException(BATTLE_ZONE_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public Collection<Battleship> getShips() {
        return ships;
    }

    @Override
    public void addBattleship(Battleship battleship) {
        if (ships.size() == capacity) {
            throw new IllegalArgumentException(NOT_ENOUGH_CAPACITY);
        }
        if (battleship.getHealth() <= 0) {
            throw new IllegalArgumentException(SHIP_HEALTH_NULL_OR_EMPTY);
        }
        ships.add(battleship);
    }

    @Override
    public Battleship getBattleshipByName(String battleshipName) {
        // връща null
        return ships.stream()
                .filter(b -> b.getName().equals(battleshipName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeBattleShip(Battleship battleship) {
        ships.remove(battleship);
    }

    @Override
    public String toString() {
        String shipInfo = ships.size() == 1
//                ? SHIP_WINS.formatted(ships.stream().map(Battleship::getName).findFirst())
                ? SHIP_WINS.formatted(ships.stream().toList().get(0).getName())
                : ships.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
        return SHIPS_IN_BATTLE_ZONE.formatted(name) + System.lineSeparator() + shipInfo;
    }
}
