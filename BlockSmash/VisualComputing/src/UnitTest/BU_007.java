package UnitTest;

import BV.VideoProcessing;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;


/**
 * @author David Waelsch
 * @version 15.12.2019
 * Testklasse BU_007 Korrekte Kalibrierung der Farbschranken
 */
public class BU_007 {
    VideoProcessing vidProc;
    VideoCapture cap;
    Mat hsvImage;
    @Before
    public void setUp(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        hsvImage = Imgcodecs.imread("../VisualComputing/src/UnitTest/TestResources/testCalcBoundaries.png");
        vidProc = new VideoProcessing(cap);
        hsvImage.convertTo(hsvImage, CvType.CV_8UC3);
        Core.flip(hsvImage, hsvImage, 1);
        Imgproc.cvtColor(hsvImage, hsvImage, Imgproc.COLOR_BGR2HSV);
    }

    @Test
    public void testCalculateBoundaries(){
        Scalar[] boundaries = vidProc.calculateBoundaries(hsvImage);
        assertTrue(boundaries[0].val[0] > 40 && boundaries[0].val[0] < 60); // untere Grüne Hue-Grenze für Hue-Wert / 2
        assertTrue(boundaries[1].val[0] > 70 && boundaries[1].val[0] < 80); // obere Grüne Hue-Grenze für Hue-Wert / 2
        assertTrue(boundaries[0].val[1] > 70 && boundaries[0].val[1] < 110); // untere Saturation-Grenze zwischen 0 und 255
        assertTrue(boundaries[1].val[1] > 160 && boundaries[1].val[1] < 220); // obere Saturation-Grenze zwischen 0 und 255
        assertTrue(boundaries[0].val[2] > 20 && boundaries[0].val[2] < 60); // untere Value-Grenze zwischen 0 und 255
        assertTrue(boundaries[1].val[2] > 120 && boundaries[1].val[2] < 200); // obere Value-Grenze zwischen 0 und 255
    }
}
