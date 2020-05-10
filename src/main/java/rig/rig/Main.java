package rig.rig;

import javafx.application.Application;
import rig.UI.GUI;

/**
 * Main class for the program. <br>
 * Only serves as a separate entrypoint that launches the user interface so the
 * GUI class is at least a bit less cluttered.
 * @author vapsolon
 */
public final class Main {
    
    /**
    * Empty private constructor so Checkstyle stops complaining.
    */
    private Main(){
        
    }
    
    /**
     * The main function. <br>
     * Simply just launches the application with the GUI class.
     * @param args Command line arguments
     */
    public static void main(String[] args){
        Application.launch(GUI.class);
    }
    
}
