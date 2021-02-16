package GC.GMath;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

/**
 * @author Patrick Pavlenko
 * @version 15.11.2019
 * Floatuniform
 */
public class Vector1f extends I_Maths
{


    /**
     * Floatwert, des "Uniforms".
     * Wird Nachher in GLSL weitergeleitet
     */
    private float[] vertex = new float[1];


    /**
     * Initialisierung des Floatwertes
     * @param x Zuweiser des Floatwertes
     */
    public Vector1f(float x)
    {
        vertex[0] = x;
    }

    /**
     * Senden eines float Wertes nach GLSL bzw die Pipeline
     */
    public void sendUniformVec1f()
    {
        setMatFloatBuffer(vertex);
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUniform1fv(uniformID,1,matBuffer);
    }

    public void setVertex(float x) {
        vertex[0] = x;
    }

    public float getVertex() {
        return vertex[0];
    }
}
