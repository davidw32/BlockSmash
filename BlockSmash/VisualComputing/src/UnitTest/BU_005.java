package UnitTest;

import GC.Reader.ShaderReader;
import Global.JWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
/**
 * @author: Patrick Pavlenko
 * @version: 11.12.2019
 * Erstellen VBO
 */
public class BU_005 implements GLEventListener {

    private final String[] testvShader = ShaderReader.readShaderFile("Shader/Vertex/vertexGeneral.glsl");
    private final String[] testfShader = ShaderReader.readShaderFile("Shader/Fragment/fragGeneral.glsl");
    private int fShaderID;
    private int vShaderID;
    private int program_ID;
    private JWindow win = new JWindow(800,600,"Test_005");
    private GLCanvas canvas;
    private BU_005 testclass;

    /**
     * Initialisierung von GraphicObject,sowie GLCanvas und Jframe
     */
    @Test
    public void setUp()
    {
        testclass = new BU_005();
        assertNotNull(testvShader);
        assertNotNull(testfShader);
        // Wenn beide Shader Strings nicht groesser als 0 sein sollten
        if(!(testvShader.length > 0) || !(testfShader.length > 0)) fail();

        canvas = new GLCanvas();
        canvas.addGLEventListener(testclass);
        win.add(canvas);
        win.setVisible(true);
        try { TimeUnit.SECONDS.sleep(10); } catch(Exception e){}
    }

    /**
     * Primaere Methode zum Testen
     * wird genutzt um GLContext zu erhalten
     * @param glAutoDrawable
     */
    public void init(GLAutoDrawable glAutoDrawable)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        //Erstellen Shader
        vShaderID = gl.glCreateShader(GL_VERTEX_SHADER);
        fShaderID = gl.glCreateShader(GL_FRAGMENT_SHADER);

        //Uberpruefen ob erstellt
        assertNotNull(vShaderID);
        assertNotNull(fShaderID);

        //Compilieren
        gl.glShaderSource(vShaderID,testvShader.length,testvShader,null,0);
        gl.glCompileShader(vShaderID);

        gl.glShaderSource(fShaderID,testfShader.length,testfShader,null,0);
        gl.glCompileShader(fShaderID);

        program_ID = gl.glCreateProgram();

        //Uberpruefen ob Program erstellt worden ist
        assertNotNull(program_ID);

        gl.glAttachShader(program_ID,vShaderID);
        gl.glAttachShader(program_ID,fShaderID);
        gl.glLinkProgram(program_ID);

        gl.glDeleteShader(vShaderID);
        gl.glDeleteShader(fShaderID);
        System.out.println("passed");
    }

    public void display(GLAutoDrawable glAutoDrawable) {  }
    public void dispose(GLAutoDrawable glAutoDrawable) { }
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) { }

}
