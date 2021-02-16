package UnitTest;
import GC.GObject.GraphicObject;
import GC.GObject.Primitives.General;
import Global.JWindow;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import org.junit.*;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
/**
 * @author: Patrick Pavlenko
 * @version: 11.12.2019
 * Erstellen GraphicObject
 */
public class BU_003 implements GLEventListener {

    GraphicObject general;
    private JWindow win = new JWindow(800,600,"Test_005");
    private GLCanvas canvas;
    private BU_003 testclass;


    /**
     * Canvas & Eventlistener und Jframe intializierung und zuweisungen
     */
    @Before
    public void setUp()
    {
        testclass = new BU_003();
        canvas = new GLCanvas();
        canvas.addGLEventListener(testclass);
        win.add(canvas);
        win.setVisible(true);
        try { TimeUnit.SECONDS.sleep(10); } catch(Exception e){}
    }

    @Test
    public void start(){}

    /**
     * Uberpruefung  GraphicObject
     * @param glAutoDrawable
     */
    public void init(GLAutoDrawable glAutoDrawable)
    {
        //Erstellen GraphicObject
        general = new General(0,0,0,"Objects/Box._low.obj");
        GL4 gl = (GL4) GLContext.getCurrentGL();
        //Genrierung und uberpruefung von VAO
        general.generateBuffers();
        assertTrue(general.getVao()[0] > 0 );
        assertTrue(general.getVao()[0] != 0 );

        //generierung und Uberpruefung von VBO
        general.loadVBOBuffer(0,general.getVertex(),3);
        general.loadVBOBuffer(1,general.getTexel(),2);
        general.loadVBOBuffer(2,general.getNormals(),3);
        assertTrue(general.getVbo()[0] > 0 );
        assertTrue(general.getVbo()[1] > 0 );
        assertTrue(general.getVbo()[2] > 0 );
    }
    public void display(GLAutoDrawable glAutoDrawable) {  }
    public void dispose(GLAutoDrawable glAutoDrawable) { }
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) { }
}
