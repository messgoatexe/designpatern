package fr.fges;

import java.util.ArrayList;
import java.util.List;

public class GameCollection {
    private final List<BoardGame> games = new ArrayList<>();
    private final GameRepository repository;
    private final GameCollectionPresenter presenter;

    public GameCollection(String storageFile) {
        this.repository = RepositoryFactory.create(storageFile);
        this.presenter = new GameCollectionPresenter();
    }

    public List<BoardGame> getGames() {
        return games;
    }

    public void addGame(BoardGame game) {
        games.add(game);
        save();
    }

    public void removeGame(BoardGame game) {
        games.remove(game);
        save();
    }

    public void viewAllGames() {
        presenter.displayAll(games);
    }

    public void loadFromFile() {
        try {
            games.addAll(repository.load());
        } catch (Exception e) {
            System.out.println("Error loading games: " + e.getMessage());
        }
    }

    private void save() {
        try {
            repository.save(games);
        } catch (Exception e) {
            System.out.println("Error saving games: " + e.getMessage());
        }
    }
}