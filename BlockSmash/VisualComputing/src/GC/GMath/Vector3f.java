package GC.GMath;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
import java.util.Arrays;

/**
 * @author Patrick Pavlenko
 * @version: 15.11.2019
 * Vektoruniform 3er Basis
 */
public class Vector3f extends I_Maths {


    /**
     * x,y,z Werte des Uniforms
     */
    private float[] vertex = new float[3];

    public Vector3f() { Arrays.fill(vertex,0); }

    /**
     * Vektor indezes intialisiert durch Parameterwerte
     * @param x
     * @param y
     * @param z
     */
    public Vector3f(float x,float y,float z)
    {
        Arrays.fill(vertex,0);
        setVertex(x,y,z);
    }


    /**
     * Hier wird eine Multiplikation mit einer 4x4 Matrix ausgefuehrt
     * Dabei ist jedoch der vierte Wert des Vektores als Konstante 1 angegeben.
     * Der Vektor des Objektes bleibt jedoch im Format 3f
     * @param mat Matrix mit dder multipliziert wirdd
     */
    public void mult(Matrix4f mat)
    {
       vertex[0] = mat.getMatrix()[0]*vertex[0]+mat.getMatrix()[1]*vertex[1]+mat.getMatrix()[2]*vertex[2]+mat.getMatrix()[3]*1;
       vertex[1] = mat.getMatrix()[4]*vertex[0]+mat.getMatrix()[5]*vertex[1]+mat.getMatrix()[6]*vertex[2]+mat.getMatrix()[7]*1;
       vertex[2] = mat.getMatrix()[8]*vertex[0]+mat.getMatrix()[9]*vertex[1]+mat.getMatrix()[10]*vertex[2]+mat.getMatrix()[11]*1;
    }

    /**
     * Vektor mit Skalar multiplizert
     * @param multiplicator der Skalar mit dem multipliziert wird
     * @return Vektor welcher durch Multiplikation entsteht
     */
    public Vector3f multOut(float multiplicator)
    {
        Vector3f vec = new Vector3f();
        vec.setVertex(vertex[0]*multiplicator,vertex[1]*multiplicator,vertex[2]*multiplicator);
        return vec;
    }

    /**
     * Addition zweier Vektoren
     * @param vec der Vektor mit welchem addiert wird
     * @return Die Summe der beider Vektoren
     */
    public Vector3f addOut(Vector3f vec)
    {
        Vector3f vecOut = new Vector3f();
        vecOut.setVertex(vertex[0]+vec.getVector()[0],vertex[1]+vec.getVector()[1],vertex[2]+vec.getVector()[2]);
        return vecOut;
    }

    /**
     * Weitere Additionsmethode nur ohne return statement
     * @param vec der Vektor mit welchem addiert wird
     */
    public void add(Vector3f vec)
    {
        setVertex(vertex[0]+vec.getVector()[0],vertex[1]+vec.getVector()[1],vertex[2]+vec.getVector()[2]);
    }

    /**
     *  Sendet den 3er Vektor als Uniform nach GLSL bzw. in die Pipeline
     */
    public void sendUniformVec3f()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUniform3f(uniformID,vertex[0],vertex[1],vertex[2]);
    }

    public void setVertex(float x,float y,float z)
    {
        vertex[0] = x;
        vertex[1] = y;
        vertex[2] = z;
    }

    public float[] getVector() { return vertex; }
}
