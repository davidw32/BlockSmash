package BV;

import Global.User;
import org.opencv.core.Point;
import org.opencv.core.*;
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

import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_SIMPLEX;

/**
 * Eine Klasse, die Bildverarbeitungsoperationen mit OpenCV auf einzelne Frames
 * eines Videostreams ausfuehrt. Sie zeigt die unverarbeiteten und die verarbeiteten
 * Bilder nebeneinader an und errechnet optimale Farbschranken für die Farberkennung.
 * Außerdem erkennt sie die Positionen und Fingeranzahlen der Haende im Frame.
 *
 * @author David Waelsch
 * @version 10.12.2019
 *
 */
public class VideoProcessing extends JFrame {

    private BufferedImagePanel imgPanel1;
    private BufferedImagePanel imgPanel2;
    private VideoCapture cap;
    private Point leftCenter = new Point(200,300);
    private Point rightCenter = new Point(400,600);
    private Point oldLeftCenter = new Point(200,300);
    private Point oldRightCenter = new Point(400,600);
    private int frameCounterRight = 0;
    private int frameCounterLeft = 0;
    private int fNumberRight = 0;
    private int fNumberLeft = 0;
    private int movementLeft = 0;
    private int movementRight = 0;

    private Mat frame = new Mat();
    private Mat hsvImage = new Mat();
    private Mat procImage = new Mat();
    private Mat colImage = new Mat();
    private Mat history = new Mat();
    //Scalar scalb = new Scalar(0,20, 120);
    //Scalar scalu = new Scalar(20,150,255);
    private Scalar scalb = new Scalar(30,70,50);
    private Scalar scalu = new Scalar(50,255,255);
    private JFrame jframe;
    private boolean continueStart = false;
    private boolean sliderStop = false;
    private JSlider hMinSlider;
    private JSlider hMaxSlider;
    private JSlider sMinSlider;
    private JSlider sMaxSlider;
    private JSlider vMinSlider;
    private JSlider vMaxSlider;


    private Point normedCenterLeft = new Point(-0.7,-0.4);
    private Point normedCenterRight = new Point(0.8,-0.4);

    /**
     * Konstruktor der Klasse VideoProcessing uebergibt der Klasse den Videostream.
     * @param cap Videostream der Klasse
     */
    public VideoProcessing(VideoCapture cap){
        this.cap = cap;
    }

    /**
     * Erzeugt ein JFrames, welches zwei Bilder und einige Schieberegler fuer die Einstellung
     * der optimalen HSV-Farbschranken anzeigt.
     *
     * Inspiriert von Gotzes, Merijam / Lehn, Karsten: WS2019 CVD Visual Computing 1: Bildverarbeitung VideoProcessingStartCode.
     */
    private void createFrame() {

        setTitle("Color Calibration");
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new FlowLayout());
        StartKeyListener keyListener = new StartKeyListener(this); // KeyListener fuer die Tasten "k" zum Kalibrieren und "ENTER" zum Fortfahren

        imgPanel1 = new BufferedImagePanel();
        contentPane.add(imgPanel1);
        imgPanel2 = new BufferedImagePanel();
        contentPane.add(imgPanel2);

        createSlider(contentPane,keyListener);

        jframe = (JFrame) SwingUtilities.windowForComponent(contentPane);
        jframe.addKeyListener(keyListener);
        jframe.setFocusable(true);


