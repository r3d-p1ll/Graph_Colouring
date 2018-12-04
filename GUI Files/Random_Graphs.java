package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorInput;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class Random_Graphs {

    static Scene scene1;
    static double orgSceneX, orgSceneY;
    static Paint[] num_of_colors;
    static Circles [] cir;
    static int width = 1200;
    static int height = 800;
    static Color color_holder = Color.WHITE;


    private static EventHandler<MouseEvent> mousePressedEventHandler = (t) ->
    {
        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();

        Circle c = (Circle) (t.getSource());
        c.toFront();
    };

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

    private static Circle createCircle(double x, double y, double r, Color color, int adj_mat_length)
    {
        Circle circle = new Circle(x, y, r, color);
        for (int i=0; i<200; i++){
            Force_Directed_Alg.display(circle, adj_mat_length, x, y);
        }
        circle = new Circle(Force_Directed_Alg.getX(), Force_Directed_Alg.getY(), r, color);

        circle.setStroke(Color.BLACK);
        circle.setCursor(Cursor.CROSSHAIR);

        circle.setOnMousePressed(mousePressedEventHandler);
        circle.setOnMouseDragged(mouseDraggedEventHandler);

        return circle;
    }

    private static Line connect(Circle c1, Circle c2)
    {
        Line line = new Line();

        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());

        line.setStrokeWidth(2);
        line.setStrokeLineCap(StrokeLineCap.BUTT);
        line.getStrokeDashArray().setAll(1.0, 4.0);

        return line;
    }

    public static int getNumColors() {
        Set<Paint> newset = new HashSet<Paint>();
        for (int i=0; i < num_of_colors.length; i++) {
            newset.add(num_of_colors[i]);
        }
        System.out.println(newset);
        return newset.size()-1;
    }

    public static void Hinter(){
        Hint.display("Hint", "Need help?");
    }

    public static boolean checkAdj(int [][] adj_matrix, int v, Paint c){
        for (int i = 0; i < adj_matrix.length; i++){
            if (adj_matrix[v][i] == 1) {
                if (cir[i].Circle1.getFill() == c) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void display(String title, String message, int [][] adj, int [] array_random){
        Stage window = new Stage();

        window.setTitle(title);
        window.setMinWidth(250);

        int [][] adj_matrix = adj;

        Group gr1 = new Group();
        GridPane pane = new GridPane();

        VBox vbox = new VBox(gr1, pane);

        //Adding HINTS button
        Button buttonhint = new Button("HINTS");
        pane.add(buttonhint, 5,0,1,1);
        buttonhint.setOnAction(e ->  Hinter());

        Label label = new Label();
        label.setText(message);
        gr1.getChildren().add(label);

        // Reading adjacency matrix for random generated values and creating the graph
//        final Circles cir[] = new Circles[adj_matrix.length];
        cir = new Circles[adj_matrix.length];
        for (int d=0; d<adj_matrix.length; d++) {
            int random_width = (int)(Math.random()*(width-50));
            int random_height = (int)(Math.random()*(height-50));
            int z = array_random[d];
            cir[d] = new Circles(random_width, random_height);
            cir[d].Circle1 = createCircle(random_width, random_height, 15, Color.WHITE, adj_matrix.length); // Generating circles in random places
            Text number = new Text(random_width+20, random_height+20, String.valueOf(z));
            gr1.getChildren().addAll(cir[d].Circle1, number);
            cir[d].Circle1.toFront();
            number.toFront();
        }

        for (int i=0; i<adj_matrix.length; i++){
            for (int j=0; j<adj_matrix[i].length; j++){
                if (adj_matrix[i][j] == 1){
                    Line line1 = connect(cir[i].Circle1, cir[j].Circle1);
                    gr1.getChildren().add(line1);
                    line1.toBack();
                }
            }
        }

        // ADDING THE COLOR PICKER
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(null);
        Text text_color = new Text("Pick Your Color." + "\n" + "After that right-click on vertex you'd like to color.");
        text_color.setFont(Font.font ("Verdana", 14));
        text_color.setFill(Color.BLACK);
        colorPicker.setOnAction((ActionEvent t) -> {color_holder = colorPicker.getValue();});
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.add(colorPicker, 0, 0, 1, 1);
        pane.add(text_color,0,1,1,1);

        //Adding Event Filter to check which circle was clicked
        num_of_colors = new Paint[cir.length]; //An array to hold the used colors.
        final Text text = new Text("\nColors used: 0");
        text.setFont(Font.font ("Verdana", 14));

        for (int i=0; i<cir.length; i++){
            final int temp_i = i;
            cir[i].Circle1.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    text.setText("");
                    if (mouseEvent.getButton() == MouseButton.SECONDARY && checkAdj(adj_matrix, temp_i, colorPicker.getValue())){
                        if (cir[temp_i].Circle1.getFill() == Color.WHITE) {
                            cir[temp_i].Circle1.setFill(colorPicker.getValue());
                            num_of_colors[temp_i] = cir[temp_i].Circle1.getFill();
                            text.setText("\nColors used: " + (Random_Graphs.getNumColors()));
                            text.setFill(Color.BLACK);

                        } else {
                            System.out.println("ALREADY COLORED");
//                            text.setText("Colors used: " + (Random_Graphs.getNumColors()));
                            text.setText("ALREADY COLORED");
                            text.setFill(Color.RED);
                        }
                    }

                    else if (mouseEvent.getButton() == MouseButton.PRIMARY){
                        text.setText("\nColors used: " + (Random_Graphs.getNumColors()));
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

        scene1 = new Scene(vbox, width+30, height+30);
        window.setScene(scene1);
        window.setTitle("Graph Coloring Game");
        window.show();
    }
}