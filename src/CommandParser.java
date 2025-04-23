/*****************************************************************************
 * Name: William Linke
 * Date: 04/23/2025
 * Assignment: Legends of the Earthen Vaults - Week 3 Implementation
 *
 * Parses user input into actions and targets for the game engine.
 */
public class CommandParser {

    public String getCommandWord(String input) {
        String[] parts = input.trim().split(" ", 2);
        return parts.length > 0 ? parts[0] : "";
    }

    public String getCommandTarget(String input) {
        String[] parts = input.trim().split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    public boolean isValidCommand(String input) {
        String command = getCommandWord(input).toLowerCase();
        return command.equals("move") || command.equals("interact") || command.equals("quit");
    }
}
