package GC.GObject.Primitives;

import GC.GObject.GraphicObject;
import GC.Reader.ShaderReader;
import Global.Camera;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
import static com.jogamp.opengl.GL.*;

/**
 * @author Patrick Pavlenko
 * @version 20.12.2019
 * Plan bzw Objekte welches als Spielhilfe dient im Tutorialbereich des Spiels
 */
public class Tutorial extends GraphicObject {

    private final String[] tutvShader = ShaderReader.readShaderFile("Shader/Vertex/vertexGeneralTextured.glsl");
    private final String[] tutfShader = ShaderReader.readShaderFile("Shader/Fragment/fragGeneralTextured.glsl");


    /**
     * texel,vertex,Shaderprogram erstellt
     * @param x x-Koordinate des Objektes
     * @param y y-Koordinate des Objektes
     * @param z z-Koordinate des Objektes
     */
    public Tutorial(float x,float y,float z)
    {
        super(x,y,z);
        texel = new float[]
                {
                     0f,0f,
                     1f,0f,
                     0f,1f,
                     1f,0f,
                     1f,1f,
                     0f,1f
                };
        vertex = new float[]
                {
                        0f,0f,0f,
                        1f,0f,0f,
                        0f,1f,0f,
                        1f,0f,0f,
                        1f,1f,0f,
                        0f,1f,0f
                };
        createProgram(tutvShader,tutfShader);
        drawType = GL_TRIANGLES;
    }

    /**
     * initialisiert das Objekt
     */
    public void spawnTutorial()
    {
        generateBuffers();
        float[] placeh = new float[]{0,0,0};
        loadVBOBuffer(0,vertex,3);
        loadVBOBuffer(1,texel,2);
        loadVBOBuffer(2,placeh,3);
        loadTexture("Textures/tutorial_description.jpg");
        pauseVAO();
    }

    /**
     * zeichnet das Objekt
     * @param cam die momentane Camera im GLMain GLEventlistener
     */
    public void drawTutorial(Camera cam)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUseProgram(program.getProgram_ID());
        loadVAO();
        createMvMat(cam.getViewMatrix());
        modelViewMatrix.loadUniform(program.getProgram_ID(),"mvMat");
        cam.getProjMatrix().loadUniform(program.getProgram_ID(),"projMat");
        modelViewMatrix.sendUniformMat4f(modelViewMatrix);
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        activateTexture();
        gl.glDrawArrays(drawType,0,6);
        pauseVAO();
    }
}
