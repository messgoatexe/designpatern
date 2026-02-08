package fr.fges;

import java.util.Scanner;

public class RemoveGame {

    private final GameCollection collection;
    private final Scanner scanner;
    private final undo.UndoManager undoManager;

    public RemoveGame(GameCollection collection, Scanner scanner, undo.UndoManager undoManager) {
        this.collection = collection;
        this.scanner = scanner;
        this.undoManager = undoManager;
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
                // Enregistrer l'action AVANT de supprimer
                undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.REMOVE, game));
                
                collection.removeGame(game);
                System.out.println("Board game removed successfully.");
                return;
            }
        }
        System.out.println("No board game found with that title.");
    }
}