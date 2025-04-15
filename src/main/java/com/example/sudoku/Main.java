package com.example.sudoku;

import com.example.sudoku.view.SudokuStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class of the Sudoku application
 * Launches the JavaFX application and opens the Sudoku window
 */
public class Main extends Application {

    /**
     * Entry point of the JavaFX application, creates and displays a new SudokuStage.
     * @param stage the primary stage (not used directly in this implementation)
     * @throws IOException if the Sudoku FXML layout cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
       new SudokuStage();
    }

    /**
     * Launches the JavaFX application lifecycle.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}