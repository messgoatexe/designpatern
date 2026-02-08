package fr.fges;

import java.util.List;

public interface GameRepository {
    void save(List<BoardGame> games) throws Exception;
    List<BoardGame> load() throws Exception;
}
