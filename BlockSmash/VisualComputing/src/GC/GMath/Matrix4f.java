package GC.GMath;

import java.util.Arrays;

/**
 * @author Patrick Pavlenko,Dennis Liebelt
 * @version 10.11.2019
 */

public class Matrix4f extends I_Maths {


    float[] matrix = new float[16];

    public Matrix4f()
    {
        Arrays.fill(matrix,0); // Alle Werte in Matrix mit 0 gefuellt zum starten
    }

    public void identity() //Matrix wird zur Einheitsmatrix bzw initialisiert
    {
        matrix = new float[16];
        Arrays.fill(matrix,0);
        matrix[0] = 1;
        matrix[5] = 1;
        matrix[10] = 1;
        matrix[15] = 1;
    }

    /**
     * nach X rotieren
     * Werde der Transformationsmatrix werde dabei gesettet
     * @param rot radianten fuer Rotation
     */
    public void rotateX(float rot)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.getMatrix()[5] = (float)Math.cos(rot);
        matrix.getMatrix()[6] = (float)-Math.sin(rot);
        matrix.getMatrix()[9] = (float)Math.sin(rot);
        matrix.getMatrix()[10] = (float)Math.cos(rot);
        this.multM4f(matrix);
    }

    /**
     * nach y rotieren
     * Werde der Transformationsmatrix werde dabei gesettet
     * @param rot radianten fuer Rotation
     */
    public void rotateY(float rot)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.getMatrix()[0] = (float)Math.cos(rot);
        matrix.getMatrix()[2] = (float)Math.sin(rot);
        matrix.getMatrix()[8] = (float)-Math.sin(rot);
        matrix.getMatrix()[10] = (float)Math.cos(rot);
        this.multM4f(matrix);
    }

    /**
     * nach z rotieren
     * Werde der Transformationsmatrix werde dabei gesettet
     * @param rot radianten fuer Rotation
     */
    public void rotateZ(float rot)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.getMatrix()[0] = (float)Math.cos(rot);
        matrix.getMatrix()[1] = (float)-Math.sin(rot);
        matrix.getMatrix()[4] = (float)Math.sin(rot);
        matrix.getMatrix()[5] = (float)Math.cos(rot);
        this.multM4f(matrix);
    }

    /**
     * Multiplikation zweier Matrixen
     * @param mult die Matrix mit welcher multipliziert wird
     */
    public void multM4f(Matrix4f mult)
    {
        float[] matrixB = mult.getMatrix();
        float[] multMatrix = new float[16];
        multMatrix[0] = matrix[0] * matrixB[0] + matrix[1] * matrixB[4] + matrix[2] * matrixB[8] + matrix[3] * matrixB[12];
        multMatrix[1] = matrix[0] * matrixB[1] + matrix[1] * matrixB[5] + matrix[2] * matrixB[9] + matrix[3] * matrixB[13];
        multMatrix[2] = matrix[0] * matrixB[2] + matrix[1] * matrixB[6] + matrix[2] * matrixB[10] + matrix[3] * matrixB[14];
        multMatrix[3] = matrix[0] * matrixB[3] + matrix[1] * matrixB[7] + matrix[2] * matrixB[11] + matrix[3] * matrixB[15];
        multMatrix[4] = matrix[4] * matrixB[0] + matrix[5] * matrixB[4] + matrix[6] * matrixB[8] + matrix[7] * matrixB[12];
        multMatrix[5] = matrix[4] * matrixB[1] + matrix[5] * matrixB[5] + matrix[6] * matrixB[9] + matrix[7] * matrixB[13];
        multMatrix[6] = matrix[4] * matrixB[2] + matrix[5] * matrixB[6] + matrix[6] * matrixB[10] + matrix[7] * matrixB[14];
        multMatrix[7] = matrix[4] * matrixB[3] + matrix[5] * matrixB[7] + matrix[6] * matrixB[11] + matrix[7] * matrixB[15];
        multMatrix[8] = matrix[8] * matrixB[0] + matrix[9] * matrixB[4] + matrix[10] * matrixB[8] + matrix[11] * matrixB[12];
        multMatrix[9] = matrix[8] * matrixB[1] + matrix[9] * matrixB[5] + matrix[10] * matrixB[9] + matrix[11] * matrixB[13];
        multMatrix[10] = matrix[8] * matrixB[2] + matrix[9] * matrixB[6] + matrix[10] * matrixB[10] + matrix[11] * matrixB[14];
        multMatrix[11] = matrix[8] * matrixB[3] + matrix[9] * matrixB[7] + matrix[10] * matrixB[11] + matrix[11] * matrixB[15];
        multMatrix[12] = matrix[12] * matrixB[0] + matrix[13] * matrixB[4] + matrix[14] * matrixB[8] + matrix[15] * matrixB[12];
        multMatrix[13] = matrix[12] * matrixB[1] + matrix[13] * matrixB[5] + matrix[14] * matrixB[9] + matrix[15] * matrixB[13];
        multMatrix[14] = matrix[12] * matrixB[2] + matrix[13] * matrixB[6] + matrix[14] * matrixB[10] + matrix[15] * matrixB[14];
        multMatrix[15] = matrix[12] * matrixB[3] + matrix[13] * matrixB[7] + matrix[14] * matrixB[11] + matrix[15] * matrixB[15];
        matrix = multMatrix;
}

    /**
     * Translationswerte werden gesettet
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param z z-Koordinate
     */
    public void translate(float x,float y,float z)
    {
        matrix[3] = x;
        matrix[7] = y;
        matrix[11] = z;
    }

    // Uberschreibt Translationswerte mittels einen Vector3f
    public void translate(Vector3f vec)
    {
        matrix[3] = vec.getVector()[0];
        matrix[7] = vec.getVector()[1];
        matrix[11] = vec.getVector()[2];
    }

    /**
     * Werte des Parametervektor werden zur momentanen Translationskoordinaten der Matrix adddiert
     * @param vec der Vektor mit dem addiert wird
     */
    public void translateAdd(Vector3f vec)
    {
        matrix[3] = matrix[3]+vec.getVector()[0];
        matrix[7] = matrix[7]+vec.getVector()[1];
        matrix[11] = matrix[11]+vec.getVector()[2];
    }


    /**
     * Skalierungswerte der Matrix werden gesettet
     * @param sx x-Faktor der Skalierung
     * @param sy y-Faktor der Skalierung
     * @param sz z-Faktor der Skalierung
     */
    public void scale(float sx,float sy,float sz)
    {
        matrix[0] = sx;
        matrix[5] = sy;
        matrix[10] = sz;
    }

    /**
     * Wie die normale scale Methode nur dass noch mit einer Matrix multipliziert wird welche die Skalierungsfaktoren beinhaltet
     * @param sx
     * @param sy
     * @param sz
     */
    public void scaleSet(float sx,float sy,float sz)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.getMatrix()[0] = sx;
        matrix.getMatrix()[5] = sy;
        matrix.getMatrix()[10] = sz;
        this.multM4f(matrix);
    }




    /**
     * Determinate einer Matrix wird berechnet
     * @return die determinante als floatwert
     */
    private float determinante() {
        return matrix[12] * matrix[9] * matrix[6] * matrix[3]
                - matrix[8] * matrix[13] * matrix[6] * matrix[3]
                - matrix[12] * matrix[5] * matrix[10] * matrix[3]
                + matrix[4] * matrix[13] * matrix[10] * matrix[3]
                + matrix[8] * matrix[5] * matrix[14] * matrix[3]
                - matrix[4] * matrix[9] * matrix[14] * matrix[3]
                - matrix[12] * matrix[9] * matrix[2] * matrix[7]
                + matrix[8] * matrix[13] * matrix[2] * matrix[7]
                + matrix[12] * matrix[1] * matrix[10] * matrix[7]
                - matrix[0] * matrix[13] * matrix[10] * matrix[7]
                - matrix[8] * matrix[1] * matrix[14] * matrix[7]
                + matrix[0] * matrix[9] * matrix[14] * matrix[7]
                + matrix[12] * matrix[5] * matrix[2] * matrix[11]
                - matrix[4] * matrix[13] * matrix[2] * matrix[11]
                - matrix[12] * matrix[1] * matrix[6] * matrix[11]
                + matrix[0] * matrix[13] * matrix[6] * matrix[11]
                + matrix[4] * matrix[1] * matrix[14] * matrix[11]
                - matrix[0] * matrix[5] * matrix[14] * matrix[11]
                - matrix[8] * matrix[5] * matrix[2] * matrix[15]
                + matrix[4] * matrix[9] * matrix[2] * matrix[15]
                + matrix[8] * matrix[1] * matrix[6] * matrix[15]
                - matrix[0] * matrix[9] * matrix[6] * matrix[15]
                - matrix[4] * matrix[1] * matrix[10] * matrix[15]
                + matrix[0] * matrix[5] * matrix[10] * matrix[15];
    }


    /**
     * Inverse Matrix berechnen
     * return null falls det = 0 ansonsten float[] der inversen Matrix
     */
    public void inverse()
    {
        float det=determinante();
        float inverse[]= new float[16];
        if (det==0)
        {
            return;
        }
        else
        {
            inverse[0] = (-matrix[13] * matrix[10] * matrix[7] + matrix[9] * matrix[14] * matrix[7] + matrix[13] * matrix[6] * matrix[11]
                    - matrix[5] * matrix[14] * matrix[11] - matrix[9] * matrix[6] * matrix[15] + matrix[5] * matrix[10] * matrix[15]) / det;
            inverse[1] = (matrix[13] * matrix[10] * matrix[3] - matrix[9] * matrix[14] * matrix[3] - matrix[13] * matrix[2] * matrix[11]
                    + matrix[1] * matrix[14] * matrix[11] + matrix[9] * matrix[2] * matrix[15] - matrix[1] * matrix[10] * matrix[15]) / det;
            inverse[2] = (-matrix[13] * matrix[6] * matrix[3] + matrix[5] * matrix[14] * matrix[3] + matrix[13] * matrix[2] * matrix[7]
                    - matrix[1] * matrix[14] * matrix[7] - matrix[5] * matrix[2] * matrix[15] + matrix[1] * matrix[6] * matrix[15]) / det;
            inverse[3] = (matrix[9] * matrix[6] * matrix[3] - matrix[5] * matrix[10] * matrix[3] - matrix[9] * matrix[2] * matrix[7]
                    + matrix[1] * matrix[10] * matrix[7] + matrix[5] * matrix[2] * matrix[11] - matrix[1] * matrix[6] * matrix[11]) / det;
            inverse[4] = (matrix[12] * matrix[10] * matrix[7] - matrix[8] * matrix[14] * matrix[7] - matrix[12] * matrix[6] * matrix[11]
                    + matrix[4] * matrix[14] * matrix[11] + matrix[8] * matrix[6] * matrix[15] - matrix[4] * matrix[10] * matrix[15]) / det;
            inverse[5] = (-matrix[12] * matrix[10] * matrix[3] + matrix[8] * matrix[14] * matrix[3] + matrix[12] * matrix[2] * matrix[11]
                    - matrix[0] * matrix[14] * matrix[11] - matrix[8] * matrix[2] * matrix[15] + matrix[0] * matrix[10] * matrix[15]) / det;
            inverse[6] = (matrix[12] * matrix[6] * matrix[3] - matrix[4] * matrix[14] * matrix[3] - matrix[12] * matrix[2] * matrix[7]
                    + matrix[0] * matrix[14] * matrix[7] + matrix[4] * matrix[2] * matrix[15] - matrix[0] * matrix[6] * matrix[15]) / det;
            inverse[7] = (-matrix[8] * matrix[6] * matrix[3] + matrix[4] * matrix[10] * matrix[3] + matrix[8] * matrix[2] * matrix[7]
                    - matrix[0] * matrix[10] * matrix[7] - matrix[4] * matrix[2] * matrix[11] + matrix[0] * matrix[6] * matrix[11]) / det;
            inverse[8] = (-matrix[12] * matrix[9] * matrix[7] + matrix[8] * matrix[13] * matrix[7] + matrix[12] * matrix[5] * matrix[11]
                    - matrix[4] * matrix[13] * matrix[11] - matrix[8] * matrix[5] * matrix[15] + matrix[4] * matrix[9] * matrix[15]) / det;
            inverse[9] = (matrix[12] * matrix[9] * matrix[3] - matrix[8] * matrix[13] * matrix[3] - matrix[12] * matrix[1] * matrix[11]
                    + matrix[0] * matrix[13] * matrix[11] + matrix[8] * matrix[1] * matrix[15] - matrix[0] * matrix[9] * matrix[15]) / det;
            inverse[10] = (-matrix[12] * matrix[5] * matrix[3] + matrix[4] * matrix[13] * matrix[3] + matrix[12] * matrix[1] * matrix[7]
                    - matrix[0] * matrix[13] * matrix[7] - matrix[4] * matrix[1] * matrix[15] + matrix[0] * matrix[5] * matrix[15]) / det;
            inverse[11] = (matrix[8] * matrix[5] * matrix[3] - matrix[4] * matrix[9] * matrix[3] - matrix[8] * matrix[1] * matrix[7]
                    + matrix[0] * matrix[9] * matrix[7] + matrix[4] * matrix[1] * matrix[11] - matrix[0] * matrix[5] * matrix[11]) / det;
            inverse[12] = (matrix[12] * matrix[9] * matrix[6] - matrix[8] * matrix[13] * matrix[6] - matrix[12] * matrix[5] * matrix[10]
                    + matrix[4] * matrix[13] * matrix[10] + matrix[8] * matrix[5] * matrix[14] - matrix[4] * matrix[9] * matrix[14]) / det;
            inverse[13] = (-matrix[12] * matrix[9] * matrix[2] + matrix[8] * matrix[13] * matrix[2] + matrix[12] * matrix[1] * matrix[10]
                    - matrix[0] * matrix[13] * matrix[10] - matrix[8] * matrix[1] * matrix[14] + matrix[0] * matrix[9] * matrix[14]) / det;
            inverse[14] = (matrix[12] * matrix[5] * matrix[2] - matrix[4] * matrix[13] * matrix[2] - matrix[12] * matrix[1] * matrix[6]
                    + matrix[0] * matrix[13] * matrix[6] + matrix[4] * matrix[1] * matrix[14] - matrix[0] * matrix[5] * matrix[14]) / det;
            inverse[15] = (-matrix[8] * matrix[5] * matrix[2] + matrix[4] * matrix[9] * matrix[2] + matrix[8] * matrix[1] * matrix[6]
                    - matrix[0] * matrix[9] * matrix[6] - matrix[4] * matrix[1] * matrix[10] + matrix[0] * matrix[5] * matrix[10]) / det;
        }
        matrix =  inverse;
    }

    /**
     * Übertraegt die Werte einer Matrix in die anddere
     * @param mat die Matrix,welche Ubertragen wird
     */
    public void transferValues(Matrix4f mat)
    {
        for(int x = 0;x<=15; x++) { matrix[x] = mat.getMatrix()[x]; }
    }


    public float[] getMatrix() { return matrix; }

}
