package GC.Reader;

import GC.GMath.Vector2f;
import GC.GMath.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
/**
 * @author Patrick Pavlenko
 * @version 29.10.2019
 * Laden eines Objektes aus einer OBJ File
 * Geladen werden:
 * Vertex,Texel,Normals,Indeces
 * Klasse abgeaendert und uebernommen aus:
 * Gordon, V. Scott / clevenger, John (2019): computer graphics programming in opengl with Java. Second Edition. Mercury learning and education: dulles, Virginia.
 * S155 V.Scott Gordon ( Hier bitte auch zitieren!)
 */
public class ModelLoader {



    private Vector3f[] vertices;
    private Vector2f[] texCoords;
    private Vector3f[] normals;
    private int numVertices;

    public ModelLoader(String filename)
    {	ModelImporter modelImporter = new ModelImporter();
        try
        {	modelImporter.parseOBJ(filename);
            numVertices   = modelImporter.getNumVertices();
            float[] verts = modelImporter.getVertices();
            float[] tcs   = modelImporter.getTextureCoordinates();
            float[] norm  = modelImporter.getNormals();

            vertices = new Vector3f[numVertices];
            texCoords = new Vector2f[numVertices];
            normals = new Vector3f[numVertices];

            for(int i=0; i<vertices.length; i++)
            {	vertices[i] = new Vector3f();
                vertices[i].setVertex(verts[i*3], verts[i*3+1], verts[i*3+2]);
                texCoords[i] = new Vector2f();
                texCoords[i].setVertex(tcs[i*2], tcs[i*2+1]);
                normals[i] = new Vector3f();
                normals[i].setVertex(norm[i*3], norm[i*3+1], norm[i*3+2]);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }



    public int getNumVertices() { return numVertices; }
    public Vector3f[] getVertices() { return vertices; }
    public Vector2f[] getTexCoords() { return texCoords; }
    public Vector3f[] getNormals() { return normals; }

}
