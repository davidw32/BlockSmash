package GC.Reader;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Patrick Pavlenko
 * @version: 20.10.2019
 */
public class ImageReader {



    /**
     * Laedt Texturobjekt
     * @param filepath wo sich die Texturdatei befindet
     * @return die ID (Referenz) der Textur
     * Entnommen & modifiziert aus:
     * Gordon, V. Scott / clevenger, John (2019): computer graphics programming in opengl with Java. Second Edition. Mercury learning and education: dulles, Virginia.
     */
    public static int readTexture(String filepath)
    {
        Texture tex = null;
        InputStream input = ImageReader.class.getResourceAsStream(filepath);
        try { tex = TextureIO.newTexture(input,false,TextureIO.JPG);}
        catch(Exception e){e.printStackTrace(); System.out.println("TEXTURE NOT LOADED");}
        return tex.getTextureObject();
    }
}
