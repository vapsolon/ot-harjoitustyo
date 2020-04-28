package rig.Utils;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestValidator {

    @Test
    public void testNumericInput(){
        Validator v = new Validator();
        assertTrue("Numeric input deemed non-numeric", v.isNumeric("512"));
    }
    
    @Test
    public void testNonNumericInput(){
        Validator v = new Validator();
        assertTrue("Non-numeric input deemed numeric", !(v.isNumeric("512wx435890")));
    }
    
}
