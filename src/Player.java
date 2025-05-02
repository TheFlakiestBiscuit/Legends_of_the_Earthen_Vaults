/*****************************************************************************
 * Name: William Linke
 * Date: 05/01/2025
 * Assignment: Legends of the Earthen Vaults - Week 4 Implementation
 *
 * Represents the player character, including inventory and equipped items.
 */
import java.util.ArrayList;

public class Player extends Entity {
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setHealth(int health) {
        this.health = health;
    }    

    public ArrayList<InventoryItem> getInventory() {
        return inventory;
    }

    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying:");
            for (InventoryItem item : inventory) {
                System.out.println("- " + item.getName() + ": " + item.getDescription());
            }
        }
    }

    public void useItem(String itemName) {
        InventoryItem toUse = null;

        for (InventoryItem item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                toUse = item;
                break;
            }
        }

        if (toUse == null) {
            System.out.println("You don't have that item.");
            return;
        }

        if (!toUse.isUsable()) {
            System.out.println("You can't use that.");
            return;
        }

        if (toUse.getName().equalsIgnoreCase("Healing Potion")) {
            System.out.println("You drink the Healing Potion. You feel better.");
            this.health = Math.min(100, this.health + 25);
            System.out.println("Health: " + this.health);
        }

        if (toUse.isConsumable()) {
            inventory.remove(toUse);
        }
    }

    @Override
    public void act() {
        System.out.println(name + " is waiting for input...");
    }
}
