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
        HelpStrategy helper = new DefaultHelpStrategy(board, boardGridPane);
        helper.provideSuggestion();
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

                // Clase interna para manejar los eventos de validación y cambios en los TextFields
                new NumberValidationHandler(textField, row, col);
            }
        }
    }

    // Clase interna para la validación de los números en las celdas
    private class NumberValidationHandler {
        private TextField textField;
        private int row;
        private int col;

        public NumberValidationHandler(TextField textField, int row, int col) {
            this.textField = textField;
            this.row = row;
            this.col = col;
            handleNumberTextField();
        }

        private void handleNumberTextField() {
            // solo permite números del 1 al 6
            UnaryOperator<TextFormatter.Change> filter = change -> {
                String newText = change.getControlNewText();
                if (newText.matches("[1-6]?")) {
                    return change;
                }
                return null;
            };
            textField.setTextFormatter(new TextFormatter<>(filter));

            // valida el número y aplica el color del borde
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
}
