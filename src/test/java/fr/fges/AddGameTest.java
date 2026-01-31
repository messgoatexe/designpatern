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

        String input = String.join("\n",
                "Catan",
                "3",
                "4",
                "Family"
        );

        Scanner scanner = new Scanner(input);
        AddGame addGame = new AddGame(collection, scanner);

        // Act
        addGame.execute();

        // Assert
        assertEquals(1, collection.getGames().size());
        BoardGame game = collection.getGames().get(0);
        assertEquals("Catan", game.title());
        assertEquals(3, game.minPlayers());
        assertEquals(4, game.maxPlayers());
        assertEquals("Family", game.category());
    }

    @Test
    void shouldNotAddGameWithDuplicateName() {

        File tempFile = new File("test-games-duplicate.json");
        tempFile.deleteOnExit();

        GameCollection collection = new GameCollection(tempFile.getPath());

        String firstInput = String.join("\n",
                "Catan",
                "3",
                "4",
                "Family"
        );

        Scanner firstScanner = new Scanner(firstInput);
        AddGame firstAddGame = new AddGame(collection, firstScanner);
        firstAddGame.execute();

        // On essaye d'ajouter un doublon (même nom)
        String secondInput = String.join("\n",
                "catan",
                "2",
                "5",
                "Strategy"
        );

        Scanner secondScanner = new Scanner(secondInput);
        AddGame secondAddGame = new AddGame(collection, secondScanner);

        secondAddGame.execute();

        // On vérifie que la collection n'a qu'un seul jeu
        assertEquals(1, collection.getGames().size());
        BoardGame game = collection.getGames().get(0);
        // Le jeu original de change pas
        assertEquals("Catan", game.title());
        assertEquals(3, game.minPlayers());
        assertEquals(4, game.maxPlayers());
        assertEquals("Family", game.category());
    }
}
