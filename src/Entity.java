/*****************************************************************************
 * Name: William Linke
 * Date: 05/11/2025
 * Assignment: Legends of the Earthen Vaults - Week 5 Implementation
 *
 * Abstract base class for all characters and combatants in the game.
 * Provides common fields and behavior for players and enemies.
 */
public abstract class Entity {
    protected String name;
    protected int health;

    public Entity(String name, int health) {
        this.name = name;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Abstract method to define unique behavior for each subclass
    public abstract void act();
}
