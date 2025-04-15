package com.example.sudoku.controller;

import com.example.sudoku.model.Board;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class DefaultHelpStrategy implements HelpStrategy {

    private final Board board;
    private final GridPane boardGridPane;

    public DefaultHelpStrategy(Board board, GridPane boardGridPane) {
        this.board = board;
        this.boardGridPane = boardGridPane;
    }

    @Override
    public void provideSuggestion() {
        for (int row = 0; row < board.getBoard().size(); row++) {
            for (int col = 0; col < board.getBoard().size(); col++) {
                if (board.getBoard().get(row).get(col) == 0) {
                    for (int candidate = 1; candidate <= 6; candidate++) {
                        if (board.isValid(row, col, candidate).equals("Válido")) {
                            highlightSuggestion(row, col, candidate);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void highlightSuggestion(int row, int col, int candidate) {
        for (javafx.scene.Node node : boardGridPane.getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);

            if (nodeRow != null && nodeCol != null && nodeRow == row && nodeCol == col && node instanceof TextField) {
                TextField tf = (TextField) node;
                if (tf.getText().isEmpty()) {
                    // Recuperar el borderRadius guardado previamente
                    String borderRadius = (String) tf.getUserData();

                    tf.setText(String.valueOf(candidate));
                    tf.setEditable(false);

                    // Aplicar el estilo con el borderRadius recuperado
                    tf.setStyle(
                            "-fx-background-color: #ffeaa7; " +
                                    "-fx-border-color: #ffe44f; " +
                                    "-fx-border-width: 2; " +
                                    borderRadius // Mantener el borderRadius original
                    );

                    // Actualizar el tablero con la sugerencia
                    board.getBoard().get(row).set(col, candidate);
                    break;
                }
            }
        }
    }
}
