package fr.fges;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeekendSummary {

    private final GameCollection collection;
    private LocalDate currentDate; 

    public WeekendSummary(GameCollection collection) {
        this.collection = collection;
        this.currentDate = null;
    }
    
    public WeekendSummary(GameCollection collection, LocalDate date) {
        this.collection = collection;
        this.currentDate = date;
    }

    public void execute() {
        LocalDate today = (currentDate != null) ? currentDate : LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
            System.out.println("Weekend summary is only available on weekends (Saturday or Sunday).");
            return;
        }

        List<BoardGame> games = collection.getGames();

        if (games.isEmpty()) {
            System.out.println("No board games in collection.");
            return;
        }
        displaySummary(games);
    }

    private void displaySummary(List<BoardGame> games) {
        List<BoardGame> shuffledGames = new ArrayList<>(games);
        Collections.shuffle(shuffledGames);
        int count = Math.min(3, shuffledGames.size());
        System.out.println("\n=== Summary (" + count + " random game" + (count > 1 ? "s" : "") + ") ===");
        for (int i = 0; i < count; i++) {
            BoardGame game = shuffledGames.get(i);
            System.out.println("- " + game.title() +
                    " (" + game.minPlayers() + "-" + game.maxPlayers() +
                    " players, " + game.category() + ")");
        }
        if (games.size() < 3) {
            System.out.println("\nNote: Collection has fewer than 3 games, displaying all available games.");
        }
    }
}