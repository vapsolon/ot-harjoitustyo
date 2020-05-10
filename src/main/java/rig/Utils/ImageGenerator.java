package rig.Utils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Generates and writes a PNG image to disk. <br>
 * Contains most of the logic for generating PNG image data. Supports a couple
 * different generation modes as well.
 * @author vapsolon
 */
public class ImageGenerator {

    private final int width;
    private final int height;
    //Generation mode, so far 0=regular, 1=black&white, 2=+-, 3=strong colours,
    //4=eliminate the weak
    private final int mode;
    //The amount of increase/decrease per pixel for generation mode 2
    private final int variation;
    //Whether alpha values are randomly generated too
    private final boolean alpha;
    
    //Have the streams and other utilities available for the whole class
    private BufferedOutputStream file;
    private ByteArrayOutputStream buffer;
    private DeflaterOutputStream compress;
    private CRC32 crc;
    private Random rand;
    
    //Store the latest RGBA data so some specialized generation methods can use
    //them
    private int lr = -1;
    private int lg = -1;
    private int lb = -1;
    private int la = -1;
    
    //Some frequently-used magic numbers go here so checkstyle stops complaining
    //without having to make an exception for the entire magic number rule
    private final int black = 0;
    private final int white = 255;
    private final int limit = 256;
    
    /**
     * Public constructor for the class. <br>
     * Takes the width and the height of the image, the image generation mode
     * and the alpha flag as arguments and initializes all the required streams 
     * and the CRC-calculator in preparation for actual use.
     * @param w Width of the image to be generated
     * @param h Height of the image to be generated
     * @param m Image generation mode
     * @param v Amount of variation for generation mode 2
     * @param a Whether alpha values are randomly generated or not
     */
    public ImageGenerator(int w, int h, int m, int v, boolean a){
        //Store any data that was passed and initialize Random
        this.width = w;
        this.height = h;
        this.rand = new Random();
        this.mode = m;
        this.variation = v;
        this.alpha = a;
        //Prepare all the required streams ahead of writing
        try{
            String tempDir = System.getProperty("java.io.tmpdir");
            file = new BufferedOutputStream(new FileOutputStream(tempDir
                    + File.separator + "RIG.png"));
            buffer = new ByteArrayOutputStream();
            //More circumventing Checkstyle's Magic Number Checker
            final int def = 9;
            compress = new DeflaterOutputStream(buffer, new Deflater(def));
            crc = new CRC32();
        }
        catch (IOException ex){
            System.out.println(ex);
        }
    }
    
    /**
     * Returns the PNG magic header. <br>
     * Also includes the length of the immediately preceding "IHDR" block.
     * The magic header will always be the same so it can be safely hardcoded.
     * @return The hardcoded magic header
     */
    public byte[] magic(){
        //File header is always fixed-length and has the same content
        final byte[] magic = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13};
        
