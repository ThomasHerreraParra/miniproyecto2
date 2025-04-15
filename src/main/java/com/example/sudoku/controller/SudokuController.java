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

/**
 * Controller class for the Sudoku game implemented using JavaFX
 * Responsible for rendering the Sudoku board, handling user input, providing help via a strategy, and validating number entries
 */
public class SudokuController {
    @FXML
    private GridPane boardGridPane;

    private Board board;

    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller
     * This method is automatically called after the FXML fields are injected
     */
    @FXML
    public void initialize() {
        errorLabel.setText("");
        fillBoard();
    }

    /**
     * Action triggered when the help button is pressed
     */
    @FXML
    private void helpAction() {
        HelpStrategy helper = new DefaultHelpStrategy(board, boardGridPane);
        helper.provideSuggestion();
    }

    /**
     * Fills the Sudoku board with initial values and prepares the UI
     * Applies styling, sets up input validation, and creates cell handlers
     */
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

                // Default colors (all light)
                String top = "#dfe6e9";
                String right = "#dfe6e9";
                String bottom = "#dfe6e9";
                String left = "#dfe6e9";

// Vertical borders
                if (col == 2) right = "#636e72"; // borde derecho del bloque vertical
                if (col == 3) left = "#636e72";  // borde izquierdo del bloque siguiente

// horizontal borders
                if (row == 1 || row == 3) bottom = "#636e72"; // borde inferior del bloque horizontal
                if (row == 2 || row == 4) top = "#636e72";    // borde superior del bloque siguiente



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


    // Inner class for validating numbers in cells
    private class NumberValidationHandler {
        private TextField textField;
        private int row;
        private int col;

        /**
         * Constructs a new NumberValidationHandler for the specified cell
         * @param textField the text field representing the cell
         * @param row       the row index of the cell
         * @param col       the column index of the cell
         */
        public NumberValidationHandler(TextField textField, int row, int col) {
            this.textField = textField;
            this.row = row;
            this.col = col;
            handleNumberTextField();
        }

        /**
         * Configures the input filtering, validation, and dynamic styling for the cell.
         */
        private void handleNumberTextField() {
            // Only allows numbers from 1 to 6
            UnaryOperator<TextFormatter.Change> filter = change -> {
                String newText = change.getControlNewText();
                if (newText.matches("[1-6]?")) {
                    return change;
                }
                return null;
            };
            textField.setTextFormatter(new TextFormatter<>(filter));

            //Validates the number and applies the border color
            textField.setOnKeyReleased(event -> {
                String text = textField.getText();
                if (!textField.isEditable()) {
                    return;
                }
                if (text.isEmpty()) {
                    // Reset style to white and normal border when field is empty
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #dfe6e9; " +  // normal border
                                    "-fx-border-width: 1px; " +
                                    textField.getUserData() + // Maintain the border-radius of the specific corner
                                    "-fx-background-radius: 8;"
                    );
                    errorLabel.setText("");  // Clear error message
                    return;
                }

                int number = Integer.parseInt(text);
                String validationResult = board.isValid(row, col, number);

                if (validationResult.equals("Válido")) {
                    // Apply style with background and border color when the number is valid
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
                    // Apply style with background and border color when the number is invalid
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

        /**
         * Displays a custom error message based on the validation result
         * @param errorType the validation error type (row, column, or block)
         */
        private void displayErrorMessage(String errorType) {
            String message = switch (errorType) {
                case "Fila" -> "El número ya existe en esta fila";
                case "Columna" -> "El número ya existe en esta columna";
                case "Bloque" -> "El número ya existe en este bloque 2x3";
                default -> "";
            };

            errorLabel.setText(message);
        }
    }
}