package com.jesus_crie.faerun.logic;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Represent a fight entry in the fight record.
 */
public interface FightEntry extends Serializable {

    boolean isHitEntry();

    @Nonnull
    Hit asHitEntry();

    boolean isDeadEntry();

    @Nonnull
    Dead asDeadEntry();

    /**
     * Reports a hit between two warriors.
     */
    final class Hit implements FightEntry {

        private static final long serialVersionUID = 6503266072216678593L;

        private final Player.Side attackerSide;
        private final int attackerIndex;
        private final int defenderIndex;
        private final int damage;

        public Hit(@Nonnull final Player.Side attackerSide,
                   final int attackerIndex,
                   final int defenderIndex,
                   final int damage) {
            this.attackerSide = attackerSide;
            this.attackerIndex = attackerIndex;
            this.defenderIndex = defenderIndex;
            this.damage = damage;
        }

        @Override
        public boolean isHitEntry() {
            return true;
        }

        @Nonnull
        @Override
        public Hit asHitEntry() {
            return this;
        }

        @Override
        public boolean isDeadEntry() {
            return false;
        }

        @Nonnull
        @Override
        public Dead asDeadEntry() {
            throw new UnsupportedOperationException();
        }

        public Player.Side getAttackerSide() {
            return attackerSide;
        }

        public int getAttackerIndex() {
            return attackerIndex;
        }

        public int getDefenderIndex() {
            return defenderIndex;
        }

        public int getDamage() {
            return damage;
        }
    }

    /**
     * Reports the death of a warrior.
     */
    final class Dead implements FightEntry {

        private static final long serialVersionUID = 3875412333491320608L;

        private final Player.Side deadSide;
        private final int warriorIndex;

        public Dead(@Nonnull final Player.Side deadSide, final int warriorIndex) {
            this.deadSide = deadSide;
            this.warriorIndex = warriorIndex;
        }

        @Override
        public boolean isHitEntry() {
            return false;
        }

        @Nonnull
        @Override
        public Hit asHitEntry() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isDeadEntry() {
            return true;
        }

        @Nonnull
        @Override
        public Dead asDeadEntry() {
            return this;
        }

        public Player.Side getDeadSide() {
            return deadSide;
        }

        public int getWarriorIndex() {
            return warriorIndex;
        }
    }
}
