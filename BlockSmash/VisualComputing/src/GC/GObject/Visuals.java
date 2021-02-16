package GC.GObject;

import GC.GMath.Vector3f;
import GC.GObject.Primitives.Point;
import GC.GObject.Primitives.SphereMap;
import GC.GObject.Subsets.Light.Light;
import GC.GObject.Subsets.Light.LightContainer;
import GC.GObject.Subsets.Material.MaterialColor;
import GC.GObject.Surfaces.StandardSurface;
import Global.Camera;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.jogamp.opengl.GL.*;
/**
 * @author Patrick Pavlenko
 * @version 02.11.2019
 * Hier sind nur optische Objekte, die zur Visualisierung eines Spielfeldes dienen
 * Z.b zentrierte Flaeche die ins nichts geht,Punkte, Hintergrundobjekte
 */
public class Visuals {



    private StandardSurface grid;
   // private Point points;
    private SphereMap bg; // Background
    private float[] pointsVertex;
    private LightContainer light;
    private LinkedList<Float> vertexTransfer = new LinkedList();


    /**
     * Loescht alle VBO,VAO,Programs dieser Klasse
     */
    public void deleteAllVisBuffers()
    {
        grid.deleteBuffers();
        bg.deleteBuffers();
    }

    /**
     * Instanziierung Objekte sowie "laden" der VBOs und VAOs
     */
    public Visuals(ArrayList<Vector3f> paths,ArrayList<Vector3f> pathEnd)
    {
        light = new LightContainer(0.75f,10,8f,0.75f,10,-150f,0.75f,200f,-250f);
        bg = new SphereMap(0,0,10); // muss sich bei der Kameraposition befinden
        bg.modelMatrix.translate(0,0,0);
        bg.modelMatrix.rotateY((float)Math.toRadians(180));
        bg.modelMatrix.scaleSet(23f,20f,20f);
        bg.loadTexture("Textures/free_star_sky_hdri_spherical_map_by_kirriaa.jpg");
        bg.generateBuffers();
        bg.loadVBOBuffer(0,bg.getVertex(),3);
        bg.loadVBOBuffer(1,bg.getTexel(),2);
        bg.pauseVAO();


        // Modelmat am besten nicht translatieren,eher Viewmatrix nutzen
       // points = new Point(0,0,0,15f);
        // Point Koordinaten der Vector3f typen an float array (vertexTransfer) transferiert zum zeichnen
        for(Vector3f vec : paths)
        {
            vertexTransfer.add(vec.getVector()[0]);
            vertexTransfer.add(vec.getVector()[1]);
            vertexTransfer.add(vec.getVector()[2]);
        }
        for(Vector3f vec : pathEnd)
        {
            vertexTransfer.add(vec.getVector()[0]);
            vertexTransfer.add(vec.getVector()[1]);
            vertexTransfer.add(vec.getVector()[2]);
        }
 /*       pointsVertex = new float[vertexTransfer.size()];
        for(int x = 0; x < vertexTransfer.size()-1;x++) { pointsVertex[x] = vertexTransfer.get(x); }
        points.setVertex(pointsVertex);
        points.setNumVertex((pointsVertex.length) /3);
        points.generateBuffers();
        points.loadVBOBuffer(0,points.getVertex(),3);
        points.pauseVAO(); */

        // Erstellen der Flaeche im Spiel
        grid = new StandardSurface(0,0,-10,10f,300f,
                MaterialColor.SILVER_AMBIENT,MaterialColor.SILVER_DIFFUSE,MaterialColor.SILVER_SPECULAR,MaterialColor.SILVER_SHININESS);
        grid.generateBuffers();
        grid.getModelMatrix().translate(-grid.getWidth()/2,2f,-290);
        grid.pauseVAO();
    }


    /**
     * Endpunkte und Anfangspunkte werden gezeichnet,wo sich ein Block hin bewegen koennte
     * @param cam die Kamera welche die Viewmatrix weiter gibt
     */
    /*
    public void spawnDots(Camera cam)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUseProgram(points.getProgram().getProgram_ID());
        points.loadVAO();
        points.createMvMat(cam.getViewMatrix());

        points.getModelViewMatrix().loadUniform(points.getProgram().getProgram_ID(),"mvMat");
        cam.getProjMatrix().loadUniform(points.getProgram().getProgram_ID(),"projMat");

        points.getModelViewMatrix().sendUniformMat4f(points.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());

        points.drawObject();
        points.pauseVAO();

    } */


    /**
     *Zeichnet die tesselierte Flaeche
     */
    public void spawnGridSurface(Camera cam)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUseProgram(grid.getProgram().getProgram_ID());
        grid.loadVAO();
        grid.createMvMat(cam.getViewMatrix());
        grid.createInvTrMat();

        grid.getModelViewMatrix().loadUniform(grid.getProgram().getProgram_ID(),"mvMat");
        grid.getInverseTransMV().loadUniform(grid.getProgram().getProgram_ID(),"invMat");
        cam.getProjMatrix().loadUniform(grid.getProgram().getProgram_ID(),"projMat");
        grid.loadShading(cam.getViewMatrix(),light.getLights());
        grid.getInverseTransMV().sendUniformMat4fTransposed(grid.getInverseTransMV());
        grid.getModelViewMatrix().sendUniformMat4f(grid.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        grid.activateShading(light.getLights());
        grid.drawObject();
        grid.pauseVAO();

    }


    // zeichnet die Cubemap bzw Cubesphere im Hintegrund
    public void spawnBackground(Camera cam)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUseProgram(bg.getProgram().getProgram_ID());
        bg.loadVAO();
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        gl.glGenerateMipmap(GL_TEXTURE_2D);

        bg.createMvMat(cam.getViewMatrix());

        bg.getModelViewMatrix().loadUniform(bg.getProgram().getProgram_ID(),"mvMat");
        cam.getProjMatrix().loadUniform(bg.getProgram().getProgram_ID(),"projMat");
        bg.getTranslateTex().loadUniform(bg.getProgram().getProgram_ID(),"translationVec;");

        bg.getModelViewMatrix().sendUniformMat4f(bg.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
        bg.getTranslateTex().sendUniformVec2f();  // Translationsvektor senden

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        bg.activateTexture();
        bg.getModelMatrix().rotateX(0.0005f);
        bg.drawObject();
        bg.pauseVAO();
    }


    public LinkedList<Light> getLight() { return light.getLights(); }
}
