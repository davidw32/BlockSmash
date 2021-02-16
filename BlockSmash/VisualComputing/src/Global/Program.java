package Global;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import java.nio.IntBuffer;

import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;
import static com.jogamp.opengl.GL3ES3.*;
/**
 * @author Patrick Pavlenko
 * @version 12.10.2019
 * Programshader
 */
public class Program {


    /**
     * Shaderprogram Referenz
     */
    public int program_ID; // Shaderprogram Referenz

    /**
     *  Shaderprogram zur Benutzung der Programmierbaren Pipeline
     * @param vShader ausgelesener Vertex-Shader
     * @param fShader ausgelesener Fragment-Shader
     */
    public Program(String[] vShader,String[] fShader)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        //initialisieren Shader -> fuehrt zu ID Referenz
        int vShader_ID = gl.glCreateShader(GL_VERTEX_SHADER);
        int fShader_ID = gl.glCreateShader(GL_FRAGMENT_SHADER);

        //Compilieren der Shader
        gl.glShaderSource(vShader_ID,vShader.length,vShader,null,0);
        gl.glCompileShader(vShader_ID);
        printShaderLog(vShader_ID);

        gl.glShaderSource(fShader_ID,fShader.length,fShader,null,0);
        gl.glCompileShader(fShader_ID);
        // Fuer Ausgaben bei Fehler
        printShaderLog(fShader_ID);

        // Program Erstellen & Attachen ins Program um anschliessen das Program zu linken
        int id = gl.glCreateProgram();
        gl.glAttachShader(id,vShader_ID);
        gl.glAttachShader(id,fShader_ID);
        gl.glLinkProgram(id);

        // Shadererferenzen werden geloescht
        gl.glDeleteShader(vShader_ID);
        gl.glDeleteShader(fShader_ID);
        program_ID = id;

        // Fuer Ausgaben bei Fehler
        printProgramLog(program_ID);
    }

    /**
     *
     * @param vShader ausgelesener Vertex-Shader
     * @param fShader ausgelesener Fragment-Shader
     * @param tes1Shader ausgelesener Control-Shader
     * @param tes2Shader ausgelesener Evaluator-Shader
     */
    public Program(String[] vShader,String[] fShader,String[] tes1Shader, String[] tes2Shader)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        int vShader_ID = gl.glCreateShader(GL_VERTEX_SHADER);
        int fShader_ID = gl.glCreateShader(GL_FRAGMENT_SHADER);
        int tes1Shader_ID = gl.glCreateShader(GL_TESS_CONTROL_SHADER);
        int tes2Shader_ID = gl.glCreateShader(GL_TESS_EVALUATION_SHADER);


        gl.glShaderSource(vShader_ID,vShader.length,vShader,null,0);
        gl.glCompileShader(vShader_ID);
        printShaderLog(vShader_ID);

        gl.glShaderSource(fShader_ID,fShader.length,fShader,null,0);
        gl.glCompileShader(fShader_ID);
        printShaderLog(fShader_ID);

        gl.glShaderSource(tes1Shader_ID,tes1Shader.length,tes1Shader,null,0);
        gl.glCompileShader(tes1Shader_ID);
        printShaderLog(tes1Shader_ID);

        gl.glShaderSource(tes2Shader_ID,tes2Shader.length,tes2Shader,null,0);
        gl.glCompileShader(tes2Shader_ID);
        printShaderLog(tes2Shader_ID);


        program_ID = gl.glCreateProgram();
        gl.glAttachShader(program_ID,vShader_ID);
        gl.glAttachShader(program_ID,fShader_ID);
        gl.glAttachShader(program_ID,tes1Shader_ID);
        gl.glAttachShader(program_ID,tes2Shader_ID);
        gl.glLinkProgram(program_ID);
        printProgramLog(program_ID);

        gl.glDeleteShader(vShader_ID);
        gl.glDeleteShader(fShader_ID);
        gl.glDeleteShader(tes1Shader_ID);
        gl.glDeleteShader(tes2Shader_ID);

    }

    /**
     * @param vShader ausgelesener Vertex-Shader
     * @param fShader ausgelesener Fragment-Shader
     * @param gShader ausgelesener Geometry Shader
     */
    public Program(String[] vShader,String[] fShader,String[] gShader)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        int vShader_ID = gl.glCreateShader(GL_VERTEX_SHADER);
        int fShader_ID = gl.glCreateShader(GL_FRAGMENT_SHADER);
        int gShader_ID = gl.glCreateShader(GL_GEOMETRY_SHADER);


        gl.glShaderSource(vShader_ID,vShader.length,vShader,null,0);
        gl.glCompileShader(vShader_ID);
        printShaderLog(vShader_ID);

        gl.glShaderSource(fShader_ID,fShader.length,fShader,null,0);
        gl.glCompileShader(fShader_ID);
        printShaderLog(fShader_ID);

        gl.glShaderSource(gShader_ID,gShader.length,gShader,null,0);
        gl.glCompileShader(gShader_ID);
        printShaderLog(gShader_ID);

        program_ID = gl.glCreateProgram();

        gl.glAttachShader(program_ID,vShader_ID);
        gl.glAttachShader(program_ID,fShader_ID);
        gl.glAttachShader(program_ID,gShader_ID);

        gl.glLinkProgram(program_ID);
        printProgramLog(program_ID);

        gl.glDeleteShader(vShader_ID);
        gl.glDeleteShader(fShader_ID);
        gl.glDeleteShader(gShader_ID);


    }


    /** Fehleranzeige aus GLSL werden in der Konsole ausgegeben
     * entnommen aus: Gordon, V. Scott / clevenger, John (2019): computer graphics programming in opengl with Java. Second Edition. Mercury learning and education: dulles, Virginia.
     * @param shader Shaderreferenz
     */
    private static void printShaderLog(int shader)
    {	GL4 gl = (GL4) GLContext.getCurrentGL();
        int[] len = new int[1];
        int[] chWrittn = new int[1];
        byte[] log = null;

        // determine the length of the shader compilation log
        gl.glGetShaderiv(shader, GL_INFO_LOG_LENGTH, len, 0);
        if (len[0] > 0)
        {	log = new byte[len[0]];
            gl.glGetShaderInfoLog(shader, len[0], chWrittn, 0, log, 0);
            System.out.println("Shader Info Log: ");
            for (int i = 0; i < log.length; i++)
            {	System.out.print((char) log[i]);
            }
        }
    }


    /** Fehleranzeigen im Programmshader werden ausgegeeben
     * Entnommen aus: Gordon, V. Scott / clevenger, John (2019): computer graphics programming in opengl with Java. Second Edition. Mercury learning and education: dulles, Virginia.
     * @param prog Programmshader referenz
     */
    public static void printProgramLog(int prog)
    {	GL4 gl = (GL4) GLContext.getCurrentGL();
        int[] len = new int[1];
        int[] chWrittn = new int[1];
        byte[] log = null;

        // determine length of the program compilation log
        gl.glGetProgramiv(prog, GL_INFO_LOG_LENGTH, len, 0);
        if (len[0] > 0)
        {	log = new byte[len[0]];
            gl.glGetProgramInfoLog(prog, len[0], chWrittn, 0, log, 0);
            System.out.println("Program Info Log: ");
            for (int i = 0; i < log.length; i++)
            {	System.out.print((char) log[i]);
            }
        }
    }

    public int getProgram_ID() { return program_ID; }
}
