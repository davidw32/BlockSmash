package GC.GMath;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
/**
 * @author Patrick Pavlenko
 * @version 21.12.2019
 * Vektoruniform 2er Basis
 */
public class Vector2f extends I_Maths {


    /**
     * x,y Werte des Uniforms
     */
    private float[] vertex = new float[2];

    /**
     * Vergabe der Koordinaten an vertex Array
     * @param x x-Koordinaten
     * @param y y-Koordinate
     */
    public Vector2f(float x,float y)
    {
        vertex[0] = x;
        vertex[1] = y;
    }

    public Vector2f(){}

    public void setVertex(float x,float y)
    {
        vertex[0] = x;
        vertex[1] = y;
    }

    /**
     * Sendet den 2er Vektor als Uniform nach GLSL bzw. die Pipeline
     */
    public void sendUniformVec2f()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUniform2f(uniformID,vertex[0],vertex[1]);
    }

    public float[] getVertex() { return vertex; }
}
