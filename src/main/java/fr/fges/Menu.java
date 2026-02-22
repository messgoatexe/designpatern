package fr.fges;

import java.util.Scanner;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class Menu {

    private final GameCollection collection;
    private final Scanner scanner;
    private final undo.UndoManager undoManager;
    private final AddGame addGame;
    private final RemoveGame removeGame;
    private final ListGames listGames;
    private final RecommendGame recommendGame;
    private final gamesForXPlayers GamesForXPlayers;
    private final WeekendSummary WeekendSummary;
    private final undo.UndoService undoService;
    private final TournamentMode tournamentMode;

    public Menu(GameCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
        this.undoManager = new undo.UndoManager();
        this.addGame = new AddGame(collection, scanner, undoManager);
        this.removeGame = new RemoveGame(collection, scanner, undoManager);
        this.listGames = new ListGames(collection);
        this.recommendGame = new RecommendGame(collection);
        this.GamesForXPlayers = new gamesForXPlayers(collection);
        this.WeekendSummary = new WeekendSummary(collection);
        this.undoService = new undo.UndoService(collection, undoManager);
        this.tournamentMode = new TournamentMode(collection, scanner);
    }

    public void displayMainMenu() {
        String menuText =" ";
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY || LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY) {
            menuText = """
                
                === Board Game Collection ===
                1. Add Board Game
                2. Remove Board Game
                3. List All Board Games
                4. Recommend Game
                5. View Summary (Weekend Special)
                6. Undo Last Action
                7. Games for X Players 
                8. Tournament Mode
                9. Exit
                Please select an option (1-9):
                """;
        } else {
            menuText = """
                    
                    === Board Game Collection ===
                    1. Add Board Game
                    2. Remove Board Game
                    3. List All Board Games
                    4. Recommend Game
                    5. Undo Last Action
                    6. Games for X Players
                    7. Tournament Mode
                    8. Exit
                    Please select an option (1-8):
                    """;
        }
        System.out.print(menuText);
    }

    public void handleMenu() {
        displayMainMenu();

        String choice = scanner.nextLine();
        if (LocalDate.now().getDayOfWeek() != DayOfWeek.SATURDAY && LocalDate.now().getDayOfWeek() != DayOfWeek.SUNDAY) {
             switch (choice) {
                case "1" -> addGame.execute();
                case "2" -> removeGame.execute();
                case "3" -> listGames.execute();
                case "4" -> recommendGame.execute();
                case "5" -> {
                    String result = undoService.execute();
                    if (result == null) {
                        System.out.println("Nothing to undo.");
                    } else {
                        System.out.println("Undone: " + result);
                    }
                }
                case "6" -> GamesForXPlayers.execute();
                case "7" -> tournamentMode.execute();
                case "8" -> exit();
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
            return;
        }
        switch (choice) {
            case "1" -> addGame.execute();
            case "2" -> removeGame.execute();
            case "3" -> listGames.execute();
            case "4" -> recommendGame.execute();
            case "5" -> WeekendSummary.execute();
            case "6" -> {
                String result = undoService.execute();
                if (result == null) {
                    System.out.println("Nothing to undo.");
                } else {
                    System.out.println("Undone: " + result);
                }
            }
            case "7" -> GamesForXPlayers.execute();
            case "8" -> tournamentMode.execute();
            case "9" -> exit();
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private void exit() {
        System.out.println("Exiting the application. Goodbye!");
        System.exit(0);
    }
}