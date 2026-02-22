package fr.fges;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Gère le mode tournoi : sélection des jeux 2 joueurs, saisie des participants.
 */
public class TournamentMode {
    private final GameCollection collection;
    private final Scanner scanner;

    public TournamentMode(GameCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    public void execute() {
        System.out.println("\n=== Tournament Mode ===");

        // Étape 1 : Filtrer les jeux compatibles 2 joueurs
        List<BoardGame> twoPlayerGames = filterTwoPlayerGames();

        if (twoPlayerGames.isEmpty()) {
            System.out.println("Error: No 2-player games available in the collection.");
            System.out.println("Please add some 2-player games first.");
            return;
        }

        // Étape 2 : Afficher et sélectionner un jeu
        BoardGame selectedGame = selectGame(twoPlayerGames);
        if (selectedGame == null) {
            System.out.println("Tournament cancelled.");
            return;
        }

        // Étape 3 : Saisir le nombre de participants
        int numberOfParticipants = getNumberOfParticipants();
        if (numberOfParticipants == -1) {
            System.out.println("Tournament cancelled.");
            return;
        }

        // Étape 4 : Saisir les noms des joueurs
        List<Player> players = getPlayerNames(numberOfParticipants);

        // Afficher le résumé
        displayTournamentSummary(selectedGame, players);
    }

    /**
     * Filtre les jeux compatibles avec exactement 2 joueurs.
     * Un jeu est compatible 2 joueurs si : minPlayers <= 2 <= maxPlayers
     */
    private List<BoardGame> filterTwoPlayerGames() {
        return collection.getGames()
                .stream()
                .filter(game -> game.minPlayers() <= 2 && game.maxPlayers() >= 2)
                .collect(Collectors.toList());
    }

    /**
     * Affiche les jeux disponibles et permet à l'utilisateur d'en sélectionner un.
     */
    private BoardGame selectGame(List<BoardGame> twoPlayerGames) {
        System.out.println("Available 2-player games:");
        for (int i = 0; i < twoPlayerGames.size(); i++) {
            BoardGame game = twoPlayerGames.get(i);
            System.out.printf("%d. %s (%d-%d players, %s)\n",
                    i + 1,
                    game.title(),
                    game.minPlayers(),
                    game.maxPlayers(),
                    game.category());
        }

        System.out.print("\nSelect game (1-" + twoPlayerGames.size() + "): ");
        String input = scanner.nextLine().trim();

        try {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= twoPlayerGames.size()) {
                return twoPlayerGames.get(choice - 1);
            } else {
                System.out.println("Invalid selection. Please choose a valid game number.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return null;
        }
    }

    /**
     * Demande le nombre de participants et valide la saisie (3-8).
     */
    private int getNumberOfParticipants() {
        while (true) {
            System.out.print("Number of participants (3-8): ");
            String input = scanner.nextLine().trim();

            try {
                int numberOfParticipants = Integer.parseInt(input);
                if (numberOfParticipants >= 3 && numberOfParticipants <= 8) {
                    return numberOfParticipants;
                } else {
                    System.out.println("Error: Number of participants must be between 3 and 8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }

    /**
     * Demande le nom de chaque joueur et les stocke dans une liste.
     */
    private List<Player> getPlayerNames(int numberOfParticipants) {
        List<Player> players = new ArrayList<>();

        for (int i = 1; i <= numberOfParticipants; i++) {
            System.out.print("Enter player " + i + " name: ");
            String playerName = scanner.nextLine().trim();

            if (playerName.isEmpty()) {
                System.out.println("Error: Player name cannot be empty.");
                i--; // Redemander le nom
            } else {
                players.add(new Player(playerName));
            }
        }

        return players;
    }

    /**
     * Affiche un résumé du tournoi avec les données saisies.
     * Cette méthode peut être modifiée par les coéquipiers pour implémenter
     * les formats de tournoi et le système de points.
     */
    private void displayTournamentSummary(BoardGame game, List<Player> players) {
        System.out.println("\n=== Tournament Summary ===");
        System.out.println("Game: " + game.title() + " (" + game.category() + ")");
        System.out.println("Number of participants: " + players.size());
        System.out.println("Participants:");
        for (int i = 0; i < players.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + players.get(i).getName());
        }
        System.out.println("\nTournament ready! (Tournament format implementation to follow)");
    }
}
