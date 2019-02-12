package faerun.warrior;

import faerun.game.Player;

import java.util.Random;

public abstract class Warrior {

    /**
     * The base cost of the unit for training.
     */
    protected static final int BASE_COST = 1;

    /**
     * Global random for all warriors.
     */
    protected static final Random RANDOM = new Random();

    private final Player owner;
    protected int strength = 10;
    protected int health = 100;

    public Warrior(final Player player) {
        owner = player;
    }

    /**
     * @return The {@link Player} That owns this warrior.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * @return The actual strength of the warrior.
     */
    public int getStrength() {
        return strength;
    }

    /**
     * @return The current amount of health point of the warrior.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Set the health of this warrior, can't be negative.
     * @param health - The wanted health.
     */
    public void setHealth(int health) {
        this.health = Math.max(health, 0);
    }

    /**
     * @return True if the warrior is alive.
     */
    public boolean isAlive() {
        return health != 0;
    }

    /**
     * @return The training cost of this unit.
     */
    public abstract int getCost();

    /**
     * Roll {@link #strength} times a dice between 1 and 3 and apply the damage
     * to the target.
     * @param other - The other warrior to attack.
     */
    public void attack(final Warrior other) {
        int damage = 0;
        for (int i = 0; i < getStrength(); i++) {
            damage += RANDOM.nextInt(3) + 1;
        }

        other.takeDamage(damage);
    }

    /**
     * Take damage, some warriors may override this method.
     * @param damage - The damage to apply.
     */
    protected void takeDamage(final int damage) {
        setHealth(getHealth() - damage);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[HP: " + getHealth() + "]";
    }
}
