package GC.GObject.Primitives;

import GC.GObject.GraphicObject;

import static com.jogamp.opengl.GL.GL_TRIANGLES;

public class General extends GraphicObject {

    public General(float x,float y,float z)
    {
        super(x,y,z);
        drawType = GL_TRIANGLES;
    }

    public General(float x,float y,float z,String objFilePath)
    {
        super(x,y,z,objFilePath);
        drawType = GL_TRIANGLES;

    }

    // Konstruktor mit Belichtung + Shader + OBJ
    public General(float x,float y,float z,String objFilePath,float[] mAmbient,float[] mDiffuse,float[] mSpecular,float mShininess)
    {
        super(x,y,z,objFilePath,mAmbient,mDiffuse,mSpecular,mShininess);
        drawType = GL_TRIANGLES;
    }

    // Konstruktor mit Belichtung + Shader
    public General(float x,float y,float z,float[] mAmbient,float[] mDiffuse,float[] mSpecular,float mShininess)
    {
        super(x,y,z,mAmbient,mDiffuse,mSpecular,mShininess);
        drawType = GL_TRIANGLES;
    }
}