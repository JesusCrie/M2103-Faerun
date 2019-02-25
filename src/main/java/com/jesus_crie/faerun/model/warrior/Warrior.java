package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.Serializable;

public abstract class Warrior implements Serializable, Cloneable {

    private transient final Player owner;
    protected transient int strength = 10;
    protected int health = 100;

    public Warrior(@Nonnull final Player player) {
        owner = player;
    }

    /**
     * @return The {@link Player} That owns this warrior.
     */
    @Nonnull
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
     *
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
     * Get the training cost factor if the unit, will be multiplied by the base cost
     * of the castle.
     *
     * @return The training cost factor of this unit.
     */
    public abstract int getCostFactor();

    /**
     * Take damage, some warriors may override this method.
     *
     * @param damage - The damage to apply.
     */
    public void takeDamage(final int damage) {
        setHealth(getHealth() - damage);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[HP: " + getHealth() + "]";
    }

    @Nonnull
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ignore) {
            return null;
        }
    }
}
