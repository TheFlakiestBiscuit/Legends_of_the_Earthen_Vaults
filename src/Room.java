/*****************************************************************************
 * Name: William Linke
 * Date: 05/11/2025
 * Assignment: Legends of the Earthen Vaults - Week 5 Implementation
 *
 * Represents a room in the dungeon with exits, enemies, and interactable objects.
 */
import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private String roomId;
    private String description;
    private HashMap<String, Room> exits;
    private HashMap<String, String> lookTargets;
    private ArrayList<Enemy> enemies;
    private ArrayList<GameObject> objects;

    public Room(String roomId, String description) {
        this.roomId = roomId;
        this.description = description;
        this.exits = new HashMap<>();
        this.lookTargets = new HashMap<>();
        this.enemies = new ArrayList<>();
        this.objects = new ArrayList<>();
    }

    public String getRoomId() {
        return roomId;
    }

    // Alias to support getId() used elsewhere
    public String getId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
    this.description = description;
}

    public void addExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public boolean canExit(String direction, Player player) {
        if (this.roomId.equals("10-E") && direction.equalsIgnoreCase("south")) {
            boolean hasFragment1 = player.hasItem("Triangular Key Fragment A");
            boolean hasFragment2 = player.hasItem("Triangular Key Fragment B");
            boolean hasFragment3 = player.hasItem("Triangular Key Fragment C");

            if (!(hasFragment1 && hasFragment2 && hasFragment3)) {
                System.out.println("The triangular door will not budge. Three fragments must be united to unlock it.");
                return false;
            }
        }
        if (this.roomId.equals("5-B") && direction.equalsIgnoreCase("south")) {
            if (!player.getFlag("buttonPressed")) {
                System.out.println("A solid stone door blocks the southern passage. It doesn’t respond to touch.");
                return false;
            }
        }

        return true;
    }

    public void enter(Player player) {
        System.out.println(description);
        if (!enemies.isEmpty()) {
            System.out.println("Enemies await...");
        }
    }

    public void interactWithObject(String objectName, Player player) {
        for (GameObject obj : objects) {
            if (obj.getName().equalsIgnoreCase(objectName)) {
                obj.interact(player);
                return;
            }
        }
        System.out.println("Nothing here by that name.");
    }

    public void addObject(GameObject obj) {
        objects.add(obj);
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public ArrayList<Enemy> getEnemies() {
    return enemies;
}

    public void addLookTarget(String keyword, String description) {
        lookTargets.put(keyword.toLowerCase(), description);
    }

    public void lookAt(String keyword, Player player) {
        String response = lookTargets.get(keyword.toLowerCase());

        if (this.roomId.equals("5-A") && keyword.equalsIgnoreCase("book")) {
        System.out.println(response);  // The regular description already includes the book crumbling
        System.out.println("You carefully take the diamond-shaped Magical Gem and place it in your pack.");

        // Check to avoid duplicates
        boolean alreadyHasGem = false;
        for (InventoryItem item : player.getInventory()) {
            if (item.getName().equalsIgnoreCase("Magical Gem")) {
                alreadyHasGem = true;
                break;
            }
        }

        if (!alreadyHasGem) {
            player.addItem(new InventoryItem(
                "Magical Gem",
                "A strange, faceted gem pulsing faintly with magical energy.",
                false, false, null, 0, 0));
        }
        return;
    }

        if (response != null) {
            System.out.println(response);
        } else {
            System.out.println("You don't see anything special about that.");
        }
    }


    // Puzzle trigger method
    public void triggerPuzzle(String puzzleId) {
        System.out.println("Puzzle triggered: " + puzzleId);
    }
}