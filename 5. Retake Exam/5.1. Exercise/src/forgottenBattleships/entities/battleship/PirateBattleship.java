package forgottenBattleships.entities.battleship;

public class PirateBattleship extends BaseBattleship {
    public PirateBattleship(String name, int health) {
        super(name, health, 80, 10);
    }

    @Override
    public void attack(Battleship battleship) {
        decreaseAmmo(10);
        battleship.takeDamage(this);
    }
}
