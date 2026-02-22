package fr.fges;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String storageFile = "data.json"; // Fichier par défaut
        
        if (args.length > 0) {
            storageFile = args[0];
        }

        if (!storageFile.endsWith(".json") && !storageFile.endsWith(".csv")) {
            System.out.println("Error: Storage file must have .json or .csv extension");
            System.exit(1);
        }

        GameCollection collection = new GameCollection(storageFile);
        collection.loadFromFile();

        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(collection, scanner);

        System.out.println("Using storage file: " + storageFile);

        while (true) {
            menu.handleMenu();
        }
    }
}