package rig.Utils;

public class Validator{
    
    public boolean isNumeric(String input){
        for(char c: input.toCharArray()){
            if(!(Character.isDigit(c))){
                return false;
            }
        }
        return true;
    }
    
}