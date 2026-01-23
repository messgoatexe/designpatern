package fr.fges;

import static fr.fges.Menu.getUserInput;

public class AddGame {

    public static void addGame() {
        String title = "";
        while (title.isEmpty()) {
            title = getUserInput("Title").trim();
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty. Please enter a valid title.");
            }
        }

        int minPlayers = 0;
        boolean validMin = false;
        while (!validMin) {
            String minInput = getUserInput("Minimum Players").trim();
            if (minInput.isEmpty()) {
                System.out.println("Minimum players cannot be empty. Please enter a number (1-15).");
                continue;
            }
            try {
                minPlayers = Integer.parseInt(minInput);
                if (minPlayers >= 1 && minPlayers <= 15) {
                    validMin = true;
                } else {
                    System.out.println("Please enter a valid number of minimum players (1-15).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number of minimum players (1-15).");
            }
        }

        int maxPlayers = 0;
        boolean validMax = false;
        while (!validMax) {
            String maxInput = getUserInput("Maximum Players").trim();
            if (maxInput.isEmpty()) {
                System.out.println("Maximum players cannot be empty. Please enter a number (" + minPlayers + "-30).");
                continue;
            }
            try {
                maxPlayers = Integer.parseInt(maxInput);
                if (maxPlayers >= minPlayers && maxPlayers <= 30) {
                    validMax = true;
                } else {
                    System.out.println("Please enter a valid number of maximum players (" + minPlayers + "-30).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number of maximum players (" + minPlayers + "-30).");
            }
        }

        String category = "";
        while (category.isEmpty()) {
            category = getUserInput("Category (e.g., fantasy, cooperative, family, strategy)").trim();
            if (category.isEmpty()) {
                System.out.println("Category cannot be empty. Please enter a valid category.");
            }
        }

        BoardGame game = new BoardGame(title, minPlayers, maxPlayers, category);
        GameCollection.addGame(game);
        System.out.println("Board game added successfully.");
    }

}