package Global;
import GC.GMath.Vector3f;
import GC.GObject.Primitives.Hudbomb;
import GC.GObject.Visuals;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import java.util.ArrayList;
/**
 * @athor Dennis Liebelt,Patrick Pavlenko
 * @version 0.2
 *  Szene des Startbildschirms
 *
 */
public class GLstartscreen
{
    private Camera cam;
    private Hudbomb stargate;
    private Hudbomb  stargate2;
    private Hudbomb  block_smash;
    private Visuals backgroundObj;
    private ArrayList<Vector3f> paths = new ArrayList<>();
    private ArrayList<Vector3f> pathsEnd = new ArrayList<>();

    public GLstartscreen(Camera cam)
    {
        this.cam = cam;
    }


    /**
     * Buffer der GraphicObjects aus dieser Klasse werden geloescht
     */
    public void deleteBuffers()
    {
        stargate.deleteBuffers();
        stargate2.deleteBuffers();
        block_smash.deleteBuffers();
        backgroundObj.deleteAllVisBuffers();
    }

    /**
     *  Stargates,Titelobjekt,Cubemap werden initialisiert bzw GraphicObjects werden geladen
     */
    public void spawn()
    {
        backgroundObj = new Visuals(paths,pathsEnd);
        this.spawnStar();
        this.spawnStar2();
        this.spawnTitle();
    }

    /**
     * Stargates,Titelobjekt,Cubemap werden gezeichnet
     */
    public void draw()
    {
        backgroundObj.spawnBackground(cam);
        this.drawStar();
        this.drawStar2();
        this.drawTitle();
    }

    /**
     *  initialisiert Titelobjekt
     */

    private void spawnTitle()
    {
        block_smash = new Hudbomb(cam.getCamX(), cam.getCamY(), 0f, "Objects/Block_Smash_high.OBJ");
        block_smash.getModelMatrix().scale(5f,5f,5f);
        block_smash.generateBuffers();
        block_smash.loadVBOBuffer(0, block_smash.getVertex(), 3);
        block_smash.loadVBOBuffer(1, block_smash.getTexel(), 2);
        block_smash.loadVBOBuffer(2, block_smash.getNormals(), 3);
        block_smash.loadTexture("Textures/BlockSmash_Texture.jpg");
        block_smash.pauseVAO();
    }

    /**
     * zeichnet Titelobjekt
     */
    private void drawTitle() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        block_smash.loadVAO();
        block_smash.createMvMat(cam.getViewMatrix());
        block_smash.getModelViewMatrix().loadUniform(block_smash.getProgram().getProgram_ID(), "mvMat");
        cam.getProjMatrix().loadUniform(block_smash.getProgram().getProgram_ID(), "projMat");
        block_smash.getModelViewMatrix().sendUniformMat4f(block_smash.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        block_smash.activateTexture();
        block_smash.drawObject();
        block_smash.pauseVAO();
    }

    /**
     * Stargate wird initialisiert
     */
    private void spawnStar()
    {
        stargate = new Hudbomb (cam.getCamX()-0.10f, cam.getCamY()-0.32f, 9f, "Objects/stargate_high.OBJ");
        stargate.getModelMatrix().scale(0.28f,0.16f,0.30f);
        stargate.generateBuffers();
        stargate.loadVBOBuffer(0, stargate.getVertex(), 3);
        stargate.loadVBOBuffer(1, stargate.getTexel(), 2);
        stargate.loadVBOBuffer(2, stargate.getNormals(), 3);
        stargate.loadTexture("Textures/stargate_high_Texture.jpg");
        stargate.pauseVAO();
    }

    /**
     * Stargate wird gezeichnet
     */
    private void drawStar() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        stargate.loadVAO();
        stargate.createMvMat(cam.getViewMatrix());
        stargate.getModelViewMatrix().loadUniform(stargate.getProgram().getProgram_ID(), "mvMat");
        cam.getProjMatrix().loadUniform(stargate.getProgram().getProgram_ID(), "projMat");
        stargate.getModelViewMatrix().sendUniformMat4f(stargate.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        stargate.activateTexture();
        stargate.getModelMatrix().rotateZ(-.0045f);
        stargate.drawObject();
        stargate.pauseVAO();
    }

    private void spawnStar2()
    {
        stargate2 = new Hudbomb (cam.getCamX()-0.10f, cam.getCamY()-0.32f, 9f, "Objects/stargate_high.OBJ");
        stargate2.getModelMatrix().scale(0.20f,0.06f,0.20f);
        stargate2.generateBuffers();
        stargate2.loadVBOBuffer(0, stargate2.getVertex(), 3);
        stargate2.loadVBOBuffer(1, stargate2.getTexel(), 2);
        stargate2.loadVBOBuffer(2, stargate2.getNormals(), 3);
        stargate2.loadTexture("Textures/stargate_high_Texture.jpg");
        stargate2.pauseVAO();
    }

    private void drawStar2() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        stargate2.loadVAO();
        stargate2.createMvMat(cam.getViewMatrix());
        stargate2.getModelViewMatrix().loadUniform(stargate2.getProgram().getProgram_ID(), "mvMat");
        cam.getProjMatrix().loadUniform(stargate.getProgram().getProgram_ID(), "projMat");
        stargate2.getModelViewMatrix().sendUniformMat4f(stargate2.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        stargate2.activateTexture();
        stargate2.getModelMatrix().rotateX(.00465f);
        stargate2.drawObject();
        stargate2.pauseVAO();
    }



}
