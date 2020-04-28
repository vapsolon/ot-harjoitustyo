package rig.UI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rig.Utils.ImageGenerator;
import rig.Utils.Validator;

/**
 * The graphical user interface for the program. <br>
 * Simply sets up the GUI, assigns the necessary handlers and shows the
 * finalized window. Most of the actual functionality of the program resides
 * elsewhere.
 * @author vapsolon
 */
public class GUI extends Application{
    
    private Validator validator;
    
    @Override
    public void start(Stage window){
        //Create the input validator well and early
        this.validator = new Validator();
        
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
        Label vLabel = new Label("Variation Amount (+-)");
        Label radioLabel = new Label("Generation Mode");
        TextField height = new TextField("512");
        TextField width = new TextField("512");
        TextField variation = new TextField("5");
        RadioButton regular = new RadioButton("Regular");
        RadioButton bnw = new RadioButton("Black and White");
        RadioButton range = new RadioButton("+-x");
        
        //Create a special label for displaying errors
        Label error = new Label("");
        error.setAlignment(Pos.CENTER);
        error.setTextAlignment(TextAlignment.CENTER);
        error.setStyle("-fx-font-size: 16;-fx-background-color: #FF6666;");
        error.setMaxWidth(Double.MAX_VALUE);
        error.setMaxHeight(Double.MAX_VALUE);
        error.setVisible(false);
        
        //Create a group for the radio buttons and add them into it
        ToggleGroup control = new ToggleGroup();
        regular.setToggleGroup(control);
        regular.setSelected(true);
        bnw.setToggleGroup(control);
        range.setToggleGroup(control);
        
        //Add the input components into their layout
        input.add(wLabel, 0, 0);
        input.add(hLabel, 1, 0);
        input.add(vLabel, 2, 0);
        input.add(width, 0, 1);
        input.add(height, 1, 1);
        input.add(variation, 2, 1);
        input.add(radioLabel, 0, 2);
        input.add(regular, 0, 3);
        input.add(bnw, 1, 3);
        input.add(range, 2, 3);
        input.add(error, 0, 4, 4, 1);
        
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
                else if(control.getSelectedToggle() == range){
                    mode = 2;
                }
                try {
                    if(!(validator.isNumeric(width.getText()) && validator.isNumeric(height.getText()) && validator.isNumeric(variation.getText()))){
                        error.setText("Incorrect input, make sure Width, Height and Variation are numeric");
                        error.setVisible(true);
                    }
                    else{
                        error.setVisible(false);
                        ImageGenerator ig = new ImageGenerator(Integer.valueOf(width.getText()), Integer.valueOf(height.getText()), mode, Integer.valueOf(variation.getText()));
                        ig.generate();
                        Image temp = new Image("file:///" + System.getProperty("java.io.tmpdir") + File.separator + "RIG.png");
                        output.setImage(temp);
                        window.sizeToScene();
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        };
        
        //Handler for image generation button
        EventHandler saveHandler = new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                FileChooser fc = new FileChooser();
                fc.setTitle("Save Image");
                fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG", "*.png")
                );
                File file = fc.showSaveDialog(window);
                if(file != null){
                    try {
                        Files.copy(Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "RIG.png"), Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
            }
        };
        
        //Create the necessary buttons
        Button generate = new Button("Generate Image");
        generate.setOnAction(generateHandler);
        Button save = new Button("Save Image");
        save.setOnAction(saveHandler);
        
        //Add buttons to their layout
        controlBox.getChildren().add(generate);
        controlBox.getChildren().add(save);
        base.setBottom(controlBox);
        
        //Create the actual scene from the base layout and set it as the default
        //view
        Scene view = new Scene(base);
        window.setScene(view);
        
        //Window is ready to be shown
        window.show();
    }
    
}
