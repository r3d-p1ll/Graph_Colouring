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

/*class Time_Graph3 to display a graph, by circles and lines,
run the time,
count the chromatic number used by the player,
compare that chromatic number to the original chromatic number pre-determined,
display gameover window once the game is over,
and to create a game with graph3
@param method setGameOver() sets the gameover window with timer
@param doTime() keeps record of the time
@param dispaly() displays the graph
 */
public class Timed_Graph3 {

    static Scene scene1;
    static Stage window;
    static Stage gameOverWindow;
    static Label layout;
    static double orgSceneX, orgSceneY;
    static final Integer starttime = 30; //edit how long the timer is from here.
    static Integer seconds = starttime;
    static Paint[] num_of_colors;
    static Color color_holder = Color.WHITE;
    static Label timeUsed;
    static Label chromaUsed;
    static ColorPicker colorPicker;
    static Color colorBeingUsed = Color.WHITE;
    static int colorCounter;
    static ArrayList<String> colorList;
    static Set<String> allColors;
    static int colorListLength;

    //hint
    static Integer colorAttached;
    static Label hintLabel;

    /**@method setGameOver() is called when doTime is 0 and displays a new window.
     */
    private static void setGameOver(){
        gameOverWindow = new Stage();
        GridPane grid = new GridPane();
        gameOverWindow.setTitle("Time's up!");
        timeUsed = new Label("Time left: ");
        GridPane.setConstraints(timeUsed,2,2);
        Label realChroma = new Label("Chromatic number: 3");
        chromaUsed = new Label("Chromatic reached:   " + colorCounter);
        GridPane.setConstraints(chromaUsed, 2,4);
        GridPane.setConstraints(realChroma, 2, 3);
        grid.getChildren().addAll(timeUsed, realChroma, chromaUsed);
        Scene gameOverScene = new Scene(grid);
        gameOverWindow.setScene(gameOverScene);
        gameOverWindow.show();
    }

    /**@method doTime() method starts the timer from a given time, and till the timer reaches zero or the game is done
     *@obj TimeLine records the time
     *@obj KeyFrame records the duration between time
     *@event handle() records the time from starting to end, and closes the window once the game is over
     */
    private static void doTime() {
        Timeline time= new Timeline();


        KeyFrame frame= new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                //@Asem change this to ++ and modify the instance variable to start at zero for your part.  Also change the if condition.
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

    /*Mouse click listener enables the action specified once the mouse is clicked
    @param t MouseEvent specified
    @param Circle the circle clicked
     */

    /* Hinter() method to help the user when lost
    @param display() method of Hint
     */
    public static void Hinter(){
        Hint.display("Hint", "Need help?");
    }
    /**Mouse drag listener enables dragging option by the mouse
     *@param t specified MouseEvent
     *@param Circle circle to be dragged
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

    /*createCircle() creates a circle object with (x,y), the radius and the color
    @param x starting coordinate of x-axis of the circle
    @param y starting coordinate of y-axis of the circle
    @param r the radius of the circle to be drawn
    @param color Color of the circle
     */
    public static void setColorAttached(Integer colorAttached) {
        Timed_Graph2.colorAttached = colorAttached;
    }
    public static Integer getColorAttached() {
        return colorAttached;
    }
    /**createCircle() creates a circle object with (x,y), the radius and the color
     *@param x starting coordinate of x-axis of the circle
     *@param y starting coordinate of y-axis of the circle
     *@param r the radius of the circle to be drawn
     *@param color Color of the circle
     * @param colorAttached is the number of color attached to this vertice and serves as the hint.
     * @return a circle with x coordinate and r radius
     */
    private static Circle createCircle(double x, double y, double r, Color color, Integer colorAttached)
    {
        Circle circle = new Circle(x, y, r, color);
        circle.setStrokeWidth(2);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.TRANSPARENT);

        circle.setCursor(Cursor.CROSSHAIR);

        circle.setOnMousePressed((t) ->
        {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            Circle c = (Circle) (t.getSource());
            c.toFront();

            if(t.getClickCount() == 2) {
//                setColorAttached(colorAttached);
//                System.out.println(getNumColors());
                Stage hintWindow = new Stage();
                VBox hint = new VBox();
                Label hintExplain = new Label("Colors surrounding this vertice:");
                hintLabel = new Label();
                hintLabel.setText(Integer.toString(colorAttached));
                hintLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");
                hint.setAlignment(Pos.CENTER);
                hint.getChildren().addAll(hintExplain,hintLabel);
                Scene hintScene = new Scene(hint);
                hintWindow.setScene(hintScene);
                hintWindow.showAndWait();
            }
        });
        circle.setOnMouseDragged(mouseDraggedEventHandler);

        return circle;
    }

