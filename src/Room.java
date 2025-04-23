/*****************************************************************************
 * Name: William Linke
 * Date: 04/23/2025
 * Assignment: Legends of the Earthen Vaults - Week 3 Implementation
 *
 * Represents a room in the dungeon with exits, enemies, and interactable objects.
 */
import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private String description;
    private HashMap<String, Room> exits;
    private ArrayList<Enemy> enemies;
    private ArrayList<GameObject> objects;

    public Room(String description) {
        this.description = description;
        this.exits = new HashMap<>();
        this.enemies = new ArrayList<>();
        this.objects = new ArrayList<>();
    }
    public String getDescription() {
        return description;
    }
    

    public void addExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
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
}
