/*****************************************************************************
 * Name: William Linke
 * Date: 04/23/2035
 * Assignment: Legends of the Earthen Vaults - Week 3 Implementation
 *
 * Represents the player character, including inventory and equipped items.
 */
import java.util.ArrayList;

public class Player extends Entity {
    private ArrayList<InventoryItem> inventory;
    private Room currentRoom;
    private InventoryItem equippedWeapon;
    private InventoryItem equippedArmor;

    public Player(String name, int health, Room startRoom) {
        super(name, health);
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
        this.equippedWeapon = null;
        this.equippedArmor = null;
    }

    public void move(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
            nextRoom.enter(this);
        } else {
            System.out.println("You can't go that way.");
        }
    }

    public void addItem(InventoryItem item) {
        inventory.add(item);
    }

    public void equipItem(InventoryItem item) {
        if (item.isEquipable()) {
            if ("weapon".equalsIgnoreCase(item.getSlotType())) {
                equippedWeapon = item;
            } else if ("armor".equalsIgnoreCase(item.getSlotType())) {
                equippedArmor = item;
            }
        }
    }

    public int getAttackPower() {
        return equippedWeapon != null ? equippedWeapon.getAttackBoost() : 0;
    }

    public int getDefense() {
        return equippedArmor != null ? equippedArmor.getDefenseBoost() : 0;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }
}
