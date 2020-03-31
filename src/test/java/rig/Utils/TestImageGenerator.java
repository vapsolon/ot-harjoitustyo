package rig.Utils;

import java.io.IOException;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestImageGenerator {

    @Test
    public void testWordWrite(){
        ImageGenerator ig = new ImageGenerator(512, 512);
        try {
            ig.writeWord(255);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        assertEquals("Got incorrect CRC for WORD write", 205926545, ig.getCRCValue());
    }
    
    @Test
    public void testIntegerWrite(){
        ImageGenerator ig = new ImageGenerator(512, 512);
        try {
            ig.writeInt(64);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        assertEquals("Got incorrect CRC for Integer write", 2766056989L, ig.getCRCValue());
    }
    
    @Test
    public void testByteWrite(){
        ImageGenerator ig = new ImageGenerator(512, 512);
        byte[] in = {127};
        try {
            ig.write(in);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        assertEquals("Got incorrect CRC for byte[] write", 314082080, ig.getCRCValue());
    }
    
    @Test
    public void testMagicConsistency(){
        ImageGenerator ig = new ImageGenerator(512, 512);
        ImageGenerator ig2 = new ImageGenerator(512, 512);
        assertTrue("Got incorrect magic value (somehow)", Arrays.equals(ig.magic(), ig2.magic()));
    }
    
    @Test
    public void testHeaderConsistency(){
        ImageGenerator ig = new ImageGenerator(512, 512);
        ImageGenerator ig2 = new ImageGenerator(512, 512);
        assertTrue("Got incorrect header value (somehow)", Arrays.equals(ig.header(), ig2.header()));
    }
    
}
