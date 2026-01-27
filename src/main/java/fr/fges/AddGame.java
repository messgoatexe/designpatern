package fr.fges;

import java.util.Scanner;

public class AddGame {

    private final GameCollection collection;
    private final Scanner scanner;

    public AddGame(GameCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    public void execute() {
        String title = readTitle();
        
        boolean gameExists = collection.getGames().stream()
                .anyMatch(game -> game.title().equalsIgnoreCase(title));
        
        int minPlayers = readMinPlayers();
        int maxPlayers = readMaxPlayers(minPlayers);
        String category = readCategory();

        BoardGame game = new BoardGame(title, minPlayers, maxPlayers, category);
        if (gameExists) {
            System.out.println("Error: A game with title \"" + title + "\" already exists in the collection\n");
            return;
        }
        collection.addGame(game);
        System.out.println("Board game added successfully.");
    }

    private String readTitle() {
        String title = "";
        while (title.isEmpty()) {
            System.out.print("Title: ");
            String input = scanner.nextLine();

            if (input == null) {
                System.out.println("Title cannot be empty. Please enter a valid title.");
                continue;
            }

            title = input.trim();
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty. Please enter a valid title.");
            }
        }
        return title;
    }

    private int readMinPlayers() {
        while (true) {
            System.out.print("Minimum Players: ");
            String minInput = scanner.nextLine();

            if (minInput == null || minInput.trim().isEmpty()) {
                System.out.println("Minimum players cannot be empty. Please enter a number (1-15).");
                continue;
            }

            try {
                int minPlayers = Integer.parseInt(minInput.trim());
                if (minPlayers >= 1 && minPlayers <= 15) {
                    return minPlayers;
                } else {
                    System.out.println("Please enter a valid number of minimum players (1-15).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number of minimum players (1-15).");
            }
        }
    }

    private int readMaxPlayers(int minPlayers) {
        while (true) {
            System.out.print("Maximum Players: ");
            String maxInput = scanner.nextLine();

            if (maxInput == null || maxInput.trim().isEmpty()) {
                System.out.println("Maximum players cannot be empty. Please enter a number (" + minPlayers + "-30).");
                continue;
            }

            try {
                int maxPlayers = Integer.parseInt(maxInput.trim());
                if (maxPlayers >= minPlayers && maxPlayers <= 30) {
                    return maxPlayers;
                } else {
                    System.out.println("Please enter a valid number of maximum players (" + minPlayers + "-30).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number of maximum players (" + minPlayers + "-30).");
            }
        }
    }

    private String readCategory() {
        String category = "";
        while (category.isEmpty()) {
            System.out.print("Category (e.g., fantasy, cooperative, family, strategy): ");
            String input = scanner.nextLine();

            if (input == null) {
                System.out.println("Category cannot be empty. Please enter a valid category.");
                continue;
            }

            category = input.trim();
            if (category.isEmpty()) {
                System.out.println("Category cannot be empty. Please enter a valid category.");
            }
        }
        return category;
    }
}