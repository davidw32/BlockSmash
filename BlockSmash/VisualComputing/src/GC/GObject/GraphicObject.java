package GC.GObject;


import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import GC.GMath.Matrix4f;
import GC.GMath.Vector1f;
import GC.GMath.Vector3f;
import GC.GObject.Subsets.Light.Light;
import GC.GObject.Subsets.Material.Material;
import GC.Reader.ImageReader;
import GC.Reader.ModelLoader;
import Global.Program;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import static com.jogamp.common.nio.Buffers.SIZEOF_FLOAT;
import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
/**
 * @author Patrick Pavlenko
 * @version 19.11.2019
 * Alle Klasse in GObject müssen von dieser Klasse erben
 * Alle grafischen Objekte erhalten ihre eigene Klasse
 */
public abstract class GraphicObject {


     /**
     * Fuer die Vbos folgendes Pattern:
     *  0 = Vertex (Objektkoordinaten)
     *  1 = texel
     *  2 = normals
     *  3 = flexibler placeholder
     *  Jeder vbo aufgreifbar in GLSL (falls gepassed worden ist ) mittels (location = vboNummmerhier) in var
     */

    protected Matrix4f modelMatrix; // "Koordinaten" des Objektes
    protected Matrix4f modelViewMatrix; // erstellt durch Multiplikation:  ViewMat * ModelMat
    protected Matrix4f inverseTransMV; // Inverexe-transponierte der MV Matrix
    protected float modelX;
    protected float modelY;
    protected float modelZ;


    protected Program program;
    protected int[] vbo = new int[4];
    protected int[] vao = new int[1]; // pro Objekt nur 1 vao am besten
    protected int[] texture_ID = new int[1];
    // Ob ein VBO in 2er ,3er oder 4er Sets pro Vertex bzw geladen wird
    protected int numVertex;

    protected float[] texel;
    protected float[] vertex;
    protected float[] normals;

    protected Material materials;
    protected boolean destroyed = false;
    protected boolean spawned = false;
    // Geradenlinien parameter t   0 <= t <= 1 voraussichtlich
    protected float iterator = 0;
    //Start und endpositionen eines GraphicObjects bei einer Animation bzw Translatierung
    protected Vector3f startPos;
    protected Vector3f endPos;
    protected Vector1f animFactor = new Vector1f(0);
    protected int drawType;

    /**
     * Initialisierung von model + modelView Matrix
     * @param x Koordinaten fuer das Model
     * @param y
     * @param z
     */
    protected GraphicObject(float x,float y,float z)
    {
        modelX = x;
        modelY = y;
        modelZ = z;
        modelMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        inverseTransMV = new Matrix4f();
        modelMatrix.identity();
        modelViewMatrix.identity();
        modelMatrix.translate(x,y,z);
    }

    /**
     * Erlaubt das gleiche wie der obige Konstruktur, nur mit laden einer OBJ Datei
     * @param x x-Koordinate der ModelMatrix
     * @param y y-Koordinate der ModelMatrix
     * @param z z-Koordinate der ModelMatrix
     * @param objFilePath Pfad der OBJ Datei
     */
    protected GraphicObject(float x,float y,float z,String objFilePath)
    {
       this(x,y,z);
       ModelLoader loader = new ModelLoader(objFilePath);
       numVertex = loader.getNumVertices();
       vertex = new float[(loader.getVertices().length)*3];
       int inc = 0;
       //Zuweisung der OBJ Attribute an das jeweilige Array
       for(int a=0;inc <= loader.getVertices().length-1;a += 3)
       {
            vertex[a] = loader.getVertices()[inc].getVector()[0];
            vertex[a+1] = loader.getVertices()[inc].getVector()[1];
            vertex[a+2] = loader.getVertices()[inc].getVector()[2];
            inc++;
       }
        inc = 0;
        texel = new float[(loader.getTexCoords().length)*2];
        for(int a=0;inc <= loader.getVertices().length-1;a += 2)
        {
            texel[a] = loader.getTexCoords()[inc].getVertex()[0];
            texel[a+1] = loader.getTexCoords()[inc].getVertex()[1];
            inc++;
        }
        inc = 0;
        normals = new float[(loader.getNormals().length)*3];
        for(int a=0;inc <= loader.getVertices().length-1;a += 3)
        {
            normals[a] = loader.getNormals()[inc].getVector()[0];
            normals[a+1] = loader.getNormals()[inc].getVector()[1];
            normals[a+2] = loader.getNormals()[inc].getVector()[2];
            inc++;
        }

    }

    // Konstruktor mit Belichtung + Shader + OBJ
    protected GraphicObject(float x,float y,float z,String objFilePath,float[] mAmbient,float[] mDiffuse,float[] mSpecular,float mShininess)
    {
        this(x,y,z,objFilePath);
        materials = new Material(mAmbient,mDiffuse,mSpecular,mShininess);
    }

    // Konstruktor mit Belichtung + Shader
    protected GraphicObject(float x,float y,float z,float[] mAmbient,float[] mDiffuse,float[] mSpecular,float mShininess)
    {
        this(x,y,z);
        materials = new Material(mAmbient,mDiffuse,mSpecular,mShininess);
    }

