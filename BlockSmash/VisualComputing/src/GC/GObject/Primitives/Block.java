package GC.GObject.Primitives;

import GC.GMath.Vector1f;
import GC.GObject.GraphicObject;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL.GL_LEQUAL;

/**
 * @author Patrick Pavlenko
 * @version 10.11.2019
 * Bloecke die auf einen zu kommen
 */

public class Block extends GraphicObject {


    /**
     *
     * @param x x-Koordinate der Modelmatrix
     * @param y y-Koordinate der Modelmatrix
     * @param z z-Koordinate der Modelmatrix
     */
   public Block(float x, float y, float z)
    {
        super(x,y,z);
        drawType = GL_TRIANGLES;
    }

    /**
     *
     * @param x x-Koordinate der Modelmatrix
     * @param y y-Koordinate der Modelmatrix
     * @param z z-Koordinate der Modelmatrix
     * @param objFilePath Obj Pfad
     * @param mAmbient ambient Werte des Materials
     * @param mDiffuse Diffusewerte des Materials
     * @param mSpecular Specularwerte des Materials
     * @param mShininess Shininesswerte des Materials
     */
    public Block(float x,float y,float z,String objFilePath,float[] mAmbient,float[] mDiffuse,float[] mSpecular,float mShininess)
    {
        super(x,y,z,objFilePath,mAmbient,mDiffuse,mSpecular,mShininess);
        drawType = GL_TRIANGLES;
    }

}
