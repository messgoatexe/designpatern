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

class WeekendSummaryTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Capturer la sortie console
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restaurer la sortie console
        System.setOut(originalOut);
    }

    @Test
    void shouldDisplayMessageWhenCollectionIsEmpty() {
        // Arrange
        File tempFile = new File("test-weekly-summary-empty.json");
        tempFile.deleteOnExit();
        GameCollection collection = new GameCollection(tempFile.getPath());
        WeekendSummary weekendSummary = new WeekendSummary(collection);

        // Act
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        if (today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY) {
            assertTrue(output.contains("No board games in collection"));
        } else {
            assertTrue(output.contains("Weekend summary is only available on weekends"));
        }
    }


    @Test
    void shouldDisplayAllGamesWhenLessThanThree() {
        // Arrange
        File tempFile = new File("test-weekly-summary-two.json");
        tempFile.deleteOnExit();
        GameCollection collection = new GameCollection(tempFile.getPath());

        collection.addGame(new BoardGame("Catan", 3, 4, "strategy"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "cooperative"));

        WeekendSummary weekendSummary = new WeekendSummary(collection);

        // Act
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        if (today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY) {
            assertTrue(output.contains("Summary (2 random games)"));
            assertTrue(output.contains("Collection has fewer than 3 games"));
            assertTrue(output.contains("Catan") || output.contains("Pandemic"));
        }
    }

    @Test
    void shouldDisplayExactlyThreeGamesWhenMoreThanThree() {
        // Arrange
        File tempFile = new File("test-weekly-summary-many.json");
        tempFile.deleteOnExit();
        GameCollection collection = new GameCollection(tempFile.getPath());

        collection.addGame(new BoardGame("Catan", 3, 4, "strategy"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "cooperative"));
        collection.addGame(new BoardGame("7 Wonders", 3, 7, "strategy"));
        collection.addGame(new BoardGame("Ticket to Ride", 2, 5, "family"));
        collection.addGame(new BoardGame("Azul", 2, 4, "family"));

        WeekendSummary weekendSummary = new WeekendSummary(collection);

        // Act
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        if (today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY) {
            assertTrue(output.contains("Summary (3 random games)"));
            assertFalse(output.contains("Collection has fewer than 3 games"));

            // Compter le nombre de tirets (chaque jeu commence par "- ")
            long dashCount = output.lines()
                    .filter(line -> line.trim().startsWith("- "))
                    .count();
            assertEquals(3, dashCount);
        }
    }

    @Test
    void shouldShowWeekendOnlyMessageOnWeekdays() {
        // Arrange
        File tempFile = new File("test-weekly-summary-weekday.json");
        tempFile.deleteOnExit();
        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Catan", 3, 4, "strategy"));

        WeekendSummary weekendSummary = new WeekendSummary(collection);

        // Act
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        weekendSummary.execute();

        // Assert
        String output = outputStream.toString();
        if (today != DayOfWeek.SATURDAY && today != DayOfWeek.SUNDAY) {
            assertTrue(output.contains("Weekend summary is only available on weekends"));
            assertFalse(output.contains("Summary"));
        }
    }
}