        return magic;
    }
    
    /**
     * Returns the hardcoded IHDR data. <br>
     * Similarly to magic(), this returns the hardcoded part of the PNG IHDR
     * block. Some of these values may end up becoming editable as support for
     * different generation methods gets slowly added, but for now simply
     * returning the hardcoded optimal set of values works.
     * @return The hardcoded part of the PNG IHDR block
     */
    public byte[] header(){
        //IHDR may change depending on colour mode, but for now it's static too
        final byte[] head = {8, 6, 0, 0, 0};
        
        return head;
    }
    
    /**
     * Returns the CRC calculator's current value. <br>
     * Currently purely for testing purposes. The simplest way so far to test
     * the writers of this class is writing data and comparing this
     * generated CRC value to a pre-calculated one to see if the data was
     * written correctly.
     * @return The current CRC value
     */
    public long getCRCValue(){
        return crc.getValue();
    }
    
    /**
     * Helper method for writing a DWORD from an Integer. <br>
     * Takes an Integer value and makes a big-endian DWORD out of it.
     * PNG wants its data to be big-endian, so some shifting is also required.
     * @param i Integer value to be written into the file
     * @throws IOException IOException
     */
    public void writeWord(int i) throws IOException{
        final int word = 4;
        byte[] b = new byte[word];
        b[0] = (byte)((i>>24) & white);
        b[1] = (byte)((i>>16) & white);
        b[2] = (byte)((i>>8) & white);
        b[3] = (byte)(i & white);
        write(b);
    }
    
    /**
     * Helper method for writing a Byte from an Integer. <br>
     * Similar to writeWord() except with the end result being a single Byte
     * instead of a DWORD this time.
     * @param i Integer value to be written into the file
     * @throws IOException IOException
     */
    public void writeInt(int i) throws IOException{
        byte[] b = {(byte)i};
        write(b);
    }

    /**
     * The actual main method for writing data into the output file. <br>
     * Takes byte-form data and writes it into the output file buffer. Also in
     * charge of keeping the CRC updated with the data that's being written.
     * @param b Bytearray to be written into the file
     * @throws IOException IOException
     */
    public void write(byte[] b) throws IOException{
        file.write(b);
        crc.update(b);
    }
    
    /**
     * Main image generation function. <br>
     * Where the magic happens. Generates a fresh PNG image from scratch. <br>
     * Starts off by writing in the magic file header and the IHDR block, both
     * of which are mostly static. Once the prepwork is done the function then
     * starts generating image data. Once the image data is generated it's
     * written to the file along with the IEND block and the file is closed.
     * @throws IOException IOException
     */
    public void generate() throws IOException{
        //Start the generation and writing process
        //Write in the magic header
        write(magic());
        //Reset CRC in preparation for generation
        crc.reset();
        //Write in the name for the header chunk
        write("IHDR".getBytes());
        //Write in IHDR data
        writeWord(width);
        writeWord(height);
        write(header());
        //Chunk done, write CRC
        writeWord((int)crc.getValue());
        
        //Start generating actual image data
        for(int i=0;i<height;i++){
            //Every scanline begins with 0
            compress.write(0);
            for(int j=0;j<width;j++){
                //Grab pixel data from genPixel() for neater code overall
                int[] pixel = genPixel();
                compress.write(pixel[0]); //RED
                compress.write(pixel[1]); //GREEN
                compress.write(pixel[2]); //BLUE
                compress.write(pixel[3]); //ALPHA
            }
        }
        //The image data has been generated, close the compressor
        compress.close();
        //Start writing the data into the file
        //Write the size of the coming IDAT chunk
        writeWord(buffer.size());
        //Reset CRC in preparation for IDAT
        crc.reset();
        //Start writing IDAT contents
        write("IDAT".getBytes());
        write(buffer.toByteArray());
        writeWord((int)crc.getValue());
        //IDAT is done, prepare for IEND
        //Write IEND length
        writeWord(0);
        //Reset CRC one more time
        crc.reset();
        //Write IEND and close the file
        write("IEND".getBytes());
        writeWord((int)crc.getValue());
        file.close();
    }
    
    /**
     * Randomly generates the colour values for an individual pixel. <br>
     * With more methods of generation being supported, pixel generation itself
     * now gets its own function. Checks the generation mode the generator has
     * been given and generates pixels with the correct colour values/range.
     * @return int[] containing 4 values matching the R, G, B and A data of a
     * single pixel
     */
    public int[] genPixel(){
        //Check generation mode and generate pixels based on it
        //Mode is Black&White
        if(this.mode == 1){
            //Generate alpha value first if needed
            int alphaVal = white;
            if(this.alpha){
                alphaVal = rand.nextInt(limit-1);
            }
            //If 0, pixel is black, if 1 white
            if(rand.nextInt(2) == 0){
                int[] res = {black, black, black, alphaVal};
                return res;
            }
            else{
                int[] res = {white, white, white, alphaVal};
                return res;
            }
        }
        //Mode is +-5
        else if(this.mode == 2){
            //If lr==-1 we're generating the first pixel which is unlimited
            if(this.lr == -1){
                lr = rand.nextInt(limit);
                lg = rand.nextInt(limit);
                lb = rand.nextInt(limit);
                if(this.alpha){
                    la = rand.nextInt(limit);
                }
                else{
                    la = white;
                }
                int[] res = {lr, lg, lb, la};
                return res;
            }
            //At least one pixel has been generated and we can use a
            //reference point
            else{
                //If 0, reduce the value of all colours
                //For simplicity looping is not allowed, so a 0-value will be
                //kept at 0 instead of rolling to 251
                if(rand.nextInt(2) == 0){
                    lr -= variation;
                    lg -= variation;
                    lb -= variation;
                    if(this.alpha){
                        la -= variation;
                    }
                }
                //Otherwise increase the values
                else{
                    lr += variation;
                    lg += variation;
                    lb += variation;
                    if(this.alpha){
                        la += variation;
                    }
                }
                int[] res = {lr, lg, lb, la};
                return res;
            }
        }
        //Strong colours mode
        else if(this.mode == 3){
            //Set the initial states of all values to be 0 and fully opaque
            int strr = black;
            int strg = black;
            int strb = black;
            int stra = white;
            //Then if the randomizer rolls a 0 allow that colour to be drawn
            if(rand.nextInt(2) == 0){
                strr = white;
            }
            if(rand.nextInt(2) == 0){
                strg = white;
            }
            if(rand.nextInt(2) == 0){
                strb = white;
            }
            //If alpha is active randomize the value
            if(this.alpha){
                stra = rand.nextInt(limit);
            }
            int[] res = {strr, strg, strb, stra};
            return res;
        }
        //Weak elimination mode
        else if(this.mode == 4){
            //Set the initial states of all values to be 0 and fully opaque
            int strr = black;
            int strg = black;
            int strb = black;
            int stra = white;
            //If alpha is active set it to full transparency by default
            if(alpha){
                stra = black;
            }
            //Generate values into a ready-made array
            int[] values = {rand.nextInt(limit), rand.nextInt(limit),
                rand.nextInt(limit)};
            //If value is strong, allow it to be kept and also turn the pixel
            //opaque
            if(values[0] == white){
                strr = white;
                stra = white;
            }
            if(values[1] == white){
                strg = white;
                stra = white;
            }
            if(values[2] == white){
                strb = white;
                stra = white;
            }
            int[] res = {strr, strg, strb, stra};
            return res;
        }
        //If mode is 0, generate a regular random pixel
        else{
            //Generate alpha value first if needed
            int alphaVal = white;
            if(this.alpha){
                alphaVal = rand.nextInt(limit-1);
            }
            //Generate array of colour data, all completely random
            int[] res = {rand.nextInt(limit), rand.nextInt(limit),
                rand.nextInt(limit), alphaVal};
            return res;
        }
    }
    
}
