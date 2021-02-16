package BV;

import org.opencv.core.MatOfPoint;

import java.util.Comparator;

/**
 * Eine Vergleichsklasse, welche die Groeße von Konturen miteinander vergleichen kann, wodurch ein absteigendes Sortieren
 * von Konturen ermöglicht wird.
 *
 * @author David Waelsch
 * @version 08.11.2019
 */
public class sizeComp implements Comparator<MatOfPoint> {

    /**
     * Vergleicht die Groeße von zwei Konturen (OpenCV MatOfPoint) miteinander.
     *
     * @param a Erste Kontur (MatOfPoint)
     * @param b Zweite Kontur (MatOfPoint)
     * @return Wenn die erste Kontur groeßer ist wird -1 zurueckgegeben.
     * Wenn die erste Kontur kleiner ist wird 1 zurueckgegeben.
     * Wenn sie beide Konturen gleich groß sind wird 0 zurueckgegeben.
     */
    public int compare(MatOfPoint a, MatOfPoint b){
        if(a.size().height > b.size().height){
            return -1;
        }
        if(a.size().height == b.size().height){
            return 0;
        }
        return 1;
    }
}

