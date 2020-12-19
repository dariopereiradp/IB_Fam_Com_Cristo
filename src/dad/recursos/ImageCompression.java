package dad.recursos;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;

import dad.fam_com_cristo.types.Membro;

/**
 * Classe para realizar a compressão de uma imagem.
 * @author <a href=" https://www.oodlestechnologies.com/blogs/Image-Compression-In-Java">Link</a>
 *
 */
public class ImageCompression {
	
    @SuppressWarnings("resource")
	public static void compress (File imageFile, Membro membro) throws FileNotFoundException, IOException{
        File compressedImageFile = new File(Membro.IMG_PATH + membro.getId() + ".jpg");

        InputStream inputStream = new FileInputStream(imageFile);
        OutputStream outputStream = new FileOutputStream(compressedImageFile);

        float imageQuality = 0.25f;

        //Create the buffered image
        ImageReader reader = ImageIO.getImageReadersBySuffix("jpg").next();
        reader.setInput(ImageIO.createImageInputStream(inputStream));
        IIOMetadata metadata = reader.getImageMetadata(0);
        BufferedImage bufferedImage = reader.read(0);

        //Get image writers
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpg");

        if (!imageWriters.hasNext())
            throw new IllegalStateException("Writers Not Found!!");

        ImageWriter imageWriter = (ImageWriter) imageWriters.next();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        imageWriter.setOutput(imageOutputStream);

        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

        //Set the compress quality metrics
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(imageQuality);
        if (imageWriteParam instanceof JPEGImageWriteParam) {
            ((JPEGImageWriteParam) imageWriteParam).setOptimizeHuffmanTables(true);
        }

        //Created image
        imageWriter.write(null, new IIOImage(bufferedImage, null, metadata), imageWriteParam);
        
        membro.setImg (new ImageIcon(compressedImageFile.getPath()));

        // close all streams
        inputStream.close();
        outputStream.close();
        imageOutputStream.close();
        imageWriter.dispose();
        
    }
}