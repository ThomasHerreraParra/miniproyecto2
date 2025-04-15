package com.example.sudoku.controller;

import com.example.sudoku.model.Board;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Default implementation of the interface
 * Provides a basic hint by finding the first empty cell that can accept a valid number according to Sudoku rules, and then highlights it on the board.
 */
public class DefaultHelpStrategy implements HelpStrategy {

    private final Board board;
    private final GridPane boardGridPane;

    /**
     * Constructs a new DefaultHelpStrategy with the given board and UI grid.
     * @param board         the logical Sudoku board model
     * @param boardGridPane the UI representation of the board
     */
    public DefaultHelpStrategy(Board board, GridPane boardGridPane) {
        this.board = board;
        this.boardGridPane = boardGridPane;
    }

    /**
     * Finds the first empty cell that can be filled with a valid number and highlights it with a suggestion.
     */
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


    /**
     * Highlights a suggested move by placing the candidate number in the cell and applying a distinct style to indicate the hint
     * @param row       the row index of the suggested cell
     * @param col       the column index of the suggested cell
     * @param candidate the valid number to be suggested
     */
    private void highlightSuggestion(int row, int col, int candidate) {
        for (javafx.scene.Node node : boardGridPane.getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);

            if (nodeRow != null && nodeCol != null && nodeRow == row && nodeCol == col && node instanceof TextField) {
                TextField tf = (TextField) node;
                if (tf.getText().isEmpty()) {
                    // Recover the previously saved borderRadius
                    String borderRadius = (String) tf.getUserData();

                    tf.setText(String.valueOf(candidate));
                    tf.setEditable(false);

                    // Apply the style with the retrieved borderRadius
                    tf.setStyle(
                            "-fx-background-color: #ffeaa7; " +
                                    "-fx-border-color: #ffe44f; " +
                                    "-fx-border-width: 2; " +
                                    borderRadius // Keep the original borderRadius
                    );

                    // Update the board with the suggestion
                    board.getBoard().get(row).set(col, candidate);
                    break;
                }
            }
        }
    }
}
