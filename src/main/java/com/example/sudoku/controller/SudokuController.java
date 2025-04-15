package com.example.sudoku.controller;

import com.example.sudoku.model.Board;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

import java.util.function.UnaryOperator;

public class SudokuController {
    @FXML
    private GridPane boardGridPane;

    private Board board;

    @FXML
    public void initialize() {
        fillBoard();
    }

    @FXML
    private Button helpButton;

    @FXML
    private void helpAction() {
        for (int row = 0; row < board.getBoard().size(); row++) {
            for (int col = 0; col < board.getBoard().size(); col++) {
                if (board.getBoard().get(row).get(col) == 0) {
                    for (int candidate = 1; candidate <= 6; candidate++) {
                        if (board.isValid(row, col, candidate)) {
                            highlightSuggestion(row, col, candidate);
                            return;
                        }
                    }
                }
            }
        }
    }

    private class DefaultHelpStrategy {
        public void provideSuggestion() {
            for (int row = 0; row < board.getBoard().size(); row++) {
                for (int col = 0; col < board.getBoard().size(); col++) {
                    if (board.getBoard().get(row).get(col) == 0) { // Celda vacía
                        for (int candidate = 1; candidate <= 6; candidate++) {
                            if (board.isValid(row, col, candidate)) {
                                highlightSuggestion(row, col, candidate); // Resalta sugerencia
                                return;
                            }
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
                    tf.setText(String.valueOf(candidate));
                    tf.setEditable(false);
                    tf.setStyle("-fx-background-color: yellow; -fx-border-color: blue; -fx-border-width: 1.5;");
                    board.getBoard().get(row).set(col, candidate);
                    break;
                }
            }
        }
    }



    private void fillBoard() {
        board = new Board();

        for (int row = 0; row < board.getBoard().size(); row++) {
            for (int col = 0; col < board.getBoard().size(); col++) {
                int number = board.getBoard().get(row).get(col);
                TextField textField = new TextField();
                textField.setMaxSize(35, 35);
                textField.setAlignment(Pos.CENTER);
                textField.setBackground(null);

                if(number > 0){
                    textField.setText(String.valueOf(number));
                    textField.setEditable(false);
                } else {
                    textField.setText("");
                }

                boardGridPane.setRowIndex(textField, row);
                boardGridPane.setColumnIndex(textField, col);
                boardGridPane.getChildren().add(textField);
                handleNumberTextField(textField, row, col);
            }
        }
    }

    private void handleNumberTextField(TextField textField, int row, int col) {
        // only allows numbers from 1 to 6 to be written
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[1-6]?")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));

        // validates number and applies border color
        textField.setOnKeyReleased(event -> {
            String text = textField.getText();

            if (text.isEmpty()) {
                textField.setStyle("");
                return;
            }

            int number = Integer.parseInt(text);

            if (board.isValid(row, col, number)) {
                textField.setStyle("-fx-border-color: blue; -fx-border-width: 1.5; -fx-background-insets: 0;");
                textField.setEditable(false);
                board.getBoard().get(row).set(col, number);
            } else if(textField.isEditable()) {
                textField.setStyle("-fx-border-color: red; -fx-border-width: 1.5;");
            }
        });
    }


}
