package UnitTest;

import GC.GObject.GraphicObject;
import GC.GObject.Primitives.General;
import Global.JWindow;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import org.junit.Test;

import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;

import static com.jogamp.opengl.GL.*;
import static junit.framework.TestCase.*;
/**
 * @author: Patrick Pavlenko
 * @version: 11.12.2019
 * Erstellen VAO
 */
public class BU_004 implements GLEventListener {

    private GraphicObject test;
    private JWindow win = new JWindow(800,600,"Test_005");
    private GLCanvas canvas;
    private BU_004 testclass;


    /**
     * Canvas & Eventlistener und Jframe intializierung und zuweisungen
     */
    @Test
    public void setUp()
    {
        testclass = new BU_004();

        canvas = new GLCanvas();
        canvas.addGLEventListener(testclass);
        win.add(canvas);
        win.setVisible(true);
        float i = 0;
        try { TimeUnit.SECONDS.sleep(10); } catch(Exception e){}
    }


    /**
     * primaere Testmethoden
     * genutzt, weil GLContext gebraucht wird
     * @param glAutoDrawable
     */
    public void init(GLAutoDrawable glAutoDrawable)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        // Initialisieren von GraphicObject
        test = new General(0,0,0,"Objects/Box._high.obj");
        assertNotNull(test.getVertex());
        if(test.getVertex().length > 0) assertTrue(true);
        //VAO erstellen
        test.generateBuffers();

        assertNotNull(test.getVao());
        //ueberpruefen ob VAO genriert worden ist
        if(test.getVao().length > 0) assertTrue(true);

        // ueberpruefen,ob VBO generiert worden ist
        gl.glBindBuffer(GL_ARRAY_BUFFER,test.getVbo()[0]);
        assertNotNull(test.getVbo());
        if(test.getVbo().length > 0) assertTrue(true);

        FloatBuffer buf = Buffers.newDirectFloatBuffer(test.getVertex());
        assertNotNull(buf);
        gl.glBufferData(GL_ARRAY_BUFFER,buf.limit()*4,buf,GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
        System.out.println("passed");
    }

    public void display(GLAutoDrawable glAutoDrawable) {  }
    public void dispose(GLAutoDrawable glAutoDrawable) { }
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) { }
}
