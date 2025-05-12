# Legends_of_the_Earthen_Vaults

*Legends of the Earthen Vaults* is a text-based fantasy dungeon crawler adventure game, operated entirely through the terminal window. Built in Java, the game emphasizes exploration, puzzle-solving, and object-oriented programming principles such as abstraction, composition, inheritance, and polymorphism.

---

## Gameplay Overview

Players navigate through interconnected dungeon rooms filled with enemies, puzzles, and lootable chests. The goal is to explore the depths of the vaults, uncover secrets, solve unique puzzles, and survive encounters using collected items and equipped gear.

---

## Core Features

- Terminal-based user input (`move north`, `interact chest`, `use potion`, `look glyphs`, etc.)
- Room-based exploration with interconnected directional movement
- Environmental storytelling and puzzles (e.g., magical fitting, sigils, button-based mechanisms)
- Inventory system with usable and equipable items (weapons, armor, healing potions, magical artifacts)
- Puzzle system that unlocks rooms, rewards key fragments, or reveals secrets
- Final room and ending sequence using the Starborn Compass and celestial lore
- Game state persistence with SQLite (auto-saves at key story events and endgame)
- Full object-oriented design with abstraction, inheritance, composition, and polymorphism

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

- Java 7+
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
| Week 2 | Room exploration, base commands, and interaction design |
| Week 3 | Inventory, combat, puzzles, and persistent save system |
| Week 4 | Database integration, room mapping, and story progression |
| **Week 5** | Major gameplay elements, end sequence, and feature polish |
| Further Progress | Weapon/Armor additions, Attack/Defense stat implementation,                          |
|                  | More in-depth enemy/combat system, Character jobs (Warrior, Mage, Hunter), and more! |

---
---

## Project Summary

### Project Description
Legends of the Earthen Vaults is a terminal-based, single-player text adventure game developed in Java. The project demonstrates core object-oriented principles—abstraction, inheritance, polymorphism, and composition—through an immersive dungeon-crawling experience. Players explore interconnected rooms, collect items, solve puzzles, and uncover a hidden narrative rooted in celestial mythology. SQLite integration allows for persistent game state storage, and the game ends with a symbolic sequence involving a sacred artifact.

### Project Tasks
- **Task 1: Project setup and planning**
  - Created a GitHub repository and initial design structure
  - Developed UML and class design for game elements
- **Task 2: Base functionality and exploration**
  - Implemented `move`, `look`, `use`, and `interact` commands
  - Established a branching room map with clear directional logic
- **Task 3: Combat and inventory system**
  - Designed enemy encounter and item acquisition
- **Task 4: Puzzle mechanics and database**
  - Added puzzle-based progression (button and gem interactions, etc.)
  - Integrated SQLite for save/load support and player tracking
- **Task 5: Final features and polish**
  - Completed the ending sequence using the magical artifact
  - Implemented full command coverage (`look`, `use`, `interact`, `move`)
  - Refined descriptions, flags, and object interactions

### Skills Demonstrated
- **Java OOP**: Designed and implemented abstract classes (`Entity`, `GameObject`), inheritance (e.g., `Enemy` extends `Entity`), and interfaces (`Interactable`)
- **Composition**: Classes like `Player` and `Room` manage related objects through composition (e.g., inventory, puzzles)
- **Access specifiers**: Correct use of `private`, `protected`, and `public` throughout the codebase
- **Polymorphism**: Applied method overriding and interface implementation across various entities
- **Database Integration**: Used SQLite to persist player inventory, progress, and save files
- **Terminal I/O**: Captured user input using `Scanner`, parsed through `CommandParser`, and generated textual game output

### Language and Technologies
- **Java** (primary programming language)
- **SQLite** (for persistent storage)
- **Git** and **GitHub** (for version control, tagging phases of development)

### Development Methodology
- **Iterative design with weekly deliverables**, progressively layering functionality
- Emphasis on planning, testing, and code clarity
- Used version control and GitHub for milestone tagging and submission

### Notes
- The player is born under the celestial sign of the Ivory Hawk, a theme reinforced by different interactions and descriptions.
- Game state is automatically saved upon entering the `quit` input, as well as upon completion of the game.
- User experience is entirely text-based, emphasizing immersive storytelling and interaction.

### Repository
[Legends of the Earthen Vaults GitHub Repository](https://github.com/TheFlakiestBiscuit/Legends_of_the_Earthen_Vaults.git)








