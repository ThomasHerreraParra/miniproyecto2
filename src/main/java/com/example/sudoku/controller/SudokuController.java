package com.example.sudoku.controller;

import com.example.sudoku.model.Board;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import java.util.function.UnaryOperator;

public class SudokuController {
    @FXML
    private GridPane boardGridPane;

    private Board board;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setText("");
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

                String baseStyle =
                        "-fx-font-size: 16px;" +
                                "-fx-background-color: #ffffff;" +
                                "-fx-border-color: #dfe6e9;" +
                                "-fx-border-width: 1.0;" +
                                "-fx-border-style: solid;";

                String borderRadius = switch (row + "," + col) {
                    case "0,0" -> "-fx-background-radius: 10 0 0 0; -fx-border-radius: 10 0 0 0;";
                    case "0,5" -> "-fx-background-radius: 0 10 0 0; -fx-border-radius: 0 10 0 0;";
                    case "5,0" -> "-fx-background-radius: 0 0 0 10; -fx-border-radius: 0 0 0 10;";
                    case "5,5" -> "-fx-background-radius: 0 0 10 0; -fx-border-radius: 0 0 10 0;";
                    default -> "-fx-background-radius: 0; -fx-border-radius: 0;";
                };

                // Colores por defecto (todos claros)
                String top = "#dfe6e9";
                String right = "#dfe6e9";
                String bottom = "#dfe6e9";
                String left = "#dfe6e9";

                // Aplicamos bordes más oscuros si es borde de bloque
                if (col == 2) right = "#636e72"; // borde derecho del bloque vertical
                if (row == 1 || row == 3) bottom = "#636e72"; // borde inferior del bloque horizontal

                String darkBorders = "-fx-border-color: " + top + " " + right + " " + bottom + " " + left + ";";

                textField.setUserData(borderRadius);
                textField.setStyle(baseStyle + borderRadius + darkBorders);
                textField.setMaxSize(35, 35);
                textField.setAlignment(Pos.CENTER);
                textField.setBackground(null);

                if (number > 0) {
                    textField.setText(String.valueOf(number));
                    textField.setEditable(false);
                } else {
                    textField.setText("");
                }

                boardGridPane.setRowIndex(textField, row);
                boardGridPane.setColumnIndex(textField, col);
                boardGridPane.getChildren().add(textField);

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
            // Solo permite números del 1 al 6
            UnaryOperator<TextFormatter.Change> filter = change -> {
                String newText = change.getControlNewText();
                if (newText.matches("[1-6]?")) {
                    return change;
                }
                return null;
            };
            textField.setTextFormatter(new TextFormatter<>(filter));

            // Valida el número y aplica el color del borde
            textField.setOnKeyReleased(event -> {
                String text = textField.getText();

                if (!textField.isEditable()) {
                    return;
                }

                if (text.isEmpty()) {
                    // Restablecer estilo a blanco y borde normal cuando el campo está vacío
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #dfe6e9; " +  // Borde normal
                                    "-fx-border-width: 1px; " +
                                    textField.getUserData() + // Mantener el border-radius de la esquina específica
                                    "-fx-background-radius: 8;"
                    );
                    errorLabel.setText("");  // Limpiar mensaje de error
                    return;
                }

                int number = Integer.parseInt(text);
                String validationResult = board.isValid(row, col, number);

                if (validationResult.equals("Válido")) {
                    // Aplicar estilo con color de fondo y borde cuando el número es válido
                    textField.setStyle(
                            "-fx-background-color: #dff9fb;" +
                                    "-fx-border-color: #74b9ff;" +
                                    "-fx-border-width: 2;" +
                                    textField.getUserData() + // Mantener el border-radius de la esquina específica
                                    "-fx-background-radius: 8;"
                    );
                    errorLabel.setText("");
                    textField.setEditable(false);
                    board.getBoard().get(row).set(col, number);
                } else {
                    // Aplicar estilo con color de fondo y borde cuando el número es inválido
                    textField.setStyle(
                            "-fx-background-color: #ffa7a7;" +
                                    "-fx-border-color: #ff7675;" +
                                    "-fx-border-width: 2;" +
                                    textField.getUserData() + // Mantener el border-radius de la esquina específica
                                    "-fx-background-radius: 8;"
                    );
                    displayErrorMessage(validationResult);
                }
            });

        }

        private void displayErrorMessage(String errorType) {
            String message = switch (errorType) {
                case "Fila" -> "Error: el número ya existe en esta fila.";
                case "Columna" -> "Error: el número ya existe en esta columna.";
                case "Bloque" -> "Error: el número ya existe en este bloque 2x3.";
                default -> "";
            };

            errorLabel.setText(message);
        }
    }
}