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
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void shouldDisplayMessageWhenCollectionIsEmptyOnWeekend() {
        File tempFile = new File("test-weekly-summary-empty.json");
        tempFile.deleteOnExit();
        GameCollection collection = new GameCollection(tempFile.getPath());
        
        LocalDate saturday = LocalDate.of(2026, 2, 7); 
        WeekendSummary weekendSummary = new WeekendSummary(collection, saturday);

        weekendSummary.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("No board games in collection"));
    }

    @Test
    void shouldDisplayAllGamesWhenLessThanThreeOnWeekend() {
        File tempFile = new File("test-weekly-summary-two.json");
        tempFile.deleteOnExit();
        GameCollection collection = new GameCollection(tempFile.getPath());

        collection.addGame(new BoardGame("Catan", 3, 4, "strategy"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "cooperative"));

        LocalDate sunday = LocalDate.of(2026, 2, 8); 
        WeekendSummary weekendSummary = new WeekendSummary(collection, sunday);

        weekendSummary.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Summary (2 random games)"));
        assertTrue(output.contains("Collection has fewer than 3 games"));
        assertTrue(output.contains("Catan") || output.contains("Pandemic"));
    }

    @Test
    void shouldDisplayExactlyThreeGamesWhenMoreThanThreeOnWeekend() {
        File tempFile = new File("test-weekly-summary-many.json");
        tempFile.deleteOnExit();
        GameCollection collection = new GameCollection(tempFile.getPath());

        collection.addGame(new BoardGame("Catan", 3, 4, "strategy"));
        collection.addGame(new BoardGame("Pandemic", 2, 4, "cooperative"));
        collection.addGame(new BoardGame("7 Wonders", 3, 7, "strategy"));
        collection.addGame(new BoardGame("Ticket to Ride", 2, 5, "family"));
        collection.addGame(new BoardGame("Azul", 2, 4, "family"));

        LocalDate saturday = LocalDate.of(2026, 2, 7);
        WeekendSummary weekendSummary = new WeekendSummary(collection, saturday);

        weekendSummary.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Summary (3 random games)"));
        assertFalse(output.contains("Collection has fewer than 3 games"));

        long dashCount = output.lines()
                .filter(line -> line.trim().startsWith("- "))
                .count();
        assertEquals(3, dashCount);
    }

    @Test
    void shouldShowWeekendOnlyMessageOnWeekdays() {
        File tempFile = new File("test-weekly-summary-weekday.json");
        tempFile.deleteOnExit();
        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Catan", 3, 4, "strategy"));
        LocalDate monday = LocalDate.of(2026, 2, 9); // Lundi
        WeekendSummary weekendSummary = new WeekendSummary(collection, monday);
        weekendSummary.execute();
        String output = outputStream.toString();
        assertTrue(output.contains("Weekend summary is only available on weekends"));
        assertFalse(output.contains("Summary"));
    }
    
    @Test
    void shouldWorkOnSaturday() {
        File tempFile = new File("test-saturday.json");
        tempFile.deleteOnExit();
        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "strategy"));

        LocalDate saturday = LocalDate.of(2026, 2, 7);
        WeekendSummary weekendSummary = new WeekendSummary(collection, saturday);

        weekendSummary.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Summary"));
        assertFalse(output.contains("only available on weekends"));
    }
    
    @Test
    void shouldWorkOnSunday() {
        File tempFile = new File("test-sunday.json");
        tempFile.deleteOnExit();
        GameCollection collection = new GameCollection(tempFile.getPath());
        collection.addGame(new BoardGame("Chess", 2, 2, "strategy"));

        LocalDate sunday = LocalDate.of(2026, 2, 8);
        WeekendSummary weekendSummary = new WeekendSummary(collection, sunday);

        weekendSummary.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Summary"));
        assertFalse(output.contains("only available on weekends"));
    }
}