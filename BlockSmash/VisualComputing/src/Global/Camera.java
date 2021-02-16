package Global;

import GC.GMath.Matrix4f;
import GC.GMath.Vector3f;
/**
 * @author Patrick Pavlenko
 * @version 19.12.2019
 */
public class Camera {


    /** Viewmatrix X Koordinate */
    private float camX;
    /** Viewmatrix Y Koordinate */
    private float camY;
    /** Viewmatrix Z Koordinate */
    private float camZ;

    private Vector3f vecCoord;
    /** zNear der Projektionsmatrix */
    private float zNear = 0.1f;
    /** zFar der Projektionsmatrix */
    private float zFar = 1000f;
    /** fov der Projektionsmatrix */
    private float fov = (float) 60;
    /** Winkel fuer die FOV */
    private double angle = Math.toRadians(fov/2);
    private float q = (float) (1/ (Math.tan(angle) ) );
    /** Viewmatrix. Wird verwendet zur erzeugung der MV Matrizen  */
    private Matrix4f viewMatrix;
    /** Projektionsmatrix zur Erzeugung der Bildlichen Ansicht */
    private Matrix4f projMatrix; // Projektionsmatrix
    /** iterator fuer eine Geradenlinien Translation (im Startmenue genutzt) */
    private float iterator = 0;

    /**
     * Achtung rechthand Koordinatensystem!
     * @param camX
     * @param camY
     * @param camZ muss negativ sein falls nach vorne gesetzt werden sollen
     * @param windowHeight Fensterhoehe des Programms
     * @param windowWidth Fensterbreite des Programms ( Jframe)
     */
    public Camera(float camX,float camY,float camZ,float windowHeight,float windowWidth)
    {
        this.camX = camX;
        this.camY = camY;
        this.camZ = camZ;
        vecCoord = new Vector3f(camX,camY,-camZ);

        viewMatrix = new Matrix4f();
        projMatrix = new Matrix4f();

        viewMatrix.identity();
        viewMatrix.translate(-camX,-camY,-camZ);

        float aspectRatio =  windowWidth / windowHeight;
        projMatrix.getMatrix()[0] = q / aspectRatio ;
        projMatrix.getMatrix()[5] = q;
        projMatrix.getMatrix()[10] = (zNear + zFar) / (zNear - zFar);
        projMatrix.getMatrix()[11] =  (2*zNear * zFar) / (zNear - zFar) ;
        projMatrix.getMatrix()[14] = -1;
        projMatrix.getMatrix()[15] = 0;

    }

    /**
     * updatet den t Parameter der Geradenlinie im Hauptmenue
     * @param add der wert der addiert werden soll zum t Parameter (iterator)
     * @param limit maximale Wert den der iterator annehmen darf
     * @return falls richtig dann wird weiter addiert,ansonsten wird t parameter zuruckgesetzt und false ausgegeben
     */
    public boolean updateIterator(float add,float limit)
    {
        if(iterator >= limit) // in startmenu  muss es > 0.0015 sein fuer true kondition
        {
            iterator = 0;
            return false;
        }
        iterator += add;
        return true;
    }

    /**
     * Geradengleichung wird gebildet um Objekt von bestimmten Pfad zu einen Endpos zu bringen
     * (1-t)* p1 + t * p2  (Geradengleichungen)
     * @param t iterator der die Geradenlinien inkremiert
     * @param p2 endpunkt als Vector3f
     */
    public void moveCam(Vector3f p2)
    {
        // (1-t)* p1 + t * p2  (Geradengleichungen)
        Vector3f vec = vecCoord.multOut(1-iterator); // (1-t) * p1
        Vector3f vec2 = p2.multOut(iterator);  // t* p2
        vecCoord = vec.addOut(vec2);        // vec + vec2
        viewMatrix.translate(vecCoord);
    }

    public Matrix4f getViewMatrix() { return viewMatrix; }
    public Matrix4f getProjMatrix() { return projMatrix; }
    public float getCamX() { return camX; }
    public float getCamY() { return camY; }
    public float getCamZ() { return camZ; }
    public Vector3f getVecCoord() { return vecCoord; }
}
