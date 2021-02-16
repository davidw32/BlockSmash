package GC.GObject.Subsets.Light;

import GC.GMath.Matrix4f;
import GC.GMath.Vector3f;
import GC.GMath.Vector4f;
/**
 * @author Patrick Pavlenko
 * @version 23.11.2019
 * Enthaelt alle Lichter einer Szene
 * Lichter werden hier eingestellt
 */
public class Light
{
    private Vector4f globalAmbient;
    private Vector4f lightAmbient;
    private Vector4f lightDiffuse;
    private Vector4f lightSpecular;
    private Vector3f lightPos;
    private Vector3f startStatePos;

    private String glslID;


    public Light(String ID,float[] gambient,float[] lambient,float[] ldiffuse,float[] lspecular,float posX,float posY,float posZ)
    {
        globalAmbient = new Vector4f(gambient[0],gambient[1],gambient[2],gambient[3]);
        lightAmbient = new Vector4f(lambient[0],lambient[1],lambient[2],lambient[3]);
        lightDiffuse = new Vector4f(ldiffuse[0],ldiffuse[1],ldiffuse[2],ldiffuse[3]);
        lightSpecular = new Vector4f(lspecular[0],lspecular[1],lspecular[2],lspecular[3]);
        lightPos = new Vector3f(posX,posY,posZ);
        startStatePos = new Vector3f(posX,posY,posZ);
        glslID = ID;

    }

    /**
     * Laed alle Lichter in die Shader
     * @param programID benoetige Shader Programm,wo das Licht hinein geladen wird
     * @param glslID Der Keywert in der Licht Hashmap in Visuals des jeweiligen Objektes und gleichzeitig
     *               der Variablennamen in GLSL
     */
    public void loadLight(int programID, Matrix4f viewMat)
    {
        lightPos.setVertex(startStatePos.getVector()[0],startStatePos.getVector()[1],startStatePos.getVector()[2]);
        lightPos.mult(viewMat);
        globalAmbient.loadUniform(programID,"globalAmbient");
        lightAmbient.loadUniform(programID,glslID+".ambient");
        lightDiffuse.loadUniform(programID,glslID+".diff");
        lightSpecular.loadUniform(programID,glslID+".spec");
        lightPos.loadUniform(programID,glslID+".pos");
    }

    /**
     * Aktiviert alle Lichter im Shader
     */
    public void activateLight()
    {
        globalAmbient.sendUniformVec4f();
        lightAmbient.sendUniformVec4f();
        lightDiffuse.sendUniformVec4f();
        lightSpecular.sendUniformVec4f();
        lightPos.sendUniformVec3f();
    }

}
