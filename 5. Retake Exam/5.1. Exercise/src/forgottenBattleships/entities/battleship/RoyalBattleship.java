package forgottenBattleships.entities.battleship;

public class RoyalBattleship extends BaseBattleship {
    public RoyalBattleship(String name, int health) {
        super(name, health, 100, 25);
    }

    @Override
    public void attack(Battleship battleship) {
        decreaseAmmo(25);
        battleship.takeDamage(this);
    }
}
