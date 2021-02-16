package GC.GObject.Primitives;

import GC.GObject.GraphicObject;
import GC.Reader.ShaderReader;

import static com.jogamp.opengl.GL.GL_TRIANGLES;

/**
 * @author Dennis Liebelt
 * @version 19.21.2019
 */
public class Hudheart extends GraphicObject {

    // Shader nur hier so einsetzen,wenn auch Shader zu 100% nicht geaendertt wird... dann ist das statische erlaubt
    private final String[] testvShader = ShaderReader.readShaderFile("Shader/Vertex/vertexHerz.glsl");
    private final String[] testfShader = ShaderReader.readShaderFile("Shader/Fragment/fragHerz.glsl");


    public Hudheart(float x,float y,float z,String path)
    {
        super(x,y,z,path);
        // Shader werden an Programm angeheftet und sind damit innerhalb des shuttles Programm attribut aufrufbar ( Siehe GraphicObject Zeile 39)
        // Program (Welches Shader hat) kriegt eine ReferenzID ( Ein integer) welcher unten bei gl.glUseProgram aufgerufen werden muss zum benutzen des Shaders
        this.createProgram(testvShader,testfShader);
        // Wichtig, wird in drawObject benutzt, damit es weiss welcher Primitive es nutzen soll
        drawType = GL_TRIANGLES;

    }
}
