/*****************************************************************************
 * Name: William Linke
 * Date: 05/11/2025
 * Assignment: Legends of the Earthen Vaults - Week 5 Implementation
 *
 * Manages SQLite database operations using JDBC.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class GameDataManager {
    private static final String DB_URL = "jdbc:sqlite:earthen_vaults.db";
    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to SQLite database.");
            setupTables();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Database close error: " + e.getMessage());
        }
    }

    private void setupTables() {
        try (Statement stmt = connection.createStatement()) {
            // Player table
            String createPlayersTable = 
                "CREATE TABLE IF NOT EXISTS players (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "name TEXT NOT NULL UNIQUE,\n"
                + "health INTEGER,\n"
                + "current_room_id TEXT"
                + ");";

            stmt.executeUpdate(createPlayersTable);

            // Inventory table
            String createInventoryTable = 
                "CREATE TABLE IF NOT EXISTS inventory (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "player_id INTEGER,\n"
                + "name TEXT,\n"
                + "description TEXT,\n"
                + "is_consumable INTEGER,\n"
                + "is_equipable INTEGER,\n"
                + "slot_type TEXT,\n"
                + "attack_boost INTEGER,\n"
                + "defense_boost INTEGER,\n"
                + "FOREIGN KEY (player_id) REFERENCES players(id)"
                + ");";
            stmt.executeUpdate(createInventoryTable);

            System.out.println("Database tables initialized.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    public void savePlayer(Player player) {
        if (connection == null) {
            System.out.println("No database connection.");
            return;
        }
    
        try {
            // Check if player exists
            String checkSQL = "SELECT id FROM players WHERE name = ?;";
            PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
            checkStmt.setString(1, player.getName());
            ResultSet rs = checkStmt.executeQuery();
    
            int playerId;
            if (rs.next()) {
                // Player exists — get ID
                playerId = rs.getInt("id");
    
                // Update player data
                String updateSQL = "UPDATE players SET health = ?, current_room_id = ? WHERE id = ?;";
                PreparedStatement updateStmt = connection.prepareStatement(updateSQL);
                updateStmt.setInt(1, player.getHealth());
                updateStmt.setString(2, player.getCurrentRoom().getRoomId());
                updateStmt.setInt(3, playerId);
                updateStmt.executeUpdate();
    
                // Delete old inventory
                String deleteInventorySQL = "DELETE FROM inventory WHERE player_id = ?;";
                PreparedStatement deleteStmt = connection.prepareStatement(deleteInventorySQL);
                deleteStmt.setInt(1, playerId);
                deleteStmt.executeUpdate();
    
            } else {
                // Player does not exist — insert new
                String insertSQL = "INSERT INTO players (name, health, current_room_id) VALUES (?, ?, ?);";
                PreparedStatement insertStmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, player.getName());
                insertStmt.setInt(2, player.getHealth());
                insertStmt.setString(3, player.getCurrentRoom().getRoomId());
                insertStmt.executeUpdate();
    
                ResultSet keys = insertStmt.getGeneratedKeys();
                if (keys.next()) {
                    playerId = keys.getInt(1);
                    player.setId(playerId);
                } else {
                    System.out.println("Failed to generate player ID.");
                    return;
                }
            }
    
            // Save current inventory
            String insertItemSQL = "INSERT INTO inventory (player_id, name, description, is_consumable, is_equipable, slot_type, attack_boost, defense_boost) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement insertItemStmt = connection.prepareStatement(insertItemSQL);
            for (InventoryItem item : player.getInventory()) {
                insertItemStmt.setInt(1, playerId);
                insertItemStmt.setString(2, item.getName());
                insertItemStmt.setString(3, item.getDescription());
                insertItemStmt.setInt(4, item.isConsumable() ? 1 : 0);
                insertItemStmt.setInt(5, item.isEquipable() ? 1 : 0);
                insertItemStmt.setString(6, item.getSlotType());
                insertItemStmt.setInt(7, item.getAttackBoost());
                insertItemStmt.setInt(8, item.getDefenseBoost());
                insertItemStmt.addBatch();
            }
            insertItemStmt.executeBatch();
    
            System.out.println("Player save updated.");
    
        } catch (SQLException e) {
            System.out.println("Error saving player: " + e.getMessage());
        }
    }
    

    public Player loadPlayer(String name, Map<String, Room> roomMap) {
        if (connection == null) {
            System.out.println("No database connection.");
            return null;
        }

        Player player = null;

        try {
            String selectPlayerSQL = "SELECT * FROM players WHERE name = ?;";
            PreparedStatement selectPlayerStmt = connection.prepareStatement(selectPlayerSQL);
            selectPlayerStmt.setString(1, name);
            ResultSet playerResult = selectPlayerStmt.executeQuery();

            if (playerResult.next()) {
                int id = playerResult.getInt("id");
                int health = playerResult.getInt("health");
                String roomId = playerResult.getString("current_room_id");

                Room startingRoom = roomMap.getOrDefault(roomId, null);

                if (startingRoom == null) {
                    System.out.println("Room ID '" + roomId + "' not found in map. Defaulting to null.");
                }

                player = new Player(name, health, startingRoom);
                player.setId(id);

                // Load inventory
                String selectInventorySQL = "SELECT * FROM inventory WHERE player_id = ?;";
                PreparedStatement inventoryStmt = connection.prepareStatement(selectInventorySQL);
                inventoryStmt.setInt(1, id);
                ResultSet inventoryResult = inventoryStmt.executeQuery();

                while (inventoryResult.next()) {
                    InventoryItem item = new InventoryItem(
                        inventoryResult.getString("name"),
                        inventoryResult.getString("description"),
                        inventoryResult.getInt("is_consumable") == 1,
                        inventoryResult.getInt("is_equipable") == 1,
                        inventoryResult.getString("slot_type"),
                        inventoryResult.getInt("attack_boost"),
                        inventoryResult.getInt("defense_boost")
                    );
                    player.addItem(item);
                }

                System.out.println("Player loaded from DB: " + name + " in room " + roomId);
            } else {
                System.out.println("No player found with the name: " + name);
            }

        } catch (SQLException e) {
            System.out.println("Error loading player: " + e.getMessage());
        }

        return player;
    }
}
