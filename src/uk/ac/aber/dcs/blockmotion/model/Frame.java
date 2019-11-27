package uk.ac.aber.dcs.blockmotion.model;

import uk.ac.aber.dcs.blockmotion.gui.Animator;

import java.io.PrintWriter;

/**
 * @author Aleksandra Madej
 * @version 2017-05-10.
 */
public class Frame implements IFrame {
    private int rows;
    private String[] lines = new String[rows];
    private char[][] chars = new char[rows][rows];

    @Override
    public void insertLines(String[] lines) {

        this.lines = new String[lines.length];
        rows = lines.length;
        // replace current lines with new lines
        for (int i = 0; i < lines.length; i++) {
            this.lines[i] = lines[i];
        }
    }

    @Override
    public int getNumRows() {
        return rows;
    }

    @Override
    public void tofile(PrintWriter outfile) {
        for (int i = 0; i < rows; i++) {
            outfile.println(lines[i]);
        }
    }

    @Override
    public char getChar(int i, int j) {
        // get char only if it represents a valid position in a grid
        if (i <= rows && j <= rows) {
            return lines[i].charAt(j);
        } else throw new IllegalArgumentException("row or column number doesn't represent a valid place in the frame");
    }

    @Override
    public void setChar(int i, int j, char ch) {
        if (i <= rows && j <= rows) {
            for (int k = 0; k < rows; k++) {
                for (int a = 0; a < rows; a++) {
                    chars[k][a] = this.getChar(k, a);
                    System.out.print(chars[k][a]);
                }
                System.out.print("\n");
            }
            chars[i][j] = ch;
            lines[i] = null;    // clear current row with replaced character

            for (int a = 0; a < rows; a++) { // add changed characters to cleared row
                lines[i] += chars[i][a];

            }
        } else throw new IllegalArgumentException("row or column number doesn't represent a valid place in the frame");
    }

    @Override
    public IFrame copy() {
        return this;
    }

    @Override
    public void replace(IFrame f) {

        if (f.getNumRows() != this.getNumRows() || f.getNumRows() == 0) {
            throw new IllegalArgumentException("Frames sizes have to be the same");
        } else {
            chars = new char[rows][rows];
            // get characters from f
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < rows; j++) {
                    chars[i][j] = f.getChar(i, j);
                }
            }
            // add characters from f to lines
            for (int i = 0; i < rows; i++) {
                lines[i] = new String(chars[i]);
            }
            // replace current lines with new ones
            this.insertLines(lines);
        }
    }
}
