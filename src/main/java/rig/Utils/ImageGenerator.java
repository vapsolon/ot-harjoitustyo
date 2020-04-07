package rig.Utils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Generates and writes a PNG image to disk. <br>
 * Contains most of the logic for generating PNG image data. Support for
 * generation modes other than full colour scale RNG is slowly being built.
 * @author vapsolon
 */
public class ImageGenerator {

    private final int width;
    private final int height;
    //Generation mode, so far 0=regular, 1=black&white
    private final int mode;
    
    //Have the streams and other utilities available for the whole class
    private BufferedOutputStream file;
    private ByteArrayOutputStream buffer;
    private DeflaterOutputStream compress;
    private CRC32 crc;
    private Random rand;
    
    //Some frequently-used magic numbers go here so checkstyle stops complaining
    //without having to make an exception for the entire magic number rule
    private final int black = 0;
    private final int white = 255;
    private final int limit = 256;
    
    /**
     * Public constructor for the class. <br>
     * Takes the width and the height of the image and the image generation mode
     * as arguments and initializes all the required streams and the
     * CRC-calculator in preparation for actual use.
     * @param w Width of the image to be generated
     * @param h Height of the image to be generated
     * @param m Image generation mode
     */
    public ImageGenerator(int w, int h, int m){
        this.width = w;
        this.height = h;
        this.rand = new Random();
        this.mode = m;
        //Prepare all the required streams ahead of writing
        try{
            String tempDir = System.getProperty("java.io.tmpdir");
            file = new BufferedOutputStream(new FileOutputStream(tempDir
                    + "RIG.png"));
            buffer = new ByteArrayOutputStream();
            compress = new DeflaterOutputStream(buffer, new Deflater(9));
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
     * the functionality of this class is writing data and comparing this
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
        byte[] b = new byte[4];
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
                compress.write(white); //ALPHA
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
     * @return int[] containing 3 values matching the R, G and B data of a
     * single pixel
     */
    public int[] genPixel(){
        //Tarkastetaan generointimoodi ja luodaan pikseleitä sen perusteella
        if(this.mode == 1){
            //Jos 0, pikseli on musta, jos 1 se on valkoinen
            if(rand.nextInt(2) == 0){
                int[] res = {black, black, black};
                return res;
            }
            else{
                int[] res = {white, white, white};
                return res;
            }
        }
        //Jos moodi on 0 luodaan tavallinen pikseli
        else{
            int[] res = {rand.nextInt(limit), rand.nextInt(limit),
                rand.nextInt(limit)};
            return res;
        }
    }
    
}
