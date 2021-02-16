package GC.GObject.Primitives;

import GC.GMath.Vector3f;
import GC.GObject.GraphicObject;
import GC.Reader.ShaderReader;
import Global.Camera;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import java.nio.FloatBuffer;

import static com.jogamp.opengl.GL.*;
/**
 * @Author: Patrick Pavlenko
 * @Version: 04.12.2019
 * Scoreanzeige im Spiel
 */


public class ScorePoints extends GraphicObject {

   // Die angewendeten Shader fuer die Punktanzeige
    private final String[] scorevShader = ShaderReader.readShaderFile("Shader/Vertex/vertexScore.glsl");
    private final String[] scorefShader = ShaderReader.readShaderFile("Shader/Fragment/fragScore.glsl");

    /**
     * Punkte des Benutzers als String
     */
    private String userPoints = "0";
    private Vector3f coordinate;
    /**
     * Abstände der einzelnen Zahlen
     */
    private final float blockDistance = 0.15f;
    /**
     * Inkremierungswert der Zahlen
     * (add = 1  bedeutet also es wird in der Sekunde 50 mal mit 1 addiert)
     */
    private int add = 1;

    /**
     * Texturkoordinaten von Zahl 0
     */
    private float[] tex0 = new float[]
    {
                0,0,
                0.1f,0,
                0.1f,1f,
                0,0,
                0.1f,1f,
                0,1f,
    };

    /**
     * Texturkoordinaten von Zahl 1
     */
    private float[] tex1 = new float[]
    {
                0.1f,0,
                0.2f,0,
                0.2f,1f,
                0.1f,0,
                0.2f,1f,
                0.1f,1f,
    };
    /**
     * Texturkoordinaten von Zahl 2
     */
    private float[] tex2 = new float[]
    {
                0.2f,0,
                0.3f,0,
                0.3f,1f,
                0.2f,0,
                0.3f,1f,
                0.2f,1f,
    };
    /**
     * Texturkoordinaten von Zahl 3
     */
    private float[] tex3 = new float[]
    {
                0.3f,0,
                0.4f,0,
                0.4f,1f,
                0.3f,0,
                0.4f,1f,
                0.3f,1f,
    };
    /**
     * Texturkoordinaten von Zahl 4
     */
    private float[] tex4 = new float[]
    {
                0.4f,0,
                0.5f,0,
                0.5f,1f,
                0.4f,0,
                0.5f,1f,
                0.4f,1f,
    };
    /**
     * Texturkoordinaten von Zahl 5
     */
    private float[] tex5 = new float[]
    {
                0.5f,0,
                0.6f,0,
                0.6f,1f,
                0.5f,0,
                0.6f,1f,
                0.5f,1f,
    };
    /**
     * Texturkoordinaten von Zahl 6
     */
    private float[] tex6 = new float[]
    {
                0.6f,0,
                0.7f,0,
                0.7f,1f,
                0.6f,0,
                0.7f,1f,
                0.6f,1f,
    };
    /**
     * Texturkoordinaten von Zahl 7
     */
    private float[] tex7 = new float[]
    {
                0.7f,0,
                0.8f,0,
                0.8f,1f,
                0.7f,0,
                0.8f,1f,
                0.7f,1f,
    };
    /**
     * Texturkoordinaten von Zahl 8
     */
    private float[] tex8 = new float[]
    {
                0.8f,0,
                0.9f,0,
                0.9f,1f,
                0.8f,0,
                0.9f,1f,
                0.8f,1f,
    };

    /**
     * Texturkoordinaten von Zahl 9
     */
    private float[] tex9 = new float[]
    {
                0.9f,0,
                1f,0,
                1f,1f,
                0.9f,0,
                1f,1f,
                0.9f,1f,
    };

    /**
     * Punkte des Spielers werden mit der Parametervariable addiert
     * @param add der zu addierende Wert
     */
    public void addPoints(int add)

    {
        userPoints = Integer.toString(Integer.parseInt(userPoints)+add);
    }

    private FloatBuffer tex0buff = Buffers.newDirectFloatBuffer(tex0);
    private FloatBuffer tex1buff = Buffers.newDirectFloatBuffer(tex1);
    private FloatBuffer tex2buff = Buffers.newDirectFloatBuffer(tex2);
    private FloatBuffer tex3buff = Buffers.newDirectFloatBuffer(tex3);
    private FloatBuffer tex4buff = Buffers.newDirectFloatBuffer(tex4);
    private FloatBuffer tex5buff = Buffers.newDirectFloatBuffer(tex5);
    private FloatBuffer tex6buff = Buffers.newDirectFloatBuffer(tex6);
    private FloatBuffer tex7buff = Buffers.newDirectFloatBuffer(tex7);
    private FloatBuffer tex8buff = Buffers.newDirectFloatBuffer(tex8);
    private FloatBuffer tex9buff = Buffers.newDirectFloatBuffer(tex9);
    private FloatBuffer[] texbuffContainer = new FloatBuffer[10];