        // place the frame at the center of the screen and show
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Erzeugt ein JFrame, welches waehrend des Spielverlaufs das Kamerabild mit den erkannten Fingerspitzen.
     */
    public void createSmallerFrame(){
        setTitle("Camera");
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.removeAll();
        pack();

        imgPanel1 = new BufferedImagePanel();
        contentPane.add(imgPanel1);
        setSize(320,240);


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width - getWidth()-20, 0);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Erzeugt sechs Schieberegler auf der ContentPane des JFrames, welche fuer die Optimierung der
     * HSV-Schranken der Farberkennung veraendert werden koennen.
     *
     * @param contentPane Die ContentPane des JFrames
     * @param keyListener Der KeyListener zum Kalibrieren und Fortfahren
     */
    private void createSlider(JPanel contentPane, StartKeyListener keyListener){
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel,BoxLayout.Y_AXIS));

        contentPane.add(sliderPanel);

        //Hue-Lower Slider
        JLabel hMinText = new JLabel("Hue-Lower");
        hMinText.setAlignmentX(CENTER_ALIGNMENT);
        hMinSlider = new JSlider(0,180,60);
        hMinSlider.setMajorTickSpacing(30);
        hMinSlider.setMinorTickSpacing(5);
        hMinSlider.setPaintTicks(true);
        hMinSlider.setPaintLabels(true);
        hMinSlider.addKeyListener(keyListener);
        sliderPanel.add(hMinText);
        sliderPanel.add(hMinSlider);
        sliderPanel.add(Box.createVerticalStrut(20));

        //Hue-Upper Slider
        JLabel hMaxText = new JLabel("Hue-Upper");
        hMaxText.setAlignmentX(CENTER_ALIGNMENT);
        hMaxSlider = new JSlider(JSlider.CENTER,0,180,60);
        hMaxSlider.setMajorTickSpacing(30);
        hMaxSlider.setMinorTickSpacing(5);
        hMaxSlider.setPaintTicks(true);
        hMaxSlider.setPaintLabels(true);
        hMaxSlider.addKeyListener(keyListener);
        sliderPanel.add(hMaxText);
        sliderPanel.add(hMaxSlider);
        sliderPanel.add(Box.createVerticalStrut(20));

        //Saturation-Lower Slider
        JLabel sMinText = new JLabel("Saturation-Lower");
        sMinText.setAlignmentX(CENTER_ALIGNMENT);
        sMinSlider = new JSlider(JSlider.CENTER,0,100,25);
        sMinSlider.setMajorTickSpacing(25);
        sMinSlider.setMinorTickSpacing(5);
        sMinSlider.setPaintTicks(true);
        sMinSlider.setPaintLabels(true);
        sMinSlider.addKeyListener(keyListener);
        sliderPanel.add(sMinText);
        sliderPanel.add(sMinSlider);
        sliderPanel.add(Box.createVerticalStrut(20));

        //Saturation-Upper Slider
        JLabel sMaxText = new JLabel("Saturation-Upper");
        sMaxText.setAlignmentX(CENTER_ALIGNMENT);
        sMaxSlider = new JSlider(JSlider.CENTER,0,100,25);
        sMaxSlider.setMajorTickSpacing(25);
        sMaxSlider.setMinorTickSpacing(5);
        sMaxSlider.setPaintTicks(true);
        sMaxSlider.setPaintLabels(true);
        sMaxSlider.addKeyListener(keyListener);
        sliderPanel.add(sMaxText);
        sliderPanel.add(sMaxSlider);
        sliderPanel.add(Box.createVerticalStrut(20));

        //Value-Lower Slider
        JLabel vMinText = new JLabel("Value-Lower");
        vMinText.setAlignmentX(CENTER_ALIGNMENT);
        vMinSlider = new JSlider(JSlider.CENTER,0,100,25);
        vMinSlider.setMajorTickSpacing(25);
        vMinSlider.setMinorTickSpacing(5);
        vMinSlider.setPaintTicks(true);
        vMinSlider.setPaintLabels(true);
        vMinSlider.addKeyListener(keyListener);
        sliderPanel.add(vMinText);
        sliderPanel.add(vMinSlider);
        sliderPanel.add(Box.createVerticalStrut(20));

        //Value-Upper Slider
        JLabel vMaxText = new JLabel("Value-Upper",JLabel.CENTER);
        vMaxText.setAlignmentX(CENTER_ALIGNMENT);
        vMaxSlider = new JSlider(JSlider.CENTER,0,100,25);
        vMaxSlider.setMajorTickSpacing(25);
        vMaxSlider.setMinorTickSpacing(5);
        vMaxSlider.setPaintTicks(true);
        vMaxSlider.setPaintLabels(true);
        vMaxSlider.addKeyListener(keyListener);
        sliderPanel.add(vMaxText);
        sliderPanel.add(vMaxSlider);
        sliderPanel.add(Box.createVerticalStrut(20));
    }

    /**
     * Normiert die Positionen der linken und rechten Handmittelpunkte auf einen Bereich von -1 bis 1 in x- und y-Richtung
     * fuer die vereinfachte Darstellung im OpenGL-Viewport.
     */
    private void normCenterCoordinates(){
        normedCenterLeft.x = (double)1/320*leftCenter.x - 1;
        normedCenterLeft.y = (double)-1/240*leftCenter.y + 1;

        normedCenterRight.x = (double)1/320*rightCenter.x - 1;
        normedCenterRight.y = (double)-1/240*rightCenter.y + 1;
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
    public String calculateMovementLeft(){
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
    public String calculateMovementRight(){
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
     * @param user Der Spieler
     */
    public void handDetection(User user){

        if(cap.read(frame)){
            frame.convertTo(frame, CvType.CV_8UC3);
            frame.copyTo(colImage);
            Core.flip(frame,frame,1); // Bild vertikal spiegeln
            frame.copyTo(colImage);

            Imgproc.cvtColor(frame,hsvImage,Imgproc.COLOR_BGR2HSV);

            Imgproc.GaussianBlur(hsvImage, hsvImage, new Size(21, 21), 0, 0);

            Core.inRange(hsvImage,scalb,scalu,hsvImage);

            //Imgproc.GaussianBlur(hsvImage, procImage, new Size(3, 3), 0, 0);


            Imgproc.medianBlur(hsvImage,procImage,3);
            ArrayList<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(procImage,contours,history,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE); // findet die aeußeren Konturen

            contours.sort(new sizeComp());

            if(contours.size()>1 && contours.get(0).size().height >= 120 && contours.get(1).size().height >= 120){ // kleine Konturen werden ignoriert
                leftCenter = findContourCenter(contours.get(0));
                countFingers(contours.get(0),user);
                rightCenter = findContourCenter(contours.get(1));
                if(rightCenter.x < leftCenter.x){ // rechte Hand wird mit der linken Hand getauscht
                    Point temp = rightCenter.clone();
                    rightCenter = leftCenter;
                    leftCenter = temp;
                }
                countFingers(contours.get(1),user);
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

            Imgproc.resize(colImage,colImage,new Size(320,240));
            imgPanel1.setImage(Mat2BufferedImage(colImage));
            pack();


            // Code zum ausblenden des Hintergrunds von einem Referenzframe ausgehend. Nur bei ausgeschaltetem Autofocus/Lichtanpassung anwendbar
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
			}
			*/

        }
        else{
            System.out.println("The Camera could not be openend!");
        }

    }

    /**
     * Zaehlt die ausgestreckten Finger einer Hand, indem die konvexen Punkte (groeßtenteils Fingerspitzen) der Hand mit den konkaven
     * Punkten (groeßtenteils Fingerzwischenraeume) verglichen werden.
     * Wenn ein konvexer Punkt zwei konkave Punkte als direkte Nachbarn hat und diese einen Winkel formen, welcher kleiner als 65 Grad ist,
     * wird bei dem konvexen Punkt, falls sich dieser nicht zu weit unter dem Handmittelpunkt befindet,
     * von einem ausgestreckten Finger ausgegangen.
     *
     * @param contour Kontur der Hand
     * @param user Der Spieler
     */
    public int countFingers(MatOfPoint contour,User user) {
        HashMap<Point, ArrayList<Point>> hullFingers = new HashMap();
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
                Point refPoint = findContourCenter(contour);
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

                for (Point key : hullFingers.keySet()) { //Zeichnet die Positionen der erkannten Fingerspitzen in "colImage" ein
                    Imgproc.circle(colImage, key, 10, new Scalar(0, 0, 255), 6);
                }

                if(rightCenter.x == refPoint.x && rightCenter.y == refPoint.y) {
                    if (hullFingers.size() <= 3 && hullFingers.size() >= 1 && hullFingers.size() == fNumberRight) { //Fingeranzahl wird nur weitergegeben wenn sie für mehrere Frames gleichbleibt
                        if (frameCounterRight == 12) {
                            user.setFingers(fNumberRight);
                            frameCounterRight = 0;
                        } else frameCounterRight++;
                    } else frameCounterRight = 0;
                    fNumberRight = hullFingers.size();
                }
                else{
                    if (hullFingers.size() <= 3 && hullFingers.size() >= 1 && hullFingers.size() == fNumberLeft) { //Fingeranzahl wird nur weitergegeben wenn sie für mehrere Frames gleichbleibt
                        if (frameCounterLeft == 12) {
                            user.setFingers(fNumberLeft);
                            frameCounterLeft = 0;
                        } else frameCounterLeft++;
                    } else frameCounterLeft = 0;
                    fNumberLeft = hullFingers.size();
                }
            }
        }
        return hullFingers.size();
    }

    /**
     * Startet das JFrame und wandelt das von dem Videostream ausgelesene Frame in ein HSV-Bild um.
     * Sobald der Spieler die Taste "k" drueckt werden die von der Methode "calculateBoundaries()" errechneten Farbschranken
     * festgesetzt und der Spieler kann diese nun durch das Verschieben der Regler optimieren.
     * Wenn der Spieler die "ENTER"-Taste drueckt werden die eingestellten Farbschranken fuer das Spiel gespeichtert.
     * Textanweisungen werden mit Hilfe von OpenCV in die angezeigten Bilder geschrieben.
     */
    public void initCalculateBoundaries(){
        //Size of the frame 640x480
        createFrame();
        Scalar boundaries[] = new Scalar[2];

        cap.read(frame);
        frame.convertTo(frame, CvType.CV_8UC3);
        frame.copyTo(hsvImage);

        while(!continueStart){
            cap.read(frame);
            Core.flip(frame, frame, 1);
            frame.copyTo(colImage);

            Imgproc.cvtColor(frame, hsvImage, Imgproc.COLOR_BGR2HSV);

            Imgproc.GaussianBlur(hsvImage, hsvImage, new Size(21, 21), 0, 0);

            if(!sliderStop) {
                boundaries = calculateBoundaries(hsvImage);

                hMinSlider.setValue((int)boundaries[0].val[0]);
                sMinSlider.setValue((int)(boundaries[0].val[1]/255*100));
                vMinSlider.setValue((int)(boundaries[0].val[2]/255*100));
                hMaxSlider.setValue((int)boundaries[1].val[0]);
                sMaxSlider.setValue((int)(boundaries[1].val[1]/255*100));
                vMaxSlider.setValue((int)(boundaries[1].val[2]/255*100));

                Imgproc.rectangle(colImage, new Rect(250, 300, 90, 90), new Scalar(117, 230, 18), 5);
                Imgproc.putText(colImage,"Cover the green rectangle entirely with your glove!",new Point(30,25), FONT_HERSHEY_SIMPLEX,0.6,new Scalar(207, 204, 196),1);
                Imgproc.putText(colImage,"Press 'k' to calibrate. Press 'Enter' to continue!",new Point(30,50), FONT_HERSHEY_SIMPLEX,0.6,new Scalar(207, 204, 196),1);
            }
            else{
                boundaries[0] = new Scalar(hMinSlider.getValue(),sMinSlider.getValue()*2.55,vMinSlider.getValue()*2.55);
                boundaries[1] = new Scalar(hMaxSlider.getValue(),sMaxSlider.getValue()*2.55,vMaxSlider.getValue()*2.55);
                Imgproc.putText(colImage,"Adjust the sliders until your glove is well",new Point(30,25),Imgproc.FONT_HERSHEY_SIMPLEX,0.6,new Scalar(207, 204, 196),1);
                Imgproc.putText(colImage,"separated from the background.",new Point(30,50),Imgproc.FONT_HERSHEY_SIMPLEX,0.6,new Scalar(207, 204, 196),1);
                Imgproc.putText(colImage,"=> Press 'Enter' to continue",new Point(300,450),Imgproc.FONT_HERSHEY_DUPLEX,0.7,new Scalar(32, 199, 154),1);
            }

            Core.inRange(hsvImage, boundaries[0], boundaries[1], hsvImage);

            //Imgproc.GaussianBlur(hsvImage, procImage, new Size(3, 3), 0, 0);
            Imgproc.medianBlur(hsvImage,hsvImage,3);


            imgPanel1.setImage(Mat2BufferedImage(colImage));
            imgPanel2.setImage(Mat2BufferedImage(hsvImage));
            pack();

        }

        this.scalb = boundaries[0];
        this.scalu = boundaries[1];
        jframe.dispose();
    }

    /**
     * Berechnet den Durchschnittswert der HSV-Werte in einem bestimmten Bereich des Bildes und
     * setzt Farbschranken fuer eine Binarisierung des Bildes um diesen Bereich fest.
     *
     * @param image Ein HSV-Bild
     * @return Die berechneten Farbschranken
     */
    public Scalar[] calculateBoundaries(Mat image) {
        Scalar[] bounds = new Scalar[2];
        double data[];
        double pixeldata[][] = new double[6400][3];
        int i = 0;
        for (int y = 250; y < 330; y++) {
            for (int x = 280; x < 360; x++) {
                data = image.get(y, x); //Bereich definieren den die for schleifen durchlaufen sollen
                pixeldata[i] = data;
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

        bounds[0] = new Scalar(minH, minS, minV);
        bounds[1] = new Scalar(maxH, maxS, maxV);
        */

        //Berechnet die Durchscnittswerte der HSV-Werte
        int averageH = 0;
        int averageS = 0;
        int averageV = 0;

        for (int a = 0; a < pixeldata.length; a++) {
            averageH += pixeldata[a][0];
            averageS += pixeldata[a][1];
            averageV += pixeldata[a][2];
        }
        averageH = averageH / pixeldata.length;
        averageS = averageS / pixeldata.length;
        averageV = averageV / pixeldata.length;

        bounds[0] = new Scalar(averageH - 10, averageS - 60, averageV - 60);
        bounds[1] = new Scalar(averageH + 10, averageS + 45, averageV + 55);

        return bounds;
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
     * @param imgMat "OpenCV" Matrix to be converted must be a one channel (grayscale) or
     * three channel (BGR) matrix, i.e. one or three byte(s) per pixel.
     * @return converted image as BufferedImage
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
     * Setzt "continueStart" auf den uebergebenen Wert.
     * @param continueStart Wert auf den "continueStart" gesetzt wird
     */
    public void setContinueStart(boolean continueStart){
        this.continueStart = continueStart;
    }

    /**
     * Setzt "sliderStop" auf den uebergebenen Wert.
     * @param sliderStop Wert auf den "sliderStop" gesetzt wird
     */
    public void setSliderStop(boolean sliderStop){
        this.sliderStop = sliderStop;
    }

    /**
     * Setzt die Position der linken Handmitte auf den uebergebenen Wert.
     * @param leftCenter Wert auf den "leftCenter" gesetzt wird
     */
    public void setLeftCenter(Point leftCenter) {
        this.leftCenter = leftCenter;
    }

    /**
     * Setzt die Position der linken Handmitte des voherigen Frames auf den uebergebenen Wert.
     * @param oldLeftCenter Wert auf den "oldLeftCenter" gesetzt wird
     */
    public void setOldLeftCenter(Point oldLeftCenter) {
        this.oldLeftCenter = oldLeftCenter;
    }

    /**
     * Setzt die Position der rechten Handmitte auf den uebergebenen Wert.
     * @param rightCenter Wert auf den "rightCenter" gesetzt wird
     */
    public void setRightCenter(Point rightCenter) {
        this.rightCenter = rightCenter;
    }

    /**
     * Setzt die Position der rechten Handmitte des voherigen Frames auf den uebergebenen Wert.
     * @param oldRightCenter Wert auf den "oldRightCenter" gesetzt wird
     */
    public void setOldRightCenter(Point oldRightCenter) {
        this.oldRightCenter = oldRightCenter;
    }

    /**
     * Gibt den auf -1 bis 1 in x- und y-Richtung normierten Wert der rechten Handmitte zurueck.
     * @return Normierter Wert der rechten Handmitte
     */
    public Point getNormedCenterRight() {
        return normedCenterRight;
    }

    /**
     * Gibt den auf -1 bis 1 in x- und y-Richtung normierten Wert der linken Handmitte zurueck.
     * @return Normierter Wert der linken Handmitte
     */
    public Point getNormedCenterLeft() {
        return normedCenterLeft;
    }

}
