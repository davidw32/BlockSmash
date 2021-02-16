package GC.GMath;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
/**
 * @author Patrick Pavlenko
 * @version 15.11.2019
 * Vektoruniform 4er Basis
 */
public class Vector4f extends I_Maths {


    /**
     * x,y,z,w Werte des Uniforms
     */
    private float[] vertex = new float[4];

    /**
     * Zuweisung homogener Koordinaten
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param z z-Koordinate
     * @param w w-Koordinate
     */
    public Vector4f(float x,float y,float z,float w)
    {
        vertex[0] = x;
        vertex[1] = y;
        vertex[2] = z;
        vertex[3] = w;
    }


    /**
     * sendet ein 4er Vektor nach GLSL mittels der Werte in dem vertex Array
     */
    public void sendUniformVec4f()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUniform4f(uniformID,vertex[0],vertex[1],vertex[2],vertex[3]);
    }


}
