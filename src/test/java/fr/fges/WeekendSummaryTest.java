package fr.fges;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Weekend Summary Tests")
class WeekendSummaryTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private GameCollection collection;
    private File tempFile;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    @DisplayName("Should display message when collection is empty on weekend")
    void shouldDisplayMessageWhenCollectionIsEmptyOnWeekend() {
        // Arrange
        tempFile = new File("test-weekly-summary-empty.json");
        collection = new GameCollection(tempFile.getPath());
        LocalDate saturday = LocalDate.of(2026, 2, 7);
        WeekendSummary weekendSummary = new WeekendSummary(collection, saturday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("No board games in collection"));
    }

    @Test
    @DisplayName("Should display all games when less than three on weekend")
    void shouldDisplayAllGamesWhenLessThanThreeOnWeekend() {
        // Arrange
        tempFile = new File("test-weekly-summary-two.json");
        collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Catan", 3, 4, "strategy"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "cooperative"));

        LocalDate sunday = LocalDate.of(2026, 2, 8);
        WeekendSummary weekendSummary = new WeekendSummary(collection, sunday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Summary (2 random games)"));
        assertTrue(output.contains("Collection has fewer than 3 games"));
        assertTrue(output.contains("Catan") || output.contains("Pandemic"));
    }

    @Test
    @DisplayName("Should display exactly three games when more than three on weekend")
    void shouldDisplayExactlyThreeGamesWhenMoreThanThreeOnWeekend() {
        // Arrange
        tempFile = new File("test-weekly-summary-many.json");
        collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Catan", 3, 4, "strategy"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "cooperative"));
        collection.addGame(new BoardGame("7 Wonders", 3, 7, "strategy"));
        collection.addGame(new BoardGame("Ticket to Ride", 2, 5, "family"));
        collection.addGame(new BoardGame("Azul", 2, 4, "family"));

        LocalDate saturday = LocalDate.of(2026, 2, 7);
        WeekendSummary weekendSummary = new WeekendSummary(collection, saturday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Summary (3 random games)"));
        assertFalse(output.contains("Collection has fewer than 3 games"));

        long dashCount = output.lines()
                .filter(line -> line.trim().startsWith("- "))
                .count();
        assertEquals(3, dashCount);
    }

    @Test
    @DisplayName("Should show weekend only message on weekdays")
    void shouldShowWeekendOnlyMessageOnWeekdays() {
        // Arrange
        tempFile = new File("test-weekly-summary-weekday.json");
        collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Catan", 3, 4, "strategy"));
        LocalDate monday = LocalDate.of(2026, 2, 9);
        WeekendSummary weekendSummary = new WeekendSummary(collection, monday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Weekend summary is only available on weekends"));
        assertFalse(output.contains("Summary"));
    }

    @Test
    @DisplayName("Should work on Saturday")
    void shouldWorkOnSaturday() {
        // Arrange
        tempFile = new File("test-saturday.json");
        collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "strategy"));
        LocalDate saturday = LocalDate.of(2026, 2, 7);
        WeekendSummary weekendSummary = new WeekendSummary(collection, saturday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Summary"));
        assertFalse(output.contains("only available on weekends"));
    }

    @Test
    @DisplayName("Should work on Sunday")
    void shouldWorkOnSunday() {
        // Arrange
        tempFile = new File("test-sunday.json");
        collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "strategy"));
        LocalDate sunday = LocalDate.of(2026, 2, 8);
        WeekendSummary weekendSummary = new WeekendSummary(collection, sunday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Summary"));
        assertFalse(output.contains("only available on weekends"));
    }

    @Test
    @DisplayName("Should not display summary on Monday")
    void shouldNotDisplaySummaryOnMonday() {
        // Arrange
        tempFile = new File("test-monday.json");
        collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "strategy"));
        LocalDate monday = LocalDate.of(2026, 2, 9);
        WeekendSummary weekendSummary = new WeekendSummary(collection, monday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("only available on weekends"));
    }

    @Test
    @DisplayName("Should not display summary on Friday")
    void shouldNotDisplaySummaryOnFriday() {
        // Arrange
        tempFile = new File("test-friday.json");
        collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "strategy"));
        LocalDate friday = LocalDate.of(2026, 2, 6);
        WeekendSummary weekendSummary = new WeekendSummary(collection, friday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("only available on weekends"));
    }

    @Test
    @DisplayName("Should display exactly one game when only one exists")
    void shouldDisplayExactlyOneGameWhenOnlyOneExists() {
        // Arrange
        tempFile = new File("test-weekly-summary-one.json");
        collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "strategy"));

        LocalDate saturday = LocalDate.of(2026, 2, 7);
        WeekendSummary weekendSummary = new WeekendSummary(collection, saturday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Summary (1 random games)") || output.contains("Summary (1 game)"));
        long dashCount = output.lines()
                .filter(line -> line.trim().startsWith("- "))
                .count();
        assertEquals(1, dashCount);
    }

    @Test
    @DisplayName("Should display all weekday names correctly")
    void shouldDisplayForAllWeekdays() {
        // Arrange
        String[] weekdayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        int[] weekdayOffsets = {2, 3, 4, 5, 6}; // Feb 9 is Monday, then +1 for each day

        for (int i = 0; i < weekdayNames.length; i++) {
            outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            tempFile = new File("test-weekday-" + i + ".json");
            collection = new GameCollection(tempFile.getPath());
            collection.addGame(new BoardGame("Chess", 2, 2, "strategy"));
            LocalDate weekday = LocalDate.of(2026, 2, weekdayOffsets[i]);
            WeekendSummary weekendSummary = new WeekendSummary(collection, weekday);

            // Act
            weekendSummary.execute();

            // Assert
            String output = outputStream.toString();
            assertTrue(output.contains("only available on weekends"), "Failed for " + weekdayNames[i]);

            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    @DisplayName("Should display three games with variety")
    void shouldDisplayThreeGamesWithVariety() {
        // Arrange
        tempFile = new File("test-variety.json");
        collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Catan", 3, 4, "strategy"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "cooperative"));
        collection.addGame(new BoardGame("7 Wonders", 3, 7, "strategy"));
        collection.addGame(new BoardGame("Ticket to Ride", 2, 5, "family"));

        LocalDate saturday = LocalDate.of(2026, 2, 7);
        WeekendSummary weekendSummary = new WeekendSummary(collection, saturday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Summary (3 random games)"));
        long dashCount = output.lines()
                .filter(line -> line.trim().startsWith("- "))
                .count();
        assertEquals(3, dashCount);
    }

    @Test
    @DisplayName("Should display game information correctly")
    void shouldDisplayGameInformationCorrectly() {
        // Arrange
        tempFile = new File("test-game-info.json");
        collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "strategy"));

        LocalDate saturday = LocalDate.of(2026, 2, 7);
        WeekendSummary weekendSummary = new WeekendSummary(collection, saturday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Chess"));
        assertTrue(output.contains("2-2 players") || output.contains("2 players"));
    }

    @Test
    @DisplayName("Should handle large collection")
    void shouldHandleLargeCollection() {
        // Arrange
        tempFile = new File("test-large-collection.json");
        collection = new GameCollection(tempFile.getPath());
        
        for (int i = 0; i < 20; i++) {
            collection.addGame(new BoardGame("Game" + i, 2, 4, "category"));
        }

        LocalDate saturday = LocalDate.of(2026, 2, 7);
        WeekendSummary weekendSummary = new WeekendSummary(collection, saturday);

        // Act
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Summary (3 random games)"));
        long dashCount = output.lines()
                .filter(line -> line.trim().startsWith("- "))
                .count();
        assertEquals(3, dashCount);
    }
}