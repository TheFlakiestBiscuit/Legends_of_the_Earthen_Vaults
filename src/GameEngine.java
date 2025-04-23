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
        System.out.println("Welcome to Legends of the Earthen Vaults!");

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
}
