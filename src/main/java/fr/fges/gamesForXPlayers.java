package fr.fges;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class gamesForXPlayers {
    private GameCollection collection;
    private Scanner scanner;

    public gamesForXPlayers(GameCollection collection) {
        this.collection = collection;
        this.scanner = new Scanner(System.in);
    }

    public void execute() {
        System.out.print("How many players? ");
        int playerCount = scanner.nextInt();
        scanner.nextLine();
        
        List<BoardGame> games = getGamesForXPlayers(playerCount);
        
        if (!games.isEmpty()) {
            System.out.println("Games available for " + playerCount + " players:");
            for (BoardGame game : games) {
                System.out.println("- \"" + game.title() + "\" (" + 
                                  game.minPlayers() + "-" + 
                                  game.maxPlayers() + " players, " + 
                                  game.category() + ")");
            }
        } else {
            System.out.println("No game found for " + playerCount + " players.");
        }
    }

    public List<BoardGame> getGamesForXPlayers(int playerCount) {
        List<BoardGame> compatibleGames = new ArrayList<>();
        
        for (BoardGame game : collection.getGames()) {
            if (game.minPlayers() <= playerCount && playerCount <= game.maxPlayers()) {
                compatibleGames.add(game);
            }
        }
        
        return compatibleGames;
    }
}
