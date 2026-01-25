package fr.fges;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BoardGameTest {

    @Test
    void shouldCreateBoardGameWithCorrectValues() {
        // Arrange
        String title = "Chess";
        int minPlayers = 2;
        int maxPlayers = 2;
        String category = "Strategy";

        // Act
        BoardGame game = new BoardGame(title, minPlayers, maxPlayers, category);

        // Assert
        assertEquals("Chess", game.title());
        assertEquals(2, game.minPlayers());
        assertEquals(2, game.maxPlayers());
        assertEquals("Strategy", game.category());
    }

    @Test
    void shouldAllowDifferentMinAndMaxPlayers() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");

        // Act & Assert
        assertTrue(game.minPlayers() < game.maxPlayers());
    }

    @Test
    void shouldConsiderTwoGamesEqualWhenAllFieldsAreEqual() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");

        // Act & Assert
        assertEquals(game1, game2);
    }

    @Test
    void shouldConsiderTwoGamesDifferentWhenTitleIsDifferent() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Chess", 2, 2, "Strategy");

        // Act & Assert
        assertNotEquals(game1, game2);
    }
}
