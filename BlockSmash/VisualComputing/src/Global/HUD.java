package Global;

import GC.GObject.Primitives.Hudbomb;
import GC.GObject.Primitives.Hudheart;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;


/**
 * @Author: Dennis Liebelt
 * @Version: 0.3
 *  Die Szene für Lebensanzeige + Bonusaktivierungen
 *
 */
public class HUD
{

    float i = 0;
    private float height; // Hohe GL Programm
    private float width; // Breite GL Programm
    private Camera cam;

    private boolean bombSpawned = true;
    private boolean uhrSpawned = true;
    private boolean starSpawned = true;
    private Hudheart heart1;
    private Hudheart heart2;
    private Hudheart heart3;
    private Hudbomb star;
    private Hudbomb bomb;
    private Hudbomb uhr;
    float x;

    /** Zaehler der die Laenge der Sternenfaehigkeit bestimmt
    * erreicht den Wert 300 (5 Sekunden) und hoert dann auf zu inkremieren */
    private int starTimer = 0;
    /** Zaehler der die Laenge der Uhrfaehigkeit bestimmt
     * erreicht den Wert 300 (5 Sekunden) und hoert dann auf zu inkremieren */
    private int uhrTimer = 0;

    public HUD(Camera camera,float width,float height)
    {
        // Alles was hier drin passiert ist Standard,und muss in einen GLEventlistener eingefuegt werden (Strukturgruende)
        // Die Kamera jedoch ist essenziel!!
        // Diese beinhaltet die Projektions und Viewmatrix die wir fuer die Shader brauchen
        this.width = width;
        this.height = height;
        this.cam = camera;
        this.x= -0.1f;
    }

    /**
     * Zuruecksetzen der HUDwerte bei Spielneustart
     */
    public void resetHUD()
    {
        bombSpawned = true;
        uhrSpawned = true;
        starSpawned = true;
        starTimer = 0;
        uhrTimer = 0;
    }

    public void spawnBomb()
    {
        bomb = new Hudbomb(this.x, 2.7f, 9.1f, "Objects/bombe.obj");
        bomb.getModelMatrix().scale(3f,3f,3f);
        bomb.generateBuffers();
        bomb.loadVBOBuffer(0, bomb.getVertex(), 3);
        bomb.loadVBOBuffer(1, bomb.getTexel(), 2);
        bomb.loadVBOBuffer(2, bomb.getNormals(), 3);
        bomb.loadTexture("Textures/bombTexture.jpg");
        bomb.pauseVAO();
    }

    public void spawnUhr()
    {
        uhr = new Hudbomb(this.x, 2.78f, 9.1f, "Objects/sanduhr.obj");
        uhr.getModelMatrix().scale(3f,3f,3f);
        uhr.generateBuffers();
        uhr.loadVBOBuffer(0, uhr.getVertex(), 3);
        uhr.loadVBOBuffer(1, uhr.getTexel(), 2);
        uhr.loadVBOBuffer(2, uhr.getNormals(), 3);
        uhr.loadTexture("Textures/uhr_Texture.jpg");
        uhr.pauseVAO();
    }

    public void spawnStar()
    {
        star = new Hudbomb(this.x, 2.62f, 9.1f, "Objects/Star.obj");
        //heart1 = new HudElement(0f,-.50f,9f,"Objects/Love.obj");
        star.getModelMatrix().scale(3.8f,3.8f,3.8f);
        star.generateBuffers();
        star.loadVBOBuffer(0, star.getVertex(), 3);
        star.loadVBOBuffer(1, star.getTexel(), 2);
        star.loadVBOBuffer(2, star.getNormals(), 3);
        star.loadTexture("Textures/Star_Texture.jpg");
        star.getModelMatrix().scale(2f,2f,2f);

        star.pauseVAO();
    }

