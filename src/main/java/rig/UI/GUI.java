package rig.UI;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import rig.Utils.ImageGenerator;

/**
 * The graphical user interface for the program. <br>
 * Simply sets up the GUI, assigns the necessary handlers and shows the
 * finalized window. Most of the actual functionality of the program resides
 * elsewhere.
 * @author vapsolon
 */
public class GUI extends Application{

    @Override
    public void start(Stage window){
        window.setTitle("RIG");
        
        //Create the base layout
        BorderPane base = new BorderPane();
        base.setPadding(new Insets(10, 10, 10, 10));
        
        //Create the layout for the generated image
        GridPane image = new GridPane();
        image.setHgap(10);
        image.setVgap(10);
        image.setPadding(new Insets(10, 10, 10, 10));
        
        //Create the ImageView component for the generated image
        ImageView output = new ImageView();
        output.setPreserveRatio(true);
        output.setSmooth(true);
        
        //Add ImageView to its layout
        image.add(output, 0, 0);
        //Add the ImageView layout to base
        base.setCenter(image);
        
        //Create the layout for the input components
        GridPane input = new GridPane();
        input.setHgap(10);
        input.setVgap(10);
        input.setPadding(new Insets(10, 10, 10, 10));
        
        //Create the input components themselves
        Label hLabel = new Label("Height");
        Label wLabel = new Label("Width");
        Label radioLabel = new Label("Generation Mode");
        TextField height = new TextField("512");
        TextField width = new TextField("512");
        RadioButton regular = new RadioButton("Regular");
        RadioButton bnw = new RadioButton("Black and White");
        
        //Create a group for the radio buttons and add them into it
        ToggleGroup control = new ToggleGroup();
        regular.setToggleGroup(control);
        regular.setSelected(true);
        bnw.setToggleGroup(control);
        
        //Add the input components into their layout
        input.add(wLabel, 0, 0);
        input.add(hLabel, 1, 0);
        input.add(width, 0, 1);
        input.add(height, 1, 1);
        input.add(radioLabel, 0, 2);
        input.add(regular, 0, 3);
        input.add(bnw, 1, 3);
        
        //Add the input layout to base
        base.setTop(input);
        
        //Create the button layout
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setPadding(new Insets(20, 0, 0, 0));
        
        //Handler for image generation button
        EventHandler generateHandler = new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                int mode = 0;
                if(control.getSelectedToggle() == bnw){
                    mode = 1;
                }
                try {
                    ImageGenerator ig = new ImageGenerator(Integer.valueOf(width.getText()), Integer.valueOf(height.getText()), mode);
                    ig.generate();
                    Image temp = new Image("file:///" + System.getProperty("java.io.tmpdir") + File.separator + "RIG.png");
                    output.setImage(temp);
                    window.sizeToScene();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        };
        
        //Create the necessary buttons
        Button generate = new Button("Generate Image");
        generate.setOnAction(generateHandler);
        
        //Add buttons to their layout
        controlBox.getChildren().add(generate);
        base.setBottom(controlBox);
        
        //Create the actual scene from the base layout and set it as the default
        //view
        Scene view = new Scene(base);
        window.setScene(view);
        
        //Window is ready to be shown
        window.show();
    }
    
}
