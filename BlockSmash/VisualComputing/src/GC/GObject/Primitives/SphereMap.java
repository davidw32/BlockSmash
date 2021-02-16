package GC.GObject.Primitives;

import GC.GMath.Vector2f;
import GC.GObject.GraphicObject;
import GC.Reader.ShaderReader;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import static com.jogamp.opengl.GL.*;
/**
 * @author Patrick Pavlenko
 * @version 13.11.2019
 * Sphere bzw Cubemap die als Hintergrund dient
 */
public class SphereMap extends GraphicObject {


  // Die benotigen Shader
    private final String[] spherevShader = ShaderReader.readShaderFile("Shader/Vertex/vertexSphereMap.glsl");
    private final String[] spherefShader = ShaderReader.readShaderFile("Shader/Fragment/fragSphereMap.glsl");

    private Vector2f translateTex;

    public SphereMap(float x,float y,float z)
    {
        super(x,y,z,"Objects/skysphere.obj");
        translateTex = new Vector2f(0,0);
        drawType = GL_TRIANGLES;
        this.createProgram(spherevShader,spherefShader);
    }

    /**
     *  Cullface aktivierung fuer rendern des inneren Bereich der Sphere
     */
    @Override
    public void drawObject() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glEnable(GL_CULL_FACE);
        gl.glFrontFace(GL_CCW);
        gl.glDrawArrays(drawType,0,numVertex);
        gl.glBindVertexArray(0);
    }


    public Vector2f getTranslateTex() { return translateTex; }
}
