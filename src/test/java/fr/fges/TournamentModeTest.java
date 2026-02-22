package fr.fges;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tournament Mode Tests")
class TournamentModeTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private GameCollection collection;
    private File tempFile;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        tempFile = new File("test-tournament-temp.json");
        collection = new GameCollection(tempFile.getPath());
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }


    private TournamentMode buildMode(String simulatedInput) {
        Scanner scanner = new Scanner(new StringReader(simulatedInput));
        return new TournamentMode(collection, scanner);
    }


    @Test
    @DisplayName("Should display error when no 2-player games are available")
    void shouldDisplayErrorWhenNoTwoPlayerGamesAvailable() {
        // Arrange – only a 3-6 player game
        collection.addGame(new BoardGame("Concept", 4, 12, "Party"));
        TournamentMode mode = buildMode("");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("No 2-player games available"));
    }

    @Test
    @DisplayName("Should display error when collection is empty")
    void shouldDisplayErrorWhenCollectionIsEmpty() {
        // Arrange
        TournamentMode mode = buildMode("");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("No 2-player games available"));
    }


    @Test
    @DisplayName("Should display available 2-player games")
    void shouldDisplayAvailableTwoPlayerGames() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Catan", 3, 4, "Family")); // not 2-player
        // Input: pick game 1, 3 participants, then cancel with invalid mode
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Chess"));
        assertFalse(output.contains("Catan")); // Catan does not support 2 players
    }

    @Test
    @DisplayName("Should include game with range covering 2 players")
    void shouldIncludeGameWithRangeCoveringTwoPlayers() {
        // Arrange – minPlayers=1, maxPlayers=5 covers 2
        collection.addGame(new BoardGame("Pandemic", 1, 5, "Cooperative"));
        TournamentMode mode = buildMode("1\n3\nA\nB\nC\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Pandemic"));
    }

    @Test
    @DisplayName("Should cancel tournament on invalid game selection")
    void shouldCancelTournamentOnInvalidGameSelection() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("99\n"); // out of range

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid selection") || output.contains("Tournament cancelled"));
    }

    @Test
    @DisplayName("Should cancel tournament on non-numeric game input")
    void shouldCancelTournamentOnNonNumericGameInput() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("abc\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid input") || output.contains("Tournament cancelled"));
    }

    @Test
    @DisplayName("Should accept participant count of 3")
    void shouldAcceptParticipantCountOfThree() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Alice") && output.contains("Bob") && output.contains("Charlie"));
    }

    @Test
    @DisplayName("Should accept participant count of 8")
    void shouldAcceptParticipantCountOfEight() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n8\nP1\nP2\nP3\nP4\nP5\nP6\nP7\nP8\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Number of participants: 8"));
    }

    @Test
    @DisplayName("Should reject participant count below 3")
    void shouldRejectParticipantCountBelowThree() {
        // Arrange – first invalid (2), then valid (3)
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n2\n3\nAlice\nBob\nCharlie\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("between 3 and 8"));
    }

    @Test
    @DisplayName("Should reject participant count above 8")
    void shouldRejectParticipantCountAboveEight() {
        // Arrange – first invalid (9), then valid (3)
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n9\n3\nAlice\nBob\nCharlie\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("between 3 and 8"));
    }

    @Test
    @DisplayName("Should reject non-numeric participant count and retry")
    void shouldRejectNonNumericParticipantCount() {
        // Arrange – non-numeric then valid
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\nabc\n3\nAlice\nBob\nCharlie\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("valid number"));
    }


    @Test
    @DisplayName("Should reject empty player name and retry")
    void shouldRejectEmptyPlayerNameAndRetry() {
        // Arrange – empty name then valid name for player 1
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\n\nAlice\nBob\nCharlie\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("cannot be empty"));
        assertTrue(output.contains("Alice"));
    }

    @Test
    @DisplayName("Should collect all player names")
    void shouldCollectAllPlayerNames() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n4\nAlice\nBob\nCharlie\nDiana\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("Bob"));
        assertTrue(output.contains("Charlie"));
        assertTrue(output.contains("Diana"));
    }


    @Test
    @DisplayName("Should display tournament setup summary")
    void shouldDisplayTournamentSetupSummary() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Tournament Setup"));
        assertTrue(output.contains("Chess"));
        assertTrue(output.contains("Number of participants: 3"));
    }

    @Test
    @DisplayName("Should display selected game category in summary")
    void shouldDisplaySelectedGameCategoryInSummary() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Strategy"));
    }


    @Test
    @DisplayName("Should run round-robin tournament when mode 1 is selected")
    void shouldRunRoundRobinTournament() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n1\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Round-Robin"));
    }

    @Test
    @DisplayName("Should display correct number of matches in round-robin with 3 players")
    void shouldDisplayCorrectMatchCountRoundRobinThreePlayers() {
        // Arrange – 3 players => C(3,2) = 3 matches
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n1\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Match 3")); // at least 3 matches displayed
        assertFalse(output.contains("Match 4")); // no 4th match
    }

    @Test
    @DisplayName("Should display correct number of matches in round-robin with 4 players")
    void shouldDisplayCorrectMatchCountRoundRobinFourPlayers() {
        // Arrange – 4 players => C(4,2) = 6 matches
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n4\nAlice\nBob\nCharlie\nDiana\n1\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Match 6"));
        assertFalse(output.contains("Match 7"));
    }

    @Test
    @DisplayName("Should display final ranking after round-robin")
    void shouldDisplayFinalRankingAfterRoundRobin() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n1\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Final Ranking"));
        assertTrue(output.contains("Tournament Winner"));
    }

    @Test
    @DisplayName("Should display points and wins in final ranking")
    void shouldDisplayPointsAndWinsInFinalRanking() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n1\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("points"));
        assertTrue(output.contains("wins"));
    }


    @Test
    @DisplayName("Should run king-of-the-hill tournament when mode 2 is selected")
    void shouldRunKingOfTheHillTournament() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n2\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("King of the Hill"));
    }

    @Test
    @DisplayName("Should display correct number of matches in king-of-the-hill with 3 players")
    void shouldDisplayCorrectMatchCountKingOfHillThreePlayers() {
        // Arrange – 3 players => 2 matches (n-1)
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n2\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Match 2"));
        assertFalse(output.contains("Match 3"));
    }

    @Test
    @DisplayName("Should label first player as King in king-of-the-hill")
    void shouldLabelFirstPlayerAsKing() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n2\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("King"));
    }

    @Test
    @DisplayName("Should display final ranking after king-of-the-hill")
    void shouldDisplayFinalRankingAfterKingOfTheHill() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n2\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Final Ranking"));
        assertTrue(output.contains("Tournament Winner"));
    }

    @Test
    @DisplayName("Should cancel tournament on invalid mode selection")
    void shouldCancelTournamentOnInvalidModeSelection() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid selection") || output.contains("Tournament cancelled"));
    }

    @Test
    @DisplayName("Should cancel tournament on non-numeric mode input")
    void shouldCancelTournamentOnNonNumericModeInput() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        TournamentMode mode = buildMode("1\n3\nAlice\nBob\nCharlie\nxyz\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid input") || output.contains("Tournament cancelled"));
    }

    @Test
    @DisplayName("Should exclude games whose max players is below 2")
    void shouldExcludeGamesWhoseMaxPlayersIsBelowTwo() {
        // Arrange – solo-only game
        collection.addGame(new BoardGame("Solitaire", 1, 1, "Solo"));
        TournamentMode mode = buildMode("");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("No 2-player games available"));
    }

    @Test
    @DisplayName("Should exclude games whose min players is above 2")
    void shouldExcludeGamesWhoseMinPlayersIsAboveTwo() {
        // Arrange – needs at least 3 players
        collection.addGame(new BoardGame("Catan", 3, 4, "Family"));
        TournamentMode mode = buildMode("");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("No 2-player games available"));
    }

    @Test
    @DisplayName("Should include only 2-player-compatible games in list")
    void shouldIncludeOnlyTwoPlayerCompatibleGamesInList() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));     // ✓
        collection.addGame(new BoardGame("Concept", 4, 12, "Party"));     // ✗
        collection.addGame(new BoardGame("Pandemic", 2, 4, "Cooperative")); // ✓
        TournamentMode mode = buildMode("1\n3\nA\nB\nC\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Chess"));
        assertTrue(output.contains("Pandemic"));
        assertFalse(output.contains("Concept"));
    }


    @Test
    @DisplayName("Should use second game when player selects 2")
    void shouldUseSecondGameWhenPlayerSelectsTwo() {
        // Arrange
        collection.addGame(new BoardGame("Chess", 2, 2, "Strategy"));
        collection.addGame(new BoardGame("Go", 2, 2, "Abstract"));
        TournamentMode mode = buildMode("2\n3\nA\nB\nC\n99\n");

        // Act
        mode.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Go"));
    }
}