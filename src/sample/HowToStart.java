package sample;


import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * this class is a part of the hints, it gives the player a startingpoint for the coloring
 */
public class HowToStart {
    public static void display (String title){

        Stage window = new Stage();
        GridPane layout = new GridPane();

        Scene scene = new Scene(layout, 300, 100);
        window.setScene(scene);


        window.setScene(scene);
        window.setTitle(title);


        Text text = new Text("It is easier to start from the vertice with the most amount of connections");
        Text text2 = new Text("from there out you can continue the colouring");
        layout.add(text, 0, 0, 2, 1);
        layout.add(text2, 0, 50, 2, 1);
        window.show();
    }
}
