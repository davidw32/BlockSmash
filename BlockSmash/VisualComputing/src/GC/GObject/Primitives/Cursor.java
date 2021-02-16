package GC.GObject.Primitives;

import GC.GObject.GraphicObject;
import GC.Reader.ShaderReader;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import java.util.ArrayList;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2GL3.GL_LINE;
/**
 * @author Patrick Pavlenko
 * @version 08.11.2019
 * "Mauszeiger" welcher mittels Handgeste gesteuert wird
 * Diese Klasse ist als aussnahme nur mit x,y Koordinaten fuer den Vertexbuffer zu benutzen
 *
 */
public class Cursor extends GraphicObject {


    /**
     * Kreis rotiert fuer zwei dimensionaler Sicht
     * runter skaliert um im Viewport nicht zu gross zu wirken
     * typische Initialisierung mit Koordinatne und OBJ Pfad etc.
     * @param x x-Koordinaten von ModelMatrix
     * @param y y-Koordinaten von ModelMatrix
     * @param z z-Koordinaten von ModelMatrix
     * @param path Pfad der OBJ Datei
     */
    public Cursor(float x,float y,float z,String path)
    {
        super(x,y,z,path);
        drawType = GL_TRIANGLES;
        modelMatrix.scaleSet(0.05f,0.025f,1f);
        modelMatrix.rotateX((float)Math.toRadians(90));
    }


    /**
     * Hier mit wird Objekt gezeichnet
     * Depth Buffer wird ausgeschaltet um Cursor priorisiert vorne in der Ansicht zu haben
     */

    @Override
    public void drawObject() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glDisable(GL_DEPTH_TEST); // Damit Cursor vorne bleiben auf dem Bildschirm!
        gl.glDrawArrays(drawType,0,numVertex);
        gl.glBindVertexArray(0);
        gl.glEnable(GL_DEPTH_TEST);
    }
}
