
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        FILE:   MinesweeperModel.java
        NAME:   KAH SHIUH TANG
        DATE:   Jan 28, 2020
  SAUCE CODE:   6
     PURPOSE:   

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
import java.util.Random;
import java.util.*;

public class MinesweeperModel {

    private int size;
    private int mines;
    private int startX;
    private int startY;
    private Random r = new Random();
    private Square[][] board;

    public MinesweeperModel(int siz, int mine) {
        size = siz;
        mines = mine;
        startX = 0;
        startY = 0;
        board = new Square[size][size];
    }

    // Sets the opening square clicked
    public void setStart(int x, int y) {
        startX = x;
        startY = y;
        setBombs();
    }

    // Status: Working
    public Square getSquare(int r, int c) {
        return board[r][c];
    }

    public void setBombCount(int bom) {
        mines = bom;
    }

    public void setSize(int siz) {
        size = siz;
    }

    // Creates how many bombs you want randomly in field
    // Status: Working
    public void setBombs() {
        // Fills in board with empty squares
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board[r][c] = new Square(false);
            }
        }
        for (int i = 0; i < mines; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            // Makes sure there is not already a bomb there and is no in in 3*3 radius of
            // start
            // Parameters are that specific coordinate
            while (board[x][y].getMineStatus() == true || !checkValidStart(x, y)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
            }
            board[x][y].setMine(true);
        }
        setSurrounding();

    }

    // Checks if you are in 3*3 radius of starting square
    // Parameters are the square you want to check
    // Status: Working
    public boolean checkValidStart(int r, int c) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (Math.abs((r + i) - startX) < 1 && Math.abs((c + j) - startY) < 1) {
                    return false;
                }
            }
        }
        return true;
    }

    // Checks to surrounding 3*3 to get a number to display
    // Parameters are the coordinates for square we are checking
    // Status: Working
    public int checkSurrounding(int r, int c) {
        int count = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (r + i > -1 && r + i < size && c + j > -1 && c + j < size
                        && board[r + i][c + j].getMineStatus() == true) {
                    count++;
                }
            }
        }
        // If that particular square is a bomb, you return -1
        if (board[r][c].getMineStatus() == true) {
            return -1;
        }
        return count;
    }

    // Sets the surrounding number for each square
    // Status: Working
    public void setSurrounding() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j].setSurroundingBombs(checkSurrounding(i, j));
            }

        }
    }

    // Checks if square is on the border
    // Not sure what this will be used for
    // Status: Working
    public boolean checkBorder(int r, int c) {
        if (r - size == 1 || r == 0 || c - size == 1 || c == 0) {
            return true;
        }
        return false;
    }

    // Should be called to uncover all surrounding zeros (In terms of neighboring
    // bombs)
    // Uncovers
    // Should just be in T-Shape Not 3*3
    // Parameters are the starting square's coordinates we are checking around
    // Status: Working
    public void uCS(int r, int c) {
        setSurrounding();
        board[r][c].setRevealed(true);
        // Only checks if the surrounding
        if (board[r][c].getSurroundingBombs() == 0) {
            // Down one
            if (r + 1 < size && board[r + 1][c].getRevealedStatus() == false
                    && board[r + 1][c].getSurroundingBombs() == 0) {
                uCS(r + 1, c);
            }
            // Up one
            if (r - 1 >= 0 && board[r - 1][c].getRevealedStatus() == false
                    && board[r - 1][c].getSurroundingBombs() == 0) {
                uCS(r - 1, c);
            }
            // Right one
            if (c + 1 < size && board[r][c + 1].getRevealedStatus() == false
                    && board[r][c + 1].getSurroundingBombs() == 0) {
                uCS(r, c + 1);
            }
            // Left One
            if (c - 1 >= 0 && board[r][c - 1].getRevealedStatus() == false
                    && board[r][c - 1].getSurroundingBombs() == 0) {
                uCS(r, c - 1);
            }
            if (r + 1 < size && c + 1 < size && board[r + 1][c + 1].getRevealedStatus() == false
                    && board[r + 1][c + 1].getSurroundingBombs() == 0) {
                uCS(r + 1, c + 1);

            }
            if (r + 1 < size && c - 1 >= 0 && board[r + 1][c - 1].getRevealedStatus() == false
                    && board[r + 1][c - 1].getSurroundingBombs() == 0) {
                uCS(r + 1, c - 1);

            }
            if (r - 1 >= 0 && c + 1 < size && board[r - 1][c + 1].getRevealedStatus() == false
                    && board[r - 1][c + 1].getSurroundingBombs() == 0) {
                uCS(r - 1, c + 1);

            }
            if (r - 1 >= 0 && c - 1 >= 0 && board[r - 1][c - 1].getRevealedStatus() == false
                    && board[r - 1][c - 1].getSurroundingBombs() == 0) {
                uCS(r - 1, c - 1);

            }
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (checkInGrid(r + i, c + j)) {
                        board[r + i][c + j].setRevealed(true);

                    }
                }
            }
        }
    }

    // Checks to make sure the square is not outside the grid
    // Status: Working
    public boolean checkInGrid(int r, int c) {
        if (r < 0 || r >= size || c < 0 || c >= size) {
            return false;
        }
        return true;

    }

    // Reset all bombs
    // Status: Working
    public void reset() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = null;
                board[i][j] = new Square(false);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int getMines() {
        return mines;
    }
}
