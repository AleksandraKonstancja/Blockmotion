package uk.ac.aber.dcs.blockmotion.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Aleksandra Madej
 * @version 2017-05-10.
 */
public class Footage implements IFootage {
    private int frameNum;
    private Frame frame;
    private ArrayList<Frame> frames = new ArrayList();
    int rowNum;
    char temp_char;
    char[][] chars;
    String[] lines;

    @Override
    public int getNumFrames() {
        return frameNum;
    }

    @Override
    public int getNumRows() {
        return rowNum;
    }

    @Override
    public IFrame getFrame(int num) {
        frame = frames.get(num);
        return frame;
    }

    @Override
    public void add(IFrame f) {
        frameNum++;  // increase number of frames
        lines = new String[rowNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                lines[i] += f.getChar(i, j);  // add characters of frame f to lines existing in footage
            }
        }
        frame.insertLines(lines);
        frames.add(frame);
    }

    @Override
    public void load(String fn) throws IOException {

        Scanner infile = new Scanner(new FileReader(fn));

        infile.useDelimiter(":|\r?\n|\r");

        frameNum = infile.nextInt();
        rowNum = infile.nextInt();
        infile.nextLine();

        lines = new String[rowNum];

        for (int i = 0; i < frameNum; i++) {
            frame = new Frame();
            for (int j = 0; j < rowNum; j++) {

                lines[j] = infile.nextLine();
                System.out.println("One line from array:" + lines[j]); // used for testing

            }
            frame.insertLines(lines);
            frames.add(frame);
        }

        infile.close();
    }

    @Override
    public void save(String fn) throws IOException {
        PrintWriter outfile = new PrintWriter(new FileWriter(fn));
        outfile.println(frameNum);
        System.out.println(frameNum); // used for testing
        outfile.println(rowNum);
        System.out.println(rowNum); // used for testing

        for (Frame f : frames) {   // print characters from every stored frame
            for (int j = 0; j < rowNum; j++) {
                for (int k = 0; k < rowNum; k++) {
                    System.out.print(f.getChar(j, k)); // used for testing
                    outfile.print(f.getChar(j, k));
                }
                System.out.print("\n");
                outfile.print("\n");
            }

        }
        outfile.close();

    }

    @Override
    public void transform(Transformer t) {

        // transform each stored frame
        for (Frame f : frames) {
            t.transform(f);
        }
    }
}
