package UnitTest;
import GC.GObject.GraphicObject;
import GC.GObject.Primitives.*;
import org.junit.*;
import static org.junit.Assert.*;
/**
 * @author: Patrick Pavlenko
 * @version: 11.12.2019
 * Transformationen der Objekte
 */
public class BU_001 {



    GraphicObject general;

    @Before
    public void setUp()
    {
        general = new General(1,2,3);
        assertNotNull(general.getModelMatrix());
    }

    /**
     * Testen der Translation
     */
    @Test
    public void translate()
    {
        general.getModelMatrix().translate(0,0,0);
        assertTrue(general.getModelMatrix().getMatrix()[3] == 0);
        assertTrue(general.getModelMatrix().getMatrix()[7] == 0);
        assertTrue(general.getModelMatrix().getMatrix()[11] == 0);
    }


    /**
     * Rotation nach X
     */
    @Test
    public void rotateX()
    {
        general.getModelMatrix().rotateX((float)Math.PI);
        if(general.getModelMatrix().getMatrix()[5] != (float)Math.cos((float)Math.PI)) fail();
        if(general.getModelMatrix().getMatrix()[6] != (float)-Math.sin((float)Math.PI)) fail();
        if(general.getModelMatrix().getMatrix()[9] != (float)Math.sin((float)Math.PI)) fail();
        if(general.getModelMatrix().getMatrix()[10] != (float)Math.cos((float)Math.PI)) fail();
    }

    /**
     * Rotation nach Y
     */
    @Test
    public void rotateY()
    {
        general.getModelMatrix().rotateY((float)Math.PI);
        if(general.getModelMatrix().getMatrix()[0] != (float)Math.cos((float)Math.PI)) fail();
        if(general.getModelMatrix().getMatrix()[2] != (float)Math.sin((float)Math.PI)) fail();
        if(general.getModelMatrix().getMatrix()[8] != (float)-Math.sin((float)Math.PI)) fail();
        if(general.getModelMatrix().getMatrix()[10] != (float)Math.cos((float)Math.PI)) fail();

    }

    /**
     * Rotation nach Z
     */
    @Test
    public void rotateZ()
    {
        general.getModelMatrix().rotateZ((float)Math.PI);
        if(general.getModelMatrix().getMatrix()[0] != (float)Math.cos((float)Math.PI))  fail();
        if(general.getModelMatrix().getMatrix()[1] != (float)-Math.sin((float)Math.PI))  fail();
        if(general.getModelMatrix().getMatrix()[4] != (float)Math.sin((float)Math.PI))  fail();
        if(general.getModelMatrix().getMatrix()[5] != (float)Math.cos((float)Math.PI))  fail();

    }

    /**
     * Skalierung wird getestet
     */
    @Test
    public void scale()
    {
        general.getModelMatrix().scale(0.5f,0.5f,0.5f);
        assertTrue(general.getModelMatrix().getMatrix()[0] == 0.5f);
        assertTrue(general.getModelMatrix().getMatrix()[5] == 0.5f);
        assertTrue(general.getModelMatrix().getMatrix()[10] == 0.5f);
    }


    /**
     * Ruecktranslatierung
     */
    @Test
    public void translateBack()
    {
        general.getModelMatrix().translate(1,2,3);
        assertTrue(general.getModelMatrix().getMatrix()[3] == 1);
        assertTrue(general.getModelMatrix().getMatrix()[7] == 2);
        assertTrue(general.getModelMatrix().getMatrix()[11] == 3);
    }


}
