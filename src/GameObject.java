/*****************************************************************************
 * 
 * Date: 05/11/2025
 * Legends of the Earthen Vaults - Week 5 Implementation
 *
 * Abstract base class for all interactive objects in rooms (e.g., chests, puzzles).
 */
public abstract class GameObject implements Interactable {
    protected String name;
    protected String description;

    public GameObject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void interact(Player player); // Defined by subclasses
}
