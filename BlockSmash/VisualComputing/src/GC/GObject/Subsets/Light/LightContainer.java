package GC.GObject.Subsets.Light;

import java.util.LinkedList;
/**
 * @author Patrick Pavlenko
 * @version 23.11.2019
 * Enthaelt alle Lichter einer Szene
 * Lichter werden hier eingestellt
 */
public class LightContainer {


    // Alle Lichter werden hier hinzugefuegt
    private LinkedList<Light> lights = new LinkedList<>();

    /**
     * Licht Objekte instanziiert
     * ADS Lichtwerte vergeben,Positionskoorinaten
     * Dann in LinkedList hinzugefuegt fuer spaetern abruf in display
     */
    public LightContainer(float x1,float y1,float z1,float x2,float y2,float z2,float x3,float y3,float z3)
    {
        float[] gAmbient = new float[] { 0.3f, 0.3f, 0.3f, 1.0f };
        float[] lAmbient = new float[] { 0.1f, 0.1f, 0.6f, 1.0f };// blue
        float[] lDiffuse = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] lSpecular = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        Light light01 = new Light("light01",gAmbient,lAmbient,lDiffuse,lSpecular,x1,y1,z1);
        Light light02 = new Light("light02",gAmbient,lAmbient,lDiffuse,lSpecular,x2,y2,z2);
        Light light03 = new Light("light03",gAmbient,lAmbient,lDiffuse,lSpecular,x3,y3,z3);
        lights.add(light01);
        lights.add(light02);
        lights.add(light03);
    }


    public LinkedList<Light> getLights() { return lights; }
}
