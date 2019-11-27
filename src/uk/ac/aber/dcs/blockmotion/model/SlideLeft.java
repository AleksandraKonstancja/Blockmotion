package uk.ac.aber.dcs.blockmotion.model;

/**
 * @author Aleksandra Madej
 * @version 2017-05-10.
 */
public class SlideLeft extends Footage implements Transformer {
    @Override
    public void transform(IFrame frame) {


        rowNum = frame.getNumRows();
        chars = new char[rowNum][rowNum];
        lines = new String[rowNum];

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                chars[i][j] = frame.getChar(i, j);
            }
        }

        for (int i = 0; i < rowNum; i++) {
            temp_char = chars[i][0];
            for (int j = 0; j < rowNum - 1; j++) {
                chars[i][j] = chars[i][j + 1];
            }
            chars[i][rowNum - 1] = temp_char;
        }

        //  TESTING
        System.out.println("After transformation: ");
        for ( int i = 0; i < rowNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                System.out.print(chars[i][j]);
            }
            System.out.print("\n");
        }

        for (int i = 0; i < rowNum; i++) {
            lines[i] = new String(chars[i]);
        }
        frame.insertLines(lines);

    }
}
