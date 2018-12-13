package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
/**
 * class Time_Graph11 to display a graph, by circles and lines,
 *  run the time,
 *  count the chromatic number used by the player,
 *  compare that chromatic number to the original chromatic number pre-determined,
 *  display gameover window once the game is over,
 *  and to create a game with graph11
 */
public class Timed_Graph11 {

    private static Scene scene1;
    private static Stage window;
    private static Stage gameOverWindow;
    private static Label layout;
    private static double orgSceneX, orgSceneY;
    private static final Integer starttime = 10;
    private static Integer seconds = starttime;
    private static Paint[] num_of_colors;
    private static Color color_holder = Color.TRANSPARENT;
    private static Label timeUsed;
    private static Label chromaUsed;
    private static ColorPicker colorPicker;
    private static ArrayList<String> colorList;
    private static Set<String> allColors;
    private static int colorListLength;
    private static int chromaticNumber = 3;
    private static String comparision;
    private static ArrayList<Circle> list;

    /**
     * Game over screen method.
     * setGameOver() keeps record of the time and once the chromatic number is reached or the time is over, shows the gameover window
     */

    private static void setGameOver(){
        gameOverWindow = new Stage();
        GridPane grid = new GridPane();
        gameOverWindow.setTitle("Time's up!");
        timeUsed = new Label("TIME'S UP!");
        timeUsed.setFont(Font.font ("Verdana", 14));
        GridPane.setConstraints(timeUsed,4,4);

        if(getNumColors() == chromaticNumber)
        {comparision = "Well done! Well played!";}
        else if(getNumColors() > chromaticNumber)
        {comparision = "Good attempt! Using less colors might've helped.";}
        else { comparision = "Good attempt! You could've used more colors.";}

        Label compareChroma = new Label(comparision);
        compareChroma.setFont(Font.font ("Verdana", 14));
        chromaUsed = new Label("You've reached " + getNumColors() + " chromatic colors.");
        chromaUsed.setFont(Font.font ("Verdana", 14));
        GridPane.setConstraints(chromaUsed, 4,6);
        GridPane.setConstraints(compareChroma, 4, 8);
        grid.getChildren().addAll(timeUsed, chromaUsed, compareChroma);
        Scene gameOverScene = new Scene(grid);
        gameOverWindow.setMinWidth(300);
        gameOverWindow.setMinHeight(100);
        gameOverWindow.setScene(gameOverScene);
        gameOverWindow.show();
    }

