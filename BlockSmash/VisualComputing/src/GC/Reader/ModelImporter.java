package GC.Reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ModelImporter
{
        /**
         * @author: Patrick Pavlenko
         * @version: 29.10.2019
         * Benutzt in : ModelLoader
         * Entnommen aus:
         * Gordon, V. Scott / clevenger, John (2019): computer graphics programming in opengl with Java. Second Edition. Mercury learning and education: dulles, Virginia.
         */

	// values as read from OBJ file
    private ArrayList<Float> vertVals = new ArrayList<>();
    private ArrayList<Float> triangleVerts = new ArrayList<>();
    private ArrayList<Float> textureCoords = new ArrayList<>();

    // values stored for later use as vertex attributes
    private ArrayList<Float> stVals = new ArrayList<>();
    private ArrayList<Float> normals = new ArrayList<>();
    private ArrayList<Float> normVals = new ArrayList<>();

    public void parseOBJ(String filename) throws IOException
    {	InputStream input = ModelImporter.class.getResourceAsStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = br.readLine()) != null)
        {	if(line.startsWith("v "))			// vertex position ("v" case)
        {	for(String s : (line.substring(2)).split(" "))
        {	vertVals.add(Float.valueOf(s));
        }	}
        else if(line.startsWith("vt"))			// texture coordinates ("vt" case)
        {	for(String s : (line.substring(3)).split(" "))
        {	stVals.add(Float.valueOf(s));
        }	}
        else if(line.startsWith("vn"))			// vertex normals ("vn" case)
        {	for(String s : (line.substring(3)).split(" "))
        {	normVals.add(Float.valueOf(s));
        }	}
        else if(line.startsWith("f"))			// triangle faces ("f" case)
        {	for(String s : (line.substring(2)).split(" "))
        {	String v = s.split("/")[0];
            String vt = s.split("/")[1];
            String vn = s.split("/")[2];

            int vertRef = (Integer.valueOf(v)-1)*3;
            int tcRef   = (Integer.valueOf(vt)-1)*2;
            int normRef = (Integer.valueOf(vn)-1)*3;

            triangleVerts.add(vertVals.get(vertRef));
            triangleVerts.add(vertVals.get((vertRef)+1));
            triangleVerts.add(vertVals.get((vertRef)+2));

            textureCoords.add(stVals.get(tcRef));
            textureCoords.add(stVals.get(tcRef+1));

            normals.add(normVals.get(normRef));
            normals.add(normVals.get(normRef+1));
            normals.add(normVals.get(normRef+2));
        }	}	}
        input.close();
    }

    public int getNumVertices() { return (triangleVerts.size()/3); }

    public float[] getVertices()
    {	float[] p = new float[triangleVerts.size()];
        for(int i = 0; i < triangleVerts.size(); i++)
        {	p[i] = triangleVerts.get(i);
        }
        return p;
    }

    public float[] getTextureCoordinates()
    {	float[] t = new float[(textureCoords.size())];
        for(int i = 0; i < textureCoords.size(); i++)
        {	t[i] = textureCoords.get(i);
        }
        return t;
    }

    public float[] getNormals()
    {	float[] n = new float[(normals.size())];
        for(int i = 0; i < normals.size(); i++)
        {	n[i] = normals.get(i);
        }
        return n;
    }
}