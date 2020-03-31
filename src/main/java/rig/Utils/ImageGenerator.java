package rig.Utils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Generates and writes a PNG image to disk. <br>
 * Will contain most of the logic for generating PNG image data. Currently only
 * supports very specific conditions (hardcoded colour mode, every pixel is
 * random-gen) but will eventually be expanded to support a range of different
 * generation methods.
 * @author vapsolon
 */
public class ImageGenerator {

    private final int width;
    private final int height;
    
    BufferedOutputStream file;
    ByteArrayOutputStream buffer;
    DeflaterOutputStream compress;
    CRC32 crc;
    Random rand;
    
    /**
     * Public constructor for the class. <br>
     * Takes the width and the height of the image as arguments and initializes
     * all the required streams and the CRC-calculator in preparation for actual
     * use.
     * @param w Width of the image to be generated
     * @param h Height of the image to be generated
     */
    public ImageGenerator(int w, int h){
        this.width = w;
        this.height = h;
        this.rand = new Random();
        //Prepare all the required streams ahead of writing
        try {
            file = new BufferedOutputStream(new FileOutputStream("image.png"));
            buffer = new ByteArrayOutputStream();
            compress = new DeflaterOutputStream(buffer, new Deflater(9));
            crc = new CRC32();
        } catch (FileNotFoundException ex) {
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
        byte[] magic = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13};
        
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
        byte[] head = {8, 6, 0, 0, 0};
        
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
     * @throws IOException
     */
    public void writeWord(int i) throws IOException{
        byte b[]= new byte[4];
        b[0] = (byte)((i>>24) & 255);
        b[1] = (byte)((i>>16) & 255);
        b[2] = (byte)((i>>8) & 255);
        b[3] = (byte)(i & 255);
        write(b);
    }
    
    /**
     * Helper method for writing a Byte from an Integer. <br>
     * Similar to writeWord() except with the end result being a single Byte
     * instead of a DWORD this time.
     * @param i Integer value to be written into the file
     * @throws IOException
     */
    public void writeInt(int i) throws IOException{
        byte b[] = {(byte)i};
        write(b);
    }

    /**
     * The actual main method for writing data into the output file. <br>
     * Takes byte-form data and writes it into the output file buffer. Also in
     * charge of keeping the CRC updated with the data that's being written.
     * @param b Bytearray to be written into the file
     * @throws IOException
     */
    public void write(byte b[]) throws IOException{
        file.write(b);
        crc.update(b);
    }
    
    /**
     * Main image generation function. <br>
     * Where the magic happens. Generates a fresh PNG image from scratch. <br>
     * Starts off by writing in the magic file header and the IHDR block, both
     * of which are mostly static. Once the prepwork is done the function then
     * starts generating image data with random RGB values and an alpha value
     * of 255. Once the image data is generated it's written to the file along
     * with the IEND block and the file is closed.
     * @throws IOException
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
                compress.write(rand.nextInt(256)); //RED
                compress.write(rand.nextInt(256)); //GREEN
                compress.write(rand.nextInt(256)); //BLUE
                compress.write(255); //ALPHA
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
    
}
