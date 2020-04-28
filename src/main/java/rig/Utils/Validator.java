package rig.Utils;

/**
 * Simple helper class for validating user input. <br>
 * Currently only checks if input is numeric. Useful for making sure incorrect
 * height/width/variation data doesn't get passed to the generator and cause
 * a hard crash.
 * @author vapsolon
 */
public class Validator{
    
    /**
     * Checks if input is numeric. <br>
     * Goes through the given input string one character at a time and
     * immediately returns false if a non-numeric character is found. If the
     * method makes it through the entire input string without failing the
     * string was indeed numeric and it can return true.
     * @param input String to check for numericness
     * @return true if string was entirely numeric, false otherwise
     */
    public boolean isNumeric(String input){
        for(char c: input.toCharArray()){
            if(!(Character.isDigit(c))){
                return false;
            }
        }
        return true;
    }
    
}