    public void drawStar() {
        if(!starSpawned) return;
        GL4 gl = (GL4) GLContext.getCurrentGL();
        star.loadVAO();
        star.createMvMat(cam.getViewMatrix());
        star.getModelViewMatrix().loadUniform(star.getProgram().getProgram_ID(), "mvMat");
        cam.getProjMatrix().loadUniform(star.getProgram().getProgram_ID(), "projMat");
        star.getModelViewMatrix().sendUniformMat4f(star.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        star.activateTexture();
        star.getModelMatrix().rotateY(-.0065f);
        star.drawObject();
        star.pauseVAO();
    }

    public void drawUhr() {
        if(!uhrSpawned) return;
        GL4 gl = (GL4) GLContext.getCurrentGL();
        uhr.loadVAO();
        uhr.createMvMat(cam.getViewMatrix());
        uhr.getModelViewMatrix().loadUniform(uhr.getProgram().getProgram_ID(), "mvMat");
        cam.getProjMatrix().loadUniform(uhr.getProgram().getProgram_ID(), "projMat");
        uhr.getModelViewMatrix().sendUniformMat4f(uhr.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        uhr.activateTexture();
        uhr.getModelMatrix().rotateY(-.0065f);
        uhr.drawObject();
        uhr.pauseVAO();
    }

    public void drawBomb() {
        if(!bombSpawned) return;
        GL4 gl = (GL4) GLContext.getCurrentGL();
        bomb.loadVAO();
        bomb.createMvMat(cam.getViewMatrix());
        bomb.getModelViewMatrix().loadUniform(bomb.getProgram().getProgram_ID(), "mvMat");
        cam.getProjMatrix().loadUniform(bomb.getProgram().getProgram_ID(), "projMat");
        bomb.getModelViewMatrix().sendUniformMat4f(bomb.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        bomb.activateTexture();
        bomb.getModelMatrix().rotateY(-.005f);
        bomb.drawObject();
        bomb.pauseVAO();
    }

    public void spawnHeart() {


            heart1 = new Hudheart(-0.25f, 3f, 8.9f, "Objects/Love.obj");
            heart1.getModelMatrix().scale(0.04f,0.04f,0.03f);

            heart1.generateBuffers();
            heart1.loadVBOBuffer(0, heart1.getVertex(), 3);
            heart1.loadVBOBuffer(1, heart1.getTexel(), 2);
            heart1.loadVBOBuffer(2, heart1.getNormals(), 3);
            heart1.loadTexture("Textures/heart_Texture.jpg");
            heart1.pauseVAO();

            heart2 = new Hudheart(-0.15f, 3f, 8.9f, "Objects/Love.obj");
            heart2.getModelMatrix().scale(0.04f,0.04f,0.04f);
            heart2.generateBuffers();
            heart2.loadVBOBuffer(0, heart2.getVertex(), 3);
            heart2.loadVBOBuffer(1, heart2.getTexel(), 2);
            heart2.loadVBOBuffer(2, heart2.getNormals(), 3);
            heart2.loadTexture("Textures/heart_Texture.jpg");
            heart2.pauseVAO();

            heart3 = new Hudheart(-0.05f, 3f, 8.9f, "Objects/Love.obj");
            heart3.getModelMatrix().scale(0.04f,0.04f,0.04f);
            heart3.generateBuffers();
            heart3.loadVBOBuffer(0, heart3.getVertex(), 3);
            heart3.loadVBOBuffer(1, heart3.getTexel(), 2);
            heart3.loadVBOBuffer(2, heart3.getNormals(), 3);
            heart3.loadTexture("Textures/heart_Texture.jpg");
            heart3.pauseVAO();

    }
    public void drawHeart(int anz) {
        GL4 gl = (GL4) GLContext.getCurrentGL();

        gl.glDisable(GL_DEPTH_BUFFER_BIT);
        if(anz>0) {
            heart1.loadVAO();
            heart1.createMvMat(cam.getViewMatrix());
            heart1.getModelViewMatrix().loadUniform(heart1.getProgram().getProgram_ID(), "mvMat");
            cam.getProjMatrix().loadUniform(heart1.getProgram().getProgram_ID(), "projMat");
            heart1.getModelViewMatrix().sendUniformMat4f(heart1.getModelViewMatrix());
            cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
            heart1.activateTexture();
            heart1.getModelMatrix().rotateY(.015f);
            heart1.drawObject();
            heart1.pauseVAO();
        }
    if(anz>1) {
        heart2.loadVAO();
        heart2.createMvMat(cam.getViewMatrix());
        heart2.getModelViewMatrix().loadUniform(heart2.getProgram().getProgram_ID(), "mvMat");
        cam.getProjMatrix().loadUniform(heart2.getProgram().getProgram_ID(), "projMat");
        heart2.getModelViewMatrix().sendUniformMat4f(heart2.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        heart2.activateTexture();
        heart2.getModelMatrix().rotateY(.02f);
        heart2.drawObject();
        heart2.pauseVAO();
    }
    if (anz ==3) {
        heart3.loadVAO();
        heart3.createMvMat(cam.getViewMatrix());
        heart3.getModelViewMatrix().loadUniform(heart3.getProgram().getProgram_ID(), "mvMat");
        cam.getProjMatrix().loadUniform(heart3.getProgram().getProgram_ID(), "projMat");
        heart3.getModelViewMatrix().sendUniformMat4f(heart3.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        heart3.activateTexture();
        heart3.getModelMatrix().rotateY(.015f);
        heart3.drawObject();
        heart3.pauseVAO();
    }
        gl.glEnable(GL_DEPTH_BUFFER_BIT);

    }

    /**
     * Buffers der GraphicObjects werden geloescht
     */
    public void deleteAllHUDBuffers()
    {
        heart1.deleteBuffers();
        heart2.deleteBuffers();
        heart3.deleteBuffers();
        star.deleteBuffers();
        bomb.deleteBuffers();
        uhr.deleteBuffers();
    }

    public void starTimerIterate() { starTimer++; }
    public void uhrTimerIterate() { uhrTimer++; }


    public void setStarSpawned(boolean starSpawned) { this.starSpawned = starSpawned; }
    public void setUhrSpawned(boolean uhrSpawned) { this.uhrSpawned = uhrSpawned; }
    public void setBombSpawned(boolean bombSpawned) { this.bombSpawned = bombSpawned; }

    public boolean isBombSpawned() { return bombSpawned; }
    public boolean isStarSpawned() { return starSpawned; }
    public boolean isUhrSpawned() { return uhrSpawned; }

    public int getStarTimer() { return starTimer; }
    public int getUhrTimer() { return uhrTimer; }
}
