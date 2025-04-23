/*****************************************************************************
 * Name: William Linke
 * Date: 04/23/2025
 * Assignment: Legends of the Earthen Vaults - Week 3 Implementation
 *
 * Represents a unique puzzle that may unlock a room or reveal an object.
 */
public class Puzzle extends GameObject {
    private String solution;
    private boolean isSolved;
    private boolean unlocksSomething;
    private String unlockTargetId;
    private String type; // Optional: e.g., "riddle", "sequence"

    public Puzzle(String name, String description, String solution,
            boolean unlocksSomething, String unlockTargetId, String type) {
        super(name, description);
        this.solution = solution;
        this.isSolved = false;
        this.unlocksSomething = unlocksSomething;
        this.unlockTargetId = unlockTargetId;
        this.type = type;
    }

    @Override
    public void interact(Player player) {
        if (isSolved) {
            System.out.println("You've already solved this puzzle.");
            return;
        }

        System.out.println("Puzzle: " + description);
        System.out.print("Your answer: ");

        @SuppressWarnings("resource")
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase(solution)) {
            isSolved = true;
            System.out.println("Correct! Something happens...");
            // Room/chest state change handled externally in GameEngine
        } else {
            System.out.println("That's not quite right...");
        }
    }

    public boolean isSolved() {
        return isSolved;
    }

    public boolean unlocksSomething() {
        return unlocksSomething;
    }

    public String getUnlockTargetId() {
        return unlockTargetId;
    }

    public String getType() {
        return type;
    }
}
