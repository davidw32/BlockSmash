package BV;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Eine Klasse zum visuellen Testen der Bildverarbeitungsoperationen der "VideoProcessing"-Klasse, indem
 * die verarbeiteten Bilder in einem JFrame angezeigt werden.
 *
 * @author David Waelsch
 * @version 05.12.2019
 *
 */
public class VideoProcessingTest extends JFrame {

    private BufferedImagePanel imgPanel1;
    private BufferedImagePanel imgPanel2;
    private int frameCounter = 0;
    private int fnumber = 0;
    private int movementLeft = 0;
    private int movementRight = 0;
    private Point leftCenter = new Point(0,0);
    private Point rightCenter = new Point(0,0);
    private Point oldLeftCenter = new Point(0,0);
    private Point oldRightCenter = new Point(0,0);
    private Point normedCenterLeft = new Point(0.0,0.0);
    private Point normedCenterRight = new Point(0.0,0.0);

    /**
     * Entnommen aus Gotzes, Merijam / Lehn, Karsten: WS2019 CVD Visual Computing 1: Bildverarbeitung VideoProcessingStartCode.
     *
     * Create object and perform the processing by calling private member functions.
     */

    public VideoProcessingTest() {
        imgPanel1 = null;
        imgPanel2 = null;

        createFrame();
        processShowVideo();
    }

    /**
     * Entnommen aus Gotzes, Merijam / Lehn, Karsten: WS2019 CVD Visual Computing 1: Bildverarbeitung VideoProcessingStartCode.
     *
     * Create the JFrame to be displayed, displaying two images.
     */
    private void createFrame() {

        setTitle("Original and processed video stream");

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new FlowLayout());

        imgPanel1 = new BufferedImagePanel();
        contentPane.add(imgPanel1);
        imgPanel2 = new BufferedImagePanel();
        contentPane.add(imgPanel2);

        // place the frame at the center of the screen and show
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Normiert die Positionen der linken und rechten Handmittelpunkte auf einen Bereich von -1 bis 1 in x- und y-Richtung
     * fuer die vereinfachte Darstellung im OpenGL-Viewport.
     */
    private void normCenterCoordinates(){
        normedCenterLeft.x = (double)1/320*leftCenter.x - 1;
        normedCenterLeft.y = (double)-1/240*leftCenter.y - 1;

        normedCenterRight.x = (double)1/320*rightCenter.x - 1;
        normedCenterRight.y = (double)-1/240*rightCenter.y - 1;
    }

    /**
     * Berechnet den Mittelpunkt einer Kontur (hier: Handmittelpunkte) mit Hilfe von OpenCV Moments.
     *
     * @param contour Die Kontur einer Hand
     * @return Position des Konturmittelpunkts
     */
    private Point findContourCenter(MatOfPoint contour){
        Moments contourMoment = Imgproc.moments(contour);
        Point center = new Point();
        center.x = (int)(contourMoment.m10/contourMoment.m00);
        center.y = (int)(contourMoment.m01/contourMoment.m00);

        return center;
    }

    /**
     * Berechnet die Bewegung der linken Hand des Spieler, indem die derzeitige Position des Handmittelpunktes
     * mit der Handposition des vorherigen Frames verglichen wird.
     *
     * @return String mit der jeweiligen Richtung
     */
    private String calculateMovementLeft(){
        String direction = "";
        int xDiffMax = 80;
        int yDiffMax = 70;
        int xDiff = (int)(leftCenter.x - oldLeftCenter.x);
        int yDiff = (int)(leftCenter.y - oldLeftCenter.y);
        if(Math.abs(xDiff) < Math.abs(yDiff)){
            if(yDiff > yDiffMax){
                direction = "L : unten";
                movementLeft = 0;
            }
            else if(yDiff < 0-yDiffMax){
                direction = "L : oben";
                movementLeft = 0;
            }
        }
        else{
            if(xDiff > xDiffMax){
                direction = "L : rechts";
                movementLeft = 0;
            }
            else if(xDiff < 0 - xDiffMax){
                direction = "L : links";
                movementLeft = 0;
            }
        }
        oldLeftCenter.x = leftCenter.x;
        oldLeftCenter.y = leftCenter.y;
        return  direction;
    }

