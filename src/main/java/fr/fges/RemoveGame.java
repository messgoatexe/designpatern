package fr.fges;

import java.util.Scanner;

public class RemoveGame {

    private final GameCollection collection;
    private final Scanner scanner;

    public RemoveGame(GameCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    public void execute() {
        var games = collection.getGames();

        if (games.isEmpty()) {
            System.out.println("No games in collection.");
            return;
        }

        System.out.println("\n=== Available Games ===");
        for (BoardGame game : games) {
            System.out.println("- " + game.title());
        }
        System.out.println();

        System.out.print("Title of game to remove: ");
        String title = scanner.nextLine();

        if (title == null || title.trim().isEmpty()) {
            System.out.println("Error: Title cannot be empty.");
            return;
        }

        for (BoardGame game : games) {
            if (game.title().equals(title)) {
                collection.removeGame(game);
                System.out.println("Board game removed successfully.");
                return;
            }
        }
        System.out.println("No board game found with that title.");
    }
}