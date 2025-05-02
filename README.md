# Legends_of_the_Earthen_Vaults

*Legends of the Earthen Vaults* is a text-based fantasy dungeon crawler adventure game, operated entirely through the terminal window. Built in Java, the game emphasizes exploration, puzzle-solving, and object-oriented programming principles such as abstraction, composition, inheritance, and polymorphism.

---

## Gameplay Overview

Players navigate through interconnected dungeon rooms filled with enemies, puzzles, and lootable chests. The goal is to explore the depths of the vaults, uncover secrets, solve unique puzzles, and survive encounters using collected items and equipped gear.

---

## Core Features

- Terminal-based user input (e.g., `move north`, `interact chest`, `use potion`)
- Room-based exploration with directional movement
- Combat with enemy entities
- Inventory system with equipable gear (weapons and armor)
- Puzzle system that unlocks rooms or reveals hidden chests
- Persistent game state (saved via SQLite in Week 4)
- Clean object-oriented design following academic standards

---

## Key Classes

| Class            | Description |
|------------------|-------------|
| `Player`         | Represents the player and inventory management |
| `Room`           | Contains enemies, puzzles, and interactable objects |
| `Enemy`          | Simple combat logic, extends `Entity` |
| `InventoryItem`  | Items that can be used or equipped |
| `Puzzle`         | Solvable object that affects game progression |
| `Chest`          | Loot container with special item rewards |
| `GameEngine`     | Controls the game loop (to be expanded) |
| `GameDataManager`| Handles database CRUD (added in Week 4) |

---

## Technologies

- Java 17+
- SQLite
- Git & GitHub (Phase tagging for project tracking)

---

## 🚀 How to Run

1. Clone the repository  
2. Open in your preferred Java IDE  
3. Run `GameEngine` as a Java application  
4. Interact via terminal input

---

## Project Timeline

| Week | Focus |
|------|-------|
| Week 1 | Proposal |
| Week 2 | Design Plan |
| Week 3 | Class Implementation |
| **Week 4** | Database Integration *(current phase)* |
| Week 5 | Final Submission & Polish |



