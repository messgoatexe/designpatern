package fr.fges;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("BoardGame Record Tests")
class BoardGameTest {

    @Test
    @DisplayName("Should create BoardGame with correct values")
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
    @DisplayName("Should allow different min and max players")
    void shouldAllowDifferentMinAndMaxPlayers() {
        // Arrange
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");

        // Act & Assert
        assertTrue(game.minPlayers() < game.maxPlayers());
    }

    @Test
    @DisplayName("Should consider two games equal when all fields are equal")
    void shouldConsiderTwoGamesEqualWhenAllFieldsAreEqual() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");

        // Act & Assert
        assertEquals(game1, game2);
    }

    @Test
    @DisplayName("Should consider two games different when title is different")
    void shouldConsiderTwoGamesDifferentWhenTitleIsDifferent() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Chess", 2, 2, "Strategy");

        // Act & Assert
        assertNotEquals(game1, game2);
    }

    @Test
    @DisplayName("Should consider two games different when min players differ")
    void shouldConsiderTwoGamesDifferentWhenMinPlayersDiffer() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Catan", 2, 4, "Family");

        // Act & Assert
        assertNotEquals(game1, game2);
    }

    @Test
    @DisplayName("Should consider two games different when max players differ")
    void shouldConsiderTwoGamesDifferentWhenMaxPlayersDiffer() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Catan", 3, 5, "Family");

        // Act & Assert
        assertNotEquals(game1, game2);
    }

    @Test
    @DisplayName("Should consider two games different when category differs")
    void shouldConsiderTwoGamesDifferentWhenCategoryDiffers() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Strategy");

        // Act & Assert
        assertNotEquals(game1, game2);
    }

    @Test
    @DisplayName("Should handle single player games")
    void shouldHandleSinglePlayerGames() {
        // Arrange & Act
        BoardGame game = new BoardGame("Solitaire", 1, 1, "Solo");

        // Assert
        assertEquals(1, game.minPlayers());
        assertEquals(1, game.maxPlayers());
    }

    @Test
    @DisplayName("Should handle games with many players")
    void shouldHandleGamesWithManyPlayers() {
        // Arrange & Act
        BoardGame game = new BoardGame("King of Tokyo", 2, 6, "Party");

        // Assert
        assertEquals(2, game.minPlayers());
        assertEquals(6, game.maxPlayers());
    }

    @Test
    @DisplayName("Should preserve case sensitivity in title")
    void shouldPreserveCaseSensitivityInTitle() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("catan", 3, 4, "Family");

        // Act & Assert
        assertNotEquals(game1, game2);
    }

    @Test
    @DisplayName("Should preserve case sensitivity in category")
    void shouldPreserveCaseSensitivityInCategory() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "family");

        // Act & Assert
        assertNotEquals(game1, game2);
    }

    @Test
    @DisplayName("Should have correct hashCode for equal objects")
    void shouldHaveCorrectHashCodeForEqualObjects() {
        // Arrange
        BoardGame game1 = new BoardGame("Catan", 3, 4, "Family");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");

        // Act & Assert
        assertEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    @DisplayName("Should have toString representation")
    void shouldHaveToStringRepresentation() {
        // Arrange & Act
        BoardGame game = new BoardGame("Catan", 3, 4, "Family");
        String toString = game.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("Catan"));
    }
}
