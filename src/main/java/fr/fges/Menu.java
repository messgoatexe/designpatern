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
        AddGame.addGame();
    }

    public static void removeGame() {
        RemoveGame.execute();
    }

    public static void listAllGames() {
        ListGames.execute();
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
