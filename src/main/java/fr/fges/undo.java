package fr.fges;

import java.util.Stack;

public class undo {
    
    // Classe pour représenter une action annulable
    public static class UndoableAction {
        public enum ActionType {
            ADD,
            REMOVE
        }
        
        private final ActionType type;
        private final BoardGame game;
        
        public UndoableAction(ActionType type, BoardGame game) {
            this.type = type;
            this.game = game;
        }
        
        public ActionType getType() {
            return type;
        }
        
        public BoardGame getGame() {
            return game;
        }
    }
    
    // Manager pour gérer l'historique des actions
    public static class UndoManager {
        private final Stack<UndoableAction> history = new Stack<>();
        
        public void recordAction(UndoableAction action) {
            history.push(action);
        }
        
        public UndoableAction getLastAction() {
            if (history.isEmpty()) {
                return null;
            }
            return history.pop();
        }
        
        public boolean hasActions() {
            return !history.isEmpty();
        }
        
        public void clear() {
            history.clear();
        }
    }
    
    // Service pour effectuer l'undo
    public static class UndoService {
        private final GameCollection collection;
        private final UndoManager undoManager;
        
        public UndoService(GameCollection collection, UndoManager undoManager) {
            this.collection = collection;
            this.undoManager = undoManager;
        }
        
        public String execute() {
            if (!undoManager.hasActions()) {
                return null;
            }
            
            UndoableAction lastAction = undoManager.getLastAction();
            
            if (lastAction == null) {
                return null;
            }
            
            BoardGame game = lastAction.getGame();
            
            switch (lastAction.getType()) {
                case ADD:
                    // Si on a ajouté, on supprime (sans enregistrer)
                    collection.removeGame(game);
                    return "Removed \"" + game.title() + "\" from collection";
                case REMOVE:
                    // Si on a supprimé, on ré-ajoute (sans enregistrer)
                    collection.addGame(game);
                    return "Added \"" + game.title() + "\" back to collection";
                default:
                    return null;
            }
        }
    }
}