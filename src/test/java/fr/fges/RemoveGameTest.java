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
        undo.UndoManager undoManager = new undo.UndoManager();
        
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));

        // Simuler l'entrée utilisateur "Catan"
        String input = "Catan\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());
        
        // Vérifier que l'action a été enregistrée
        assertTrue(undoManager.hasActions());
    }

    @Test
    void shouldNotRemoveGameWhenTitleDoesNotExist() {
        // Arrange
        File tempFile = new File("test-remove-game-not-exists.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();
        
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        // Simuler l'entrée utilisateur "Monopoly"
        String input = "Monopoly\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Act
        removeGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());
        
        // Vérifier qu'aucune action n'a été enregistrée (échec de suppression)
        assertFalse(undoManager.hasActions());
    }

    @Test
    void shouldNotRemoveGameWhenCollectionIsEmpty() {
        // Arrange
        File tempFile = new File("test-remove-game-empty.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();

        // Simuler l'entrée utilisateur (n'importe quoi car la collection est vide)
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
    void shouldNotRemoveGameWhenTitleIsEmpty() {
        // Arrange
        File tempFile = new File("test-remove-game-empty-title.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();
        
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        // Simuler l'entrée utilisateur avec titre vide
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
    void shouldRecordActionInUndoManager() {
        File tempFile = new File("test-remove-undo-recording.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();
        
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game);

        String input = "Catan\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);

        // Vérifier qu'il n'y a pas d'actions avant
        assertFalse(undoManager.hasActions());

        removeGame.execute();

        // Vérifier qu'une action a été enregistrée
        assertTrue(undoManager.hasActions());
        
        undo.UndoableAction action = undoManager.getLastAction();
        assertNotNull(action);
        assertEquals(undo.UndoableAction.ActionType.REMOVE, action.getType());
        assertEquals("Catan", action.getGame().title());
    }

    @Test
    void shouldHandleMultipleRemoves() {
        File tempFile = new File("test-multiple-removes.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();
        
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Poker", 2, 8, "Card"));

        assertEquals(3, collection.getGames().size());

        // Supprimer Catan
        String input1 = "Catan\n";
        InputStream inputStream1 = new ByteArrayInputStream(input1.getBytes());
        java.util.Scanner scanner1 = new java.util.Scanner(inputStream1);
        RemoveGame removeGame1 = new RemoveGame(collection, scanner1, undoManager);
        removeGame1.execute();

        assertEquals(2, collection.getGames().size());

        // Supprimer Chess
        String input2 = "Chess\n";
        InputStream inputStream2 = new ByteArrayInputStream(input2.getBytes());
        java.util.Scanner scanner2 = new java.util.Scanner(inputStream2);
        RemoveGame removeGame2 = new RemoveGame(collection, scanner2, undoManager);
        removeGame2.execute();

        assertEquals(1, collection.getGames().size());
        assertEquals("Poker", collection.getGames().get(0).title());
        
        // Vérifier que 2 actions ont été enregistrées
        assertTrue(undoManager.hasActions());
    }

    @Test
    void shouldRemoveAndAllowUndo() {
        File tempFile = new File("test-remove-and-undo.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();
        undo.UndoService undoService = new undo.UndoService(collection, undoManager);
        
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game);

        String input = "Catan\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        java.util.Scanner scanner = new java.util.Scanner(inputStream);

        RemoveGame removeGame = new RemoveGame(collection, scanner, undoManager);
        removeGame.execute();

        assertEquals(0, collection.getGames().size());

        // Undo la suppression
        String result = undoService.execute();
        
        assertEquals("Added \"Catan\" back to collection", result);
        assertEquals(1, collection.getGames().size());
        assertEquals("Catan", collection.getGames().get(0).title());
    }
}