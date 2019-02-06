package faerun.game;

public class Player implements Comparable<Player> {

    private final String pseudo;
    private int score = 0;

    /**
     * @param pseudo - The username of the player.
     */
    public Player(final String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * @return The username of the player.
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @return The current score of the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Increment the score of the player
     *
     * @param amount - The amount of points to add.
     */
    public void incrementScore(final int amount) {
        score += amount;
    }

    /**
     * 2 players are equals if they have the same username.
     *
     * @param obj - The object to compare with.
     * @return True if the 2 objects are the same player, otherwise false.
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Player && pseudo.equals(((Player) obj).pseudo);
    }

    /**
     * Compare 2 players based on their respective scores.
     *
     * @param player - The other player to compare to.
     * @return A negative value, zero or a positive value wether this object is less than, equal or greater respectively
     * that the other player.
     */
    @Override
    public int compareTo(final Player player) {
        return score - player.score;
    }
}
