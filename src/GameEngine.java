/*****************************************************************************
 * Name: William Linke
 * Date: 05/01/2025
 * Assignment: Legends of the Earthen Vaults - Week 4 Implementation
 *
 * Controls the main game loop and manages database-backed player data.
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameEngine {
    private Player player;
    private boolean isRunning;
    private CommandParser parser;
    private Scanner scanner;

    public GameEngine(Player player, Scanner scanner) {
        this.player = player;
        this.scanner = scanner;
        this.isRunning = true;
        this.parser = new CommandParser();
    }

    public void start() {
        System.out.println();
        System.out.println("Welcome to Legends of the Earthen Vaults!");
        System.out.println(player.getCurrentRoom().getDescription());

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
            case "use":
                InventoryItem toUse = null;
                for (InventoryItem item : player.getInventory()) {
                    if (item.getName().equalsIgnoreCase(target)) {
                        toUse = item;
                        break;
                    }
                }
    
                if (toUse == null) {
                    System.out.println("You don't have that item.");
                    break;
                }
    
                if (!toUse.isUsable()) {
                    System.out.println("You can't use that.");
                    break;
                }
    
                if (toUse.getName().equalsIgnoreCase("Healing Potion")) {
                    int healAmount = 25;
                    int currentHealth = player.getHealth();
                    player.setHealth(currentHealth + healAmount);
                    player.getInventory().remove(toUse);
                    System.out.println("You used the Healing Potion. You feel rejuvenated! Health +25.");
                } else {
                    System.out.println("You used the " + toUse.getName() + ", but nothing seemed to happen.");
                }
                break;
            case "inventory":
                List<InventoryItem> inventory = player.getInventory();
                if (inventory.isEmpty()) {
                    System.out.println("Your inventory is empty.");
                } else {
                    System.out.println("You are carrying:");
                    for (InventoryItem item : inventory) {
                        System.out.println("- " + item.getName() + ": " + item.getDescription());
                    }
                }
                break;
            default:
                System.out.println("Unknown command.");
        }
    }

    public static void main(String[] args) {
        GameDataManager db = new GameDataManager();
        db.connect();

        // Create rooms with IDs
        // Forest entrance
        Room room0A = new Room("0-A", "You stand in a quiet forest clearing. The light filters gently through the trees, and birdsong fills the air.\nA stone path leads south, where the entrance to a shadowed cave yawns beneath mossy rocks.");

        // Damp cave region (columns 1–3)
        Room room1A = new Room("1-A", "You enter the cave mouth. The air turns cool and damp, and moss clings to the stone walls. \nA trickle of water echoes in the distance.\nPaths lead north back to the forest, east and west into side passages, and deeper south into the cave.");
        Room room1B = new Room("1-B", "This narrow tunnel veers off from the main cave. \nCracks in the wall reveal thin roots reaching for moisture.\nThe only exit is west.");
        Room room1C = new Room("1-C", "This low alcove smells of mildew. \nAn age-worn Dirty Chest rests beneath a large broken root. \nThe only way out is east, toward the larger chamber.");
        Room room2A = new Room("2-A", "A small underground stream trickles along the edge of this chamber. \nWater drips rhythmically from the ceiling. \nThere is a narrow passage leading east.");
        Room room2B = new Room("2-B", "You arrive at a shallow, moss-ringed pool fed by a steady trickle of water. \nThe air is cooler here. \nThe only path continues south.");
        Room room3A = new Room("3-A", "You descend into a large stone chamber where the natural cave gives way to worn architecture.\nCrumbling tiles line the floor, and carved pillars flank a large stone archway.\nThe only way forward lies south, through the arch.");

        // Ancient ruins region (columns 4+)
        Room room4A = new Room("4-A", "You step into a grand corridor, its high ceiling supported by ancient columns. \nThe stonework hums faintly beneath your boots. \nA sealed doorway lies to the north, with branching passages east and west.");
        Room room4B = new Room("4-B", "A pile of broken masonry blocks most of this hall. \nAmong the rubble, you make out the shapes of heavily worn statues. \nYou can return west.");
        Room room4C = new Room("4-C", "This gallery bears faded murals and damaged statues along its cracked walls. \nFlickering light leaks in from above, casting distorted shadows.\nExits lead east, west, and south.");
        Room room4D = new Room("4-D", "Dust hangs thick in the air. Stone benches line the walls, \nand fragments of carved tablets are scattered across the floor.\nA narrow passage continues south. To the east is the damaged gallery.");
        Room room5A = new Room("5-A", "You enter a decaying library, its shelves long empty and the scent of mildew thick in the air. \nToward the back of the room stands a spotless stone lectern, oddly untouched by the decay. \nAtop the lectern rests a thick book - strangely new, and completely out of place.\nThe only visible exit is north.");
        Room room5B = new Room("5-B", "You enter an engraved hall where glowing symbols shift faintly in the stone. \nOn the western wall is a diamond-shaped recess, pulsing softly — as if awaiting something.\nTo the south stands a large, sealed stone door.");
        Room room5C = new Room("5-C", "This quiet vault is bare, save for a small platform built into the western wall. \nA single stone juts out slightly from its surface, worn from centuries of contact — or intent. \nA small chest lies on the floor in the corner across from the platform. \nThe only way out is east.");
        Room room6A = new Room("6-A", "The floor here is uneven, and sections of ancient tiling have slipped out of alignment. \nStrange carvings on the walls appear to shift when viewed from the corner of your eye. \nTo the north lies the engraved hall. The path continues south.");
        Room room7A = new Room("7-A", "You step into a hollow stone corridor lined with shallow alcoves. \nThe air feels heavier here, and the silence is nearly complete. \nThe path leads further south.");
        Room room7B = new Room("7-B", "This fractured chamber appears to have collapsed partially in the past. Jagged stone juts from the walls. \nOne section has caved inward, creating a narrow gap to the east.\nYou can go through the gap or back to the southern hall.");
        Room room7C = new Room("7-C", "Faded carvings decorate the walls of this narrow junction. \nDust clings to the carvings, undisturbed. \nFaint glyphs mark an archway to the east.");
        Room room7D = new Room("7-D", "The air changes subtly as you enter this chamber. \nConstellations are etched into the stone ceiling above. They glow faintly as you pass beneath them.\nTo the south lies another chamber.");
        Room room8A = new Room("8-A", "You enter a tall, narrow hall with alcoves lining both sides. \nFaint whispering seems to echo from within the walls, though no source is visible.\n An arched opening lies to the east.");
        Room room8B = new Room("8-B", "The floor here is inscribed with an intricate arcane sigil, its grooves softly illuminated. \nThe air tingles with latent energy. \nYou may continue east.");
        Room room8C = new Room("8-C", "A long corridor stretches into darkness. \nThe walls appear to move ever so slightly when not directly observed. \nYou feel a presence watching. \nA narrow staircase rises to the north.");
        Room room8D = new Room("8-D", "The room opens to a crumbling stone balcony. \nA deep abyss stretches beyond the edge, and a strange wind howls upward from the void. \nThe only way forward is a southern doorway.");
        Room room9A = new Room("9-A", "Dust swirls across the floor of this vaulted landing. \nAn archway to the east is broken, with thick roots curling through cracks in the stone. \nYou can continue west.");
        Room room9B = new Room("9-B", "You find yourself in a chamber where the walls are lined with cracked runestones. \nThe symbols flicker faintly when approached.\nThe path continues south, where you hear an occasional thud.");
        Room room9C = new Room("9-C", "This chamber is partially collapsed. \nRubble and twisted metal clutter the floor, making traversal slow. \nFaint light trickles in from above, illuminating a broken lever on the wall. \nThe only way out is to the south.");
        Room room10A = new Room("10-A", "Massive stone gears and worn levers protrude from the walls. \nSome still twitch as if trying to move. \nSymbols on the floor form a pattern that's been scuffed and obscured. \nYou can continue west.");
        Room room10B = new Room("10-B", "This dim corridor descends into the deep. \nDust thickens as you move, and the walls narrow to a crawl space. \nYou may travel west.");
        Room room10C = new Room("10-C", "Tattered banners hang limp in the still air. \nThe shattered remains of ceremonial relics crunch underfoot. \nThis room feels tense — as if something once watched from the shadows. \nAn imposing, large wooden door stands behind a lectern to the south \nYou can travel through a western arch or through the door.");
        Room room10D = new Room("10-D", "A jagged split runs across the floor, revealing glowing crystal formations. \nFaint pulses of light escape from deep within the crystals, illuminating fragmented carvings on the walls. \nYou may continue through the western corridor.");
        Room room10E = new Room("10-E", "Energy hums through the stone in this chamber. \nIt feels like everything beneath the ruins connects here - and something below still lives. \nStrange echoes ripple through the air. \nGigantic stone doors lie to the south, with no visible way to open them. \nA small doorway lies to the north.");
        Room room11A = new Room("11-A", "Massive doors of rusted iron guard the shelves of forgotten knowledge. \nA single gate stands ajar, leading to a darkened cell. \nThe only path lies north.");
        Room room11B = new Room("11-B", "This chamber is eerily quiet. \nIn the center stands a cracked pedestal that hums faintly, as though reacting to your presence. \nYou feel a decision must be made here. \nThe only exit is to the north.");

        // Add rooms to the map
        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put("0-A", room0A);
        roomMap.put("1-A", room1A);
        roomMap.put("1-B", room1B);
        roomMap.put("1-C", room1C);
        roomMap.put("2-A", room2A);
        roomMap.put("2-B", room2B);
        roomMap.put("3-A", room3A);
        roomMap.put("4-A", room4A);
        roomMap.put("4-B", room4B);
        roomMap.put("4-C", room4C);
        roomMap.put("4-D", room4D);
        roomMap.put("5-A", room5A);
        roomMap.put("5-B", room5B);
        roomMap.put("5-C", room5C);
        roomMap.put("6-A", room6A);
        roomMap.put("7-A", room7A);
        roomMap.put("7-B", room7B);
        roomMap.put("7-C", room7C);
        roomMap.put("7-D", room7D);
        roomMap.put("8-A", room8A);
        roomMap.put("8-B", room8B);
        roomMap.put("8-C", room8C);
        roomMap.put("8-D", room8D);
        roomMap.put("9-A", room9A);
        roomMap.put("9-B", room9B);
        roomMap.put("9-C", room9C);
        roomMap.put("10-A", room10A);
        roomMap.put("10-B", room10B);
        roomMap.put("10-C", room10C);
        roomMap.put("10-D", room10D);
        roomMap.put("10-E", room10E);
        roomMap.put("11-A", room11A);
        roomMap.put("11-B", room11B);

        // Link exits between rooms
        room1A.addExit("north", room0A);
        room0A.addExit("south", room1A);
        room1A.addExit("east", room1B);
        room1A.addExit("west", room1C);
        room1C.addExit("east", room1A);
        room1B.addExit("west", room1A);
        room1A.addExit("south", room2A);
        room2A.addExit("north", room1A);
        room2A.addExit("east", room2B);
        room2B.addExit("west", room2A);
        room2B.addExit("south", room3A);
        room3A.addExit("north", room2B);
        room3A.addExit("south", room4A);
        room4A.addExit("north", room3A);
        room4A.addExit("east", room4B);
        room4B.addExit("west", room4A);
        room4A.addExit("west", room4C);
        room4C.addExit("east", room4A);
        room4C.addExit("south", room5A);
        room5A.addExit("north", room4C);
        room4C.addExit("west", room4D);
        room4D.addExit("east", room4C);
        room4D.addExit("south", room5B);
        room5B.addExit("north", room4D);
        room5B.addExit("west", room5C);
        room5C.addExit("east", room5B);
        room5B.addExit("south", room6A);
        room6A.addExit("north", room5B);
        room6A.addExit("south", room7A);
        room7A.addExit("north", room6A);
        room7A.addExit("south", room8A);
        room8A.addExit("north", room7A);
        room8A.addExit("east", room8B);
        room8B.addExit("west", room8A);
        room8B.addExit("east", room8C);
        room8C.addExit("west", room8B);
        room8C.addExit("north", room7B);
        room7B.addExit("south", room8C);
        room7B.addExit("east", room7C);
        room7C.addExit("west", room7B);
        room7C.addExit("east", room7D);
        room7D.addExit("west", room7C);
        room7D.addExit("south", room8D);
        room8D.addExit("north", room7D);
        room8D.addExit("south", room9A);
        room9A.addExit("north", room8D);
        room9A.addExit("west", room9B);
        room9B.addExit("east", room9A);
        room9B.addExit("south", room10A);
        room10A.addExit("north", room9B);
        room10A.addExit("west", room10B);
        room10B.addExit("east", room10A);
        room10B.addExit("west", room10C);
        room10C.addExit("east", room10B);
        room10C.addExit("south", room11A);
        room11A.addExit("north", room10C);
        room10C.addExit("west", room10D);
        room10D.addExit("east", room10C);
        room10D.addExit("west", room10E);
        room10E.addExit("east", room10D);
        room10E.addExit("north", room9C);
        room9C.addExit("south", room10E);
        room10E.addExit("south", room11B);
        room11B.addExit("north", room10E);


        // Use shared scanner
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to load a saved game? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        Player player;
        if (response.equals("yes")) {
            player = db.loadPlayer("Hero", roomMap);
            if (player == null) {
                System.out.println("No save file found. Creating a new player.");
                player = new Player("Hero", 100, room0A);

            }
        } else {
            player = new Player("Hero", 100, room0A);
            System.out.println("New player created.");
        }

        // Add a chest to room 1-C
        Chest chest = new Chest("Dirty Chest", "A wooden and very worn, Dirty Chest. It creaks loudly when disturbed.", false);
        chest.addItem(new InventoryItem("Healing Potion", "Restores 25 health.", true, false, "", 0, 0));
        room1C.addObject(chest);

        // Start the game
        GameEngine engine = new GameEngine(player, scanner);
        engine.start();

        // Save game state and close DB
        db.savePlayer(player);
        db.close();
        scanner.close();
    }
}
