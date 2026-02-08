package fr.fges;

public class RepositoryFactory {
    public static GameRepository create(String filePath) {
        if (filePath.endsWith(".csv")) {
            return new CsvGameRepository(filePath);
        }
        return new JsonGameRepository(filePath);
    }
}
