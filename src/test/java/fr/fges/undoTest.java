package fr.fges;

import org.junit.jupiter.api.Test;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class undoTest {

    @Test
    void shouldUndoAddOperation() {
        File tempFile = new File("test-undo-add.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();
        undo.UndoService undoService = new undo.UndoService(collection, undoManager);

        BoardGame game = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game));

        assertEquals(1, collection.getGames().size());

        String result = undoService.execute();

        assertEquals("Removed \"Catan\" from collection", result);
        assertEquals(0, collection.getGames().size());
    }

    @Test
    void shouldUndoRemoveOperation() {
        File tempFile = new File("test-undo-remove.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();
        undo.UndoService undoService = new undo.UndoService(collection, undoManager);

        BoardGame game = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game));
        
        collection.removeGame(game);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.REMOVE, game));

        assertEquals(0, collection.getGames().size());

        String result = undoService.execute();

        assertEquals("Added \"Catan\" back to collection", result);
        assertEquals(1, collection.getGames().size());
    }

    @Test
    void shouldHandleMultipleUndos() {
        File tempFile = new File("test-multiple-undos.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();
        undo.UndoService undoService = new undo.UndoService(collection, undoManager);

        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Chess", 2, 2, "Strategy");
        
        collection.addGame(game1);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game1));
        
        collection.addGame(game2);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game2));

        assertEquals(2, collection.getGames().size());

        undoService.execute(); // Undo Chess
        assertEquals(1, collection.getGames().size());

        undoService.execute(); // Undo Catan
        assertEquals(0, collection.getGames().size());
    }

    @Test
    void shouldReturnNullWhenNothingToUndo() {
        File tempFile = new File("test-nothing-to-undo.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();
        undo.UndoService undoService = new undo.UndoService(collection, undoManager);

        String result = undoService.execute();

        assertNull(result);
    }

    @Test
    void shouldUndoAlternatingOperations() {
        File tempFile = new File("test-alternating.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();
        undo.UndoService undoService = new undo.UndoService(collection, undoManager);

        // Add Catan
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game1);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game1));

        // Add Chess
        BoardGame game2 = new BoardGame("Chess", 2, 2, "Strategy");
        collection.addGame(game2);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game2));

        // Remove Catan
        collection.removeGame(game1);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.REMOVE, game1));

        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());

        // Undo remove (re-add Catan)
        String result1 = undoService.execute();
        assertEquals("Added \"Catan\" back to collection", result1);
        assertEquals(2, collection.getGames().size());

        // Undo add Chess
        String result2 = undoService.execute();
        assertEquals("Removed \"Chess\" from collection", result2);
        assertEquals(1, collection.getGames().size());

        // Undo add Catan
        String result3 = undoService.execute();
        assertEquals("Removed \"Catan\" from collection", result3);
        assertEquals(0, collection.getGames().size());
    }
}