    /**
     * Koordinaten gesetzt,zeichnungstyp,Shaderprogram erstellt, Buffer geladen und texel Buffer in Array verpackt
     * @param x
     * @param y
     * @param z
     */
    public ScorePoints(float x,float y,float z)
    {
        super(x,y,z);
        drawType = GL_TRIANGLES;
        coordinate = new Vector3f(x,y,z);
        createProgram(scorevShader,scorefShader);
        vertex = new float[]
                {
                        0,0,0,
                        0.1f,0,0,
                        0.1f,0.1f,0,
                        0,0,0,
                        0.1f,0.1f,0,
                        0,0.1f,0,
                };
        generateBuffers();
        loadVBOBuffer(0,vertex,3);
        loadVBOBuffer(1,tex0,2);
        loadVBOBuffer(2,tex0,2);
        pauseVAO();
        loadTexture("Textures/Bitmap.jpg");

        texbuffContainer[0] = tex0buff;
        texbuffContainer[1] = tex1buff;
        texbuffContainer[2] = tex2buff;
        texbuffContainer[3] = tex3buff;
        texbuffContainer[4] = tex4buff;
        texbuffContainer[5] = tex5buff;
        texbuffContainer[6] = tex6buff;
        texbuffContainer[7] = tex7buff;
        texbuffContainer[8] = tex8buff;
        texbuffContainer[9] = tex9buff;
    }


    /**
     * Jeder einzelner Char des Punktestrings wird abgefragt,um welchen numerischen Wert es sich handelt
     * der numerische Wert wird anschließend benutzt um den richtigen Array Indeze zu benutzen,
     * um die richtigen Texturkoordinaten zu benutzen
     * Dabei wird pro Nummer,um Abstand zu erzeugen es weiter nach rechts translatiert
     * @param cam die Kamera des GLEventlisteners
     */
    public void drawNumbers(Camera cam)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glDisable(GL.GL_DEPTH_BUFFER_BIT);
        gl.glUseProgram(program.getProgram_ID());
        loadVAO();

        //Iterieren  von der Laenge des Punktesstrings ( So viele Elemente werden dementsprechend gezeichnet)
        for(int x=0; x<=userPoints.length()-1; x++)
        {
            if(x == 0) modelMatrix.translate(3.45f,3f,5);
            createMvMat(cam.getViewMatrix());
            modelViewMatrix.loadUniform(program.getProgram_ID(),"mvMat");
            cam.getProjMatrix().loadUniform(program.getProgram_ID(),"projMat");
            //Uberpruefen, welche Nummer die Zahl im bestimmten Stringindex (x)
            // Die jeweilige Zahl wird dann ihren bestimmten Texel Buffer zugewiesen
            //Ausnahmsweise dynamicdraw zum ersetzen der buffervalues
            gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
            gl.glBufferData(GL_ARRAY_BUFFER, texbuffContainer[Character.getNumericValue(userPoints.charAt(x))].limit()*4,
                    texbuffContainer[Character.getNumericValue(userPoints.charAt(x))], GL_DYNAMIC_DRAW);
            loadVBOBuffer(2,tex0,2);
            modelViewMatrix.sendUniformMat4f(modelViewMatrix);
            cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
            activateTexture();
            gl.glDrawArrays(drawType,0,6);
            // Modelviewmatrix leicht translatiert um Abstaende einzelner Bloecke zu konstruieren ( Damit nicht alle ubereinander angehaeuft sind)
            modelMatrix.translate(coordinate);
            coordinate.setVertex(coordinate.getVector()[0]+blockDistance,coordinate.getVector()[1],coordinate.getVector()[2]);
        }
        //Translatierungsvektor zurueckgesetzt ,damit Modelviewmatrix wieder alte Koordinaten kriegt
        coordinate.setVertex(coordinate.getVector()[0]-blockDistance*userPoints.length(),coordinate.getVector()[1],coordinate.getVector()[2]);
        pauseVAO();
        gl.glEnable(GL.GL_DEPTH_BUFFER_BIT);
        // Pro Zeichnung werden die Punkte +1 erhöht
        addPoints(this.add);
    }
    public void setAdd(int add)
    {
        this.add= add;
    }

    /**
     * Zuruecksetzen der Punkte nach einem Neustart bzw GameOver
     */
    public void resetPoints(){userPoints = "0";}
}

