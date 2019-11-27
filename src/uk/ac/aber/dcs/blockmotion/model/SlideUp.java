package uk.ac.aber.dcs.blockmotion.model;

/** * @author Aleksandra Madej
 * @version 2017-05-10.
 */
public class SlideUp extends Footage implements Transformer {
    @Override
    public void transform(IFrame frame) {

        rowNum = frame.getNumRows();
        chars = new char[rowNum][rowNum];
        lines = new String[rowNum];

        System.out.println("Before transformation: ");
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                chars[i][j] = frame.getChar(i, j);
              System.out.print(chars[i][j]);
            }
            System.out.print("\n");
        }


        for (int i = 0; i < rowNum; i++) {
            temp_char = chars[0][i];
            for (int j = 0; j < rowNum - 1; j++) {
                chars[j][i] = chars[j + 1][i];
            }
            chars[rowNum - 1][i] = temp_char;
        }

        // TESTING
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
