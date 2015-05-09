package hu.elte.pt.store.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Nagy Krisztián
 */
public final class ImageReader {
    public static BufferedImage readImage(String relativePath) throws IOException{
        ClassLoader cl = Thread.currentThread().getContextClassLoader();   
        return ImageIO.read(cl.getResource(relativePath));
    }    
}
