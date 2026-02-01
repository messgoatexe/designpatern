package fr.fges;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

class RemoveGameTest {

    @Test
    void shouldRemoveGameWhenTitleExists() {
        // Arrange
        File tempFile = new File("test-remove-game-exists.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));

        // Simuler l'entrée utilisateur "Catan"
        String input = "Catan\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());
    }

    @Test
    void shouldNotRemoveGameWhenTitleDoesNotExist() {
        // Arrange
        File tempFile = new File("test-remove-game-not-exists.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        // Simuler l'entrée utilisateur "Monopoly"
        String input = "Monopoly\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());
    }

    @Test
    void shouldNotRemoveGameWhenCollectionIsEmpty() {
        // Arrange
        File tempFile = new File("test-remove-game-empty.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());

        // Simuler l'entrée utilisateur (n'importe quoi car la collection est vide)
        String input = "AnyGame\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(0, collection.getGames().size());
    }

    @Test
    void shouldNotRemoveGameWhenTitleIsEmpty() {
        // Arrange
        File tempFile = new File("test-remove-game-empty-title.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        // Simuler l'entrée utilisateur avec titre vide
        String input = "\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());
    }
}