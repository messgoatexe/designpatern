package fr.fges;

import java.util.Scanner;

public class Menu {

    private final GameCollection collection;
    private final Scanner scanner;
    private final AddGame addGame;
    private final RemoveGame removeGame;
    private final ListGames listGames;
    private final RecommendGame recommendGame;

    public Menu(GameCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
        this.addGame = new AddGame(collection, scanner);
        this.removeGame = new RemoveGame(collection, scanner);
        this.listGames = new ListGames(collection);
        this.recommendGame =  new RecommendGame(collection);
    }

    public void displayMainMenu() {
        String menuText = """
                === Board Game Collection ===
                1. Add Board Game
                2. Remove Board Game
                3. List All Board Games
                4. Exit
                5. Recommend Game
                Please select an option (1-4):
                """;

        System.out.println(menuText);
    }

    public void handleMenu() {
        displayMainMenu();

        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addGame.execute();
            case "2" -> removeGame.execute();
            case "3" -> listGames.execute();
            case "4" -> exit();
            case "5" -> recommendGame.execute();
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private void exit() {
        System.out.println("Exiting the application. Goodbye!");
        System.exit(0);
    }
}