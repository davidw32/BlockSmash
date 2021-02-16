package GC;

import BV.VideoProcessing;
import GC.GMath.Vector3f;
import GC.GObject.Primitives.ScorePoints;
import Global.Camera;
import Global.GLgameover;
import Global.Game;
import Global.HUD;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import Global.JWindow;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_CULL_FACE;
/**
 * @author Patrick Pavlenko & Dennis Liebelt
 * @version 19.10.2019
 *  canvas ist das Fensterobjekt fuer GL
 *  cam ist die "Kamera" (View + Projektionsmatrix)
 */
public class GLGame implements GLEventListener {


    private VideoProcessing vidProc;
    private float height;
    private float width;
    private Camera cam;
    private Game game;
    private GLMain main;
    private ScorePoints points;
    private HUD hud;
    private GLCanvas starterCanvas; // Die Canvas aus der Starterklasse
    private GLMain startscreen;
    private GLgameover gLgameover;
    Vector3f translator = new Vector3f(0,0f,0.008f);
    private float moveIterator = 0.003f;
    private Vector3f endPosMid = new Vector3f(0.0f,2.5f,9f);
    private Vector3f pathPosMid = new Vector3f(0f,3f,-100f);
    private JWindow window;

    /**
     *
     * @param width breite des Programmfensters
     * @param height hoehe des Programmfensters
     * @param vidProc
     * @param canvas die GLCanvas innerhalb Start wird wietergeben um Eventlistener wechsel zu ermoeglichen
     * @param startscreen das Startmenue um Schnittstellen Methoden auszufuehren (Methoden die zur abhaenigkeit beider Klassen sorgen, bzw wechseln der Eventlistener)
     * @param window das Jframe welchsels primaer genutzt wird
     */
    public GLGame(float width, float height, VideoProcessing vidProc, GLCanvas canvas,GLMain startscreen,JWindow window)
    {
        this.height = width;
        this.width = height;
        this.window = window;
        this.vidProc = vidProc;
        cam = new Camera(0.75f,3f,10,height,width);
        hud = new HUD(cam,width,height);
        gLgameover = new GLgameover(cam,width,height);
        starterCanvas = canvas;
        this.startscreen = startscreen;
    }

    /**
     * Buffer und Objekte initlaisiert und konfiguriert
     * @param glAutoDrawable
     */
    @Override
    public void init(GLAutoDrawable glAutoDrawable)
    {
        game = new Game(vidProc,this.hud);
        points = new ScorePoints(3.6f,3f,5);
        game.setScore(points);
        game.spawnhud();
        gLgameover.spawn();
        gLgameover.gameover.setStartPos(this.pathPosMid);
        startscreen.reset();
    }

    /**
     * Spielverlauf wird wird abgelaufen
     * entweder normaler spielverlauf wenn man mehr als 1 Leben hat
     * oder der Ablauf des Gameovers wenn keine Leben vorhanden sind
     * @param glAutoDrawable
     */
    @Override
    public void display(GLAutoDrawable glAutoDrawable)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glClear(GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL_CULL_FACE);
        gl.glViewport(0,0,(int)(width+width),(int)(height));
        // Solange Spieler genug Leben hat ( mindestens 1 Leben )
        // Finger ueberpruefen wie viele Finger angezeigt werden
        // HUD,bloecke,cubemap,cursor,punkte zeichen
        game.spawnBackgroundObjs(cam);

        if (game.getLife()>0) { //
            game.checkFinger();
            game.drawhud();
            game.spawnBlocks(cam);
            game.spawnCursor(cam);
            points.drawNumbers(cam);
        }
        // Ansonsten Gameoverscreen zeichen,punkte stoppen, Menü Button sichtbar machen
        else {
            points.setAdd(0);
            points.drawNumbers(cam);
            gLgameover.draw();
            window.gameoverButtonAppear();

            if (gLgameover.gameover.getIterator() < 0.975f) { // Game-Over über die Geradengleichung zeichnen lassen
                translator = game.iterate(gLgameover.gameover.getIterator(), gLgameover.gameover.getStartPos(), endPosMid);
                gLgameover.gameover.getModelMatrix().translate(translator);
                gLgameover.gameover.setIterator((float) (gLgameover.gameover.getIterator() + moveIterator));
            }
        }
    }

    /**
     * Werte zurueckgesetzt und es werden alle Werte zurueckgesetzt
     */
    public void goBackToMenue()
    {
        points.setAdd(1);
        hud.resetHUD();
        game.reset();
        window.gameoverButtonDisappear();

        // Wechseln Eventlistener
        starterCanvas.removeGLEventListener(this);
        starterCanvas.addGLEventListener(startscreen);
    }

    /**
     * Spielneustart nach GameOver
     */
    public void restartGame()
    {
        points.setAdd(1);
        points.resetPoints();
        game.reset();
        hud.resetHUD();
        window.gameoverButtonDisappear();

    }


    public void deleteBuffers()
    {
        game.deleteAllBuffer();
        hud.deleteAllHUDBuffers();
        points.deleteBuffers();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        deleteBuffers();
        System.exit(0);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int height, int width) {

    }

    public void setMain(GLMain main) { this.main = main; }
}
