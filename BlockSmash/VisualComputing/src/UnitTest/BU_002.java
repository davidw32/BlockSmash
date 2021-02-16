package UnitTest;
import GC.GObject.Primitives.Cursor;
import GC.GObject.Primitives.General;
import Global.Camera;
import org.junit.*;
import static org.junit.Assert.*;
/**
 * @author: Patrick Pavlenko
 * @version: 11.12.2019
 * Cursor vs Block Kollision
 */
public class BU_002 {

    General general;
    Cursor cursorRight;
    Cursor cursorLeft;
    Camera cam;

    /**
     * Initialisierung Kamera und GraphicObject
     */
    @Before
    public void setUp()
    {
        general = new General(2.2f,1.8f,2,"Objects/Box._low.obj");
        cam = new Camera(0,0,0,600,800);
        cursorRight = new Cursor(2f,2f,2f,"Objects/Cursor.obj");
        cursorLeft = new Cursor(2f,5f,2f,"Objects/Cursor.obj");
    }

    /**
     * Uberpruefen Model und ModelviewMatrix
     */
    @Test
    public void checkMundMvMat()
    {
        assertNotNull(general.getModelMatrix());
        assertTrue(general.getModelMatrix().getMatrix()[3] != 0);
        assertTrue(general.getModelMatrix().getMatrix()[7] != 0);
        assertTrue(general.getModelMatrix().getMatrix()[11] != 0);
        general.createMvMat(cam.getViewMatrix());
        assertNotNull(general.getModelViewMatrix());
    }

    /**
     * Vbos ueberpruefen ob diese Werte haben
     */
    @Test
    public void checkVertexAttributes()
    {
        assertNotNull(general.getVertex());
        assertNotNull(general.getTexel());
        assertNotNull(general.getNormals());
        assertTrue(general.getVertex().length>1);
        assertTrue(general.getTexel().length>1);
        assertTrue(general.getNormals().length>1);
    }


    /**
     * Kollisionsabfrage
     * @param block der Block mit dem Cursor kollidiert wird
     * @param cur der Cursor mit dem der Block kollidiert
     * @return
     */
    private boolean checkCollisionGame(General block,Cursor cur)
    {
        float[]  bMat = block.getModelMatrix().getMatrix();
        float[] c = cur.getModelMatrix().getMatrix();
        // Y-Achsenvergleich Block vs left Cursor( Skalierungen der Y-Achse werden dabei mit beachtet)
        // Cursor obere Bereich groesser als untere Blockbereich && obere Blockbereich kleiner als untere Cursorbereich
        if(bMat[7] < c[7]+c[5]/2 && bMat[7]+bMat[5]/2 > c[7])
        {
            //X-Achsenvergleich
            if(bMat[3]-bMat[0]/2 < c[3]+c[0]/2 && bMat[3]+bMat[0]/2 > c[3])
            {
                block.setIterator(0);
                block.setDestroyed(true);
                return true;
            }
        }
        return false;

    }

    @Test
    public void checkCollision()
    {
        if(checkCollisionGame(general,cursorLeft))
        {
            fail();
        }
        if(checkCollisionGame(general,cursorRight))
        {
            assertTrue(true);
        }
    }
}
