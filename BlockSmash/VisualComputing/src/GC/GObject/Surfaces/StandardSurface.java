package GC.GObject.Surfaces;

import GC.GObject.GraphicObject;
import GC.Reader.ShaderReader;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.GL2GL3.GL_LINE;
import static com.jogamp.opengl.GL3ES3.GL_PATCHES;
import static com.jogamp.opengl.GL3ES3.GL_PATCH_VERTICES;
/**
 * @author Patrick Pavlenko
 * @version 06.11.2019
 * Tessellierte Standardflaeche (Flach)
 * Shader sind vorgegeben
 */
public class StandardSurface extends GraphicObject {


    // benoetigte Shader
    String[] vShader = ShaderReader.readShaderFile("Shader/Vertex/vertexSurface.glsl");
    String[] fShader = ShaderReader.readShaderFile("Shader/Fragment/fragSurface.glsl");
    String[] controlShader = ShaderReader.readShaderFile("Shader/Tes_Control/tesControlSurface.glsl");
    String[] evalShader = ShaderReader.readShaderFile("Shader/Tes_Evaluator/tesEvaluatorSurface.glsl");

    private float width; // Breite von Flaeche
    private float height; // Laenge von Flaeche



    /**
     *
     * @param x Modelmatrix X Koordinate
     * @param y Modelmatrix Y Koordinate
     * @param z Modelmatrix Z Koordinate
     * @param width x-laenge
     * @param height Z-Laenge
     */
    public StandardSurface(float x,float y,float z,float width,float height,float[] mAmbient,float[] mDiffuse,float[] mSpecular,float mShininess)
    {
        super(x,y,z,mAmbient,mDiffuse,mSpecular,mShininess);
        this.height = height;
        this.width = width;
        modelMatrix.scale(width,0,height);
        drawType = GL_PATCHES;
        createProgram(vShader,fShader,controlShader,evalShader);
    }

    /**
     * Flaeche wird fzeichnet
     * ohne Kontrollpunkte ( 1 Kontrollpunkt angegeben damit control Shader sich 1x ausfuehrt )
     */
    @Override
    public void drawObject()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glDisable(GL_CULL_FACE);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glPatchParameteri(GL_PATCH_VERTICES,1);
        gl.glDrawArrays(GL_PATCHES,0,1);
        gl.glEnable(GL_CULL_FACE);
    }

    public float getWidth() { return width; }
}
