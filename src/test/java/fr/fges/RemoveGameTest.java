package fr.fges;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class RemoveGameTest {

    @Test
    void shouldRemoveGameWhenTitleExists() {
        // Arrange
        GameCollection mockCollection = mock(GameCollection.class);
        Scanner mockScanner = mock(Scanner.class);

        BoardGame game1 = new BoardGame("Chess", 2, 2, "Strategy");
        BoardGame game2 = new BoardGame("Catan", 3, 4, "Family");

        List<BoardGame> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);

        when(mockCollection.getGames()).thenReturn(games);
        when(mockScanner.nextLine()).thenReturn("Catan");

        RemoveGame removeGame = new RemoveGame(mockCollection, mockScanner);

        // Act
        removeGame.execute();

        // Assert
        verify(mockCollection).removeGame(game2);
    }

    @Test
    void shouldNotRemoveGameWhenTitleDoesNotExist() {
        // Arrange
        GameCollection mockCollection = mock(GameCollection.class);
        Scanner mockScanner = mock(Scanner.class);

        BoardGame game = new BoardGame("Chess", 2, 2, "Strategy");

        when(mockCollection.getGames()).thenReturn(List.of(game));
        when(mockScanner.nextLine()).thenReturn("Monopoly");

        RemoveGame removeGame = new RemoveGame(mockCollection, mockScanner);

        // Act
        removeGame.execute();

        // Assert
        verify(mockCollection, never()).removeGame(any());
    }

    @Test
    void shouldNotRemoveGameWhenCollectionIsEmpty() {
        // Arrange
        GameCollection mockCollection = mock(GameCollection.class);
        Scanner mockScanner = mock(Scanner.class);

        when(mockCollection.getGames()).thenReturn(List.of());

        RemoveGame removeGame = new RemoveGame(mockCollection, mockScanner);

        // Act
        removeGame.execute();

        // Assert
        verify(mockCollection, never()).removeGame(any());
    }
}
