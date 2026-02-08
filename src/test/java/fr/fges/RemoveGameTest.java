package fr.fges;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

@DisplayName("Remove Game Tests")
class RemoveGameTest {

    private GameCollection collection;
    private undo.UndoManager undoManager;
    private File tempFile;

    @BeforeEach
    void setUp() {
        tempFile = new File("test-remove-temp.json");
        collection = new GameCollection(tempFile.getPath());
        undoManager = new undo.UndoManager();
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    @DisplayName("Should remove game when title exists")
    void shouldRemoveGameWhenTitleExists() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));

        String input = "Catan\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());
        assertTrue(undoManager.hasActions());
    }

    @Test
    @DisplayName("Should not remove game when title does not exist")
    void shouldNotRemoveGameWhenTitleDoesNotExist() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        String input = "Monopoly\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());
        assertFalse(undoManager.hasActions());
    }

    @Test
    @DisplayName("Should not remove game when collection is empty")
    void shouldNotRemoveGameWhenCollectionIsEmpty() {
        // Arrange
        String input = "AnyGame\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(0, collection.getGames().size());
        assertFalse(undoManager.hasActions());
    }

    @Test
    @DisplayName("Should not remove game when title is empty")
    void shouldNotRemoveGameWhenTitleIsEmpty() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        String input = "\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());
        assertFalse(undoManager.hasActions());
    }

    @Test
    @DisplayName("Should record action in undo manager")
    void shouldRecordActionInUndoManager() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game);

        String input = "Catan\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        assertFalse(undoManager.hasActions());

        // Act
        removeGame.execute();

        // Assert
        assertTrue(undoManager.hasActions());
        undo.UndoableAction action = undoManager.getLastAction();
        assertNotNull(action);
        assertEquals(undo.UndoableAction.ActionType.REMOVE, action.getType());
        assertEquals("Catan", action.getGame().title());
    }

    @Test
    @DisplayName("Should handle multiple removes")
    void shouldHandleMultipleRemoves() {
        // Arrange
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Poker", 2, 8, "Card"));
        assertEquals(3, collection.getGames().size());

        // Act - Remove Catan
        String input1 = "Catan\n";
        InputStream inputStream1 = new ByteArrayInputStream(input1.getBytes());
        java.util.Scanner scanner1 = new java.util.Scanner(inputStream1);
        new RemoveGame(collection, scanner1, undoManager).execute();

        // Assert
        assertEquals(2, collection.getGames().size());

        // Act - Remove Chess
        String input2 = "Chess\n";
        InputStream inputStream2 = new ByteArrayInputStream(input2.getBytes());
        java.util.Scanner scanner2 = new java.util.Scanner(inputStream2);
        new RemoveGame(collection, scanner2, undoManager).execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Poker", collection.getGames().get(0).title());
        assertTrue(undoManager.hasActions());
    }

    @Test
    @DisplayName("Should remove and allow undo")
    void shouldRemoveAndAllowUndo() {
        // Arrange
        undo.UndoService undoService = new undo.UndoService(collection, undoManager);
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game);

        String input = "Catan\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);
        removeGame.execute();

        assertEquals(0, collection.getGames().size());

        // Act - Undo removal
        String result = undoService.execute();

        // Assert
        assertEquals("Added \"Catan\" back to collection", result);
        assertEquals(1, collection.getGames().size());
        assertEquals("Catan", collection.getGames().get(0).title());
    }

    @Test
    @DisplayName("Should remove game case-insensitive")
    void shouldRemoveGameCaseInsensitive() {
        // Arrange
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        String input = "catan\n"; // lowercase
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert - Check if removal was successful
        // If the implementation is case-insensitive, size should be 1
        assertTrue(collection.getGames().size() <= 1);
    }

    @Test
    @DisplayName("Should remove correct game from multiple games")
    void shouldRemoveCorrectGameFromMultipleGames() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame game3 = new BoardGame("Poker", 2, 8, "Card");
        
        collection.addGame(game1);
        collection.addGame(game2);
        collection.addGame(game3);

        String input = "Chess\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(2, collection.getGames().size());
        assertFalse(collection.getGames().stream().anyMatch(g -> g.title().equals("Chess")));
        assertTrue(collection.getGames().stream().anyMatch(g -> g.title().equals("Catan")));
        assertTrue(collection.getGames().stream().anyMatch(g -> g.title().equals("Poker")));
    }

    @Test
    @DisplayName("Should remove last game from collection")
    void shouldRemoveLastGameFromCollection() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        assertEquals(1, collection.getGames().size());

        String input = "Chess\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(0, collection.getGames().size());
        assertTrue(undoManager.hasActions());
    }

    @Test
    @DisplayName("Should handle remove with spaces in title")
    void shouldHandleRemoveWithSpacesInTitle() {
        // Arrange
        collection.addGame(new BoardGame("7 Wonders", 3, 7, "Strategy"));
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        String input = "7 Wonders\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());
    }

    @Test
    @DisplayName("Should handle remove with special characters")
    void shouldHandleRemoveWithSpecialCharacters() {
        // Arrange
        collection.addGame(new BoardGame("Ticket to Ride: Europe", 2, 6, "Adventure"));
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        String input = "Ticket to Ride: Europe\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());
    }

    @Test
    @DisplayName("Should not remove if only partial title matches")
    void shouldNotRemoveIfOnlyPartialTitleMatches() {
        // Arrange
        collection.addGame(new BoardGame("Ticket to Ride", 2, 6, "Adventure"));

        String input = "Ticket\n"; // only partial match
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertFalse(undoManager.hasActions());
    }
}