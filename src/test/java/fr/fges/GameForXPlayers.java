package fr.fges;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

class GameForXPlayers {

    @Test
    void shouldReturnGamesForExactPlayerCount() {

        File tempFile = new File("test-games-xplayers.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Poker", 2, 8, "Card"));

        gamesForXPlayers gamesForXPlayers = new gamesForXPlayers(collection);

        List<BoardGame> gamesFor3Players = gamesForXPlayers.getGamesForXPlayers(3);

        assertEquals(2, gamesFor3Players.size());
        assertTrue(gamesFor3Players.stream().anyMatch(g -> g.title().equals("Catan")));
        assertTrue(gamesFor3Players.stream().anyMatch(g -> g.title().equals("Poker")));
    }

    @Test
    void shouldReturnEmptyListWhenNoGamesAvailable() {

        File tempFile = new File("test-games-xplayers-empty.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        gamesForXPlayers gamesForXPlayers = new gamesForXPlayers(collection);


        List<BoardGame> gamesFor10Players = gamesForXPlayers.getGamesForXPlayers(10);

        assertEquals(0, gamesFor10Players.size());
    }

    @Test
    void shouldReturnAllGamesForPlayerCountInRange() {

        File tempFile = new File("test-games-xplayers-all.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Uno", 2, 10, "Card"));
        collection.addGame(new BoardGame("Monopoly", 2, 8, "Family"));
        collection.addGame(new BoardGame("Risk", 2, 6, "Strategy"));

        gamesForXPlayers gamesForXPlayers = new gamesForXPlayers(collection);

        List<BoardGame> gamesFor5Players = gamesForXPlayers.getGamesForXPlayers(5);


        assertEquals(3, gamesFor5Players.size());
        assertTrue(gamesFor5Players.stream().anyMatch(g -> g.title().equals("Uno")));
        assertTrue(gamesFor5Players.stream().anyMatch(g -> g.title().equals("Monopoly")));
        assertTrue(gamesFor5Players.stream().anyMatch(g -> g.title().equals("Risk")));
    }

    @Test
    void shouldReturnGamesForMinimumPlayers() {

        File tempFile = new File("test-games-xplayers-min.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));

        gamesForXPlayers gamesForXPlayers = new gamesForXPlayers(collection);


        List<BoardGame> gamesFor2Players = gamesForXPlayers.getGamesForXPlayers(2);


        assertEquals(1, gamesFor2Players.size());
        assertEquals("Chess", gamesFor2Players.get(0).title());
    }

    @Test
    void shouldReturnGamesForMaximumPlayers() {

        File tempFile = new File("test-games-xplayers-max.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        collection.addGame(new BoardGame("Poker", 2, 8, "Card"));

        gamesForXPlayers gamesForXPlayers = new gamesForXPlayers(collection);


        List<BoardGame> gamesFor8Players = gamesForXPlayers.getGamesForXPlayers(8);


        assertEquals(1, gamesFor8Players.size());
        assertEquals("Poker", gamesFor8Players.get(0).title());
    }
}
