/*****************************************************************************
 * 
 * Date: 05/11/2025
 * Legends of the Earthen Vaults - Week 5 Implementation
 *
 * Represents an enemy the player may encounter in a room.
 * Inherits basic combat behavior from the Entity class.
 */
public class Enemy extends Entity {
    private int attackPower;
    private boolean isAggressive;

    public Enemy(String name, int health, int attackPower, boolean isAggressive) {
        super(name, health);
        this.attackPower = attackPower;
        this.isAggressive = isAggressive;
    }

    public int attack() {
        return attackPower;
    }

    @Override
    public void act() {
        if (isAggressive) {
            System.out.println(name + " growls and moves to attack!");
        } else {
            System.out.println(name + " watches you cautiously.");
        }
    }
}
