package fr.fges;

import java.util.Comparator;
import java.util.List;

public class GameCollectionPresenter {
    public void displayAll(List<BoardGame> games) {
        if (games.isEmpty()) {
            System.out.println("No board games in collection.");
            return;
        }

        games.stream()
                .sorted(Comparator.comparing(BoardGame::title))
                .forEach(this::displayGame);
    }

    private void displayGame(BoardGame game) {
        System.out.println("Game: " + game.title() + " (" + game.minPlayers() + "-" + game.maxPlayers() + " players) - " + game.category());
    }
}
