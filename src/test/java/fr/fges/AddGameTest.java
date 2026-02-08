package fr.fges;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class AddGameTest {

    @Test
    void shouldAddGameToCollection() {
        // Arrange
        File tempFile = new File("test-games.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();

        String input = String.join("\n",
                "Catan",
                "3",
                "4",
                "Family"
        );

        Scanner scanner = new Scanner(input);
        AddGame addGame = new AddGame(collection, scanner, undoManager);

        // Act
        addGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        BoardGame game = collection.getGames().get(0);
        assertEquals("Catan", game.title());
        assertEquals(3, game.minPlayers());
        assertEquals(4, game.maxPlayers());
        assertEquals("Family", game.category());
        
        // Vérifier que l'action a été enregistrée dans l'undo
        assertTrue(undoManager.hasActions());
    }

    @Test
    void shouldNotAddGameWithDuplicateName() {
        File tempFile = new File("test-games-duplicate.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();

        String firstInput = String.join("\n",
                "Catan",
                "3",
                "4",
                "Family"
        );

        Scanner firstScanner = new Scanner(firstInput);
        AddGame firstAddGame = new AddGame(collection, firstScanner, undoManager);
        firstAddGame.execute();

        // On essaye d'ajouter un doublon (même nom)
        String secondInput = String.join("\n",
                "catan",
                "2",
                "5",
                "Strategy"
        );

        Scanner secondScanner = new Scanner(secondInput);
        AddGame secondAddGame = new AddGame(collection, secondScanner, undoManager);

        secondAddGame.execute();

        // On vérifie que la collection n'a qu'un seul jeu
        assertEquals(1, collection.getGames().size());
        BoardGame game = collection.getGames().get(0);
        // Le jeu original ne change pas
        assertEquals("Catan", game.title());
        assertEquals(3, game.minPlayers());
        assertEquals(4, game.maxPlayers());
        assertEquals("Family", game.category());
        
        // Vérifier qu'une seule action a été enregistrée (le doublon n'est pas ajouté)
        assertTrue(undoManager.hasActions());
        undoManager.getLastAction(); // Retire la première action
        assertFalse(undoManager.hasActions()); // Plus d'actions
    }

    @Test
    void shouldRecordActionInUndoManager() {
        File tempFile = new File("test-undo-recording.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();

        String input = String.join("\n",
                "Chess",
                "2",
                "2",
                "Strategy"
        );

        Scanner scanner = new Scanner(input);
        AddGame addGame = new AddGame(collection, scanner, undoManager);

        // Vérifier qu'il n'y a pas d'actions avant
        assertFalse(undoManager.hasActions());

        addGame.execute();

        // Vérifier qu'une action a été enregistrée
        assertTrue(undoManager.hasActions());
        
        undo.UndoableAction action = undoManager.getLastAction();
        assertNotNull(action);
        assertEquals(undo.UndoableAction.ActionType.ADD, action.getType());
        assertEquals("Chess", action.getGame().title());
    }

    @Test
    void shouldHandleMultipleAdds() {
        File tempFile = new File("test-multiple-adds.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());
        undo.UndoManager undoManager = new undo.UndoManager();

        // Ajouter premier jeu
        String input1 = String.join("\n", "Catan", "3", "4", "Family");
        Scanner scanner1 = new Scanner(input1);
        AddGame addGame1 = new AddGame(collection, scanner1, undoManager);
        addGame1.execute();

        // Ajouter deuxième jeu
        String input2 = String.join("\n", "Chess", "2", "2", "Strategy");
        Scanner scanner2 = new Scanner(input2);
        AddGame addGame2 = new AddGame(collection, scanner2, undoManager);
        addGame2.execute();

        // Ajouter troisième jeu
        String input3 = String.join("\n", "Poker", "2", "8", "Card");
        Scanner scanner3 = new Scanner(input3);
        AddGame addGame3 = new AddGame(collection, scanner3, undoManager);
        addGame3.execute();

        assertEquals(3, collection.getGames().size());
        assertTrue(undoManager.hasActions());
    }
}