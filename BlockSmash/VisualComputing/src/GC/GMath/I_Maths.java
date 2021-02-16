package GC.GMath;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
import java.nio.FloatBuffer;

import static com.jogamp.opengl.GL.*;
/**
 * @Author: Patrick Pavlenko
 * @Version: 22.10.2019
 */
public abstract class I_Maths {


     /**
      * UniformID die auf die erstelle Uniform referenziert
      */
     protected int uniformID;
     /**
      * Wird benutzt um die Uniform zu erstellen
      * Werte der Matrix werden im matBuffer als FloatBuffer verpackt.
      */
     protected FloatBuffer matBuffer;


     /**
      * @param programID ID des Program Objektes,welcher die Shader hat
      * @param glslMatrixName der Variablenname in der GLSL Datei,welcher die Referenz kriegen soll
      */
     public void loadUniform(int programID,String glslMatrixName)
     {
          GL4 gl = (GL4) GLContext.getCurrentGL();
          uniformID = gl.glGetUniformLocation(programID,glslMatrixName);
     }


     /**
      * @param mat die Matrix / der Vektor welcher in ein FloatBuffer geladen werden soll
      * Speichert den floatbuffer in matBuffer Objektattribut ab
      * gebraucht in sendUniform Methoden
      */
     protected void setMatFloatBuffer(float[] mat)
     {
          GL4 gl = (GL4) GLContext.getCurrentGL();
          FloatBuffer buf = Buffers.newDirectFloatBuffer(mat);
          gl.glBufferData(GL_ARRAY_BUFFER,buf.limit()*4,buf,GL_STATIC_DRAW);
          matBuffer = buf;
     }

     /**
      * sendet 4x4 Matrix an glsl
      * @param mat die benoetigte 4x4 Matrix.Gleiche Matrixobjekt muss benutzt werden von ,wo sie aufgerufen wird!
      */
     public void sendUniformMat4f(Matrix4f mat)
     {
          setMatFloatBuffer(mat.getMatrix());
          GL4 gl = (GL4) GLContext.getCurrentGL();
          gl.glUniformMatrix4fv(uniformID,1,true,matBuffer);
     }

     /**
      * Matrix nicht transponiert von row major nach column major ( die inverse von column majaor  = row major)
      * @param mat
      */
     public void sendUniformMat4fTransposed(Matrix4f mat)
     {
          setMatFloatBuffer(mat.getMatrix());
          GL4 gl = (GL4) GLContext.getCurrentGL();
          gl.glUniformMatrix4fv(uniformID,1,false,matBuffer);
     }

     public FloatBuffer getMatBuffer() { return matBuffer; }
     public int getUniformID() { return uniformID; }
}
