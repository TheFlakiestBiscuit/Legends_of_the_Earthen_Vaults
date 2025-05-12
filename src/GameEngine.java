/*****************************************************************************
 * Name: William Linke
 * Date: 05/11/2025
 * Assignment: Legends of the Earthen Vaults - Week 5 Implementation
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
        
        if (player.getFlag("gameComplete") && !action.equalsIgnoreCase("quit")) {
            System.out.println("You have completed your journey. You may type 'quit' to exit the game.");
            return;
        }
        switch (action.toLowerCase()) {
            case "move":
                if (player.getCurrentRoom().getId().equals("10-E") && target.equalsIgnoreCase("south") &&
                    !player.getFlag("vaultUnlocked")) {
                    System.out.println("The triangular vault door blocks your path. Three key fragments are needed to proceed.");
                    break;
                }
                player.move(target);
                if (target.equalsIgnoreCase("forward") &&
                    player.getCurrentRoom().getId().equals("11-B") &&
                    player.hasItem("Starborn Compass") &&
                    player.getFlag("readyToExit")) {
                    System.out.println("You clutch the Starborn Compass as the chamber fades behind you.\n"
                        + "Light gathers beneath your feet, and the world shifts.\n"
                        + "Stone gives way to soft earth, silence to birdsong.\n"
                        + "You emerge into the forest clearing where your journey once began.\n"
                        + "You do not look back. The compass stirs in your palm.\n"
                        + "The next adventure lies ahead.\n\n"
                        + "Your adventure has been recorded.\n"
                        + "Thank you for playing Legends of The Earthen Vaults!");
                    player.setFlag("gameComplete", true);
                    return;
                }
                break;
            case "interact":
                if (player.getCurrentRoom().getId().equals("4-B") &&
                    (target.equalsIgnoreCase("slime") || target.equalsIgnoreCase("monster"))) {
                    
                    if (!player.getCurrentRoom().getEnemies().isEmpty()) {
                        System.out.println("The creature oozes forward, sluggish and menacing. \n"
                            + "You exhale, draw steel, and strike in one fluid motion. \n"
                            + "The slime splits cleanly in two, dissolving into the floor with a sound like boiling water. \n"
                            + "The threat is gone.\n\n"
                            + "The way is now clear. You can return west.");
                        player.getCurrentRoom().getEnemies().clear();
                        // Update the room description to reflect the absence of the slime
                        player.getCurrentRoom().setDescription(
                            "You enter a quiet stone chamber. A pile of broken masonry blocks most of this hall.\n" +
                            "Among the rubble, you make out the shapes of heavily worn statues.\n" +
                            "You can return west."
                        );
                    } else {
                        System.out.println("There is no threat here anymore. The remains of the slime shimmer faintly on the floor.");
                    }
                    return;
                }
                if (player.getCurrentRoom().getId().equals("10-E") &&
                    (target.equalsIgnoreCase("door") || target.equalsIgnoreCase("triangle door"))) {
                    if (!player.hasItem("Triangular Key Fragment A") ||
                        !player.hasItem("Triangular Key Fragment B") ||
                        !player.hasItem("Triangular Key Fragment C")) {
                        System.out.println("You examine the triangular door, but it's missing something. The indentations suggest three pieces are required to open it.");
                    } else if (!player.getFlag("vaultUnlocked")) {
                        System.out.println("You place the three triangular fragments into the door's matching grooves.");
                        System.out.println("With a deep pulse of magic, the pieces vanish and the door trembles. The stone splits diagonally, revealing a large Vault.");
                        player.setFlag("vaultUnlocked", true);
                    } else {
                        System.out.println("The triangular door stands open. The way forward lies clear.");
                    }
                    return;
                }
                if (player.getCurrentRoom().getId().equals("11-B") && target.equalsIgnoreCase("compass") && !player.hasItem("Starborn Compass")) {
                    System.out.println("As your hand nears the compass, the air grows warmer - charged. The hawk-shaped needle halts, pointing directly toward you.\n"
                        + "The compass slowly descends into your waiting hand. For a moment, light arcs across the chamber like starlight pulled into motion.\n"
                        + "The pedestal dims, its purpose fulfilled.\n"
                        + "Far above, beyond the stone and darkness, the stars seem to shift.\n\n"
                        + "You have obtained the Starborn Compass.\n"
                        + "The journey that brought you here has ended - but its direction continues: Forward.\n\n"
                        + "Where would you like to move?");
                    player.addItem(new InventoryItem("Starborn Compass", "A compass bound to your fate.",
                        false, false, null, 0, 0));
                    player.setFlag("readyToExit", true);
                    return;
                }
                if (player.getCurrentRoom().getId().equals("9-A") && target.equalsIgnoreCase("fitting")) {
                    if (!player.hasItem("Magical Crystal")) {
                        System.out.println("You approach the fitting, but have nothing that would fit here.");
                        return;
                    }
                    if (!player.getFlag("fittingActivated")) {
                        System.out.println("You remove the Magical Crystal from your pack and lower it toward the fitting.\n"
                            + "It pulls itself into place, locking perfectly into the crystal-shaped recess.");
                        System.out.println("Bright magical energy courses through the spiral patterns etched in the floor, channeling power into the crystal.");
                        System.out.println("A beam of magical light surges upward, striking the ceiling with a crackling burst.");
                        System.out.println("As the light fades, droplets of magic-infused water begin to drip from the ceiling,\n"
                            + "forming a floating sphere in midair. Inside it, a triangular key fragment begins to shimmer.");
                        player.removeItem("Magical Crystal");
                        player.addItem(new InventoryItem("Triangular Key Fragment B", "One of three ancient key shards.",
                            false, false, null, 0, 0));
                        player.setFlag("fittingActivated", true);
                    } else {
                        System.out.println("The fitting is already filled, and the magical energy has long since dispersed.");
                    }
                    return;
                }
                if (player.getCurrentRoom().getId().equals("5-B") && target.equalsIgnoreCase("recess")) {
                    if (!player.hasItem("Magical Gem")) {
                        System.out.println("The recess is cut with uncanny precision, shaped like a diamond. \n" 
                            + "Faint energy shimmers around the edge, gently tugging at the gem in your pack.");
                        System.out.println("You don't seem to have anything that fits here.");
                        return;
                    }
                    if (!player.getFlag("recessUnlocked")) {
                        System.out.println("You hold out the gem and feel it pull itself into place within the recess.");
                        System.out.println("A low hum fills the room as the wall begins to shift, revealing a hidden chamber to the west.");
                        player.setFlag("recessUnlocked", true);
                    } else {
                        System.out.println("The gem already rests within the recess. The hidden chamber to the west remains open.");
                    }
                    return;
                }
                if (player.getCurrentRoom().getId().equals("5-C") &&
                    (target.equalsIgnoreCase("button") || target.equalsIgnoreCase("stone"))) {
                    if (!player.getFlag("buttonPressed")) {
                        System.out.println("You press the protruding stone. It clicks audibly, and a faint tremor runs through the floor.");
                        System.out.println("Somewhere nearby, stone grinds against stone - a sealed door has opened.");
                        player.setFlag("buttonPressed", true);
                        Room room5B = player.getCurrentRoom().getExit("east");
                        room5B.setDescription(
                            "You enter an engraved hall where glowing symbols shift faintly in the stone.\n"
                            + "On the western wall is a diamond-shaped recess, now dark and inactive.\n"
                            + "The stone around it is smooth and unbroken, save for a thin line running along the floor.\n"
                            + "To the south, the sealed stone door now stands open, revealing a corridor beyond."
                        );
                    } else {
                        System.out.println("The button is already pressed. The stone no longer responds.");
                    }  
                    return;
                }
                if (player.getCurrentRoom().getId().equals("5-C") && target.equalsIgnoreCase("chest")) {
                    if (!player.getFlag("chest5COpened")) {
                        System.out.println("You open the fragile wooden chest. Inside, nestled in old cloth, is a glowing triangular shard.");
                        player.addItem(new InventoryItem("Triangular Key Fragment A", "One of three ancient key shards.",
                            false, false, null, 0, 0));
                        player.setFlag("chest5COpened", true);
                    } else {
                        System.out.println("The chest lies empty, its contents already taken.");
                    }
                    return;
                }
                if (player.getCurrentRoom().getId().equals("8-A") && target.equalsIgnoreCase("chest")) {
                    if (!player.getFlag("chest8AOpened")) {
                        System.out.println("You pry open the chest, the hinges creaking sharply. \n" 
                            + "Inside rests a heavy glass vial filled with a thick, deep red liquid - nearly the color of blood.");
                        System.out.println("You recognize this as a Restoration Potion, far too dangerous to consume unless mortally ill.");
                        player.addItem(new InventoryItem("Restoration Potion", "A thick red potion, only safe in dire moments.",
                            true, false, null, 0, 0));
                        player.setFlag("chest8AOpened", true);
                    } else {
                        System.out.println("The chest is empty, its only treasure already claimed.");
                    }
                    return;
                }
                if (player.getCurrentRoom().getId().equals("8-B") && target.equalsIgnoreCase("crystal")) {
                    if (!player.hasItem("Magical Crystal")) {
                        System.out.println("As you reach toward the glowing crystal, the sigil pulses in response. The crystal lifts slightly, then gently lowers into your hand.");
                        System.out.println("Its surface is cool, but you feel a faint hum of energy pulsing inside.");
                        player.addItem(new InventoryItem("Magical Crystal", "A strange, faceted gem pulsing faintly with magical energy.", false, false, null, 0, 0));
                    } else {
                        System.out.println("The arcane sigil glows faintly, but there's nothing more to take.");
                    }   
                    return;
                }
                if (player.getCurrentRoom().getId().equals("11-A") && target.equalsIgnoreCase("basin")) {
                    if (player.getFlag("basinUsed")) {
                        System.out.println("The basin is quiet. Whatever ritual it once held has already been fulfilled.");
                        return;
                    }
                    if (player.hasItem("Restoration Potion")) {
                        System.out.println("You unstopper the Restoration Potion and pour it into the basin.");
                        System.out.println("The liquid pulses with unnatural light, spreading across the runes.");
                        System.out.println("A blinding flash fills the room - and then silence.");
                        System.out.println("In the basin's center now rests a glowing triangular key fragment.");
                        player.removeItem("Restoration Potion");
                        player.addItem(new InventoryItem("Triangular Key Fragment C", "One of three ancient key shards.",
                            false, false, null, 0, 0));
                        player.setFlag("basinUsed", true);
                    } else {
                        System.out.println("You approach the basin, but have nothing suitable to offer.");
                    }
                    return;
                }
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
                if (player.getCurrentRoom().getId().equals("10-E") &&
                    target.equalsIgnoreCase("fragments")) {
                    if (!player.hasItem("Triangular Key Fragment A") ||
                        !player.hasItem("Triangular Key Fragment B") ||
                        !player.hasItem("Triangular Key Fragment C")) {
                        System.out.println("You examine the triangular door, but it's missing something. The indentations suggest three pieces are required to open it.");
                    } else if (!player.getFlag("vaultUnlocked")) {
                        System.out.println("You place the three triangular fragments into the door's matching grooves.");
                        System.out.println("With a deep pulse of magic, the pieces vanish and the door trembles. The stone splits diagonally, revealing a large Vault.");
                        player.setFlag("vaultUnlocked", true);
                    } else {
                        System.out.println("The triangular door stands open. The way forward lies clear.");
                    }
                    break;
                }
                if (toUse == null) {
                    System.out.println("You don't have that item.");
                    break;
                }
                if (!toUse.isUsable()) {
                    System.out.println("You can't use that.");
                    break;
                }
                if (player.getCurrentRoom().getId().equals("11-B") &&
                    toUse.getName().equalsIgnoreCase("Starborn Compass") &&
                    !player.getFlag("readyToExit")) {
                    System.out.println("As your hand nears the compass, the air grows warmer - charged. The hawk-shaped needle halts, pointing directly toward you.\n"
                        + "The compass slowly descends into your waiting hand. For a moment, light arcs across the chamber like starlight pulled into motion.\n"
                        + "The pedestal dims, its purpose fulfilled.\n"
                        + "Far above, beyond the stone and darkness, the stars seem to shift.\n\n"
                        + "You have obtained the Starborn Compass.\n"
                        + "The journey that brought you here has ended - but its direction continues: Forward.\n\n"
                        + "Where would you like to move?");
                    player.setFlag("readyToExit", true);
                    break;
                }
                if (player.getCurrentRoom().getId().equals("11-A") &&
                    toUse.getName().equalsIgnoreCase("Restoration Potion") &&
                    !player.getFlag("basinUsed")) {
                    System.out.println("You pour the Restoration Potion into the basin.");
                    System.out.println("The runes ignite with golden light, and the air vibrates with power.");
                    System.out.println("As the glow fades, a triangular key fragment appears at the basin's center.");
                    player.removeItem("Restoration Potion");
                    player.addItem(new InventoryItem("Triangular Key Fragment C", "One of three ancient key shards.",
                        false, false, null, 0, 0));
                    player.setFlag("basinUsed", true);
                    return;
                }
                if (player.getCurrentRoom().getId().equals("9-A") &&
                    toUse.getName().equalsIgnoreCase("Magical Crystal") &&
                    !player.getFlag("fittingActivated")) {
                    System.out.println("You remove the Magical Crystal from your pack and lower it toward the fitting.\n"
                        + "It pulls itself into place, locking perfectly into the crystal-shaped recess.");
                    System.out.println("Bright magical energy courses through the spiral patterns etched in the floor, channeling power into the crystal.");
                    System.out.println("A beam of magical light surges upward, striking the ceiling with a crackling burst.");
                    System.out.println("As the light fades, droplets of magic-infused water begin to drip from the ceiling,\n"
                        + "forming a floating sphere in midair. Inside it, a triangular key fragment begins to shimmer.");
                    player.removeItem("Magical Crystal");
                    player.addItem(new InventoryItem("Triangular Key Fragment B", "One of three ancient key shards.",
                        false, false, null, 0, 0));
                    player.setFlag("fittingActivated", true);
                    return;
                }
                if (toUse.getName().equalsIgnoreCase("Healing Potion")) {
                    int healAmount = 25;
                    int currentHealth = player.getHealth();
                    player.setHealth(currentHealth + healAmount);
                    player.getInventory().remove(toUse);
                    System.out.println("You used the Healing Potion. You feel rejuvenated!");
                } else {
                    System.out.println("You used the " + toUse.getName() + ", but nothing seemed to happen.");
                }
                break;
            case "look":
                if (target == null || target.isEmpty()) {
                    System.out.println("Look at what?");
                } else {
                    player.getCurrentRoom().lookAt(target, player);
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
        Room room0A = new Room("0-A", "You stand in a quiet forest clearing. \n"
            + "The light filters gently through the trees, and birdsong fills the air.\n" 
            + "A stone path leads south, where the entrance to a shadowed cave yawns beneath mossy rocks.");
        room0A.addLookTarget("forest", "The trees sway gently in the breeze. Dappled light plays across the mossy ground.");
        room0A.addLookTarget("cave", "The cave yawns beneath mossy rocks. Your quest has brought you here.");
        // Damp cave region (columns 1–3)
        Room room1A = new Room("1-A", "You enter the cave mouth. The air turns cool and damp, and moss clings to the stone walls.\n" 
            + "A trickle of water echoes in the distance.\n" 
            + "Paths lead north back to the forest, east and west into side passages, and deeper south into the cave.");
        Room room1B = new Room("1-B", "This narrow tunnel veers off from the main cave.\n" 
            + "Cracks in the wall reveal thin roots reaching for moisture.\n" 
            + "The only exit is west.");
        Room room1C = new Room("1-C", "This low alcove smells of mildew.\n" 
            + "An age-worn Dirty Chest rests beneath a large broken root.\n" 
            + "The only way out is east, toward the larger chamber.");
        room1C.addLookTarget("chest", "The chest is weathered and stained, its metal bands corroded. It may still open.");
        Room room2A = new Room("2-A", "You follow the winding passage deeper into the cave.\n" 
            + "A small underground stream trickles along the edge of this chamber.\n" 
            + "Water drips rhythmically from the ceiling.\n" 
            + "There is a narrow passage leading east.");
        room2A.addLookTarget("stream", "The narrow stream trickles along the wall, vanishing eastward into the next chamber.");
        Room room2B = new Room("2-B", "You arrive at a shallow, moss-ringed pool fed by a steady trickle of water.\n" 
            + "The air is cooler here.\n" 
            + "The only path continues south.");
        room2B.addLookTarget("pool", "The pool gleams with cold water. A faint current moves beneath the surface.");
        Room room3A = new Room("3-A", "You descend into a large stone chamber where the natural cave gives way to worn architecture.\n" 
            + "Crumbling tiles line the floor, and carved pillars flank a large stone archway.\n" 
            + "To either side, the cave walls shift from jagged rock to the chiseled stone of an ancient structure.\n" 
            + "The only way forward lies south, through the arch.");
        room3A.addLookTarget("arch", "The archway is worn by time, its carvings nearly faded. Still, it stands firm.");
            // Ancient ruins region (columns 4+)
        Room room4A = new Room("4-A", "You step into a grand corridor, its high ceiling supported by ancient columns.\n" 
            + "The stonework hums faintly beneath your boots.\n" 
            + "Faintly glowing crystal sconces line the walls, their soft light casting wavering shadows.\n" 
            + "The path branches to a chamber in the east, and a long, branching hallway to the west.");
        room4A.addLookTarget("sconces", "Each sconce holds a glowing crystal. Their soft hum calms your nerves.");
        Room room4B = new Room("4-B", "You enter a quiet stone chamber. A pile of broken masonry blocks most of this hall.\n"
            + "Among the rubble, you make out the shapes of heavily worn statues.\n"
            + "From the cracks between the rubble, a slime monster oozes forward, drawn to your presence.");
        room4B.addLookTarget("statues", "The statues are worn beyond recognition. Their eyes seem to follow you faintly.");
        room4B.addEnemy(new Enemy("Slime Monster", 20, 5, true));
        Room room4C = new Room("4-C", "This gallery bears faded murals and damaged statues along its cracked walls.\n"
            + "Flickering light leaks in from above, casting distorted shadows.\n"
            + "A worn wooden door to the south stands slightly ajar, its ornate carvings cracked and weathered by time. \n" 
            + "To the west, the corridor continues into shadow.");
        room4C.addLookTarget("murals", "Faded images of lost cultures cover the walls. They evoke a forgotten struggle.");
        Room room4D = new Room("4-D", "Dust hangs thick in the air. Stone benches line the walls,\n"
            + "and fragments of carved tablets are scattered across the floor. \n"
            + "A strange aura radiates faintly from the southern passage, tugging at your awareness.\n"
            + "A narrow passage continues south. To the east is the damaged gallery.");
        room4D.addLookTarget("tablets", "The tablets are cracked and scattered, their inscriptions too faint to decipher.");
        room4D.addLookTarget("fragments", "The tablets are cracked and scattered, their inscriptions too faint to decipher.");
        Room room5A = new Room("5-A", "You step into a crumbling library. Ancient shelves line the walls, their contents long decayed.\n"
            + "The air is thick with dust, and pages from ruined tomes litter the floor.\n"
            + "In the center of the room sits a thick, unusually pristine book atop a spotless stone lectern - far too intact for its age.\n"
            + "The only visible exit is north.");
        room5A.addLookTarget("book", "The book looks untouched. As you open it, you discover a recess inside containing a diamond-shaped gem.\n" + ""
            + "Below it, a page reads: 'Seek the wall that echoes the gem's shape.'\nAs you close it, the book crumbles to dust. \n");
        Room room5B = new Room("5-B", "You enter an engraved hall where glowing symbols shift faintly in the stone.\n"
            + "On the western wall is a diamond-shaped recess, pulsing softly - as if awaiting something.\n"
            + "The stone around it is smooth and unbroken, save for a thin line running along the floor.\n"
            + "To the south stands a large, sealed stone door.");
        room5B.addLookTarget("recess", "The recess is cut with uncanny precision, shaped like a diamond. \n" 
            + "Faint energy shimmers around the edge, gently tugging at the gem in your pack.");
        room5B.addLookTarget("wall", "The recess is cut with uncanny precision, shaped like a diamond. \n" 
            + "Faint energy shimmers around the edge, gently tugging at the gem in your pack.");
        Room room5C = new Room("5-C", "You step into a narrow, quiet vault carved into the stone.\n"
            + "At the center of the room is a raised platform with a protruding stone set into the western wall.\n"
            + "A small chest lies in the opposite corner, half-covered in dust.\n"
            + "The only exit is east, back into the hall.");
        room5C.addLookTarget("chest", "A small wooden chest sits in the corner, half-covered in dust. Its lid looks fragile but intact.");
        room5C.addLookTarget("stone", "The stone protrudes slightly from the wall - worn smooth from time or purpose.");
        room5C.addLookTarget("button", "The stone protrudes slightly from the wall - worn smooth from time or purpose.");
        Room room6A = new Room("6-A", "The floor here is uneven, and sections of ancient tiling have slipped out of alignment.\n"
            + "Strange carvings on the walls appear to shift when viewed from the corner of your eye.\n"
            + "To the north lies the engraved hall. The path continues south.");
        room6A.addLookTarget("carvings", "The carvings shimmer faintly. You recognize some of the runes from earlier rooms.");
        Room room7A = new Room("7-A", "The hallway narrows here, and the air feels denser. \n"
            + "Chunks of stone are strewn across the floor where parts of the ceiling have fallen.\n"
            + "A large crack splits the eastern wall, and the faint sound of something dripping echoes faintly from below.\n"
            + "The path leads further south.");
        Room room7B = new Room("7-B", "This fractured chamber appears to have collapsed partially in the past.\n"
            + "Jagged stone juts from the walls, and one section has caved inward, creating a narrow gap to the east.\n"
            + "You can continue through the gap.");
        room7B.addLookTarget("gap", "The gap looks strangely formed. Something massive must have forced its way through.");
        Room room7C = new Room("7-C", "Faded carvings decorate the walls of this narrow junction.\n"
            + "Dust clings to the carvings, undisturbed.\n"
            + "Faint glyphs mark an archway to the east.");
        room7C.addLookTarget("glyphs", "The glyphs etched into the arch resemble the ancient runes you've seen before. \n" 
            + "Their shapes are distorted by age, but their power lingers.");
        Room room7D = new Room("7-D", "The air changes subtly as you enter this chamber.\n"
            + "Constellations are etched into the stone ceiling above.\n" 
            + "One constellation pulses faintly as you pass beneath it.\n"
            + "To the south lies another chamber.");
        room7D.addLookTarget("constellation", "As you gaze up, one constellation pulses faintly - the Ivory Hawk.\n" 
            + "It is the sign under which you were born, tied to great wanderers and fated journeys.\n" 
            + "The stars feel nearer than they should, as though they await your next step.");
        Room room8A = new Room("8-A", "You enter a tall, narrow hall with alcoves lining both sides.\n"
            + "Faint whispering seems to echo from within the walls, though no source is visible.\n"
            + "A small chest rests inside one of the alcoves, half-shrouded in shadow.\n"
            + "An arched opening lies to the east.");
        room8A.addLookTarget("chest", "The small chest nestled in the alcove is aged but intact. Its lid creaks when disturbed.");
        Room room8B = new Room("8-B", "The floor here is inscribed with an intricate arcane sigil, its grooves softly illuminated.\n"
            + "In the center rests a Magical Crystal, hovering just above the surface as if suspended by unseen energy.\n"
            + "The air tingles with latent power, though something about the room feels incomplete.\n"
            + "The path continues east.");
        room8B.addLookTarget("sigil", "The arcane sigil on the floor glows softly. \n" 
            + "At its center, the Magical Crystal pulses with gentle light.");
        Room room8C = new Room("8-C", "A long corridor stretches into darkness.\n"
            + "The walls appear to move ever so slightly when not directly observed.\n"
            + "You feel a presence watching.\n"
            + "A narrow staircase descends to the north.");
        Room room8D = new Room("8-D", "The room opens to a crumbling stone balcony.\n"
            + "A deep abyss stretches beyond the edge, and a strange wind howls upward from the void.\n"
            + "The only way forward is a southern doorway.");
        Room room9A = new Room("9-A", "You descend into a vast, vaulted chamber carved with geometric precision. \n" 
            + "The walls bear symmetrical patterns that spiral inward toward the center of the room. \n"
            + "A heavy silence presses on your ears, broken only by the rhythmic drip of water somewhere above.\n"
            + "On the floor lies a shallow fitting, shaped to hold something precise - likely a crystal.\n"
            + "The passage continues west.");
        room9A.addLookTarget("patterns", "The spiral patterns shift subtly as you watch them. Focusing too long makes your vision swim.");
        room9A.addLookTarget("center", "A geometric-shaped fitting sits embedded in the floor, surrounded by concentric runes. \n" 
            + "It looks like something could fit inside.");
        Room room9B = new Room("9-B", "You find yourself in a chamber where the walls are lined with cracked runestones. \n"
            + "The symbols flicker faintly when approached, pulsing with subtle energy.\n"
            + "The path continues south, where you hear an occasional thud.");
        room9B.addLookTarget("runestones", "As you study the runestones, the symbols pulse gently, reacting to your presence.\n" 
            + "Though you cannot decipher them, you feel a rhythm - like an ancient spell awakening.");
        Room room9C = new Room("9-C", "This narrow room distorts your senses. \n"
            + "The sound of your footsteps arrives out of sync, and the walls seem to shimmer faintly.\n"
            + "Something once powerful passed through here, and the air still recoils from its presence.\n"
            + "The only way out is to the south.");
        Room room10A = new Room("10-A", "Massive stone gears and worn levers protrude from the walls. \n"
            + "Some still twitch as if trying to move. \n"
            + "Symbols on the floor form a pattern that's been scuffed and obscured. \n"
            + "You can continue west or return north.");
        room10A.addLookTarget("gears", "The massive gears twitch and groan as if trying to move. One appears half-broken.");
        room10A.addLookTarget("levers", "Most of the levers are jammed or cracked. \n" 
            + "Whatever they once controlled is long lost.");
        Room room10B = new Room("10-B", "The corridor tilts downward, the air growing heavier with each step. \n"
            + "The dust here is undisturbed, blanketing the floor in a thick, silent layer. \n"
            + "The walls seem to close in as the passage narrows. You may travel west.");
        Room room10C = new Room("10-C", "Tattered banners hang limp in the still air.\n"
            + "The shattered remains of ceremonial relics crunch underfoot.\n"
            + "This room feels tense, as if something once watched from the shadows.\n"
            + "An imposing, large wooden door stands behind a lectern to the south.\n"
            + "You can travel through a western arch or through the door.");
        room10C.addLookTarget("banners", "The tattered banners display symbols of an ancient order - possibly a forgotten cult.\n" 
            + "Time has faded most of their meaning, but the designs still hold a strange reverence.");
        Room room10D = new Room("10-D", "A jagged split runs across the floor, revealing glowing crystal formations.\n"
            + "Faint pulses of light escape from deep within the crystals, illuminating fragmented carvings on the walls.\n"
            + "You may continue through the western corridor.");
        room10D.addLookTarget("crystals", "The crystals glow in a slow, pulsing rhythm - their colors shifting in the light.");
        room10D.addLookTarget("carvings", "The wall carvings are too damaged to interpret, but they may have once been decorative.");
        Room room10E = new Room("10-E", "Energy hums through the stone in this chamber.\n"
            + "It feels like everything beneath the ruins connects here.\n"
            + "Strange echoes ripple through the air.\n"
            + "At the southern end of the room stands a massive triangular door, carved from a single slab of obsidian-like stone.\n"
            + "Three distinct indentations mark its surface - each shaped to hold a fragment of a triangular key.\n"
            + "A small doorway lies to the north.");
        room10E.addLookTarget("door", "The massive triangular stone door is etched with three recesses \n" 
            + "- each shaped to fit a shard of an ancient key. \n" 
            + "It radiates a subtle magical energy, dormant until completed.");
        Room room11A = new Room("11-A", "You pass through the heavy wooden door into a still, circular room.\n"
            + "A shallow basin rests atop a pedestal in the center of the chamber.\n"
            + "The air is motionless, and the walls bear mirrored inlays that reflect your image in faint, distorted ripples.\n"
            + "A presence lingers here - not hostile, but ancient and watchful.\n"
            + "The only way out is north.");
        room11A.addLookTarget("basin", "The basin's rim is etched with shifting runes. \n" 
            + "As you focus, the markings subtly realign, revealing a message:\n" 
            + "'Upon this altar, offer restoration to the omnipotent.'");
        Room room11B = new Room("11-B", "You step into the heart of the Vault, a chamber untouched by time. \n"
            + "Obsidian tiles gleam beneath your feet, and a short, gold-trimmed staircase rises to a raised platform at the far end.\n"
            + "Atop it rests a stark-white pedestal resembling carved ivory, smooth and unblemished.\n"
            + "Hovering just above it is a radiant object: a silver-framed compass with a hawk-shaped needle gliding in slow, deliberate arcs.\n"
            + "Beneath the needle, the lines of the Ivory Hawk constellation are etched into its face - unmistakable, and waiting.");
        room11B.addLookTarget("compass", "The compass hovers in still air, its delicate frame untouched by dust or time. \n" 
            + "The needle moves in smooth circles, unbound by cardinal directions.\n" 
            + "Below it, etched into the surface, the constellation of the Ivory Hawk gleams - the very sign you were born under. \n" 
            + "You feel as though the artifact recognizes you.");

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
        Chest chest = new Chest("chest", "A wooden and very worn, Dirty Chest. It creaks loudly when disturbed.", false);
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
