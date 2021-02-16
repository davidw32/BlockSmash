package GC;
import GC.GMath.Vector3f;
import GC.GObject.Primitives.Tutorial;
import GC.GObject.Surfaces.BezierSurface;
import Global.Camera;
import Global.GLgameover;
import Global.GLstartscreen;
import Global.JWindow;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
/**
 * @author Patrick Pavlenko & Dennis Liebelt
 * @version 22.12.2019
 *  Startbildschirm, navigierbar zu Tutorialbereich oder Spiel
 *
 */
public class GLMain implements GLEventListener {


    private float height; // Hohe GL Programm
    private float width; // Breite GL Programm
    private Camera cam;
    private JWindow window;
    private GLstartscreen start;
    private BezierSurface triangle;
    // Wenn einer dieser drei Booleans true ist,dann kommt es zur Translation zu einem anderne Bereich im Screen
    /** Translationsverifikator um zum Startmenu zu gelangen */
    public boolean transition = false;
    /** Translationsverifikator um zum Tutorial zu gelangen */
    public boolean tutorial = false;
    /** Translationsverifikator um translation zu stoppen zu gelangen */
    public boolean back = false;
    //Anderer Eventlistener
    private GLGame game;
    private GLCanvas canvas;
    private Tutorial tutorialscreen;
    /** Endpuntk der Kamera bei Translation frontal bei Startknopf betaetigung */
    private Vector3f camEndPos = new Vector3f(0,0,5f); // von Start anfang zu Startende ( ins Spiel)
    /** Endpuntk der Kamera bei Translation zum Tutorialbereich */
    private Vector3f camTutorialPos = new Vector3f(29.2f,0,-10); // Position beim Tutorialfenster
    /** Startpunkt der Kamera um wieder zuruck zu gelangen */
    private Vector3f camStartPos = new Vector3f(-0.2f,0,-10); // Startposition beim Spielstart
    /**
     * Um GLContext nullpointer zu vermeiden wird diese Variable beim erstmaligen Starten der GLMain zu true gesetzt
     */
    private boolean firstStart = true;


    // Objekte per Konstruktor gepassed fuer spaetere Referenz bei Uebergaenge in andere Eventlistener

    /**
     *
     * @param width breite des Programmfensters
     * @param height hoehe des Programmfensters
     * @param window das primaere Jframe was genutzt wird
     * @param canvas die GLCanvas welche genutzt wird
     */
    public GLMain(float width, float height, JWindow window,GLCanvas canvas)
    {
        this.height = width;
        this.width = height;
        cam = new Camera(0f,0f,10,height,width);
        start = new GLstartscreen(cam);
        this.window = window;
        this.canvas = canvas;
    }

    /**
     * Alle Objekte werden initialisiert bzw Buffers geladen
     */
    @Override
    public void init(GLAutoDrawable glAutoDrawable)
    {

        if(!firstStart)
        {
            game.deleteBuffers();
        }
        else
        {
            firstStart = false;
        }
        window.buttonAppear();
        start.spawn();
        triangle = new BezierSurface(-2.2f,-1.15f,8);
        triangle.initBezier();
        tutorialscreen = new Tutorial(-30f,-0.5f,9);
        tutorialscreen.spawnTutorial();
        tutorialscreen.getModelMatrix().scale(1.4f,0.5f,1f);
    }

    /**
     * Zeichnen von Stargate Objekt,Titel,Tutorialscreen
     * und ueberpruefung ob ein eine Translation ausgefuehrt werden muss
     * @param glAutoDrawable
     */
    @Override
    public void display(GLAutoDrawable glAutoDrawable)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        gl.glClear(GL_COLOR_BUFFER_BIT);
        gl.glViewport(0,0,(int)(width+width),(int)(height)); // Vorerst einfach so lassen
        tutorialscreen.drawTutorial(cam);
        start.draw();
        triangle.drawObject(cam);
        checkTransition();
    }

    /**
     * zuruecksetzen der translationswerte , damit nachher wieder zur selben Stelle wieder translatiert wird bei Buttonclick ( Start Game bzw )
     */
    public void reset()
    {
        cam.getVecCoord().setVertex(camStartPos.getVector()[0],camStartPos.getVector()[1],camStartPos.getVector()[2]);
        cam.getViewMatrix().translate(camStartPos);
    }



    /**
     * Translation bei einem Buttonclick
     * Als Ubergangseffekt
     */
    private void checkTransition()
    {
        // Von Startscreen zu tutorial
        if(transition && tutorial)
        {
            window.buttonDisappear();
            // Ende Translation wenn true
            if(!cam.updateIterator(0.0005f,0.073f))
            {
                // Buttons von Tutorial erscheinen wieder
                transition = false;
                tutorial = false;
                window.tutorialButtonAppear();
                return;
            }
            // Ansonsten translatieren
            cam.moveCam(camTutorialPos);
            return;
        }
        // Von Tutorial nach Startmenue
        if(transition && back)
        {
            window.tutorialButtonDisappear();
            // Ende Translation wenn true
            if(!cam.updateIterator(0.0005f,0.073f))
            {
                // Buttons fuer Startmenue erscheinen
                back = false;
                transition = false;
                window.buttonAppear();
                return;
            }
            cam.moveCam(camStartPos);
            return;
        }
        // Von Startmenue in Spielszene
        if(transition)
        {
            window.buttonDisappear();
            // Ende Translation wenn true
            if(!cam.updateIterator(0.00008f,0.0135f))
            {
                // Buffer loeschen und eventlistener wechseln
                transition = false;
                deleteAllBuffers();
                canvas.addGLEventListener(game);
                canvas.removeGLEventListener(this);
            }
            cam.moveCam(camEndPos);
        }

    }


    /**
     * Entfernt alle Buffers des Eventlisteners
     */
    public void deleteAllBuffers() // Hier nachher alle loeschungen der Graphicobjects...
    {
        triangle.deleteBuffers();
        start.deleteBuffers();
    }



    @Override
    public void dispose(GLAutoDrawable glAutoDrawable)
    {
        deleteAllBuffers();
        System.exit(0);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) { }

    public void setGame(GLGame game) { this.game = game; }
}
