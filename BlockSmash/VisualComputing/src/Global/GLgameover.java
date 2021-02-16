package Global;
import GC.GMath.Vector3f;
import GC.GObject.Primitives.Hudbomb;
import GC.GObject.Visuals;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import java.util.ArrayList;
/**
 * @author Dennis Liebelt,Patrick Pavlenko
 * @version 20.12.2019
 */

public class GLgameover
{


    /** Hoehe des Fensters */
    private float height;
    /** Breite des Fensters */
    private float width;
    private Camera cam;


    public Hudbomb gameover;
    private Visuals backgroundObj;
    private ArrayList<Vector3f> paths = new ArrayList<>();
    private ArrayList<Vector3f> pathsEnd = new ArrayList<>();

    /**
     *
     * @param cam Kamera des Eventlisteners
     * @param width breite des Programmfenster
     * @param height hoehe des Programmfensters
     */
    public GLgameover(Camera cam,float width,float height)
    {
        // Alles was hier drin passiert ist Standard,und muss in einen GLEventlistener eingefuegt werden (Strukturgruende)
        // Die Kamera jedoch ist essenziel!!
        // Diese beinhaltet die Projektions und Viewmatrix die wir fuer die Shader brauchen
        this.height = width;
        this.width = height;
        this.cam = cam;
    }

    public void spawn()
    {
        backgroundObj = new Visuals(paths,pathsEnd);
        spawnGameOver();
    }

    public void draw()
    {
        drawGameOver();
    }

    /**
     *  Initialisiert das GameOver Objekt
     */
    private void spawnGameOver()
    {
        gameover = new Hudbomb(cam.getCamX(), cam.getCamY()-0.3f, cam.getCamZ()-2, "Objects/gameover.obj");
        gameover.getModelMatrix().scale(3f,3f,3f);
        gameover.generateBuffers();
        gameover.loadVBOBuffer(0, gameover.getVertex(), 3);
        gameover.loadVBOBuffer(1, gameover.getTexel(), 2);
        gameover.loadVBOBuffer(2, gameover.getNormals(), 3);
        gameover.loadTexture("Textures/GameOver_Texture.jpg");
        gameover.pauseVAO();
    }


    /**
     * Zeichnet das GameOver Objekt
     */
    private void drawGameOver() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gameover.loadVAO();
        gameover.createMvMat(cam.getViewMatrix());
        gameover.getModelViewMatrix().loadUniform(gameover.getProgram().getProgram_ID(), "mvMat");
        cam.getProjMatrix().loadUniform(gameover.getProgram().getProgram_ID(), "projMat");
        gameover.getModelViewMatrix().sendUniformMat4f(gameover.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        gameover.activateTexture();
        gameover.drawObject();
        gameover.pauseVAO();
    }

}
