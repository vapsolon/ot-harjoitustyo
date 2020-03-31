package rig.rig;

import java.io.IOException;
import rig.Utils.ImageGenerator;

/**
 * Main class for the program. <br>
 * Currently in charge of setting the actual image generator up and letting it
 * run. Will expand to set up and run the GUI class in the future.
 * @author vapsolon
 */
public class Main {
    
    /**
     * The main function. <br>
     * Simply initializes and calls the actual image generator with a hardcoded
     * resolution for now.
     * @param args Command line arguments
     */
    public static void main(String[] args){
        ImageGenerator ig = new ImageGenerator(512, 512);
        try {
            ig.generate();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
}
