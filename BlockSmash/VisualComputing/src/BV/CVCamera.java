package BV;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Klasse fuer den Videostream der Bildverarbeitungs-Klassen.
 *
 * @author David Waelsch
 * @version 05.11.2019
 */
public class CVCamera {

    private VideoCapture capture;

    /**
     * Konstruktor initalisiert den VideoStream und ueberprueft ob dieser geoeffnet werden kann, sonst wird eine Exception geworfen.
     */
    public CVCamera(){
        capture = new VideoCapture(0);
        // Ueberprueft ob der Videostream geoeffnet werden konnte
        if(!capture.isOpened())
            throw new CvException("The Camera could not be opened!");
    }

    /**
     * Gibt den Videostream zurueck.
     * @return Videostream
     */
    public VideoCapture getCapture(){
        return capture;
    }
}
