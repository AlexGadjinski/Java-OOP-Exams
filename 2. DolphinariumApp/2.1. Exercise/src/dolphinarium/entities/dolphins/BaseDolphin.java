package dolphinarium.entities.dolphins;

public abstract class BaseDolphin implements Dolphin {
    private String name;
    private int energy;

    protected BaseDolphin(String name, int energy) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getEnergy() {
        return energy;
    }
}