    /**
     * Berechnet die Bewegung der rechten Hand des Spieler, indem die derzeitige Position des Handmittelpunktes
     * mit der Handposition des vorherigen Frames verglichen wird.
     *
     * @return String mit der jeweiligen Richtung
     */
    private String calculateMovementRight(){
        String direction = "";
        int xDiffMax = 80;
        int yDiffMax = 70;
        int xDiff = (int)(rightCenter.x - oldRightCenter.x);
        int yDiff = (int)(rightCenter.y - oldRightCenter.y);
        if(Math.abs(xDiff) < Math.abs(yDiff)){
            if(yDiff > yDiffMax){
                direction = "R : unten";
                movementRight = 0;
            }
            else if(yDiff < 0-yDiffMax){
                direction = "R : oben";
                movementRight = 0;
            }
        }
        else{
            if(xDiff > xDiffMax){
                direction = "R : rechts";
                movementRight = 0;
            }
            else if(xDiff < 0 - xDiffMax){
                direction = "R : links";
                movementRight = 0;
            }
        }
        oldRightCenter.x = rightCenter.x;
        oldRightCenter.y = rightCenter.y;
        return direction;
    }

    /**
     * Ereknnt die Konturen der Haende des Spielers, indem das Bild mit Hilfe von Farbschranken, welche
     * durch Schieberegler festgesetzt werden, in ein Binärbild umgewandelt wird. Nach der absteigenden Sortierung der
     * Konturen nach ihren Groeßen, repraesentieren die beiden groeßten Konturen die Haende des Spielers.
     *
     * @param cap Videostream der Web-Kamera
     */
    public void handDetection(VideoCapture cap){
        Mat frame = new Mat();
        Mat hsvImage = new Mat();
        Mat procImage = new Mat();
        Mat colImage = new Mat();
        Mat history = new Mat();
        //Scalar scalb = new Scalar(0,20, 120);
        //Scalar scalu = new Scalar(20,150,255);
        Scalar scalb = new Scalar(30,70, 50);
        Scalar scalu = new Scalar(50,255,255);


        cap.read(frame);
        frame.convertTo(frame, CvType.CV_8UC3);
        frame.copyTo(hsvImage);

        while(cap.read(frame)){
            Core.flip(frame,frame,1);
            frame.copyTo(colImage);

            Imgproc.cvtColor(frame,hsvImage,Imgproc.COLOR_BGR2HSV);

            Imgproc.GaussianBlur(hsvImage, hsvImage, new Size(21, 21), 0, 0);

            Core.inRange(hsvImage,scalb,scalu,hsvImage);

            //Imgproc.GaussianBlur(hsvImage, procImage, new Size(3, 3), 0, 0);


            Imgproc.medianBlur(hsvImage,procImage,3);
            ArrayList<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(procImage,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);

            contours.sort(new sizeComp());


            if(contours.size()>1 && contours.get(0).size().height >= 120 && contours.get(1).size().height >= 120){
                leftCenter = findContourCenter(contours.get(0));
                countFingers(colImage, contours.get(0));
                rightCenter = findContourCenter(contours.get(1));
                if(rightCenter.x < leftCenter.x){
                    Point temp = rightCenter.clone();
                    rightCenter = leftCenter;
                    leftCenter = temp;
                }
                countFingers(colImage, contours.get(1));
                if(movementLeft >=5) {
                    String lDir = calculateMovementLeft();
                }else movementLeft++;
                if(movementRight >=5) {
                    String rDir = calculateMovementRight();
                }else movementRight++;
                /*if(!lDir.equals("")){
                        System.out.println(lDir);
                    }
                    if(!rDir.equals("")){
                        System.out.println(rDir);
                    }*/
                normCenterCoordinates();
            }


            //Imgproc.drawContours(colImage, contours, 0, new Scalar(0,0,255));
            //Imgproc.drawContours(colImage, hullList, 0, new Scalar(0,255,0) );


            //Hintergrund ausblenden
			/*Mat grayImage = new Mat();
			double[] grayData;
			double[] startData;
			double diff = 20;
			double[] black = {0,10,0};
			double[] white = {255,150,255};
			double[] a;
			int count = 0;
			Mat startImage = new Mat();
			frame.copyTo(startImage);
			count++;
			if(count == 20){
				frame.copyTo(startImage);
				System.out.println("hi");
			}
			frame.copyTo(grayImage);
			frame.copyTo(procImage);
			for(int x = 0; x<frame.rows();x++){
				for(int y = 0; y<frame.cols();y++){
					grayData = grayImage.get(x,y);
					startData = startImage.get(x,y);
					if(grayData[0] > startData[0]-diff && grayData[0] < startData[0]+diff  ||
							grayData[1] > startData[1]-diff && grayData[1] < startData[1]+diff ||
							grayData[2] > startData[2]-diff && grayData[2] < startData[2]+diff){
						grayImage.put(x,y,black);
					}
					//else{
						//grayImage.put(x,y,white);
					//}

				}
			}*/

            imgPanel1.setImage(Mat2BufferedImage(procImage));
            imgPanel2.setImage(Mat2BufferedImage(colImage));
            pack();

        }

    }

