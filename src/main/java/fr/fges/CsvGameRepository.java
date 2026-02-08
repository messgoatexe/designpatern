package fr.fges;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvGameRepository implements GameRepository {
    private final String filePath;
    private static final String HEADER = "title,minPlayers,maxPlayers,category";

    public CsvGameRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(List<BoardGame> games) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(HEADER);
            writer.newLine();
            for (BoardGame game : games) {
                writer.write(formatGame(game));
                writer.newLine();
            }
        }
    }

    @Override
    public List<BoardGame> load() throws IOException {
        List<BoardGame> games = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return games;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                BoardGame game = parseGame(line);
                if (game != null) {
                    games.add(game);
                }
            }
        }
        return games;
    }

    private String formatGame(BoardGame game) {
        return game.title() + "," + game.minPlayers() + "," + game.maxPlayers() + "," + game.category();
    }

    private BoardGame parseGame(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) {
            return null;
        }
        try {
            return new BoardGame(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
