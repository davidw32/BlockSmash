package BV;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Ein KeyListener, welcher im Farbkalibrierungs-JFrame der "VideoProcessing"-Klasse
 * bei dem Tastenbefehl "k" die automatische Kalibrierung stoppt und den Spieler beim Druecken
 * der "ENTER"-Taste zum Hauptmenue fortfahren laesst.
 *
 * @author David Waelsch
 * @version 10.12.2019
 */
public class StartKeyListener implements KeyListener {
    VideoProcessing vidProc;

    /**
     * Der Konstruktor uebergibt dem KeyListener das entsprechende "VideoProcessing"-Objekt.
     *
     * @param vidProc VideoProcessing-Objekt in dem der KeyListener aktiviert werden soll.
     */
    public StartKeyListener(VideoProcessing vidProc){
        this.vidProc = vidProc;
    }

    /**
     * Wird aufgerufen wenn eine Tastatur-Eingabe erfolgt ist.
     *
     * @param e Tastatur-Event
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Wird aufgerufen wenn eine Taste gedrueckt wird.
     *
     * @param e Tastatur-Event
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Wird aufgerufen wenn eine Taste losgelassen wird.
     * Setzt "continueStart" von dem "VideoProcessing"-Objekt auf true sobald die "k"-Taste losgelassen wird.
     * Setzt "sliderStop" von dem "VideoProcessing"-Objekt auf true sobald die "ENTER"-Taste losgelassen wird.
     * @param e Tastatur-Event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            vidProc.setContinueStart(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_K){
            vidProc.setSliderStop(true);
        }
    }
}