    /**
     * generiert die VAO und VBOs damit diese ladbar sind fuer loadSingleBuffer
     * @param vaoIndex welcher VAO-Index (Also Objekt) die VBOs kriegt
     */
    public void generateBuffers()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        // vao.length anzahl an vaos erstellt
        gl.glGenVertexArrays(vao.length,vao,0);
        // aktivieren VAO
        gl.glBindVertexArray(vao[0]);
        // wie viele Buffer generiert werden sollen fuer VBO (ermittelt durch vbo.length)
        gl.glGenBuffers(vbo.length,vbo,0);
    }

    public void pauseVAO()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glBindVertexArray(0);
    }

    public void loadVAO() //reaktiviert den VAO ( nur bei einer zeichnungsmethode gedacht)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glBindVertexArray(vao[0]);
    }

    /**
     * laed VBO Buffer ( Achtung! , VAOs muessen erst erstellt werden  / generateBuffers ausfuehren)
     * @param vboIndex ob bzw
     * @param vertices Objektvertices
     */
    public void loadVBOBuffer(int vboIndex,float[] vertices,int vectorNum)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glBindBuffer(GL_ARRAY_BUFFER,vbo[vboIndex]); // Bufferzuweisung an VBO
        FloatBuffer buf = Buffers.newDirectFloatBuffer(vertices);
        gl.glBufferData(GL_ARRAY_BUFFER,buf.limit()*4,buf,GL_STATIC_DRAW); // Buffer erhaelt Werte
        gl.glEnableVertexAttribArray(vboIndex); // Buffer aktiviert
        gl.glVertexAttribPointer(vboIndex,vectorNum,GL_FLOAT,false,0,0); // VBO an VAO referenziert
    }


    public void loadTexture(String path) { texture_ID[0] = ImageReader.readTexture(path); }

    /**
     * ADS LIGHT + Material ADS wird hier nach GLSL geladen
     */
    public void loadShading(Matrix4f vMat,LinkedList<Light> light)
    {
        materials.loadMaterial(program.getProgram_ID());
        for(Light lig : light) { lig.loadLight(program.getProgram_ID(),vMat); }
    }


    /**
     * ADS Light + Material ADS wird hier aktiviert
     */
    public void activateShading(LinkedList<Light> light)
    {
        materials.activateMaterial();
        for(Light lig : light) { lig.activateLight(); }
    }
    /**
     * @param texture_ID ID der Textur
     * @param GL_TEXTURE welche unit der Textur ( gewoehnlicherweise GL_TEXTURE0)
     * fuer verschiedene Texturen mehrere Texture Units aktivieren bzw nutzen
     * GL_TEXTURE0, GL_TEXTURE1 .....
     */
    public void activateTexture()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D,texture_ID[0]);
    }


    //ModelViewMatrix wird erstellt durch multiplikation einer view Matrix der Camera Klasse und der ModelMatrix eines GraphicObjects
    public void createMvMat(Matrix4f viewMat)
    {
        modelViewMatrix.identity();
        modelViewMatrix.multM4f(viewMat);
        modelViewMatrix.multM4f(modelMatrix);
    }

    //Erstellt eine Inverse transponierte Matrix
    public void createInvTrMat()
    {
        inverseTransMV.transferValues(modelViewMatrix);
        inverseTransMV.inverse();
    }

    /**
     * Objekt wird gezeichnet
     * Bedingung: Angabe numVertex + program im Graphicobject instanziiert
     */
    public void drawObject()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glDrawArrays(drawType,0,numVertex);
        gl.glBindVertexArray(0);
    }

    public void createProgram(String[] vShader,String[] fShader)
    {
        program = new Program(vShader,fShader);
    }

    public void createProgram(String[] vShader,String[] fShader,String[] tes1Shader,String[] tes2Shader)
    {
        program = new Program(vShader,fShader,tes1Shader,tes2Shader);
    }

    public void deleteBuffers()
    {
            GL4 gl = (GL4) GLContext.getCurrentGL();
            gl.glDeleteBuffers(4, vbo, 0);
            gl.glDeleteVertexArrays(1, vao, 0);
            gl.glDeleteProgram(program.getProgram_ID());
            gl.glDeleteTextures(1, texture_ID, 0);
    }

    public void iterateAnimFac(float iteration) {
        animFactor.setVertex(animFactor.getVertex()+iteration);
    }


    public void setVertex(float[] vertex) { this.vertex = vertex; }
    public void setNumVertex(int numVertex) { this.numVertex = numVertex; }
    public void setSpawned(boolean spawned) { this.spawned = spawned; }
    public void setEndPos(Vector3f endPos) { this.endPos = endPos; }
    public void setStartPos(Vector3f startPos) { this.startPos = startPos; }
    public void setIterator(float iterator) { this.iterator = iterator; }
    public void setProgram(Program program) { this.program = program; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }

    public Vector1f getAnimFactor() { return animFactor; }
    public boolean isDestroyed() { return destroyed; }
    public Matrix4f getInverseTransMV() { return inverseTransMV; }
    public float[] getNormals() { return normals; }
    public float[] getTexel() { return texel; }
    public float[] getVertex() { return vertex; }
    public Matrix4f getModelViewMatrix() { return modelViewMatrix; }
    public Matrix4f getModelMatrix() { return modelMatrix; }
    public float getIterator() { return iterator; }
    public boolean isSpawned() { return spawned; }
    public Program getProgram() { return program; }
    public Vector3f getStartPos() { return startPos; }
    public Vector3f getEndPos() { return endPos; }
    public int[] getVbo() { return vbo; }
    public int[] getVao() { return vao; }
}