    /**
     * Zaehlt die ausgestreckten Finger einer Hand, indem die konvexen Punkte (groeßtenteils Fingerspitzen) der Hand mit den konkaven
     * Punkten (groeßtenteils Fingerzwischenraeume) verglichen werden.
     * Wenn ein konvexer Punkt zwei konkave Punkte als direkte Nachbarn hat und diese einen Winkel formen, welcher kleiner als 65 Grad ist,
     * wird bei dem konvexen Punkt, falls sich dieser nicht zu weit unter dem Handmittelpunkt befindet,
     * von einem ausgestreckten Finger ausgegangen.
     * Die Positionen der erkannten Fingerspitzen werden mit blauen Punkten und die Positionen der Nachbarn werden mit gruenen Punkten
     * im bearbeiteten Bild markiert.
     *
     * @param colImage Das zu bearbeitende Bild
     * @param contour Kontur der Hand
     */
    private void countFingers(Mat colImage, MatOfPoint contour) {
        List<MatOfPoint> hullList = new ArrayList<>();
        MatOfInt matInt = new MatOfInt();
        MatOfInt hull = new MatOfInt();
        MatOfInt4 defects = new MatOfInt4();
        Imgproc.convexHull(contour, hull);

        Point[] contourArray = contour.toArray();
        ArrayList<Point> hullPointArray = new ArrayList();
        ArrayList<Integer> clusteredHullInts = new ArrayList();
        Point centerPoint;
        double difX;
        double difY;
        double maxDif = 20;
        List<Integer> hullIndexList = hull.toList();
        centerPoint = contourArray[hullIndexList.get(0)];
        for (int i = 0; i < hullIndexList.size(); i++) { //Alle konvexen Punkte, die weniger als 20 Pixel von dem nächsten Punkt entfernt sind werden nicht betrachtet
            difX = Math.abs(centerPoint.x - contourArray[hullIndexList.get(i)].x);
            difY = Math.abs(centerPoint.y - contourArray[hullIndexList.get(i)].y);
            if(difX > maxDif || difY > maxDif) {
                hullPointArray.add(contourArray[hullIndexList.get(i)]);
                clusteredHullInts.add(hullIndexList.get(i));
                centerPoint = contourArray[hullIndexList.get(i)];
            }

        }
        if(!hullPointArray.isEmpty()) {
            Point[] clusteredPoints = new Point[hullPointArray.size()];
            for(int i = 0; i< hullPointArray.size();i++){
                clusteredPoints[i] = hullPointArray.get(i);
            }
            hullList.add(new MatOfPoint(clusteredPoints));


            if(!clusteredHullInts.isEmpty() && hullList.get(0).size().height >3) {
                matInt.fromList(clusteredHullInts);
            }

        }


        if(!hullList.isEmpty() && hullList.get(0).size().height > 3) {
            Imgproc.convexityDefects(contour, matInt, defects); // Findet die konkaven Punkte zu den konvexen Punkten der Hand

            HashMap<Point, ArrayList<Point>> hullDefects = new HashMap();

            if (defects != null && defects.size().height > 0) {
                List<Integer> defectPointList = defects.toList();
                Point data[] = contour.toArray();
                // Die konkaven Punkte werden den konvexen Punkten nach ihrer Position zugeordnet
                for (int j = 0; j < defectPointList.size(); j = j + 4) {
                    Point start = data[defectPointList.get(j)];
                    Point end = data[defectPointList.get(j + 1)];
                    Point defect = data[defectPointList.get(j + 2)];

                    if (!hullDefects.containsKey(start)) {
                        hullDefects.put(start, new ArrayList());
                    }
                    hullDefects.get(start).add(defect);
                    if (!hullDefects.containsKey(end)) {
                        hullDefects.put(end, new ArrayList());
                    }
                    hullDefects.get(end).add(defect);


                }
                HashMap<Point, ArrayList<Point>> hullWith2Neighbors = new HashMap();
                for (Point key : hullDefects.keySet()) {
                    if (hullDefects.get(key).size() == 2) { // Nur konvexe Punkte mit zwei konkaven Nachbarn werden betrachtet
                        hullWith2Neighbors.put(key, hullDefects.get(key));
                    }
                }

                HashMap<Point, ArrayList<Point>> hullFingers = new HashMap();
                for(Point key : hullWith2Neighbors.keySet()){
                    Point def1 = hullWith2Neighbors.get(key).get(0).clone();
                    Point def2 = hullWith2Neighbors.get(key).get(1).clone();
                    double vecNorm;
                    def1.x = def1.x - key.x;
                    def1.y = def1.y - key.y;
                    vecNorm = Math.sqrt(def1.x * def1.x + def1.y*def1.y); //Normiert den Vektor von dem konvexen Punkt zum ersten Nachbar
                    def1.x = def1.x/vecNorm;
                    def1.y = def1.y/vecNorm;

                    def2.x = def2.x - key.x;
                    def2.y = def2.y - key.y;
                    vecNorm = Math.sqrt(def2.x * def2.x + def2.y*def2.y); //Normiert den Vektor von dem konvexen Punkt zum zweiten Nachbar
                    def2.x = def2.x /vecNorm;
                    def2.y = def2.y/vecNorm;
                    int angle = (int)(Math.acos((def1.x *def2.x + def1.y * def2.y)) * (180 / Math.PI)); //Berechnet den Winkel zwischen den konvexen Punkten und ihren zwei konkaven Nachbarn
                    Point refPoint = findContourCenter(contour);
                    if(angle < 65){ //Nur Finger mit einem Winkel von 65 Grad werden betrachtet
                        if(rightCenter.x == refPoint.x && rightCenter.y == refPoint.y) {
                            if(rightCenter.y - key.y > -50) { //Finger duerfen nicht ueber 50 Pixel unter dem Handmittelpunkt liegen
                                hullFingers.put(key, hullWith2Neighbors.get(key));
                            }
                        }else{
                            if(leftCenter.y - key.y > -50) { //Finger duerfen nicht ueber 50 Pixel unter dem Handmittelpunkt liegen
                                hullFingers.put(key, hullWith2Neighbors.get(key));
                            }
                        }
                    }
                }
                for (Point key : hullFingers.keySet()) { //Zeichnet die Positionen der erkannten Fingerspitzen und Fingerzwischenraeume in "colImage" ein
                    Imgproc.circle(colImage, key, 8, new Scalar(255, 0, 0), 4);
                    for (Point val : hullFingers.get(key)) {
                        Imgproc.circle(colImage, val, 8, new Scalar(0, 255, 0), 4);
                    }
                }

                if(hullFingers.size() <= 3 && hullFingers.size() >= 1 && hullFingers.size() == fnumber){
                    if(frameCounter == 50){
                        System.out.println(hullFingers.size());
                        frameCounter = 0;
                    }else frameCounter++;
                }else frameCounter = 0;
                fnumber = hullFingers.size();
            }
        }
    }