    /**connect() connects the two circles specified with a line
     *@param c1 the circle from, i.e the starting point
     *@param c2 the circle to, i.e the end point
     * @return a dash line that serves as edge.
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
    /**method display() to display the graph chosen by the user
     *@param title title of the graph
     *@param message message printed to the user
     */
    public static void display(String title, String message){
        window = new Stage();

        window.setTitle(title);
        window.setMinWidth(250);

        Pane pane_graph = new Pane();
        pane_graph.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        pane_graph.setStyle(
                "-fx-background-position: center center;"+
                        "-fx-effect: dropshadow(three-pass-box, grey, 30, 0.2, 0, 0);");

        GridPane pane = new GridPane();
        VBox vbox = new VBox(pane_graph, pane);

        //Adding HINTS button
        Button buttonhint = new Button("HINTS");
        pane.add(buttonhint, 5,0,1,1);
        buttonhint.setOnAction(e ->  Hinter());

        Label label = new Label();
        label.setText(message);
        pane_graph.getChildren().add(label);
        //creating a circle objects
        Circle Circle1 = createCircle(75, 83, 15, Color.WHITE,1);
        Circle Circle2 = createCircle(310, 83, 15, Color.WHITE,1);
        Circle Circle3 = createCircle(310, 245, 15, Color.WHITE,1);
        Circle Circle4 = createCircle(75, 245, 15, Color.WHITE,1);
        Circle Circle5 = createCircle(210, 175, 15, Color.WHITE,1);
        Circle Circle6 = createCircle(440, 175, 15, Color.WHITE,1);
        Circle Circle7 = createCircle(440, 330, 15, Color.WHITE,1);
        Circle Circle8 = createCircle(210, 330, 15, Color.WHITE,1);
        Circle Circle9 = createCircle(365, 280, 15, Color.WHITE,1);
        Circle Circle10 = createCircle(570, 280, 15, Color.WHITE,1);
        Circle Circle11 = createCircle(570, 420, 15, Color.WHITE,1);
        Circle Circle12 = createCircle(365, 420, 15, Color.WHITE,1);
        Circle Circle13 = createCircle(510, 370, 15, Color.WHITE,1);
        Circle Circle14 = createCircle(700, 370, 15, Color.WHITE,1);
        Circle Circle15 = createCircle(700, 495, 15, Color.WHITE,1);
        Circle Circle16 = createCircle(510, 495, 15, Color.WHITE,1);
        Circle Circle17 = createCircle(635, 445, 15, Color.WHITE,1);
        Circle Circle18 = createCircle(790, 445, 15, Color.WHITE,1);
        Circle Circle19 = createCircle(790, 550, 15, Color.WHITE,1);
        Circle Circle20 = createCircle(635, 550, 15, Color.WHITE,1);

        //connecting specified circles with lines
        Line line1 = connect(Circle1, Circle2);
        Line line2 = connect(Circle2, Circle3);
        Line line3 = connect(Circle3, Circle4);
        Line line4 = connect(Circle4, Circle1);
        Line line5 = connect(Circle5, Circle6);
        Line line6 = connect(Circle6, Circle7);
        Line line7 = connect(Circle7, Circle8);
        Line line8 = connect(Circle8, Circle5);
        Line line9 = connect(Circle7, Circle13);
        Line line10 = connect(Circle9, Circle10);
        Line line11 = connect(Circle11, Circle10);
        Line line12 = connect(Circle11, Circle12);
        Line line13 = connect(Circle12, Circle9);
        Line line14 = connect(Circle13, Circle14);
        Line line15 = connect(Circle14, Circle15);
        Line line16 = connect(Circle15, Circle16);
        Line line17 = connect(Circle16, Circle13);
        Line line18 = connect(Circle15, Circle19);
        Line line19 = connect(Circle17, Circle18);
        Line line20 = connect(Circle18, Circle19);
        Line line21 = connect(Circle19, Circle20);
        Line line22 = connect(Circle20, Circle17);
        Line line23 = connect(Circle1, Circle5);
        Line line24 = connect(Circle4, Circle8);
        Line line25 = connect(Circle2, Circle6);
        Line line26 = connect(Circle11, Circle17);
        Line line27 = connect(Circle10, Circle14);
        Line line28 = connect(Circle6, Circle10);
        Line line29 = connect(Circle14, Circle18);
        Line line30 = connect(Circle8, Circle12);
        Line line31 = connect(Circle12, Circle16);
        Line line32 = connect(Circle16, Circle20);
        Line line33 = connect(Circle3, Circle9);

        //add the circles
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

        // add the lines
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

        //for the timer
        layout = new Label();
        layout.setText("T: 10");
        doTime();
        pane_graph.getChildren().addAll(layout);

        // bring the circles to the front of the lines
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

        num_of_colors = new Paint[20]; //An array to hold the used colors.
        //adding all circles to an array, to calculate the colors used by the user
        ArrayList<Circle> list = new ArrayList<Circle>();
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

        // ADDING THE COLOR PICKER
        colorPicker = new ColorPicker();
        colorPicker.setValue(null);
        Text text_color = new Text("Pick Your Color." + "\n" + "After that right-click on vertex you'd like to color.");
        text_color.setFont(Font.font ("Verdana", 14));
        text_color.setFill(Color.BLACK);
        colorPicker.setOnAction((ActionEvent t) -> {color_holder = colorPicker.getValue();});
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.add(colorPicker, 0, 0, 1, 1);
        pane.add(text_color,0,1,1,1);

        final Text text = new Text("\nColors used: 0");
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

                    else if (mouseEvent.getButton() == MouseButton.SECONDARY){
                        list.get(temp_i).setFill(colorPicker.getValue());
                        num_of_colors[temp_i] = list.get(temp_i).getFill();
                        text.setText("\nColors used: " + (getNumColors()));
                        text.setFill(Color.BLACK);
                    }

                    else if (mouseEvent.getButton() == MouseButton.PRIMARY){
                        text.setText("\nColors used: " + (getNumColors()));
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

        scene1 = new Scene(vbox, 900, 780);
        window.setScene(scene1);
        window.setTitle("Graph Coloring Game");
        window.show();

    }
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

}