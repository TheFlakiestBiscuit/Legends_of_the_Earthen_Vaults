/*****************************************************************************
 * Name: William Linke
 * Date: 05/11/2025
 * Assignment: Legends of the Earthen Vaults - Week 5 Implementation
 *
 * Represents a lootable chest that can contain inventory items.
 */
import java.util.ArrayList;

public class Chest extends GameObject {
    private ArrayList<InventoryItem> contents;
    private boolean isOpened;

    public Chest(String name, String description, boolean isSpecial) {
        super(name, description);
        this.contents = new ArrayList<>();
        this.isOpened = false;
    }

    public void addItem(InventoryItem item) {
        contents.add(item);
    }

    @Override
    public void interact(Player player) {
        if (!isOpened) {
            System.out.println("You open the chest and find:");
            for (InventoryItem item : contents) {
                System.out.println("- " + item.getName());
                player.addItem(item);
            }
            isOpened = true;
        } else {
            System.out.println("This chest is empty.");
        }
    }
}
