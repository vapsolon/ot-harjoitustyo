package rig.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestImageGenerator {

    @Test
    public void testWordWrite(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0);
        try {
            ig.writeWord(255);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        assertEquals("Got incorrect CRC for WORD write", 205926545, ig.getCRCValue());
    }
    
    @Test
    public void testIntegerWrite(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0);
        try {
            ig.writeInt(64);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        assertEquals("Got incorrect CRC for Integer write", 2766056989L, ig.getCRCValue());
    }
    
    @Test
    public void testByteWrite(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0);
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
        ImageGenerator ig = new ImageGenerator(512, 512, 0);
        ImageGenerator ig2 = new ImageGenerator(512, 512, 0);
        assertTrue("Got incorrect magic value (somehow)", Arrays.equals(ig.magic(), ig2.magic()));
    }
    
    @Test
    public void testHeaderConsistency(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0);
        ImageGenerator ig2 = new ImageGenerator(512, 512, 0);
        assertTrue("Got incorrect header value (somehow)", Arrays.equals(ig.header(), ig2.header()));
    }
    
    @Test
    public void testBlackAndWhitePixelGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 1);
        int[] pixel = ig.genPixel();
        pixel = ig.genPixel();
        pixel = ig.genPixel();
        int sum = pixel[0] + pixel[1] + pixel[2];
        assertTrue("Generated incorrect Black&White pixel", (sum == 0 || sum == 765));
    }
    
    @Test
    public void badlyTestRegularPixelGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0);
        int[] pixel = ig.genPixel();
        int sum = pixel[0] + pixel[1] + pixel[2];
        assertTrue("Generated incorrect Regular pixel", (sum > 0 && sum < 765));
    }
    
    @Test
    public void generatedImageExists(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0);
        try {
            ig.generate();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        File f = new File(System.getProperty("java.io.tmpdir") + "RIG.png");
        assertTrue("Generated incorrect Regular pixel", (f.exists()));
    }
    
}
