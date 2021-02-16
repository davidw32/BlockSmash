package GC.GObject.Subsets.Material;

import GC.GMath.Vector1f;
import GC.GMath.Vector4f;
/**
 * @author Patrick Pavlenko
 * @version 10.11.2019
 */
public class Material {

    private Vector4f materialAmbient;
    private Vector4f materialDiffuse;
    private Vector4f materialSpecular;
    private Vector1f materialShininess;


    public Material(float[] ambient,float[] diffuse,float[] specular,float shininess)
    {
        materialAmbient = new Vector4f(ambient[0],ambient[1],ambient[2],ambient[3]);
        materialDiffuse = new Vector4f(diffuse[0],diffuse[1],diffuse[2],diffuse[3]);
        materialSpecular = new Vector4f(specular[0],specular[1],specular[2],specular[3]);
        materialShininess = new Vector1f(shininess);
    }

    /**
     * Laed alle Materials in die Shader
     * @param programID benoetige Shader Programm,wo die Materials hinein geladen werden
     */
    public void loadMaterial(int programID)
    {
        materialAmbient.loadUniform(programID,"material.ambient");
        materialDiffuse.loadUniform(programID,"material.diff");
        materialSpecular.loadUniform(programID,"material.spec");
        materialShininess.loadUniform(programID,"material.shine");
    }

    /**
     * Aktiviert alle Materials im Shader
     */
    public void activateMaterial()
    {
        materialAmbient.sendUniformVec4f();
        materialDiffuse.sendUniformVec4f();
        materialSpecular.sendUniformVec4f();
        materialShininess.sendUniformVec1f();
    }


}
