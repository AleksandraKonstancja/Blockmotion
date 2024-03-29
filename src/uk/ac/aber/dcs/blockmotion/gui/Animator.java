package uk.ac.aber.dcs.blockmotion.gui;

/**
 * This is the template Animator to display the animation. You will
 * need to update this file
 *
 * @author Chris Loftus, Aleksandra Madej
 * @version 2.0, 2017-05-10.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uk.ac.aber.dcs.blockmotion.model.*;

import java.io.IOException;
import java.util.Scanner;

public class Animator extends Application {

    private Button[][] gridArray;
    private GridPane grid;
    private Thread animation;
    private boolean doRun;
    private IFootage footage = new Footage();
    private Stage stage;
    private Scene scene;
    private String filename; // name of currently loaded file
    private String filename2; // name used in save as option
    private FlipHorizontal fH = new FlipHorizontal();
    private FlipVertical fV = new FlipVertical();
    private SlideLeft sL = new SlideLeft();
    private SlideUp sU = new SlideUp();
    private SlideDown sD = new SlideDown();
    private SlideRight sR = new SlideRight();
    private int times;  // number of times to slide by in slide by number option

    // You will need more instance variables

    public static void main(String[] args) {
        // This is how a javafx class is run.
        // This will cause the start method to run.
        // You don't need to change main.
        launch(args);
    }

    // This is the javafx main starting method. The JVM will run this
    // inside an object of this class (Animator). You do not need to
    // create that object yourself.
    @Override
    public void start(Stage primaryStage) {

        // The Stage is where we place GUI stuff, such as a GridPane (see later).
        // I'll look more at this after Easter, but please read the
        // following comments
        stage = primaryStage;

        // In this version of the app we will drive
        // the app using a command line menu.
        // YOU ARE REQUIRED TO IMPLEMENT THIS METHOD
        runMenu();

        // This is how we can handle a window close event. I will talk
        // about the strange syntax later (lambdas), but essentially it's a Java 8
        // was of providing a callback function: when someone clicks the
        // close window icon then this bit of code is run, passing in the event
        // object that represents the click event.
        primaryStage.setOnCloseRequest((event) -> {
            // Prevent window from closing. We only want quitting via
            // the command line menu.
            event.consume();

            System.out.println("Please quit the application via the menu");
        });
    }


    private void printMenu() {
        System.out.println("Menu: \n l - Load footage file \n s - Save footage file" +
                "\n sa - Save as footage file \n a - Run footage animation \n " +
                "t - Stop footage animation \n e - Edit current footage \n q - Quit" +
                "\n Enter option: ");
    }

    private void printSubmenu() {
        System.out.println("Menu: \n fh - Flip horizontal \n fv - Flip vertical" +
                "\n sl - Slide left \n sr - Slide right \n su - Slide up" +
                "\n sd - Slide down \n nr - Slide right number, Currently = 1" +
                "\n nl - Slide left number, Currently = 1 \n " +
                "nd - Slide down number, Currently = 1 \n nu - Slide up number, Currently = 1" +
                "\n r - Repeat last operation \n Q - Quit editing \n Enter option:");
    }

    private void runMenu() {
        // The GUI runs in its own thread of control. Here
        // we start by defining the function we want the thread
        // to call using that Java 8 lambda syntax. The Thread
        // constructor takes a Runnable
        Runnable commandLineTask = () -> {

            String choice, lastchoice = ""; // lastchoice initialised to blank space to avoid nullPointerException
            Scanner in;
            in = new Scanner(System.in);

            do {
                printMenu();
                choice = in.nextLine();

                switch (choice) {
                    case "l":
                        System.out.println("Filename: ");
                        filename = in.nextLine();
                        try {
                            footage.load(filename);
                            createGrid();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "s":
                        try {
                            footage.save(filename);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "sa":
                        System.out.println("How do you want to name your file?");
                        filename2 = in.nextLine();
                        try {
                            footage.save(filename2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "a":
                        runAnimation();
                        break;
                    case "t":
                        terminateAnimation();
                        break;
                    case "q":
                        Platform.exit();
                        break;
                    case "e":
                        do {
                            printSubmenu();
                            choice = in.nextLine();

                            // repetition option based on last choice
                            if (choice.equals("r") && !(lastchoice.equals(""))) {
                                choice = lastchoice;
                            }
                            lastchoice = choice;
                            switch (choice) {
                                case "fh":
                                        footage.transform(fH);
                                    break;
                                case "fv":
                                    footage.transform(fV);
                                    break;
                                case "sl":
                                    footage.transform(sL);
                                    break;
                                case "su":
                                    footage.transform(sU);
                                    break;
                                case "sd":
                                    footage.transform(sD);
                                    break;
                                case "sr":
                                    footage.transform(sR);
                                    break;
                                case "nr":
                                    System.out.println("How many times do you want to slide?");
                                    times = in.nextInt();
                                    for (int i = 0; i < times; i++) {
                                        footage.transform(sR);
                                    }
                                    break;
                                case "nl":
                                    System.out.println("How many times do you want to slide?");
                                    times = in.nextInt();
                                    for (int i = 0; i < times; i++) {
                                        footage.transform(sL);
                                    }
                                    break;
                                case "nu":
                                    System.out.println("How many times do you want to slide?");
                                    times = in.nextInt();
                                    for (int i = 0; i < times; i++) {
                                        footage.transform(sU);
                                    }
                                    break;
                                case "nd":
                                    System.out.println("How many times do you want to slide?");
                                    times = in.nextInt();
                                    for (int i = 0; i < times; i++) {
                                        footage.transform(sD);
                                    }
                                    break;
                            }

                        } while (!choice.equals("Q"));
                        break;
                }
            } while (!choice.equals("q"));


        };
        Thread commandLineThread = new Thread(commandLineTask);

        commandLineThread.start();

        // You can stop javafx with the command

    }

    // An example method that you might like to call from your
    // menu. Whenever you need to do something in the GUI thread
    // from another thread you have to call Platform.runLater
    // This is a javafx method that queues your code ready for the GUI
    // thread to run when it is ready. We use that strange lambda Java 8 syntax
    // again although this time there are no parameters hence ()
    private void terminateAnimation() {
        doRun = false;
    }

    // Here is another example. This one is useful because it creates
    // the GUI grid. You will need to call this from the menu, e.g. when starting
    // an animation.
    private void createGrid() {
        Platform.runLater(() -> {

            // Update UI here
            //createGrid(numRows);   // E.g. let's create a 20 x 20 grid
            createGrid(20);
        });
    }

    // I'll give you this method since I haven't done
    // much on javafx. This also creates a scene and adds it to the stage;
    // all very theatrical.
    private void createGrid(int numRows) {
        // Creates a grid so that we can display the animation. We will see
        // other layout panes in the lectures.
        grid = new GridPane();

        // We need to create a 2D array of javafx Buttons that we will
        // add to the grid. The 2D array makes it much easier to change
        // the colour of the buttons in the grid; easy lookup using
        // 2D indicies. Note that we make this read-only for this display
        // onlt grid. If you go for flair marks, then I'd imagine that you
        // could create something similar that allows you to edits frames
        // in the footage.
        gridArray = new Button[numRows][numRows];
        Button displayButton = null;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numRows; col++) {  // The display is square
                displayButton = new Button();
                gridArray[row][col] = displayButton;
                displayButton.setDisable(true);
                grid.add(displayButton, col, row);
            }
        }

        // Create a scene to hold the grid of buttons
        // The stage will "shrink wrap" around the grid of buttons,
        // so we don't need to set its height and width.
        scene = new Scene(grid);
        stage.setScene(scene);
        scene.getStylesheets().add(Animator.class.getResource("styling.css").toExternalForm());

        // Make it resizable so that the window shrinks to fit the scene grid
        stage.setResizable(true);
        stage.sizeToScene();
        // Raise the curtain on the stage!
        stage.show();
        // Stop the user from resizing the window
        stage.setResizable(false);
    }

    // I'll also give you this one too. This does the animation and sets colours for
    // the grid buttons. You will have to call this from the menu. You should not
    // need to change this code, unless you want to add more colours
    private void runAnimation() {
        if (footage == null) {
            System.out.println("Load the footage first");
        } else {
            Runnable animationToRun = () -> {

                Background yellowBg = new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY));
                Background blackBg = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
                Background blueBg = new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY));
                Background whiteBg = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));

                doRun = true;
                int numFrames = footage.getNumFrames();
                int currentFrameIndex = 0;
                Background bck = null;
                while (doRun) {
                    if (currentFrameIndex >= numFrames - 1) currentFrameIndex = 0;
                    IFrame currentFrame = footage.getFrame(currentFrameIndex);
                    // Iterate through the current frame displaying appropriate colour
                    for (int row = 0; row < footage.getNumRows(); row++) {
                        for (int col = 0; col < footage.getNumRows(); col++) {
                            switch (currentFrame.getChar(row, col)) {
                                case 'l':
                                    bck = yellowBg;
                                    break;
                                case 'r':
                                    bck = blackBg;
                                    break;
                                case 'b':
                                    bck = blueBg;
                                    break;
                                default:
                                    bck = whiteBg;
                            }
                            final int theRow = row;
                            final int theCol = col;
                            final Background backgroundColour = bck;
                            // This is another lambda callback. When the Platform
                            // GUI thread is ready it will run this code.
                            Platform.runLater(() -> {

                                // Update UI here
                                // We have to make this request on a queue that the GUI thread
                                // will run when ready.
                                gridArray[theRow][theCol].setBackground(backgroundColour);
                            });

                        }
                    }
                    try {
                        // This is how we delay for 200th of a second
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                    currentFrameIndex++;
                }
            };
            animation = new Thread(animationToRun);
            animation.start();
        }
    }
}

