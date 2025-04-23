/*****************************************************************************
 * Name: William Linke
 * Date: 04/23/2025
 * Assignment: Legends of the Earthen Vaults - Week 3 Implementation
 *
 * Controls the main game loop and handles user input routing.
 * This version includes structural logic but omits full loop and DB for now.
 */
import java.util.Scanner;

public class GameEngine {
    private Player player;
    private boolean isRunning;
    private CommandParser parser;

    public GameEngine(Player player) {
        this.player = player;
        this.isRunning = true;
        this.parser = new CommandParser();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(); // Blank line for buffer
        System.out.println("Welcome to Legends of the Earthen Vaults!");
        System.out.println("Type 'quit' to exit the game.");
        System.out.println("You can move or interact with objects in the room.");
        System.out.println("Available commands: move <direction>, interact <object>");
        System.out.println(); // Blank line for buffer
        System.out.println(player.getCurrentRoom().getDescription()); // Show room description

        while (isRunning) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) {
                isRunning = false;
                System.out.println("Thanks for playing!");
            } else {
                processCommand(input);
            }
        }

        scanner.close();
    }

    private void processCommand(String input) {
        String action = parser.getCommandWord(input);
        String target = parser.getCommandTarget(input);

        switch (action.toLowerCase()) {
            case "move":
                player.move(target);
                break;
            case "interact":
                player.getCurrentRoom().interactWithObject(target, player);
                break;
            default:
                System.out.println("Unknown command.");
        }
    }
    // Runtime test case - Test commands: "interact Ancient Chest", "quit"
    public static void main(String[] args) {
        // Create the starting room and exits
        Room startingRoom = new Room("You are in the entrance to a dimly lit cavern. North of you lies the exit into a forest clearing. Paths lead in all other directions. There is an Ancient Chest in the corner.");

        Room southRoom = new Room("You are in a dark tunnel. The walls are damp and the air is musty.");
        Room northRoom = new Room("You are in a forest clearing. The sun is shining and birds are singing.");
        Room eastRoom = new Room("You are in a small cave. The walls are covered in strange markings.");
        Room westRoom = new Room("You are in a narrow passage. The walls are closing in on you.");

        // Set exits from starting room
        startingRoom.addExit("south", southRoom);
        startingRoom.addExit("north", northRoom);
        startingRoom.addExit("east", eastRoom);
        startingRoom.addExit("west", westRoom);

        // Set reverse exits back to starting room
        southRoom.addExit("north", startingRoom);
        northRoom.addExit("south", startingRoom);
        eastRoom.addExit("west", startingRoom);
        westRoom.addExit("east", startingRoom);

        // Create the player in the starting room
        Player player = new Player("Hero", 100, startingRoom);
        
        // Add a simple chest for interaction
        Chest chest = new Chest("Ancient Chest", "A dusty, old wooden chest.", false);
        chest.addItem(new InventoryItem("Healing Potion", "Restores 25 health.", true, false, "", 0, 0));
        startingRoom.addObject(chest);
        
        // Start the game
        GameEngine engine = new GameEngine(player);
        engine.start();
    }
    
}

