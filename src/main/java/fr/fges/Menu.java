package fr.fges;

import java.util.Scanner;

public class Menu {

    public static String getUserInput(String prompt) {
        // Scanner is a class in java that helps to read input from various sources like keyboard input, files, etc.
        Scanner scanner = new Scanner(System.in);
        // No new line for this one
        System.out.printf("%s: ", prompt);
        // Read input for the keyboard
        return scanner.nextLine();
    }

    public static void displayMainMenu() {
        String menuText = """
                === Board Game Collection ===
                1. Add Board Game
                2. Remove Board Game
                3. List All Board Games
                4. Exit
                Please select an option (1-4):
                """;

        System.out.println(menuText);
    }

    public static void addGame() {
        String title = "";
        while (title.isEmpty()) {
            title = getUserInput("Title").trim();
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty. Please enter a valid title.");
            }
        }

        int minPlayers = 0;
        boolean validMin = false;
        while (!validMin) {
            String minInput = getUserInput("Minimum Players").trim();
            if (minInput.isEmpty()) {
                System.out.println("Minimum players cannot be empty. Please enter a number (1-15).");
                continue;
            }
            try {
                minPlayers = Integer.parseInt(minInput);
                if (minPlayers >= 1 && minPlayers <= 15) {
                    validMin = true;
                } else {
                    System.out.println("Please enter a valid number of minimum players (1-15).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number of minimum players (1-15).");
            }
        }

        int maxPlayers = 0;
        boolean validMax = false;
        while (!validMax) {
            String maxInput = getUserInput("Maximum Players").trim();
            if (maxInput.isEmpty()) {
                System.out.println("Maximum players cannot be empty. Please enter a number (" + minPlayers + "-30).");
                continue;
            }
            try {
                maxPlayers = Integer.parseInt(maxInput);
                if (maxPlayers >= minPlayers && maxPlayers <= 30) {
                    validMax = true;
                } else {
                    System.out.println("Please enter a valid number of maximum players (" + minPlayers + "-30).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number of maximum players (" + minPlayers + "-30).");
            }
        }

        String category = "";
        while (category.isEmpty()) {
            category = getUserInput("Category (e.g., fantasy, cooperative, family, strategy)").trim();
            if (category.isEmpty()) {
                System.out.println("Category cannot be empty. Please enter a valid category.");
            }
        }

        BoardGame game = new BoardGame(title, minPlayers, maxPlayers, category);
        GameCollection.addGame(game);
        System.out.println("Board game added successfully.");
    }

    public static void removeGame() {
        String title = getUserInput("Title of game to remove");

        // get games from the collection, find the one that matches the title given by the user and remove
        var games = GameCollection.getGames();

        for (BoardGame game : games) {
            if (game.title().equals(title)) {
                GameCollection.removeGame(game);
                System.out.println("Board game removed successfully.");
                return;
            }
        }
        System.out.println("No board game found with that title.");
    }

    public static void listAllGames() {
        GameCollection.viewAllGames();
    }

    public static void exit() {
        System.out.println("Exiting the application. Goodbye!");
        System.exit(0);
    }

    public static void handleMenu() {
        displayMainMenu();

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addGame();
            case "2" -> removeGame();
            case "3" -> listAllGames();
            case "4" -> exit();
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }
}
