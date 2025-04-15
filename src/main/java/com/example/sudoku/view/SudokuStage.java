package com.example.sudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * Custom JavaFX Stage for displaying the Sudoku game window
 * Loads the FXML layout, sets up the window properties, and applies a custom icon
 */
public class SudokuStage extends Stage {

    /**
     * Constructs a new SudokuStage
     * Loads the FXML layout, creates the scene, sets the title
     * adds an icon, and displays the stage
     * @throws IOException if the FXML file cannot be loaded.
     */
    public SudokuStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/sudoku/sudoku-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        setTitle("Sudoku");
        setResizable(false);
        setScene(scene);

        // Set the window icon
        setIcon();

        show();
    }
    /**
     * Sets the icon for the window using the resource
     */
    private void setIcon() {
        getIcons().add(new Image(getClass().getResourceAsStream("/sudoku.png")));
    }
}
