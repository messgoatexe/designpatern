package fr.fges;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Gère le mode tournoi : sélection des jeux 2 joueurs, saisie des participants.
 */
public class TournamentMode {
    private final GameCollection collection;
    private final Scanner scanner;
    private final Random random;

    public TournamentMode(GameCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
        this.random = new Random();
    }

    /**
     * Classe interne pour suivre les statistiques d'un joueur dans le tournoi.
     */
    private static class PlayerStats {
        private final Player player;
        private int points;
        private int wins;

        public PlayerStats(Player player) {
            this.player = player;
            this.points = 0;
            this.wins = 0;
        }

        public Player getPlayer() {
            return player;
        }

        public int getPoints() {
            return points;
        }

        public int getWins() {
            return wins;
        }

        public void addWin() {
            points += 3;
            wins++;
        }

        public void addLoss() {
            points += 1;
        }

        @Override
        public String toString() {
            return player.getName() + " - " + points + " points (" + wins + " wins)";
        }
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
     * Affiche le menu de sélection du mode de tournoi et lance le tournoi choisi.
     */
    private void displayTournamentSummary(BoardGame game, List<Player> players) {
        System.out.println("\n=== Tournament Setup ===");
        System.out.println("Game: " + game.title() + " (" + game.category() + ")");
        System.out.println("Number of participants: " + players.size());
        System.out.println("Participants:");
        for (int i = 0; i < players.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + players.get(i).getName());
        }

        // Demander le mode de tournoi
        System.out.println("\nSelect tournament mode:");
        System.out.println("1. Round-robin (everyone plays everyone)");
        System.out.println("2. King of the Hill (winner stays)");
        System.out.print("Your choice (1-2): ");
        
        String input = scanner.nextLine().trim();
        
        try {
            int choice = Integer.parseInt(input);
            if (choice == 1) {
                runRoundRobinTournament(game, players);
            } else if (choice == 2) {
                runKingOfTheHillTournament(game, players);
            } else {
                System.out.println("Invalid selection. Tournament cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Tournament cancelled.");
        }
    }

    /**
     * Simule un match entre deux joueurs et retourne l'index du gagnant (0 ou 1).
     */
    private int simulateMatch(PlayerStats player1, PlayerStats player2) {
        return random.nextInt(2);
    }

    /**
     * Mode Round-Robin : tous les joueurs s'affrontent.
     */
    private void runRoundRobinTournament(BoardGame game, List<Player> players) {
        System.out.println("\n=== Round-Robin Tournament ===");
        System.out.println("Each player will play against every other player.\n");

        // Initialiser les statistiques
        List<PlayerStats> stats = players.stream()
                .map(PlayerStats::new)
                .collect(Collectors.toList());

        // Tous les joueurs s'affrontent
        int matchNumber = 1;
        for (int i = 0; i < stats.size(); i++) {
            for (int j = i + 1; j < stats.size(); j++) {
                PlayerStats p1 = stats.get(i);
                PlayerStats p2 = stats.get(j);

                System.out.println("Match " + matchNumber + ": " + p1.getPlayer().getName() + 
                                   " vs " + p2.getPlayer().getName());

                int winner = simulateMatch(p1, p2);
                if (winner == 0) {
                    p1.addWin();
                    p2.addLoss();
                    System.out.println("  Winner: " + p1.getPlayer().getName());
                } else {
                    p2.addWin();
                    p1.addLoss();
                    System.out.println("  Winner: " + p2.getPlayer().getName());
                }
                System.out.println();
                matchNumber++;
            }
        }

        displayFinalResults(stats);
    }

    /**
     * Mode King of the Hill : le gagnant reste et affronte le prochain challenger.
     */
    private void runKingOfTheHillTournament(BoardGame game, List<Player> players) {
        System.out.println("\n=== King of the Hill Tournament ===");
        System.out.println("The winner stays and faces the next challenger.\n");

        // Initialiser les statistiques
        List<PlayerStats> stats = players.stream()
                .map(PlayerStats::new)
                .collect(Collectors.toList());

        // Le premier joueur commence comme "king"
        PlayerStats currentKing = stats.get(0);
        int matchNumber = 1;

        // Chaque challenger affronte le king actuel
        for (int i = 1; i < stats.size(); i++) {
            PlayerStats challenger = stats.get(i);

            System.out.println("Match " + matchNumber + ": " + currentKing.getPlayer().getName() + 
                               " (King) vs " + challenger.getPlayer().getName() + " (Challenger)");

            int winner = simulateMatch(currentKing, challenger);
            if (winner == 0) {
                currentKing.addWin();
                challenger.addLoss();
                System.out.println("  Winner: " + currentKing.getPlayer().getName() + " (King retains the throne!)");
            } else {
                challenger.addWin();
                currentKing.addLoss();
                System.out.println("  Winner: " + challenger.getPlayer().getName() + " (New King!)");
                currentKing = challenger;
            }
            System.out.println();
            matchNumber++;
        }

        displayFinalResults(stats);
    }

    /**
     * Affiche les résultats finaux triés selon les critères :
     * 1. Plus de points
     * 2. Plus de victoires
     * 3. Ordre alphabétique
     */
    private void displayFinalResults(List<PlayerStats> stats) {
        System.out.println("=== Final Results ===");

        // Trier selon les critères : points décroissant, victoires décroissant, nom croissant
        stats.sort((ps1, ps2) -> {
            // 1. Comparer les points (décroissant)
            int pointsCompare = Integer.compare(ps2.getPoints(), ps1.getPoints());
            if (pointsCompare != 0) return pointsCompare;
            
            // 2. Comparer les victoires (décroissant)
            int winsCompare = Integer.compare(ps2.getWins(), ps1.getWins());
            if (winsCompare != 0) return winsCompare;
            
            // 3. Comparer les noms (alphabétique croissant)
            return ps1.getPlayer().getName().compareTo(ps2.getPlayer().getName());
        });

        System.out.println("\nFinal Ranking:");
        for (int i = 0; i < stats.size(); i++) {
            PlayerStats ps = stats.get(i);
            System.out.println((i + 1) + ". " + ps.toString());
        }

        PlayerStats winner = stats.get(0);
        System.out.println("\nTournament Winner: " + winner.getPlayer().getName() + 
                           " with " + winner.getPoints() + " points and " + 
                           winner.getWins() + " wins!");
    }
}
