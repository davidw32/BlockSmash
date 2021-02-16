package GC.GObject.Surfaces;

import GC.GObject.GraphicObject;
import GC.Reader.ShaderReader;
import Global.Camera;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.GL2GL3.GL_LINE;
import static com.jogamp.opengl.GL3ES3.GL_PATCHES;
import static com.jogamp.opengl.GL3ES3.GL_PATCH_VERTICES;
/**
 * @author Patrick Pavlenko
 * @version 18.12.2019
 * Bezierdreiecke im Startmenue
 */
public class BezierSurface extends GraphicObject {




    // Benoetigte Shader
    private final String[] beziervShader = ShaderReader.readShaderFile("Shader/Vertex/vertexBezierSurface.glsl");
    private final String[] bezierfShader = ShaderReader.readShaderFile("Shader/Fragment/fragBezierSurface.glsl");
    private final String[] beziercontrolShader = ShaderReader.readShaderFile("Shader/Tes_Control/tesControlBezierSurface.glsl");
    private final String[] bezierevalShader = ShaderReader.readShaderFile("Shader/Tes_Evaluator/tesEvaluatorBezierSurface.glsl");

    /**
     * Matrizen initlaisierungen,Programmerstellung,Zeichnungstyp definieren
     * @param x x-Koordinaten ModelMatrix
     * @param y y-Koordinaten ModelMatrix
     * @param z z-Koordinaten ModelMatrix
     */
    public BezierSurface(float x,float y,float z)
    {
        super(x,y,z);
        createProgram(beziervShader,bezierfShader,beziercontrolShader,bezierevalShader);
        drawType = GL_PATCHES;
    }

    // Initialisieren des Bezierobjekte ( Laden Buffern)
    public void initBezier()
    {
        generateBuffers();
        float[] arr = new float[]{0,0,0};
        loadVBOBuffer(0,arr,3);
        pauseVAO();
        modelMatrix.scale(2.1f,2.2f,1.5f);
    }


    /**
     * zeichnet 2 Bezierdreiecke
     * @param cam Kamera bzw ViewMatrix im jeweiligen Eventlistener
     */
    public void drawObject(Camera cam) {
        GL4 gl = (GL4) GLContext.getCurrentGL();

        gl.glUseProgram(program.getProgram_ID());
        loadVAO();
        createMvMat(cam.getViewMatrix());

        modelViewMatrix.loadUniform(program.getProgram_ID(),"mvMat");
        cam.getProjMatrix().loadUniform(program.getProgram_ID(),"projMat");

        modelViewMatrix.sendUniformMat4f(modelViewMatrix);
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());

        // 10 Kontrollpunkte
        gl.glPatchParameteri(GL_PATCH_VERTICES,10);
        // Linienzeichnunge keine fuell interpolierung
        gl.glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
        gl.glDrawArrays(GL_PATCHES,0,10);


        // Bezierdreieck nr 2

        //Translation des zweite objektes ( Abhaengig von koordinaten des ersten Bezierdreiecks)
        modelMatrix.translate(modelMatrix.getMatrix()[3]+2f,modelMatrix.getMatrix()[7],modelMatrix.getMatrix()[11]);
        createMvMat(cam.getViewMatrix());

        modelViewMatrix.loadUniform(program.getProgram_ID(),"mvMat");

        modelViewMatrix.sendUniformMat4f(modelViewMatrix);
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());


        gl.glDrawArrays(GL_PATCHES,0,10);

        pauseVAO();

        //Zuruecksetzen der Koordinaten,damit das erste wieder gezeichnet werden kann
        modelMatrix.translate(modelMatrix.getMatrix()[3]-2f,modelMatrix.getMatrix()[7],modelMatrix.getMatrix()[11]);
        gl.glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
    }
}
