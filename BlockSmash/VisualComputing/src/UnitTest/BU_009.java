package UnitTest;

import BV.VideoProcessing;
import BV.sizeComp;
import Global.User;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author David Waelsch
 * @version 15.12.2019
 * Testklasse BU_009 Korrekte Erkennung der Fingeranzahl
 */
public class BU_009 {
    VideoProcessing vidProc;
    VideoCapture cap;
    Mat image;
    Mat history;
    User user;
    ArrayList<MatOfPoint> contours;
    Scalar scalb;
    Scalar scalu;

    @Before
    public void setUp(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        user = new User(0,3);
        scalb = new Scalar(30,70, 50);
        scalu = new Scalar(50,255,255);
        history = new Mat();
        contours = new ArrayList<>();
        vidProc = new VideoProcessing(cap);
    }

    @Test
    public void testCountFingers0(){
        image = Imgcodecs.imread("../VisualComputing/src/UnitTest/TestResources/testCountFingers0.png");
        image.convertTo(image, CvType.CV_8UC3);
        Core.flip(image,image,1);
        Imgproc.cvtColor(image,image,Imgproc.COLOR_BGR2HSV);
        Imgproc.GaussianBlur(image, image, new Size(21, 21), 0, 0);
        Core.inRange(image,scalb,scalu,image);
        Imgproc.medianBlur(image,image,3);
        Imgproc.findContours(image,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(new sizeComp());

        assertEquals(0,vidProc.countFingers(contours.get(0),new User(0,3)));
    }

    @Test
    public void testCountFingers1(){
        image = Imgcodecs.imread("../VisualComputing/src/UnitTest/TestResources/testCountFingers1.png");
        image.convertTo(image, CvType.CV_8UC3);
        Core.flip(image,image,1);
        Imgproc.cvtColor(image,image,Imgproc.COLOR_BGR2HSV);
        Imgproc.GaussianBlur(image, image, new Size(21, 21), 0, 0);
        Core.inRange(image,scalb,scalu,image);
        Imgproc.medianBlur(image,image,3);
        Imgproc.findContours(image,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(new sizeComp());

        assertEquals(1,vidProc.countFingers(contours.get(0),new User(0,3)));
    }

    @Test
    public void testCountFingers2(){
        image = Imgcodecs.imread("../VisualComputing/src/UnitTest/TestResources/testCountFingers2.png");
        image.convertTo(image, CvType.CV_8UC3);
        Core.flip(image,image,1);
        Imgproc.cvtColor(image,image,Imgproc.COLOR_BGR2HSV);
        Imgproc.GaussianBlur(image, image, new Size(21, 21), 0, 0);
        Core.inRange(image,scalb,scalu,image);
        Imgproc.medianBlur(image,image,3);
        Imgproc.findContours(image,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(new sizeComp());

        assertEquals(2,vidProc.countFingers(contours.get(0),new User(0,3)));
    }

    @Test
    public void testCountFingers3(){
        image = Imgcodecs.imread("../VisualComputing/src/UnitTest/TestResources/testCountFingers3.png");
        image.convertTo(image, CvType.CV_8UC3);
        Core.flip(image,image,1);
        Imgproc.cvtColor(image,image,Imgproc.COLOR_BGR2HSV);
        Imgproc.GaussianBlur(image, image, new Size(21, 21), 0, 0);
        Core.inRange(image,scalb,scalu,image);
        Imgproc.medianBlur(image,image,3);
        Imgproc.findContours(image,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(new sizeComp());

        assertEquals(3,vidProc.countFingers(contours.get(0),new User(0,3)));
    }
}
