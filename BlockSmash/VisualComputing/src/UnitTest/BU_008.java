package UnitTest;

import BV.VideoProcessing;
import BV.sizeComp;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author David Waelsch
 * @version 15.12.2019
 * Testklasse BU_008 Erkennung der Handbewegung
 */
public class BU_008 {
    VideoProcessing vidProc;
    VideoCapture cap;
    Mat frame1;
    Mat frame2;
    Mat history;
    ArrayList<MatOfPoint> contours;
    Scalar scalb;
    Scalar scalu;
    Point oldCenter;
    @Before
    public void setUp(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        scalb = new Scalar(30,70, 50);
        scalu = new Scalar(50,255,255);
        history = new Mat();
        contours = new ArrayList<>();
        vidProc = new VideoProcessing(cap);

        frame1 = Imgcodecs.imread("../VisualComputing/src/UnitTest/TestResources/testCalcMovementFrame1.png");
        frame1.convertTo(frame1, CvType.CV_8UC3);
        Imgproc.cvtColor(frame1,frame1,Imgproc.COLOR_BGR2HSV);
        Imgproc.GaussianBlur(frame1, frame1, new Size(21, 21), 0, 0);
        Core.inRange(frame1,scalb,scalu,frame1);
        Imgproc.medianBlur(frame1,frame1,3);
        Imgproc.findContours(frame1,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(new sizeComp());

        Moments contourMoment = Imgproc.moments(contours.get(0));
        oldCenter = new Point((int)(contourMoment.m10/contourMoment.m00),(int)(contourMoment.m01/contourMoment.m00));
        vidProc.setOldLeftCenter(oldCenter);
    }

    @Test
    public void testCalculateMovementToRight(){
        frame2 = Imgcodecs.imread("../VisualComputing/src/UnitTest/TestResources/testCalcMovementRight.png");
        frame2.convertTo(frame2, CvType.CV_8UC3);
        Imgproc.cvtColor(frame2,frame2,Imgproc.COLOR_BGR2HSV);
        Imgproc.GaussianBlur(frame2, frame2, new Size(21, 21), 0, 0);
        Core.inRange(frame2,scalb,scalu,frame2);
        Imgproc.medianBlur(frame2,frame2,3);
        Imgproc.findContours(frame2,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(new sizeComp());

        Moments contourMoment = Imgproc.moments(contours.get(0));
        vidProc.setLeftCenter(new Point((int)(contourMoment.m10/contourMoment.m00),(int)(contourMoment.m01/contourMoment.m00)));

        assertEquals("L : rechts",vidProc.calculateMovementLeft());
    }

    @Test
    public void testCalculateMovementToLeft(){
        frame2 = Imgcodecs.imread("../VisualComputing/src/UnitTest/TestResources/testCalcMovementLeft.png");
        frame2.convertTo(frame2, CvType.CV_8UC3);
        Imgproc.cvtColor(frame2,frame2,Imgproc.COLOR_BGR2HSV);
        Imgproc.GaussianBlur(frame2, frame2, new Size(21, 21), 0, 0);
        Core.inRange(frame2,scalb,scalu,frame2);
        Imgproc.medianBlur(frame2,frame2,3);
        Imgproc.findContours(frame2,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(new sizeComp());

        Moments contourMoment = Imgproc.moments(contours.get(0));
        vidProc.setLeftCenter(new Point((int)(contourMoment.m10/contourMoment.m00),(int)(contourMoment.m01/contourMoment.m00)));

        assertEquals("L : links",vidProc.calculateMovementLeft());
    }

    @Test
    public void testCalculateMovementToUp(){
        frame2 = Imgcodecs.imread("../VisualComputing/src/UnitTest/TestResources/testCalcMovementUp.png");
        frame2.convertTo(frame2, CvType.CV_8UC3);
        Imgproc.cvtColor(frame2,frame2,Imgproc.COLOR_BGR2HSV);
        Imgproc.GaussianBlur(frame2, frame2, new Size(21, 21), 0, 0);
        Core.inRange(frame2,scalb,scalu,frame2);
        Imgproc.medianBlur(frame2,frame2,3);
        Imgproc.findContours(frame2,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(new sizeComp());

        Moments contourMoment = Imgproc.moments(contours.get(0));
        vidProc.setLeftCenter(new Point((int)(contourMoment.m10/contourMoment.m00),(int)(contourMoment.m01/contourMoment.m00)));

        assertEquals("L : oben",vidProc.calculateMovementLeft());
    }

    @Test
    public void testCalculateMovementToDown(){
        frame2 = Imgcodecs.imread("../VisualComputing/src/UnitTest/TestResources/testCalcMovementDown.png");
        frame2.convertTo(frame2, CvType.CV_8UC3);
        Imgproc.cvtColor(frame2,frame2,Imgproc.COLOR_BGR2HSV);
        Imgproc.GaussianBlur(frame2, frame2, new Size(21, 21), 0, 0);
        Core.inRange(frame2,scalb,scalu,frame2);
        Imgproc.medianBlur(frame2,frame2,3);
        Imgproc.findContours(frame2,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(new sizeComp());

        Moments contourMoment = Imgproc.moments(contours.get(0));
        vidProc.setLeftCenter(new Point((int)(contourMoment.m10/contourMoment.m00),(int)(contourMoment.m01/contourMoment.m00)));

        assertEquals("L : unten",vidProc.calculateMovementLeft());
    }
}
