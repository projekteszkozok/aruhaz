package hu.elte.pt.store.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Képek beolvasásához szükséges metódusok megvalósításait tartalmazó osztály
 *
 * @author Nagy Krisztián
 * @version 0.%I%
 */
public final class ImageReader {

    /**
     * Olyan metódus, mely egy megadott relatív útvonalon található fájlt
     * megpróbál betölteni egy BufferedImage-be.
     *
     * @param relativePath a relatív útvonalat tartalmazó String baraméter.
     * @return a betöltött képet tartalmazó objektum.
     * @throws IOException amennyibe nem található fájl a megadott útvonalon,
     * vagy sikertelen volt a kép betöltése.
     * @see BufferedImage
     */
    public static BufferedImage readImage(String relativePath) throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return ImageIO.read(cl.getResource(relativePath));
    }
}
