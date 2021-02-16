package GC.GObject.Primitives;

import GC.GObject.GraphicObject;
import GC.Reader.ShaderReader;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import static com.jogamp.opengl.GL.GL_POINTS;

/**
 * @author Patrick Pavlenko
 * @version 02.11.2019
 * mehrere/einzelne Primitive Points
 */
public class Point extends GraphicObject {



    private float pointSize;
    //zu Shader die fuer dieses Primitives benutzt werden
    private final String[] pointvShader = ShaderReader.readShaderFile("Shader/Vertex/vertexGeneral.glsl");
    private final String[] pointfShader = ShaderReader.readShaderFile("Shader/Fragment/fragGeneral.glsl");


    /**
     *
     * @param x X-Koordinate von Modelmatrix
     * @param y Y-Koordinate von Modelmatrix
     * @param z Z-Koordinate von Modelmatrix
     * @param pointSize Wie viele Pixel  gross der Punkt nachher seien soll
     */
    public Point(float x,float y,float z,float pointSize)
    {
        super(x,y,z);
        GL4 gl = (GL4) GLContext.getCurrentGL();
        drawType = GL_POINTS;
        this.pointSize = pointSize;
        createProgram(pointvShader,pointfShader);
    }

    /**
     * Objekt zeichnen mittels drawObject mit Unterschied,dass hier PointSize noch gesetzt wird
     */
    @Override
    public void drawObject() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glPointSize(pointSize);
        super.drawObject();
    }


}
