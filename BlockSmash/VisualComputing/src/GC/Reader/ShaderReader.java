package GC.Reader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import static java.nio.file.Files.readAllLines;
        /**
        * @author Patrick Pavlenko
        *  Ausleser von Shader bzw GLSL Dateien
         *  Entnommen aus:
         *  Gordon, V. Scott / clevenger, John (2019): computer graphics programming in opengl with Java. Second Edition. Mercury learning and education: dulles, Virginia.
        * @version 12.10.2019
        */
public class ShaderReader {


    /**
     * @param path Pfad der Datei, die ausgelesen werden soll
     * @return String Array, wobei jeder Indeze eine Zeile der Datei hat
     */

    /**
     * @param filename Dateiname
     * @return String ,wo jeder Indeze eine Zeile des Quellcodes von der ausgelesenen datei ist
     */
    public static String[] readShaderFile(String filename)
    {	Vector<String> lines = new Vector<String>();
        Scanner sc;
        String[] program;
        String[] name = new String[1];
        try
        {
            InputStream input = ShaderReader.class.getResourceAsStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            for (String liner = br.readLine(); liner != null; liner = br.readLine()) {
             //   System.out.println(liner);
                lines.add(liner);
            }
            input.close();
            br.close();

            program = new String[lines.size()];
            for (int i = 0; i < lines.size(); i++)
            {	program[i] = (String) lines.elementAt(i) +"\n";
            }
        }
        catch (Exception e)
        {	System.err.println("IOException reading file: " + e );
            System.err.println("path: " + filename );
            System.err.println(ShaderReader.class.getResourceAsStream(filename));
            return null;
        }
        return program;
    }


}
