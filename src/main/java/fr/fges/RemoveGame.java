package fr.fges;

public class RemoveGame {

    public static void execute() {
        // Display available games
        System.out.println("\n=== Available Games ===");
        var games = GameCollection.getGames();
        
        if (games.isEmpty()) {
            System.out.println("No games in collection.");
            return;
        }
        
        for (BoardGame game : games) {
            System.out.println("- " + game.title());
        }
        System.out.println();
        
        String title = Menu.getUserInput("Title of game to remove");

        // Validate input
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Error: Title cannot be empty.");
            return;
        }

        // Find the one that matches the title given by the user and remove
        for (BoardGame game : games) {
            if (game.title().equals(title)) {
                GameCollection.removeGame(game);
                System.out.println("Board game removed successfully.");
                return;
            }
        }
        System.out.println("No board game found with that title.");
    }
}
