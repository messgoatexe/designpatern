package fr.fges;

public class ListGames {

    private final GameCollection collection;

    public ListGames(GameCollection collection) {
        this.collection = collection;
    }

    public void execute() {
        collection.viewAllGames();
    }
}