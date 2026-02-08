package fr.fges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Undo Manager and Service Tests")
class undoTest {

    private GameCollection collection;
    private undo.UndoManager undoManager;
    private undo.UndoService undoService;
    private File tempFile;

    @BeforeEach
    void setUp() {
        tempFile = new File("test-undo-temp.json");
        collection = new GameCollection(tempFile.getPath());
        undoManager = new undo.UndoManager();
        undoService = new undo.UndoService(collection, undoManager);
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    @DisplayName("Should undo add operation")
    void shouldUndoAddOperation() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game));

        assertEquals(1, collection.getGames().size());

        // Act
        String result = undoService.execute();

        // Assert
        assertEquals("Removed \"Catan\" from collection", result);
        assertEquals(0, collection.getGames().size());
    }

    @Test
    @DisplayName("Should undo remove operation")
    void shouldUndoRemoveOperation() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game));
        
        collection.removeGame(game);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.REMOVE, game));

        assertEquals(0, collection.getGames().size());

        // Act
        String result = undoService.execute();

        // Assert
        assertEquals("Added \"Catan\" back to collection", result);
        assertEquals(1, collection.getGames().size());
    }

    @Test
    @DisplayName("Should handle multiple undos")
    void shouldHandleMultipleUndos() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Chess", 2, 2, "Strategy");
        
        collection.addGame(game1);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game1));
        
        collection.addGame(game2);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game2));

        assertEquals(2, collection.getGames().size());

        // Act & Assert - Undo Chess
        undoService.execute();
        assertEquals(1, collection.getGames().size());

        // Act & Assert - Undo Catan
        undoService.execute();
        assertEquals(0, collection.getGames().size());
    }

    @Test
    @DisplayName("Should return null when nothing to undo")
    void shouldReturnNullWhenNothingToUndo() {
        // Arrange (no actions recorded)

        // Act
        String result = undoService.execute();

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should undo alternating operations")
    void shouldUndoAlternatingOperations() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Chess", 2, 2, "Strategy");
        
        collection.addGame(game1);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game1));

        collection.addGame(game2);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game2));

        collection.removeGame(game1);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.REMOVE, game1));

        assertEquals(1, collection.getGames().size());
        assertEquals("Chess", collection.getGames().get(0).title());

        // Act & Assert - Undo remove (re-add Catan)
        String result1 = undoService.execute();
        assertEquals("Added \"Catan\" back to collection", result1);
        assertEquals(2, collection.getGames().size());

        // Act & Assert - Undo add Chess
        String result2 = undoService.execute();
        assertEquals("Removed \"Chess\" from collection", result2);
        assertEquals(1, collection.getGames().size());

        // Act & Assert - Undo add Catan
        String result3 = undoService.execute();
        assertEquals("Removed \"Catan\" from collection", result3);
        assertEquals(0, collection.getGames().size());
    }

    @Test
    @DisplayName("Should track action types correctly")
    void shouldTrackActionTypesCorrectly() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");

        // Act
        undo.UndoableAction addAction = new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game);
        undo.UndoableAction removeAction = new undo.UndoableAction(undo.UndoableAction.ActionType.REMOVE, game);

        // Assert
        assertEquals(undo.UndoableAction.ActionType.ADD, addAction.getType());
        assertEquals(undo.UndoableAction.ActionType.REMOVE, removeAction.getType());
    }

    @Test
    @DisplayName("Should preserve game information in undo action")
    void shouldPreserveGameInformationInUndoAction() {
        // Arrange
        BoardGame game = new BoardGame("Pandemic", 2, 4, "Cooperative");

        // Act
        undo.UndoableAction action = new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game);

        // Assert
        assertEquals("Pandemic", action.getGame().title());
        assertEquals(2, action.getGame().minPlayers());
        assertEquals(4, action.getGame().maxPlayers());
        assertEquals("Cooperative", action.getGame().category());
    }

    @Test
    @DisplayName("Should handle three consecutive undos")
    void shouldHandleThreeConsecutiveUndos() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame game3 = new BoardGame("Poker", 2, 8, "Card");

        collection.addGame(game1);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game1));
        
        collection.addGame(game2);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game2));
        
        collection.addGame(game3);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game3));

        assertEquals(3, collection.getGames().size());

        // Act & Assert
        undoService.execute();
        assertEquals(2, collection.getGames().size());

        undoService.execute();
        assertEquals(1, collection.getGames().size());

        undoService.execute();
        assertEquals(0, collection.getGames().size());

        // Act & Assert - No more undos
        assertNull(undoService.execute());
    }

    @Test
    @DisplayName("Should handle undo after multiple add/remove cycles")
    void shouldHandleUndoAfterMultipleAddRemoveCycles() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");

        // Cycle 1: Add Catan
        collection.addGame(game);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game));

        // Cycle 2: Remove Catan
        collection.removeGame(game);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.REMOVE, game));

        // Cycle 3: Add Catan again
        collection.addGame(game);
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game));

        assertEquals(1, collection.getGames().size());

        // Act & Assert - Undo add
        String result1 = undoService.execute();
        assertEquals("Removed \"Catan\" from collection", result1);
        assertEquals(0, collection.getGames().size());

        // Act & Assert - Undo remove (re-add)
        String result2 = undoService.execute();
        assertEquals("Added \"Catan\" back to collection", result2);
        assertEquals(1, collection.getGames().size());

        // Act & Assert - Undo original add
        String result3 = undoService.execute();
        assertEquals("Removed \"Catan\" from collection", result3);
        assertEquals(0, collection.getGames().size());
    }

    @Test
    @DisplayName("Should record and retrieve multiple actions in order")
    void shouldRecordAndRetrieveMultipleActionsInOrder() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Chess", 2, 2, "Strategy");

        // Act
        undo.UndoableAction action1 = new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game1);
        undo.UndoableAction action2 = new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game2);

        undoManager.recordAction(action1);
        undoManager.recordAction(action2);

        assertTrue(undoManager.hasActions());

        // Assert
        undo.UndoableAction retrieved1 = undoManager.getLastAction();
        assertEquals("Chess", retrieved1.getGame().title());

        undo.UndoableAction retrieved2 = undoManager.getLastAction();
        assertEquals("Catan", retrieved2.getGame().title());
    }

    @Test
    @DisplayName("Should indicate when no actions remain")
    void shouldIndicateWhenNoActionsRemain() {
        // Arrange
        assertFalse(undoManager.hasActions());

        // Act
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");
        undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game));

        // Assert
        assertTrue(undoManager.hasActions());

        // Act
        undoManager.getLastAction();

        // Assert
        assertFalse(undoManager.hasActions());
    }

    @Test
    @DisplayName("Should undo with different game categories")
    void shouldUndoWithDifferentGameCategories() {
        // Arrange
        String[] categories = {"Strategy", "Family", "Cooperative", "Party", "Abstract"};
        
        for (String category : categories) {
            BoardGame game = new BoardGame("TestGame", 2, 4, category);
            collection.addGame(game);
            undoManager.recordAction(new undo.UndoableAction(undo.UndoableAction.ActionType.ADD, game));
        }

        assertEquals(5, collection.getGames().size());

        // Act & Assert
        for (int i = 0; i < 5; i++) {
            undoService.execute();
            assertEquals(4 - i, collection.getGames().size());
        }
    }
}