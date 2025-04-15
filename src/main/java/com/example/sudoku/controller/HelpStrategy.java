package com.example.sudoku.controller;

/**
 * Interface for different help strategies in Sudoku.
 */
public interface HelpStrategy {
    /**
     * Finds the first empty cell that can be filled with a valid number and highlights it with a suggestion.
     */
    void provideSuggestion();
}
