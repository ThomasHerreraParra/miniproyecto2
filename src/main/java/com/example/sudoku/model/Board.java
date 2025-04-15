package com.example.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class generates a 6x6 board divided into 2x3 blocks.
 * In each 2x3 block exactly one cell is assigned a random number (from 1 to 6),
 * and all the other cells are left as 0. Additionally, the placed number is not repeated
 * in any row or column across the entire board.
 *
 * The board is represented as a list of lists (ArrayLists) rather than using arrays,
 * and the board is generated using a backtracking algorithm that works block by block.
 *
 * Java JDK 17.
 */
public class Board {
    // Board dimensions and block dimensions.
    private final int SIZE = 6;
    private final int BLOCK_ROWS = 2;
    private final int BLOCK_COLS = 3;

    // Number of block rows and block columns.
    private final int TOTAL_BLOCK_ROWS = SIZE / BLOCK_ROWS; // 6/2 = 3
    private final int TOTAL_BLOCK_COLS = SIZE / BLOCK_COLS; // 6/3 = 2
    private final int TOTAL_BLOCKS = TOTAL_BLOCK_ROWS * TOTAL_BLOCK_COLS; // 3 * 2 = 6

    // The board represented as a List of Lists (each inner list is a row)
    private final List<List<Integer>> board;
    private final Random random = new Random();

    /**
     * Constructor initializes the board with zeros and then fills each block with one number.
     */
    public Board() {
        board = new ArrayList<>();
        // Initialize the board with zeros.
        for (int i = 0; i < SIZE; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                row.add(0);
            }
            board.add(row);
        }
        // Attempt to fill each block with a valid number.
        if (!fillBlocks(0)) {
            System.out.println("Failed to generate the Sudoku board.");
        }
    }

    /**
     * Randomly fills each 2x3 block with two number.
     *
     * @param blockIndex the index of the current block (ranging from 0 to TOTAL_BLOCKS - 1).
     * @return true if all blocks have been successfully filled; false otherwise.
     */
    private boolean fillBlocks(int blockIndex) {
        if (blockIndex == TOTAL_BLOCKS) {
            return true;
        }

        int blockRow = blockIndex / TOTAL_BLOCK_COLS;
        int blockCol = blockIndex % TOTAL_BLOCK_COLS;
        int startRow = blockRow * BLOCK_ROWS;
        int startCol = blockCol * BLOCK_COLS;

        // Create a list of all positions at the block and mix it
        List<int[]> blockPositions = new ArrayList<>();
        for (int i = 0; i < BLOCK_ROWS; i++) {
            for (int j = 0; j < BLOCK_COLS; j++) {
                blockPositions.add(new int[]{startRow + i, startCol + j});
            }
        }
        Collections.shuffle(blockPositions, random); // Randomiza posiciones

        List<Integer> numbers = new ArrayList<>();
        for (int n = 1; n <= SIZE; n++) {
            numbers.add(n);
        }
        Collections.shuffle(numbers, random); // Randomiza números

        // Intentar todas las combinaciones posibles de 2 celdas y 2 números
        for (int i = 0; i < blockPositions.size(); i++) {
            for (int j = i + 1; j < blockPositions.size(); j++) {
                int[] pos1 = blockPositions.get(i);
                int[] pos2 = blockPositions.get(j);

                for (int m = 0; m < numbers.size(); m++) {
                    for (int n = m + 1; n < numbers.size(); n++) {
                        int num1 = numbers.get(m);
                        int num2 = numbers.get(n);

                        if (isValid(pos1[0], pos1[1], num1) && isValid(pos2[0], pos2[1], num2)) {
                            board.get(pos1[0]).set(pos1[1], num1);
                            board.get(pos2[0]).set(pos2[1], num2);

                            if (fillBlocks(blockIndex + 1)) {
                                return true;
                            }

                            // Backtrack
                            board.get(pos1[0]).set(pos1[1], 0);
                            board.get(pos2[0]).set(pos2[1], 0);
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * Checks whether placing a candidate number at cell (row, col) violates the row or column uniqueness.
     *
     * @param row       the row index.
     * @param col       the column index.
     * @param candidate the number to place (from 1 to 6).
     * @return true if the candidate can be placed without conflict; false otherwise.
     */
    public boolean isValid(int row, int col, int candidate) {
        // Check the current row for an existing occurrence of the candidate.
        for (int j = 0; j < SIZE; j++) {
            if (board.get(row).get(j) == candidate) {
                return false;
            }
        }
        // Check the current column for an existing occurrence of the candidate.
        for (int i = 0; i < SIZE; i++) {
            if (board.get(i).get(col) == candidate) {
                return false;
            }
        }

        int startRow = (row/BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col/BLOCK_COLS) * BLOCK_COLS;

        for (int i = startRow; i < BLOCK_ROWS; i++) {
            for (int j = startCol; j < BLOCK_COLS; j++) {
                if (board.get(i).get(j) == candidate && (i != row || j != col)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the generated board.
     *
     * @return a list of lists representing the board.
     */
    public List<List<Integer>> getBoard() {
        return board;
    }
}
