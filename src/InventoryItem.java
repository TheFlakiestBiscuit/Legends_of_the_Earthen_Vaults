/*****************************************************************************
 *
 * Date: 05/11/2025
 * Legends of the Earthen Vaults - Week 5 Implementation
 *
 * Represents an item that can be stored in the player's inventory.
 * Items may be consumable, equipable, or used to interact with puzzles.
 */
public class InventoryItem {
    private String name;
    private String description;
    private boolean isConsumable;
    private boolean isEquipable;
    private String slotType; // e.g., "weapon", "armor"
    private int attackBoost;
    private int defenseBoost;

    public InventoryItem(String name, String description, boolean isConsumable, boolean isEquipable,
            String slotType, int attackBoost, int defenseBoost) {
        this.name = name;
        this.description = description;
        this.isConsumable = isConsumable;
        this.isEquipable = isEquipable;
        this.slotType = slotType;
        this.attackBoost = attackBoost;
        this.defenseBoost = defenseBoost;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isConsumable() {
        return isConsumable;
    }

    public boolean isEquipable() {
        return isEquipable;
    }

    public String getSlotType() {
        return slotType;
    }

    public int getAttackBoost() {
        return attackBoost;
    }

    public int getDefenseBoost() {
        return defenseBoost;
    }

    public boolean isUsable() {
        return isConsumable || isEquipable;
    }
    

    @Override
    public String toString() {
        return name + ": " + description;
    }
}
