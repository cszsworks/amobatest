# Amoba – Java Lanterna Game Project


**Amoba** is an experimental interactive tic-tac-toe game project exploring a "closer-to-metal" **MVC architecture** in Java, while pushing the limits of a terminal-based user interface. The project leverages the **Lanterna UI library** for terminal rendering and input handling.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Persistence](#persistence)
- [Future Work](#future-work)
- [License](#license)

---

## Overview

This project is designed to experiment with interactive terminal games in Java, emphasizing:

- **MVC design** with a near-metal approach.
- **Lanterna library** usage for terminal rendering (`Screen` layer) and a GUI abstraction layer (`GUI2`).
- **Terminal-based Tic Tac Toe** implementation with full keyboard navigation.
- Exploring scalability and modularity for future game expansions.

The current iteration demonstrates the principles of **clean separation of concerns** and **UI abstraction**, allowing further games to reuse core logic and rendering patterns.

---

## Current Features

- Simple **Tic Tac Toe game** playable entirely in the terminal.
- Full **keyboard controls** supported via Lanterna input handling.
- **Persistent saves**:
  - Cloud-based **SQL database** for historical player scores.
  - Local **JSON-based save system** as an alternative.
  - Resume latest game using **Java byte stream serialization**.
- Modular and **future-proof architecture**, supporting future game types and UI improvements.

---

## Architecture

The project structure follows a strict **MVC pattern**:

```
com.cszsworks
├── controller      // Game and menu controllers
├── model           // Game state and table representations
├── view            // Lanterna-based renderers
├── persistence     // SQL & JSON storage, data models
├── saves           // Game save management
├── util            // Utility classes (scoring, themes, key handling)
```

Key abstractions:

- **Lanterna Screen Layer** – handles low-level terminal drawing and input.
- **GUI2 Components** – custom abstraction for menus and user interface rendering.
- **GameController** – coordinates game logic, input, and UI updates.
- **SaveManager** – handles local serialization and cloud SQL interactions.

---

## Installation

1. **Clone the repository**:

```bash
git clone https://github.com/yourusername/amoba.git
cd amoba
```

2. **Build the project** (Maven required):

```bash
mvn clean install
```

3. **Run the game**:

```bash
mvn exec:java -Dexec.mainClass="com.cszsworks.GameLauncher"
```

---

## Usage

- Navigate menus using arrow keys.
- Select options with Enter.
- Escape or F5 to leave game and save current state
- Access high scores database, new game, and resume saved game from the main menu.

---

## Persistence

- **SQL Cloud Database**:
  - Stores historical player scores and game statistics.
  - Centralized and accessible across devices.

- **Local JSON Save**:
  - Alternative offline save format.
  - Easy export and debugging.

- **Byte Stream Serialization**:
  - Save and resume the **latest game state**.

---

## Future Work

- Add **additional games** using the same MVC + Lanterna architecture.
- Enhance UI with **dynamic themes and color schemes**.
- Improve networked save support.
- Integrate **AI opponents** for different difficulty levels.


> **Note:** This project is primarily an experimental learning playground for exploring terminal UI, MVC design, and Java game architecture.