    /**
     * Berechnet den Durchschnittswert der HSV-Werte in einem bestimmten Bereich des Bildes und
     * setzt Farbschranken fuer eine Binarisierung des Bildes um diesen Bereich fest.
     *
     * @param hsvFrame Ein HSV-Bild
     * @return Die berechneten Farbschranken
     */
    private Scalar[] calculateBoundaries(Mat hsvFrame){

        //Size of the frame 480x640

        double data [] = new double[3];
        double pixeldata [][] = new double [6400][3];
        int i = 0;
        for (int y = 250; y < 330; y++) {
            for (int x = 280; x < 360 ; x++) {
                data = hsvFrame.get(y,x); //Bereich definieren den die for schleifen durchlaufen sollen
                pixeldata [i] = data;
                i++;
            }
        }

        /*

        // Code zur Bestimmung der Farbschranken durch finden der minimalen und maximalen HSV-Wert in dem Bildbereich. Binarisiertes Ergebnis ist nicht optimal

        int maxH = (int)pixeldata [0][0];
        for (int v = 0; v < pixeldata.length; v++){
            if(pixeldata[v][0] > maxH){
                maxH = (int)pixeldata[v][0];
            }
        }

        int maxS = (int)pixeldata[0][1];
        for(int v = 0; v < pixeldata.length; v++){
            if(pixeldata[v][1] > maxS){
                maxS = (int)pixeldata[v][1];
            }
        }

        int maxV = (int)pixeldata[0][2];
        for(int v = 0; v < pixeldata.length; v++){
            if(pixeldata[v][2] > maxV){
                maxV = (int)pixeldata[v][2];
            }
        }

        int minH = (int)pixeldata[0][0];
        for(int v = 0; v < pixeldata.length; v++){
            if(minH > pixeldata[v][0]){
               minH = (int)pixeldata[v][0];
            }
        }

        int minS = (int)pixeldata[0][1];
        for(int v = 0; v < pixeldata.length; v++){
            if(minS > pixeldata[v][1]){
                minS = (int)pixeldata[v][1];
            }
        }

        int minV = (int)pixeldata[0][2];
        for(int v = 0; v < pixeldata.length; v++){
            if(minV > pixeldata[v][2]){
                minV = (int)pixeldata[v][2];
            }
        }

         */
        //Berechnet die Durchscnittswerte der HSV-Werte
        int averageH = 0;
        int averageS = 0;
        int averageV = 0;

        for (int a = 0;  a < pixeldata.length; a++){
            averageH +=  pixeldata[a][0];
            averageS +=  pixeldata[a][1];
            averageV +=  pixeldata[a][2];
        }
        averageH = averageH/pixeldata.length;
        averageS = averageS/pixeldata.length;
        averageV = averageV/pixeldata.length;

        Scalar bounderies [] = new Scalar[2];

        bounderies [0] = new Scalar(averageH-35, averageS-35, averageV-35);
        bounderies [1] = new Scalar(averageH+35, averageS+35, averageV+35);

        /*
        bounderies [0] = new Scalar(minH, minS, minV);
        bounderies [1] = new Scalar(maxH, maxS, maxV);
         */
        return bounderies;
    }


