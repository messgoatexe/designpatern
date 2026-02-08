package fr.fges;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonGameRepository implements GameRepository {
    private final String filePath;

    public JsonGameRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(List<BoardGame> games) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), games);
    }

    @Override
    public List<BoardGame> load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);
        if (!file.exists()) {
            return List.of();
        }
        return mapper.readValue(file, new TypeReference<List<BoardGame>>() {});
    }
}