    /**
     * doTime() method starts the timer from a given time, and till the timer reaches zero or the game is done
     */
    private static void doTime() {
        Timeline time= new Timeline();


        KeyFrame frame= new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                seconds--;
                layout.setText("T: "+seconds.toString());
                if(seconds<=0){
                    seconds = starttime;
                    time.stop();
                    window.close();
                    setGameOver();
                }
            }
        });

        time.setCycleCount(Timeline.INDEFINITE);
        time.getKeyFrames().add(frame);
        if(time!= null){
            time.stop();
        }
        time.play();
    }

    /**
     * Mouse click listener enables the action specified once the mouse is clicked
     */
    private static EventHandler<MouseEvent> mousePressedEventHandler = (t) ->
    {
        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();

        Circle c = (Circle) (t.getSource());
        c.toFront();
    };



    /**
     * Mouse drag listener enables dragging option by the mouse
     */
    private static EventHandler<MouseEvent> mouseDraggedEventHandler = (t) ->
    {
        double offsetX = t.getSceneX() - orgSceneX;
        double offsetY = t.getSceneY() - orgSceneY;

        Circle c = (Circle) (t.getSource());

        c.setCenterX(c.getCenterX() + offsetX);
        c.setCenterY(c.getCenterY() + offsetY);

        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();
    };

    /**
     * createCircle() creates a circle object with (x,y), the radius and the color
     * @param x
     * @param y
     * @param r
     * @param color
     * @return
     */
    private static Circle createCircle(double x, double y, double r, Color color)
    {
        Circle circle = new Circle(x, y, r, color);
        circle.setStrokeWidth(2);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.TRANSPARENT);

        circle.setCursor(Cursor.CROSSHAIR);

        circle.setOnMousePressed(mousePressedEventHandler);
        circle.setOnMouseDragged(mouseDraggedEventHandler);

        return circle;
    }

    /**
     * connect() connects the two circles specified with a line
     * @param c1 the first circle to connect the line from
     * @param c2 the other circle to connect the line to
     * @return a line that's connected by the two specific circles
     */
    private static Line connect(Circle c1, Circle c2)
    {
        Line line = new Line();

        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());

        line.setStrokeWidth(2);
        line.setStrokeLineCap(StrokeLineCap.BUTT);
        line.getStrokeDashArray().setAll(3.0, 4.0);

        return line;
    }

    /**
     * getNumColors() method to return the number of colors used for coloring the graph
     * @return the number of colors used
     */
    public static int getNumColors() {
        Set<Paint> newset = new HashSet<Paint>();
        for (int i=0; i < num_of_colors.length; i++) {
            newset.add(num_of_colors[i]);
        }
        if (newset.iterator().next() != null) {
            return newset.size();
        }
        System.out.println(num_of_colors.length);
        System.out.println(newset.size());

        return newset.size()-1;
    }

    public static boolean checkAdj(int [][] adj_matrix, int v, Paint c){
        for (int i = 0; i < adj_matrix.length; i++){
            if (adj_matrix[v][i] == 1) {
                if (list.get(i).getFill() == c) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * method display() to display the graph chosen by the user
     * @param title title of the displayed graph window
     * @param message message of through the window
     */
    public static void display(String title, String message){
        window = new Stage();
        window.setTitle(title);
        window.setMinWidth(250);

        int[][] adj_matrix = new int[][]{
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}
        };

        Pane pane_graph = new Pane();
        pane_graph.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        pane_graph.setStyle(
                "-fx-background-position: center center;"+
                        "-fx-effect: dropshadow(three-pass-box, grey, 30, 0.2, 0, 0);");

        GridPane pane = new GridPane();
        VBox vbox = new VBox(pane_graph, pane);

        /*
         * Adding HINTS button
         */

        Button buttonhint = new Button("HINTS");
        pane.add(buttonhint, 5,0,1,1);
        buttonhint.setOnAction(e ->  Hint.display(" ", " ", getNumColors(),chromaticNumber));

        Label label = new Label();
        label.setText(message);
        pane_graph.getChildren().add(label);
        /*
         * create circle objects
         */
        Circle Circle1 = createCircle(440, 85, 15, Color.TRANSPARENT);
        Circle Circle2 = createCircle(300, 165, 15, Color.TRANSPARENT);
        Circle Circle3 = createCircle(440, 165, 15, Color.TRANSPARENT);
        Circle Circle4 = createCircle(580, 165, 15, Color.TRANSPARENT);
        Circle Circle5 = createCircle(240, 225, 15, Color.TRANSPARENT);
        Circle Circle6 = createCircle(300, 225, 15, Color.TRANSPARENT);
        Circle Circle7 = createCircle(360, 225, 15, Color.TRANSPARENT);
        Circle Circle8 = createCircle(520, 225, 15, Color.TRANSPARENT);
        Circle Circle9 = createCircle(580, 225, 15, Color.TRANSPARENT);
        Circle Circle10 = createCircle(640, 225, 15, Color.TRANSPARENT);
        Circle Circle11 = createCircle(700, 225, 15, Color.TRANSPARENT);
        Circle Circle12 = createCircle(300, 290, 15, Color.TRANSPARENT);
        Circle Circle13 = createCircle(360, 290, 15, Color.TRANSPARENT);
        Circle Circle14 = createCircle(520, 290, 15, Color.TRANSPARENT);
        Circle Circle15 = createCircle(125, 380, 15, Color.TRANSPARENT);
        Circle Circle16 = createCircle(185, 380, 15, Color.TRANSPARENT);
        Circle Circle17 = createCircle(240, 380, 15, Color.TRANSPARENT);
        Circle Circle18 = createCircle(300, 380, 15, Color.TRANSPARENT);
        Circle Circle19 = createCircle(360, 380, 15, Color.TRANSPARENT);
        Circle Circle20 = createCircle(520, 380, 15, Color.TRANSPARENT);
        Circle Circle21 = createCircle(580, 380, 15, Color.TRANSPARENT);
        Circle Circle22 = createCircle(640, 380, 15, Color.TRANSPARENT);
        Circle Circle23 = createCircle(185, 440, 15, Color.TRANSPARENT);
        Circle Circle24 = createCircle(300, 440, 15, Color.TRANSPARENT);
        Circle Circle25 = createCircle(360, 440, 15, Color.TRANSPARENT);
        Circle Circle26 = createCircle(440, 440, 15, Color.TRANSPARENT);
        Circle Circle27 = createCircle(580, 440, 15, Color.TRANSPARENT);
        Circle Circle28 = createCircle(640, 440, 15, Color.TRANSPARENT);
        Circle Circle29 = createCircle(125, 530, 15, Color.TRANSPARENT);
        Circle Circle30 = createCircle(240, 530, 15, Color.TRANSPARENT);
        Circle Circle31 = createCircle(300, 530, 15, Color.TRANSPARENT);
        Circle Circle32 = createCircle(360, 530, 15, Color.TRANSPARENT);
        Circle Circle33 = createCircle(440, 530, 15, Color.TRANSPARENT);
        Circle Circle34 = createCircle(520, 530, 15, Color.TRANSPARENT);
        Circle Circle35 = createCircle(700, 530, 15, Color.TRANSPARENT);
        Circle Circle36 = createCircle(240, 600, 15, Color.TRANSPARENT);
        Circle Circle37 = createCircle(300, 600, 15, Color.TRANSPARENT);
        Circle Circle38 = createCircle(360, 600, 15, Color.TRANSPARENT);
        Circle Circle39 = createCircle(440, 600, 15, Color.TRANSPARENT);
        Circle Circle40 = createCircle(520, 600, 15, Color.TRANSPARENT);

        /*lines to connect the specified circles
         *
         */
        Line line1 = connect(Circle1, Circle3);
        Line line2 = connect(Circle3, Circle2);
        Line line3 = connect(Circle3, Circle4);
        Line line4 = connect(Circle2, Circle6);
        Line line5 = connect(Circle3, Circle26);
        Line line6 = connect(Circle4, Circle9);
        Line line7 = connect(Circle5, Circle6);
        Line line8 = connect(Circle5, Circle17);
        Line line9 = connect(Circle6, Circle12);
        Line line10 = connect(Circle6, Circle7);
        Line line11 = connect(Circle7, Circle13);
        Line line12 = connect(Circle8, Circle14);
        Line line13 = connect(Circle8, Circle9);
        Line line14 = connect(Circle9, Circle10);
        Line line15 = connect(Circle9, Circle21);
        Line line16 = connect(Circle10, Circle11);
        Line line17 = connect(Circle10, Circle22);
        Line line18 = connect(Circle11, Circle35);
        Line line19 = connect(Circle15, Circle16);
        Line line20 = connect(Circle15, Circle29);
        Line line21 = connect(Circle16, Circle23);
        Line line22 = connect(Circle16, Circle17);
        Line line23 = connect(Circle17, Circle18);
        Line line24 = connect(Circle17, Circle30);
        Line line25 = connect(Circle18, Circle19);
        Line line26 = connect(Circle18, Circle24);
        Line line27 = connect(Circle19, Circle25);
        Line line28 = connect(Circle20, Circle21);
        Line line29 = connect(Circle20, Circle34);
        Line line30 = connect(Circle21, Circle27);
        Line line31 = connect(Circle21, Circle22);
        Line line32 = connect(Circle22, Circle28);
        Line line33 = connect(Circle29, Circle30);
        Line line34 = connect(Circle30, Circle36);
        Line line35 = connect(Circle30, Circle31);
        Line line36 = connect(Circle31, Circle32);
        Line line37 = connect(Circle32, Circle38);
        Line line38 = connect(Circle32, Circle33);
        Line line39 = connect(Circle33, Circle39);
        Line line40 = connect(Circle33, Circle34);
        Line line41 = connect(Circle34, Circle40);
        Line line42 = connect(Circle34, Circle35);
        Line line43 = connect(Circle31, Circle37);


        /*add the circles
         *
         */
        pane_graph.getChildren().add(Circle1);
        pane_graph.getChildren().add(Circle2);
        pane_graph.getChildren().add(Circle3);
        pane_graph.getChildren().add(Circle4);
        pane_graph.getChildren().add(Circle5);
        pane_graph.getChildren().add(Circle6);
        pane_graph.getChildren().add(Circle7);
        pane_graph.getChildren().add(Circle8);
        pane_graph.getChildren().add(Circle9);
        pane_graph.getChildren().add(Circle10);
        pane_graph.getChildren().add(Circle11);
        pane_graph.getChildren().add(Circle12);
        pane_graph.getChildren().add(Circle13);
        pane_graph.getChildren().add(Circle14);
        pane_graph.getChildren().add(Circle15);
        pane_graph.getChildren().add(Circle16);
        pane_graph.getChildren().add(Circle17);
        pane_graph.getChildren().add(Circle18);
        pane_graph.getChildren().add(Circle19);
        pane_graph.getChildren().add(Circle20);
        pane_graph.getChildren().add(Circle21);
        pane_graph.getChildren().add(Circle22);
        pane_graph.getChildren().add(Circle23);
        pane_graph.getChildren().add(Circle24);
        pane_graph.getChildren().add(Circle25);
        pane_graph.getChildren().add(Circle26);
        pane_graph.getChildren().add(Circle27);
        pane_graph.getChildren().add(Circle28);
        pane_graph.getChildren().add(Circle29);
        pane_graph.getChildren().add(Circle30);
        pane_graph.getChildren().add(Circle31);
        pane_graph.getChildren().add(Circle32);
        pane_graph.getChildren().add(Circle33);
        pane_graph.getChildren().add(Circle34);
        pane_graph.getChildren().add(Circle35);
        pane_graph.getChildren().add(Circle36);
        pane_graph.getChildren().add(Circle37);
        pane_graph.getChildren().add(Circle38);
        pane_graph.getChildren().add(Circle39);
        pane_graph.getChildren().add(Circle40);

        /*add the lines
         *
         */
        pane_graph.getChildren().add(line1);
        pane_graph.getChildren().add(line2);
        pane_graph.getChildren().add(line3);
        pane_graph.getChildren().add(line4);
        pane_graph.getChildren().add(line5);
        pane_graph.getChildren().add(line6);
        pane_graph.getChildren().add(line7);
        pane_graph.getChildren().add(line8);
        pane_graph.getChildren().add(line9);
        pane_graph.getChildren().add(line10);
        pane_graph.getChildren().add(line11);
        pane_graph.getChildren().add(line12);
        pane_graph.getChildren().add(line13);
        pane_graph.getChildren().add(line14);
        pane_graph.getChildren().add(line15);
        pane_graph.getChildren().add(line16);
        pane_graph.getChildren().add(line17);
        pane_graph.getChildren().add(line18);
        pane_graph.getChildren().add(line19);
        pane_graph.getChildren().add(line20);
        pane_graph.getChildren().add(line21);
        pane_graph.getChildren().add(line22);
        pane_graph.getChildren().add(line23);
        pane_graph.getChildren().add(line24);
        pane_graph.getChildren().add(line25);
        pane_graph.getChildren().add(line26);
        pane_graph.getChildren().add(line27);
        pane_graph.getChildren().add(line28);
        pane_graph.getChildren().add(line29);
        pane_graph.getChildren().add(line30);
        pane_graph.getChildren().add(line31);
        pane_graph.getChildren().add(line32);
        pane_graph.getChildren().add(line33);
        pane_graph.getChildren().add(line34);
        pane_graph.getChildren().add(line35);
        pane_graph.getChildren().add(line36);
        pane_graph.getChildren().add(line37);
        pane_graph.getChildren().add(line38);
        pane_graph.getChildren().add(line39);
        pane_graph.getChildren().add(line40);
        pane_graph.getChildren().add(line41);
        pane_graph.getChildren().add(line42);
        pane_graph.getChildren().add(line43);

        /*for the timer
         *
         */
        layout = new Label();
        layout.setText("T: 10");
        doTime();
        pane_graph.getChildren().addAll(layout);

        /* bring the circles to the front of the lines
         *
         */
        Circle1.toFront();
        Circle2.toFront();
        Circle3.toFront();
        Circle4.toFront();
        Circle5.toFront();
        Circle6.toFront();
        Circle7.toFront();
        Circle8.toFront();
        Circle9.toFront();
        Circle10.toFront();
        Circle11.toFront();
        Circle12.toFront();
        Circle13.toFront();
        Circle14.toFront();
        Circle15.toFront();
        Circle16.toFront();
        Circle17.toFront();
        Circle18.toFront();
        Circle19.toFront();
        Circle20.toFront();
        Circle21.toFront();
        Circle22.toFront();
        Circle23.toFront();
        Circle24.toFront();
        Circle25.toFront();
        Circle26.toFront();
        Circle27.toFront();
        Circle28.toFront();
        Circle29.toFront();
        Circle30.toFront();
        Circle31.toFront();
        Circle32.toFront();
        Circle33.toFront();
        Circle34.toFront();
        Circle35.toFront();
        Circle36.toFront();
        Circle37.toFront();
        Circle38.toFront();
        Circle39.toFront();
        Circle40.toFront();

        /*
         * An array to hold the used colors.
         */

        num_of_colors = new Paint[40];

        list = new ArrayList<Circle>();
        list.add(Circle1);
        list.add(Circle2);
        list.add(Circle3);
        list.add(Circle4);
        list.add(Circle5);
        list.add(Circle6);
        list.add(Circle7);
        list.add(Circle8);
        list.add(Circle9);
        list.add(Circle10);
        list.add(Circle11);
        list.add(Circle12);
        list.add(Circle13);
        list.add(Circle14);
        list.add(Circle15);
        list.add(Circle16);
        list.add(Circle17);
        list.add(Circle18);
        list.add(Circle19);
        list.add(Circle20);
        list.add(Circle21);
        list.add(Circle22);
        list.add(Circle23);
        list.add(Circle24);
        list.add(Circle25);
        list.add(Circle26);
        list.add(Circle27);
        list.add(Circle28);
        list.add(Circle29);
        list.add(Circle30);
        list.add(Circle31);
        list.add(Circle32);
        list.add(Circle33);
        list.add(Circle34);
        list.add(Circle35);
        list.add(Circle36);
        list.add(Circle37);
        list.add(Circle38);
        list.add(Circle39);
        list.add(Circle40);

        /*
         * ADDING THE COLOR PICKER
         */
        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.TRANSPARENT);
        Text text_color = new Text("Pick Your Color and right-click on the vertex you'd like to color.");
        text_color.setFont(Font.font ("Verdana", 14));
        text_color.setFill(Color.BLACK);
        colorPicker.setOnAction((ActionEvent t) -> {color_holder = colorPicker.getValue();});
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.add(colorPicker, 0, 0, 1, 1);
        pane.add(text_color,0,1,1,1);

        final Text text = new Text("                                Colors used: 0");
        text.setFont(Font.font ("Verdana", 14));
        for (int i=0; i<list.size(); i++){
            final int temp_i = i;
            list.get(i).addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    text.setText("");
                    if (mouseEvent.getButton() == MouseButton.SECONDARY && colorPicker.getValue() == Color.TRANSPARENT){
                        text.setText("Pick a color first");
                        text.setFill(Color.RED);
                    }

                    else if (mouseEvent.getButton() == MouseButton.SECONDARY && checkAdj(adj_matrix, temp_i, colorPicker.getValue())){
                        list.get(temp_i).setFill(colorPicker.getValue());
                        num_of_colors[temp_i] = list.get(temp_i).getFill();
                        text.setText("                              Colors used: " + (getNumColors()));
                        text.setFill(Color.BLACK);
                    }

                    else if (mouseEvent.getButton() == MouseButton.PRIMARY){
                        text.setText("                              Colors used: " + (getNumColors()));
                        text.setFill(Color.BLACK);
                    }

                    else{
                        text.setText("Adjacent circles can't have the same color!");
                        text.setFill(Color.RED);
                    }
                }
            });
        }
        pane.add(text,5,1,1,1);

        /*
         * final scene
         */
        scene1 = new Scene(vbox, 850, 850);
        window.setScene(scene1);
        window.setTitle("Graph Coloring Game");
        window.show();

    }

}