    /**
     * Laedt den Videostream der Web-Kamera aus und startet die Erkennung der Haende.
     */
    private void processShowVideo() {

        // BEGIN: Prepare streaming from internal web cam
        VideoCapture cap = new VideoCapture(0);
        // END: Prepare streaming from internal web cam


        // Check of camera can be opened
        if(!cap.isOpened())
            throw new CvException("The Video File or the Camera could not be opened!");
        handDetection(cap);


    }

    /**
     * Entnommen aus Gotzes, Merijam / Lehn, Karsten: WS2019 CVD Visual Computing 1: Bildverarbeitung VideoProcessingStartCode.
     *
     * Converts an OpenCV matrix into a BufferedImage.
     *
     * Inspired by
     * http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
     * Fastest code
     *
     * @param "OpenCV" Matrix to be converted must be a one channel (grayscale) or
     * three channel (BGR) matrix, i.e. one or three byte(s) per pixel.
     * @return converted image as BufferedImage
     *
     */
    public BufferedImage Mat2BufferedImage(Mat imgMat){
        int bufferedImageType = 0;
        switch (imgMat.channels()) {
            case 1:
                bufferedImageType = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                bufferedImageType = BufferedImage.TYPE_3BYTE_BGR;
                break;
            default:
                throw new IllegalArgumentException("Unknown matrix type. Only one byte per pixel (one channel) or three bytes pre pixel (three channels) are allowed.");
        }
        BufferedImage bufferedImage = new BufferedImage(imgMat.cols(), imgMat.rows(), bufferedImageType);
        final byte[] bufferedImageBuffer = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        imgMat.get(0, 0, bufferedImageBuffer);
        return bufferedImage;
    }

    /**
     * Gibt den auf -1 bis 1 in x- und y-Richtung normierten Wert der rechten Handmitte zurueck.
     * @return Normierter Wert der linken Handmitte
     */
    public Point getNormedCenterLeft() {
        return normedCenterLeft;
    }

    /**
     * Gibt den auf -1 bis 1 in x- und y-Richtung normierten Wert der linken Handmitte zurueck.
     * @return Normierter Wert der rechten Handmitte
     */
    public Point getNormedCenterRight() {
        return normedCenterRight;
    }
}