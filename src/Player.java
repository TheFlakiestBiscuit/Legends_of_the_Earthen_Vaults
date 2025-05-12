/*****************************************************************************
 * Name: William Linke
 * Date: 05/11/2025
 * Assignment: Legends of the Earthen Vaults - Week 5 Implementation
 *
 * Represents the player character, including inventory and equipped items.
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

public class Player extends Entity {
    private int id;
    private ArrayList<InventoryItem> inventory;
    private Room currentRoom;
    private InventoryItem equippedWeapon;
    private InventoryItem equippedArmor;
    private HashMap<String, Boolean> flags = new HashMap<>();

    public Player(String name, int health, Room startRoom) {
        super(name, health);
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
        this.equippedWeapon = null;
        this.equippedArmor = null;
    }

    public int getHealth() {
        return this.health;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void move(String direction) {
        if (!currentRoom.canExit(direction, this)) {
            return;
        }

        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
            nextRoom.enter(this);
        } else {
            System.out.println("You can't go that way.");
        }
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(100, health)); // optional bounds check
    }

    public void setFlag(String key, boolean value) {
        flags.put(key, value);
    }

    public boolean getFlag(String key) {
        return flags.getOrDefault(key, false);
    }


    public void addItem(InventoryItem item) {
        inventory.add(item);
        System.out.println(item.getName() + " has been added to your inventory.");
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

    public boolean hasItem(String itemName) {
        for (InventoryItem item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public void removeItem(String itemName) {
        Iterator<InventoryItem> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            InventoryItem item = iterator.next();
            if (item.getName().equalsIgnoreCase(itemName)) {
                iterator.remove();
                break;
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

        String currentRoomId = currentRoom.getId();

        if (toUse.getName().equalsIgnoreCase("Healing Potion")) {
            System.out.println("You drink the Healing Potion. You feel better.");
            this.health = Math.min(100, this.health + 25);
            System.out.println("Health: " + this.health);
            if (toUse.isConsumable()) {
                inventory.remove(toUse);
            }
            return;
        }

        if (toUse.getName().equalsIgnoreCase("Restoration Potion")) {
            if ("11-A".equals(currentRoomId)) {
                currentRoom.triggerPuzzle("restoration");
                if (toUse.isConsumable()) {
                    inventory.remove(toUse);
                }
            } else {
                System.out.println("Restoration Potions are highly dangerous to those who are not gravely ill.");
            }
            return;
        }

        if (toUse.getName().equalsIgnoreCase("Magical Crystal")) {
            if ("9-A".equals(currentRoomId)) {
                currentRoom.triggerPuzzle("crystal fitting");
                if (toUse.isConsumable()) {
                    inventory.remove(toUse);
                }
            } else {
                System.out.println("You can't use that here.");
            }
            return;
        }

        System.out.println("You can’t use that right now.");
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

    @Override
    public void act() {
        System.out.println(name + " is waiting for input...");
    }
}
