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
        ImageGenerator ig = new ImageGenerator(512, 512, 0, 0, false);
        try {
            ig.writeWord(255);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        assertEquals("Got incorrect CRC for WORD write", 205926545, ig.getCRCValue());
    }
    
    @Test
    public void testIntegerWrite(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0, 0, false);
        try {
            ig.writeInt(64);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        assertEquals("Got incorrect CRC for Integer write", 2766056989L, ig.getCRCValue());
    }
    
    @Test
    public void testByteWrite(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0, 0, false);
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
        ImageGenerator ig = new ImageGenerator(512, 512, 0, 0, false);
        ImageGenerator ig2 = new ImageGenerator(512, 512, 0, 0, false);
        assertTrue("Got incorrect magic value (somehow)", Arrays.equals(ig.magic(), ig2.magic()));
    }
    
    @Test
    public void testHeaderConsistency(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0, 0, false);
        ImageGenerator ig2 = new ImageGenerator(512, 512, 0, 0, false);
        assertTrue("Got incorrect header value (somehow)", Arrays.equals(ig.header(), ig2.header()));
    }
    
    @Test
    public void testBlackAndWhitePixelGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 1, 0, false);
        for(int i=0;i<1000;i++){
            int[] pixel = ig.genPixel();
            int sum = pixel[0] + pixel[1] + pixel[2];
            assertTrue("Generated incorrect Black&White pixel", (sum == 0 || sum == 765));
        }
    }
    
    @Test
    public void badlyTestRegularPixelGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0, 0, true);
        for(int i=0;i<1000;i++){
            int[] pixel = ig.genPixel();
            int sum = pixel[0] + pixel[1] + pixel[2];
            assertTrue("Generated incorrect Regular pixel", (sum > 0 && sum < 765));
        }
    }
    
    @Test
    public void testVariationPixelGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 2, 5, false);
        for(int i=0;i<1000;i++){
            int[] pixel = ig.genPixel();
            int[] comparison = ig.genPixel();
            int sum = pixel[0] + pixel[1] + pixel[2];
            int compSum = comparison[0] + comparison[1] + comparison[2];
            assertTrue("Generated incorrect Variation pixel", (sum - compSum == -15 || sum - compSum == 15));
        }
    }
    
    @Test
    public void testStrongPixelGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 3, 0, false);
        for(int i=0;i<1000;i++){
            int[] pixel = ig.genPixel();
            assertTrue("Generated incorrect Strong pixel R value", (pixel[0] == 0 || pixel[0] == 255));
            assertTrue("Generated incorrect Strong pixel G value", (pixel[1] == 0 || pixel[1] == 255));
            assertTrue("Generated incorrect Strong pixel B value", (pixel[2] == 0 || pixel[2] == 255));
        }
    }
    
    @Test
    public void testStrongAlphaPixelGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 3, 0, true);
        for(int i=0;i<1000;i++){
            int[] pixel = ig.genPixel();
            assertTrue("Generated incorrect Strong pixel A value", (pixel[3] >= 0 || pixel[3] <= 255));
        }
    }
    
    @Test
    public void testEliminationPixelGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 4, 0, false);
        for(int i=0;i<1000;i++){
            int[] pixel = ig.genPixel();
            assertTrue("Generated incorrect Elimination pixel R value", (pixel[0] == 0 || pixel[0] == 255));
            assertTrue("Generated incorrect Elimination pixel G value", (pixel[1] == 0 || pixel[1] == 255));
            assertTrue("Generated incorrect Elimination pixel B value", (pixel[2] == 0 || pixel[2] == 255));
        }
    }
    
    @Test
    public void testEliminationAlphaPixelGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 4, 0, true);
        for(int i=0;i<1000;i++){
            int[] pixel = ig.genPixel();
            assertTrue("Generated incorrect Elimination pixel A value", (pixel[3] == 0 || pixel[3] == 255));
        }
    }
    
    @Test
    public void generatedImageExists(){
        ImageGenerator ig = new ImageGenerator(512, 512, 0, 0, false);
        try {
            ig.generate();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        File f = new File(System.getProperty("java.io.tmpdir") + File.separator + "RIG.png");
        assertTrue("Generated incorrect Regular pixel", (f.exists()));
    }
    
    @Test
    public void testAlphaGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 1, 0, true);
        for(int i=0;i<1000;i++){
            int[] pixel = ig.genPixel();
            assertTrue("Generated opaque pixel in alpha mode", (pixel[3] >= 0 && pixel[3] < 255));
        }
    }
    
    @Test
    public void testVariationAlphaPixelGeneration(){
        ImageGenerator ig = new ImageGenerator(512, 512, 2, 5, true);
        for(int i=0;i<1000;i++){
            int[] pixel = ig.genPixel();
            int[] comparison = ig.genPixel();
            int sum = pixel[0] + pixel[1] + pixel[2] + pixel[3];
            int compSum = comparison[0] + comparison[1] + comparison[2] + comparison[3];
            assertTrue("Generated incorrect Alpha Variation pixel", (sum - compSum == -20 || sum - compSum == 20));
        }
    }
    
}
