package fr.fges;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RecommendGame {
    private GameCollection collection;
    private Scanner scanner;

    public RecommendGame(GameCollection collection) {
        this.collection = collection;
        this.scanner = new Scanner(System.in);
    }

    public void execute() {
        System.out.print("How many players?: ");
        int playerCount = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante
        
        BoardGame recommendation = recommendGame(playerCount);
        
        if (recommendation != null) {
            System.out.println("Recommended game: \"" + recommendation.title() + "\" (" + 
                              recommendation.minPlayers() + "-" + 
                              recommendation.maxPlayers() + " players, " + 
                              recommendation.category() + ")");
        } else {
            System.out.println("No game found for " + playerCount + " players.");
        }
    }

    public BoardGame recommendGame(int playerCount) {
        List<BoardGame> compatibleGames = new ArrayList<>();
        
        // Filtrer les jeux compatibles avec le nombre de joueurs
        for (BoardGame game : collection.getGames()) {
            if (game.minPlayers() <= playerCount && playerCount <= game.maxPlayers()) {
                compatibleGames.add(game);
            }
        }
        
        // Si aucun jeu compatible, retourner null
        if (compatibleGames.isEmpty()) {
            return null;
        }
        
        // Retourner un jeu aléatoire
        Random random = new Random();
        return compatibleGames.get(random.nextInt(compatibleGames.size()));
    }
}