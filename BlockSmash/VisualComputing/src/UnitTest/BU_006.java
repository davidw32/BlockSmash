package UnitTest;

import GC.GMath.Matrix4f;
import GC.GObject.GraphicObject;
import GC.GObject.Primitives.General;
import GC.Reader.ShaderReader;
import Global.JWindow;
import Global.Program;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
/**
 * @author: Patrick Pavlenko
 * @version: 11.12.2019
 * Generieren und laden von Uniforms
 */
public class BU_006 implements GLEventListener {

    Matrix4f mat;
    Program prog;

    private final String[] testvShader = ShaderReader.readShaderFile("Shader/Vertex/vertexGeneral.glsl");
    private final String[] testfShader = ShaderReader.readShaderFile("Shader/Fragment/fragGeneral.glsl");
    private JWindow win = new JWindow(800,600,"Test_006");
    private GLCanvas canvas;
    private BU_006 testclass;
    private GraphicObject obj;
    private static float[] test = new float[]{1f,2f,3f};


    /**
     * Canvas & Eventlistener und Jframe intializierung und zuweisungen
     */
    @Test
    public void setUp()
    {
        assertNotNull(testvShader);
        assertNotNull(testfShader);
        // Wenn beide Shader Strings nicht groesser als 0 sein sollten
        if(!(testvShader.length > 0) || !(testfShader.length > 0)) fail();

        testclass = new BU_006();

        // Wenn beide Shader Strings nicht groesser als 0 sein sollten
        if(!(testvShader.length > 0) || !(testfShader.length > 0)) fail();

        canvas = new GLCanvas();
        canvas.addGLEventListener(testclass);
        win.add(canvas);
        win.setVisible(true);
        try { TimeUnit.SECONDS.sleep(5); } catch(Exception e){}
    }

    /**
     * primare Methode zum testen
     * wird genutzt um GLContext zu erhalten
     * @param glAutoDrawable
     */
    public void init(GLAutoDrawable glAutoDrawable)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        //erstellen GraphicObject und VAOs und VBOs
        obj = new General(0,0,0);
        obj.generateBuffers();
        obj.createProgram(testvShader,testfShader);
        obj.loadVBOBuffer(0,test,3);
        assertNotNull(obj.getProgram().getProgram_ID());

        // Laden der Matrix als Uniform in das Shaderprogram
        mat = new Matrix4f();

        gl.glUseProgram(obj.getProgram().getProgram_ID());
        mat.loadUniform(obj.getProgram().getProgram_ID(),"mvMat");

        assertNotNull(mat.getUniformID());
        mat.sendUniformMat4f(mat);

        assertNotNull(mat.getMatBuffer());
    }

    public void display(GLAutoDrawable glAutoDrawable) {  }
    public void dispose(GLAutoDrawable glAutoDrawable) { }
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) { }
}
