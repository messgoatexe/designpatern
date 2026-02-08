package fr.fges;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

@DisplayName("Games For X Players Tests")
class GamesForXPlayersTest {

    private GameCollection collection;
    private File tempFile;

    @BeforeEach
    void setUp() {
        tempFile = new File("test-games-for-x-players.json");
        collection = new GameCollection(tempFile.getPath());
    }

    @AfterEach
    void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    @DisplayName("Should return games compatible with 2 players")
    void shouldReturnGamesCompatibleWith2Players() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "Cooperative"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(2);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(g -> g.title().equals("Chess")));
        assertTrue(result.stream().anyMatch(g -> g.title().equals("Pandemic")));
        assertFalse(result.stream().anyMatch(g -> g.title().equals("Catan")));
    }

    @Test
    @DisplayName("Should return games compatible with 3 players")
    void shouldReturnGamesCompatibleWith3Players() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "Cooperative"));
        collection.addGame(new BoardGame("7 Wonders", 3, 7, "Strategy"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(3);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(g -> g.title().equals("Catan")));
        assertTrue(result.stream().anyMatch(g -> g.title().equals("Pandemic")));
        assertTrue(result.stream().anyMatch(g -> g.title().equals("7 Wonders")));
        assertFalse(result.stream().anyMatch(g -> g.title().equals("Chess")));
    }

    @Test
    @DisplayName("Should return empty list when no games match")
    void shouldReturnEmptyListWhenNoGamesMatch() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(10);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return empty list when collection is empty")
    void shouldReturnEmptyListWhenCollectionIsEmpty() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(2);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return all games when player count fits all ranges")
    void shouldReturnAllGamesWhenPlayerCountFitsAllRanges() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 2, 4, "Family"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "Cooperative"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(2);

        // Assert
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Should handle single player games")
    void shouldHandleSinglePlayerGames() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Solitaire", 1, 1, "Solo"));
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        // Act
        List<BoardGame> result1 = filter.getGamesForXPlayers(1);
        List<BoardGame> result2 = filter.getGamesForXPlayers(2);

        // Assert
        assertEquals(1, result1.size());
        assertTrue(result1.stream().anyMatch(g -> g.title().equals("Solitaire")));
        assertEquals(1, result2.size());
        assertTrue(result2.stream().anyMatch(g -> g.title().equals("Chess")));
    }

    @Test
    @DisplayName("Should handle large player counts")
    void shouldHandleLargePlayerCounts() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("King of Tokyo", 2, 6, "Party"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "Cooperative"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(6);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(g -> g.title().equals("King of Tokyo")));
    }

    @Test
    @DisplayName("Should return correct game at min boundary")
    void shouldReturnCorrectGameAtMinBoundary() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(3);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Catan", result.get(0).title());
    }

    @Test
    @DisplayName("Should return correct game at max boundary")
    void shouldReturnCorrectGameAtMaxBoundary() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(4);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Catan", result.get(0).title());
    }

    @Test
    @DisplayName("Should not return game below min players")
    void shouldNotReturnGameBelowMinPlayers() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(2);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should not return game above max players")
    void shouldNotReturnGameAboveMaxPlayers() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(5);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should preserve game information in results")
    void shouldPreserveGameInformationInResults() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Pandemic", 2, 4, "Cooperative"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(2);

        // Assert
        assertEquals(1, result.size());
        BoardGame game = result.get(0);
        assertEquals("Pandemic", game.title());
        assertEquals(2, game.minPlayers());
        assertEquals(4, game.maxPlayers());
        assertEquals("Cooperative", game.category());
    }

    @Test
    @DisplayName("Should filter multiple games with different ranges")
    void shouldFilterMultipleGamesWithDifferentRanges() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Solitaire", 1, 1, "Solo"));
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        collection.addGame(new BoardGame("7 Wonders", 3, 7, "Strategy"));
        collection.addGame(new BoardGame("King of Tokyo", 2, 6, "Party"));

        // Act
        List<BoardGame> result1 = filter.getGamesForXPlayers(1);
        List<BoardGame> result2 = filter.getGamesForXPlayers(2);
        List<BoardGame> result3 = filter.getGamesForXPlayers(3);
        List<BoardGame> result4 = filter.getGamesForXPlayers(4);
        List<BoardGame> result5 = filter.getGamesForXPlayers(7);

        // Assert
        assertEquals(1, result1.size());
        assertEquals(3, result2.size());
        assertEquals(4, result3.size());
        assertEquals(4, result4.size());
        assertEquals(2, result5.size());
    }

    @Test
    @DisplayName("Should maintain insertion order in results")
    void shouldMaintainInsertionOrderInResults() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "Cooperative"));
        collection.addGame(new BoardGame("7 Wonders", 3, 7, "Strategy"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(3);

        // Assert
        assertEquals(3, result.size());
        assertEquals("Catan", result.get(0).title());
        assertEquals("Pandemic", result.get(1).title());
        assertEquals("7 Wonders", result.get(2).title());
    }

    @Test
    @DisplayName("Should handle zero players correctly")
    void shouldHandleZeroPlayersCorrectly() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(0);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should handle negative player count")
    void shouldHandleNegativePlayerCount() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(-1);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should handle very large player count")
    void shouldHandleVeryLargePlayerCount() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("King of Tokyo", 2, 6, "Party"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(1000);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return copy of list (immutability test)")
    void shouldReturnNewListInstance() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));

        // Act
        List<BoardGame> result1 = filter.getGamesForXPlayers(2);
        List<BoardGame> result2 = filter.getGamesForXPlayers(2);

        // Assert
        assertNotSame(result1, result2);
        assertEquals(result1.size(), result2.size());
    }

    @Test
    @DisplayName("Should work with large number of games")
    void shouldWorkWithLargeNumberOfGames() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        for (int i = 1; i <= 10; i++) {
            collection.addGame(new BoardGame("Game" + i, i, i + 3, "Category"));
        }

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(5);

        // Assert
        assertEquals(3, result.size()); // Games with range containing 5
    }

    @Test
    @DisplayName("Should filter with same min and max players")
    void shouldFilterWithSameMinAndMaxPlayers() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));

        // Act
        List<BoardGame> result = filter.getGamesForXPlayers(2);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Chess", result.get(0).title());
    }

    @Test
    @DisplayName("Should handle player count at exact range")
    void shouldHandlePlayerCountAtExactRange() {
        // Arrange
        gamesForXPlayers filter = new gamesForXPlayers(collection);
        collection.addGame(new BoardGame("Medium Game", 4, 6, "Family"));

        // Act
        List<BoardGame> result4 = filter.getGamesForXPlayers(4);
        List<BoardGame> result5 = filter.getGamesForXPlayers(5);
        List<BoardGame> result6 = filter.getGamesForXPlayers(6);
        List<BoardGame> result3 = filter.getGamesForXPlayers(3);
        List<BoardGame> result7 = filter.getGamesForXPlayers(7);

        // Assert
        assertEquals(1, result4.size());
        assertEquals(1, result5.size());
        assertEquals(1, result6.size());
        assertEquals(0, result3.size());
        assertEquals(0, result7.size());
    }
}
