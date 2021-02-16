import BV.CVCamera;
import BV.VideoProcessing;
import GC.GLGame;
import GC.GLMain;
import Global.JWindow;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import org.opencv.core.Core;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * @author Patrick Pavlenko & David Waelsch
 * @version 14.10.2019
 * Starten des Programmes CV / GL + Swing
 */
public class Starter
{


    /** GLEventlistener des Hauptmenues **/
    private GLMain startscreen;
    /** GLEventlistener des Spieles */
    private GLGame game;
    /** GLCanvas welche das jeweilige GLEventlistener benoetigt.
     *  beide Eventlistener werden dabei ausgetauscht bei wechseln der Szenen */
    private GLCanvas canvas = new GLCanvas(); // 1 canvas = mehrere GLEventListener
    private CVCamera cvCam = new CVCamera();
    private VideoProcessing vidProc;
    private JWindow app = new JWindow(1600,900,"Block Smash");

    public Starter()
    {
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        /**
         * Teile der Applikation:
         * 1. JFrame (Fenster)
         * 2.GLEventlistener + GLCanvass
         * 3.JPanel
         */
        vidProc = new VideoProcessing(cvCam.getCapture());
        canvas.setSize(1600,900);
        startscreen = new GLMain(1600,900,app,canvas);
        game = new GLGame(1600,900, vidProc,canvas,startscreen,app);
        startscreen.setGame(game);


        // GLCanvas Objekt erhält im inneren von GLMain Objekt das GLMain Objekt selbst
        canvas.addGLEventListener(startscreen);


        //GLcanvas + JPanel werden zum Jframe hinzugefuegt
        app.add(canvas);


        FPSAnimator fa = new FPSAnimator(canvas,50);
        fa.start();

        /**
         * Startbutton um zum Spiel zu gelangen
         * Eventlistener (GLMain zu GLGame) werden ausgetauscht
         */
            app.getButtonStart().addActionListener(e->{
            startscreen.transition = true;
        });

        /**
         * vom Startscreen zum Tutorial
         * Keine Eventlistener getauscht,nur translation nach links
         */
        app.getButtontest().addActionListener(e->{
            startscreen.tutorial = true;
            startscreen.transition = true;
        });

        app.getButtonquit().addActionListener(e -> {
            canvas.removeGLEventListener(startscreen);
            canvas.addGLEventListener(game);
            fa.stop();
            app.dispose();
            vidProc.dispose();
        });

        app.getButtonbackGame().addActionListener(e -> { //Wenn Button gedrückt wird: Punkte,hud resetten,buffer löschen und wechsel zum Startscreen
            game.goBackToMenue();
        });

        app.getButtonbackTutor().addActionListener(e -> {
            startscreen.transition = true;
            startscreen.back = true;
        });

        app.getButtonrestart().addActionListener(e -> {
            game.restartGame();
        });


        //Berechnet im Anfangsfenster die Farbgrenzen der Farberkennung und oeffnet Kamera-Fenster fuer den Spielverlauf
        vidProc.initCalculateBoundaries();
        vidProc.createSmallerFrame();


        //Start
        app.setVisible(true);

        game.setMain(startscreen);

        app.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

}

