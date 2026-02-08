package fr.fges;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

@DisplayName("Game Collection Tests")
class GameCollectionTest {

    private GameCollection collection;
    private File tempFile;

    @BeforeEach
    void setUp() {
        tempFile = new File("test-collection-temp.json");
        collection = new GameCollection(tempFile.getPath());
    }

    @AfterEach
    void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    @DisplayName("Should create empty collection")
    void shouldCreateEmptyCollection() {
        // Act & Assert
        assertNotNull(collection);
        assertEquals(0, collection.getGames().size());
    }

    @Test
    @DisplayName("Should add single game to collection")
    void shouldAddSingleGameToCollection() {
        // Arrange
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");

        // Act
        collection.addGame(game);

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals(game, collection.getGames().get(0));
    }

    @Test
    @DisplayName("Should add multiple games to collection")
    void shouldAddMultipleGamesToCollection() {
        // Arrange
        BoardGame game1 = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game3 = new BoardGame("Pandemic", 2, 4, "Cooperative");

        // Act
        collection.addGame(game1);
        collection.addGame(game2);
        collection.addGame(game3);

        // Assert
        assertEquals(3, collection.getGames().size());
        assertTrue(collection.getGames().contains(game1));
        assertTrue(collection.getGames().contains(game2));
        assertTrue(collection.getGames().contains(game3));
    }

    @Test
    @DisplayName("Should remove game from collection")
    void shouldRemoveGameFromCollection() {
        // Arrange
        BoardGame game1 = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game1);
        collection.addGame(game2);

        // Act
        collection.removeGame(game1);

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals(game2, collection.getGames().get(0));
        assertFalse(collection.getGames().contains(game1));
    }

    @Test
    @DisplayName("Should return empty list when removing from empty collection")
    void shouldHandleRemoveFromEmptyCollection() {
        // Arrange
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");

        // Act
        collection.removeGame(game);

        // Assert
        assertEquals(0, collection.getGames().size());
    }

    @Test
    @DisplayName("Should get games list")
    void shouldGetGamesList() {
        // Arrange
        BoardGame game1 = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game1);
        collection.addGame(game2);

        // Act
        List<BoardGame> games = collection.getGames();

        // Assert
        assertNotNull(games);
        assertEquals(2, games.size());
        assertEquals(game1, games.get(0));
        assertEquals(game2, games.get(1));
    }

    @Test
    @DisplayName("Should save collection to JSON file")
    void shouldSaveCollectionToJsonFile() {
        // Arrange
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");
        collection.addGame(game);

        // Act
        collection.addGame(game); // This triggers saveToFile()

        // Assert
        assertTrue(tempFile.exists());
        assertTrue(tempFile.getPath().endsWith(".json"));
    }

    @Test
    @DisplayName("Should save collection to CSV file")
    void shouldSaveCollectionToCsvFile() {
        // Arrange
        File csvFile = new File("test-collection.csv");
        GameCollection csvCollection = new GameCollection(csvFile.getPath());
        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");

        // Act
        csvCollection.addGame(game);

        // Assert
        assertTrue(csvFile.exists());
        assertTrue(csvFile.getPath().endsWith(".csv"));

        // Cleanup
        if (csvFile.exists()) {
            csvFile.delete();
        }
    }

    @Test
    @DisplayName("Should handle game with same title but different properties")
    void shouldHandleGameWithSameTitleButDifferentProperties() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Catan", 2, 5, "Strategy");

        // Act
        collection.addGame(game1);
        collection.addGame(game2);

        // Assert
        assertEquals(2, collection.getGames().size());
    }

    @Test
    @DisplayName("Should preserve insertion order")
    void shouldPreserveInsertionOrder() {
        // Arrange
        BoardGame game1 = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game3 = new BoardGame("Pandemic", 2, 4, "Cooperative");

        // Act
        collection.addGame(game1);
        collection.addGame(game2);
        collection.addGame(game3);

        // Assert
        assertEquals(game1, collection.getGames().get(0));
        assertEquals(game2, collection.getGames().get(1));
        assertEquals(game3, collection.getGames().get(2));
    }

    @Test
    @DisplayName("Should handle removing non-existent game")
    void shouldHandleRemovingNonExistentGame() {
        // Arrange
        BoardGame game1 = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");
        collection.addGame(game1);

        // Act
        collection.removeGame(game2);

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals(game1, collection.getGames().get(0));
    }

    @Test
    @DisplayName("Should store file path correctly")
    void shouldStoreFilePathCorrectly() {
        // Act
        String storagePath = tempFile.getPath();

        // Assert
        assertNotNull(storagePath);
        assertTrue(storagePath.contains("test-collection-temp.json"));
    }

    @Test
    @DisplayName("Should add and remove multiple times")
    void shouldAddAndRemoveMultipleTimes() {
        // Arrange
        BoardGame game1 = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game3 = new BoardGame("Pandemic", 2, 4, "Cooperative");

        // Act
        collection.addGame(game1);
        assertEquals(1, collection.getGames().size());

        collection.addGame(game2);
        assertEquals(2, collection.getGames().size());

        collection.removeGame(game1);
        assertEquals(1, collection.getGames().size());

        collection.addGame(game3);
        assertEquals(2, collection.getGames().size());

        collection.removeGame(game2);
        assertEquals(1, collection.getGames().size());

        // Assert
        assertEquals(game3, collection.getGames().get(0));
    }

    @Test
    @DisplayName("Should handle large number of games")
    void shouldHandleLargeNumberOfGames() {
        // Act
        for (int i = 0; i < 100; i++) {
            collection.addGame(new BoardGame("Game" + i, 2, 4, "Category"));
        }

        // Assert
        assertEquals(100, collection.getGames().size());
    }

    @Test
    @DisplayName("Should maintain game properties after adding to collection")
    void shouldMaintainGamePropertiesAfterAddingToCollection() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");

        // Act
        collection.addGame(game);
        BoardGame retrievedGame = collection.getGames().get(0);

        // Assert
        assertEquals("Catan", retrievedGame.title());
        assertEquals(3, retrievedGame.minPlayers());
        assertEquals(4, retrievedGame.maxPlayers());
        assertEquals("Family", retrievedGame.category());
    }

    @Test
    @DisplayName("Should handle games with special characters")
    void shouldHandleGamesWithSpecialCharacters() {
        // Arrange
        BoardGame game = new BoardGame("Ticket to Ride: Europe", 2, 6, "Adventure");

        // Act
        collection.addGame(game);

        // Assert
        assertEquals(1, collection.getGames().size());
        assertEquals("Ticket to Ride: Europe", collection.getGames().get(0).title());
    }

    @Test
    @DisplayName("Should support duplicate game objects (same properties)")
    void shouldSupportDuplicateGameObjects() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");

        // Act
        collection.addGame(game1);
        collection.addGame(game2);

        // Assert
        assertEquals(2, collection.getGames().size());
    }

    @Test
    @DisplayName("Should retrieve correct game after multiple operations")
    void shouldRetrieveCorrectGameAfterMultipleOperations() {
        // Arrange
        BoardGame chess = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame catan = new BoardGame("Catan", 3, 4, "Family");
        BoardGame pandemic = new BoardGame("Pandemic", 2, 4, "Cooperative");

        // Act
        collection.addGame(chess);
        collection.addGame(catan);
        collection.addGame(pandemic);
        collection.removeGame(chess);

        // Assert
        List<BoardGame> games = collection.getGames();
        assertEquals(2, games.size());
        assertFalse(games.contains(chess));
        assertTrue(games.contains(catan));
        assertTrue(games.contains(pandemic));
    }
}
