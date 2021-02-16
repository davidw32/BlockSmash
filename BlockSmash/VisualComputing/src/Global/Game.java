package Global;
import BV.VideoProcessing;
import GC.GMath.Vector3f;
import GC.GObject.GraphicObject;
import GC.GObject.Primitives.Block;
import GC.GObject.Primitives.Cursor;
import GC.GObject.Primitives.ScorePoints;
import GC.GObject.Subsets.Material.MaterialColor;
import GC.GObject.Visuals;
import GC.Reader.ShaderReader;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.jogamp.opengl.GL.*;
/**
 * @author Patrick Pavlenko & David Waelsch
 * @version 06.11.2019
 * Hier wird die Spielmechanik konstruiert
 * Unter diesen Aspekten fallen:
 * -Zeichen von Elemente ( Buffer laden,aktivieren...)
 * -Animieren bzw bewegen von Elemente
 * Spielverlauf
 */
public class Game {

    private LinkedList<GraphicObject> objects = new LinkedList<>();
    private LinkedList<GraphicObject> objectContainer = new LinkedList<>();
    private ArrayList<Vector3f> paths = new ArrayList<>();
    private ArrayList<Vector3f> pathsEnd = new ArrayList<>();
    /** Translationswerte der Bloecke,Startpunkte der Bloecke */
    private Vector3f pathPosLeft = new Vector3f(-2f,3f,-100f);
    private Vector3f pathPosMid = new Vector3f(0f,3f,-100f);
    private Vector3f pathPosRight = new Vector3f(2f,3f,-100f);
    /** unterer Endpunkt in der linken Seite des Bildschirmes */
    private Vector3f endPosLeft = new Vector3f(0.35f,2.6f,9f);
    /** unterer Endpunkt in der mitte des Bildschirmes */
    private Vector3f endPosMid = new Vector3f(0.75f,2.6f,9f);
    /** unterer Endpunkt in der rechten Seite des Bildschirmes */
    private Vector3f endPosRight = new Vector3f(1.15f,2.6f,9f);
    /** unterer Endpunkt oben links im Bildschirm */
    private Vector3f endPosHighLeft = new Vector3f(0.4f,2.97f,9f);
    /** unterer Endpunkt oben rechts im Bildschirmes */
    private Vector3f endPosHighRight = new Vector3f(1.25f,2.8f,9f);

    private float[] placeholder = new float[]{0f,0f,0f};
    private Cursor cursorLeft = new Cursor(0f,3.5f,9,"Objects/Cursor.obj");
    private Cursor cursorRight = new Cursor(1f,3f,9,"Objects/Cursor.obj");
    private VideoProcessing vidProc;
    /** hud welche aus GLGame referenziert wird. initlaisiert bei Game Initialisierung */
    private HUD hud;
    /** "Schnelligkeit" bzw. inkremierung der Bloecke. dient als inkremierungswert des t Parameters einer Geradengleichung */
    private float moveIterator = 0.003f;
    private float saveMoveIterator = 0.003f;
    private int countIteratorTime = 0;
    private User user = new User(0,3);
    /** Punktanzeige */
    ScorePoints score;
    /** wird aktiv,sobald Bombenfähigkeit aktiviert wird,um weiteres hinzufuegen von Bloeckn zu blockieren */
    private boolean bombMode = false;

    private Program blockProgram = new Program(
            ShaderReader.readShaderFile("Shader/Vertex/vertexBlock.glsl"),
            ShaderReader.readShaderFile("Shader/Fragment/fragBlock.glsl"));

    /** Wenn Bloeck zerstoert wird dann wird dieser Shader benutzt */
    private Program blockProgram_destroyed = new Program(
            ShaderReader.readShaderFile("Shader/Vertex/vertexBlockDestroyed.glsl"),
            ShaderReader.readShaderFile("Shader/Fragment/fragBlockDestroyed.glsl"),
            ShaderReader.readShaderFile("Shader/Geometry/blockGeometry.glsl"));

    /** Programshader der beiden Cursor  */
    private Program cursorProgram = new Program(
            ShaderReader.readShaderFile("Shader/Vertex/vertexCursor.glsl"),
            ShaderReader.readShaderFile("Shader/Fragment/fragCursor.glsl"));

    /** Alle visuellen Objekte des Spieles */
    private Visuals backgroundObj;
    private GraphicObject previousBlock = new Block(0,0,0);
    /** der bereits gewandderte Pfad des neusten Objektes von ( 0 bis 1) */
    private float spawnInterval = 0.2f;


    /**
     * Initialisierung aller GrapicObjects
     * @param vidProc Kameradetektion
     * @param hud die HUD bzw Lebensnanzeigen,Faehigkeitsanzeige
     */
    public Game(VideoProcessing vidProc,HUD hud)
    {
        this.hud=hud;
        this.vidProc = vidProc;
        cursorLeft.setProgram(cursorProgram);
        cursorRight.setProgram(cursorProgram);
        cursorLeft.generateBuffers();
        cursorLeft.loadVBOBuffer(0,cursorLeft.getVertex(),3);
        cursorLeft.pauseVAO();
        cursorRight.generateBuffers();
        cursorRight.loadVBOBuffer(0, cursorRight.getVertex(),3);
        cursorRight.pauseVAO();

        previousBlock.setIterator(0.51f);
        paths.add(pathPosLeft);
        paths.add(pathPosMid);
        paths.add(pathPosRight);
        pathsEnd.add(endPosLeft);
        pathsEnd.add(endPosMid);
        pathsEnd.add(endPosRight);
        pathsEnd.add(endPosHighLeft);
        pathsEnd.add(endPosHighRight);
        backgroundObj = new Visuals(paths,pathsEnd);
        for(int x = 0; x < 10; x++)
        {
            Block block = new Block(0,0,0,"Objects/Box._high.obj",
                    MaterialColor.BRONZE_AMBIENT,MaterialColor.BRONZE_DIFFUSE,MaterialColor.BRONZE_SPECUCLAR,MaterialColor.BRONZE_SHININESS);
            block.getModelMatrix().scaleSet(0.6f,0.35f,0.6f);
            block.setProgram(blockProgram);
            block.generateBuffers();
            block.loadVBOBuffer(0,block.getVertex(),3);
            block.loadVBOBuffer(1,block.getTexel(),2);
            block.loadVBOBuffer(2,block.getTexel(),3);
            block.loadVBOBuffer(3,placeholder,3);
            objectContainer.add(block);
            block.pauseVAO();
        }
    }


    /**
     * Objekte werden gezeichnet
     * Zufaelliges Objekt wird gewaehlt aus einer Liste und wird dann in der Iterationsliste hinzugefuegt
     */
    public void checkSpawnedObjects()
    {
        //Diese Abfrage wird nur gemacht,wenn die Bombe aktiviert wird.
        //Sobald die Zeichnungslinked List leer ist wird es gestattet neue Objekte rein zu tun,nach dem Geometry Shader effekt
        if(bombMode && objects.size() == 0)
        {
            for(GraphicObject obj : objectContainer)
            {
                obj.setIterator(0);
            }
            bombMode = false;
            previousBlock = new Block(0,0,0);
            previousBlock.setIterator(0);
        }
        // Wenn weniger als 10 Objekte auf dem Spielfeld sind
        if(objects.size() < 10 && !bombMode)
        {

            int choosenObj = (int)((Math.random() * (((objectContainer.size()-1) - 1) + 1)) + 1);
            //Falls keine Objekte bis jetzt gezeichnet werden bzw die LinkedList dafuer leer ist
            if(!objectContainer.get(choosenObj).isSpawned() && objects.size() == 0)
            {
                //Zufaelliger Start + Endpunkt waehlen
                int numStart = (int)(Math.random() * paths.size());
                int numEnd = (int)(Math.random() * pathsEnd.size());
                objectContainer.get(choosenObj).setSpawned(true);
                objectContainer.get(choosenObj).setStartPos(paths.get(numStart));
                objectContainer.get(choosenObj).setEndPos(pathsEnd.get(numEnd));
                // In zu zeichnende Liste einfuegen
                objects.add(objectContainer.get(choosenObj));
                previousBlock = objectContainer.get(choosenObj);
                return;
            }
            // dann zufaelliges Objekt nehmen,was nicht gespawnt ist & Vorherige Objekt hat bestimmte Anteil an Pfads sich nach vorn bewegt
            if(!objectContainer.get(choosenObj).isSpawned() && previousBlock.getIterator() > spawnInterval)
            {
                //Zufaelliger Start + Endpunkt waehlen
                int numStart = (int)(Math.random() * paths.size());
                int numEnd = (int)(Math.random() * pathsEnd.size());
                objectContainer.get(choosenObj).setSpawned(true);
                objectContainer.get(choosenObj).setStartPos(paths.get(numStart));
                objectContainer.get(choosenObj).setEndPos(pathsEnd.get(numEnd));
                // In zu zeichnende Liste einfuegen
                objects.add(objectContainer.get(choosenObj));
                previousBlock = objectContainer.get(choosenObj);
            }
        }
    }

    /**
     * Geradengleichung wird gebildet um Objekt von bestimmten Pfad zu einen Endpos zu bringen
     * (1-t)* p1 + t * p2  (Geradengleichungen)
     * @param t iterator
     * @param p1 startpunkt
     * @param p2 endpunkt
     */
    public Vector3f iterate(float t,Vector3f p1,Vector3f p2)
    {
        // (1-t)* p1 + t * p2  (Geradengleichungen)
        Vector3f vec = p1.multOut(1-t); // (1-t) * p1
        Vector3f vec2 = p2.multOut(t);  // t* p2
        return vec.addOut(vec2);        // vec + vec2
    }

    /**
     * iteriert alle Bloecke in der objects LinkedList und benutzt die Geradengleichung
     * um das momentane Blockelement zu transformieren ( momentan nur translation)
     */
    public void moveBlock()
    {
        if(moveIterator < 0.009f && !bombMode && countIteratorTime == 300)
        {
            moveIterator += 0.0005;
            countIteratorTime = 0;
        }
        countIteratorTime++;
        ArrayList<GraphicObject> save = new ArrayList<>(); // Um Index abzuspeichern der zu loeschenden Elemente (Fehlervermeidung von ConcurrentException)
        for(GraphicObject block : objects)
        {
            // Wenn Objekt und Cursor Kollidiert ist
            if(block.isDestroyed())
            {
                // Wenn wahr, dann aufhören entlang Y zu translatieren und aus LinkedList raus tun
                //Objekt komplett zurückgesetzt
                if(block.getAnimFactor().getVertex() > 20f)
                {
                    block.setSpawned(false);
                    //Abspeichern der zu entfernenden Bloecke (Nullpointervermeidung)
                    save.add(block);
                    block.setIterator(0);
                    block.getAnimFactor().setVertex(0);
                    block.setDestroyed(false);

                    continue;
                }
                Vector3f translator = new Vector3f(0,0.001f,0);
                block.getModelMatrix().translateAdd(translator);
                block.iterateAnimFac(0.5f); // Iterierung von Animation (Ausbreiten Objekt)
                continue;
            }
            // Abfragen ob Objekt ziel erreicht hat
            // Wenn ja,dann entfernen aus der zu zeichnenden LinkedList
            if(block.getIterator() > 1.0f)
            {
                save.add(block);
                block.setIterator(0);
                block.setSpawned(false);
                user.loseLife();
                continue;
            }
            //Iteration der Translation von Geradenlinie
            Vector3f translator = iterate(block.getIterator(),block.getStartPos(),block.getEndPos());
            block.getModelMatrix().translate(translator);
            block.setIterator((float)(block.getIterator()+moveIterator));
            // Kollisionsabfrage
            if(block.getIterator() > 0.99 && checkCollision(block)) { return; }
        }
        for(GraphicObject blockIndex : save)
        {
            objects.remove(blockIndex);
        }
    }

    /**
     * fuer das Uhr powerup
     * dient dazu um die Objekte langsamer translatieren zu lassen ( Verlangsamungseffekt)
     * @param speed
     */
    public void changeBlockSpeed(float speed) { moveIterator = speed; }


    /**
     * block Objekte innheralb objects LinkedList werden gezeichnet
     * @param cam gebraucht fuer ModelViewMatrix erstellung
     */
    public void spawnBlocks(Camera cam)
    {
        checkSpawnedObjects();
        moveBlock();
        GL4 gl = (GL4) GLContext.getCurrentGL();
        for(GraphicObject block : objects)
        {
                if(block.isDestroyed()) {
                    gl.glUseProgram(blockProgram_destroyed.getProgram_ID());
                    gl.glPointSize(5);
                    block.loadVAO();
                    block.createMvMat(cam.getViewMatrix());
                    block.createInvTrMat();
                    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

                    block.getInverseTransMV().loadUniform(blockProgram_destroyed.getProgram_ID(),"invMat");
                    block.getModelViewMatrix().loadUniform(blockProgram_destroyed.getProgram_ID(),"mvMat");
                    block.getAnimFactor().loadUniform(blockProgram_destroyed.getProgram_ID(),"explodeFac");
                    cam.getProjMatrix().loadUniform(blockProgram_destroyed.getProgram_ID(),"projMat");

                    block.getModelViewMatrix().sendUniformMat4f(block.getModelViewMatrix());
                    cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());
                    block.getAnimFactor().sendUniformVec1f();
                    block.getInverseTransMV().sendUniformMat4fTransposed(block.getInverseTransMV());

                    block.drawObject();
                    block.pauseVAO();
                }
                else
                    {
                        gl.glUseProgram(block.getProgram().getProgram_ID());
                        block.loadVAO();
                        block.createMvMat(cam.getViewMatrix());
                        block.createInvTrMat();

                        block.getInverseTransMV().loadUniform(block.getProgram().getProgram_ID(),"invMat");
                        block.loadShading(cam.getViewMatrix(),backgroundObj.getLight());

                        block.getModelViewMatrix().loadUniform(block.getProgram().getProgram_ID(),"mvMat");
                        cam.getProjMatrix().loadUniform(block.getProgram().getProgram_ID(),"projMat");

                        block.getModelViewMatrix().sendUniformMat4f(block.getModelViewMatrix());
                        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());

                        block.getInverseTransMV().sendUniformMat4fTransposed(block.getInverseTransMV());
                        block.activateShading(backgroundObj.getLight());

                        block.drawObject();
                        block.pauseVAO();
                    }
        }
    }

    private void processVideo(){ vidProc.handDetection(user); }

    /**
     * Einzelner Cursor wird gezeichnet
     * @param cam Kamera des Eventlisteners
     * @param cursor der Cursor,welcher gezeichnet werden soll
     */
    private void spawnSingleCursor(Camera cam,Cursor cursor)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        cursor.createMvMat(cam.getViewMatrix());

        cursor.getModelViewMatrix().loadUniform(cursorProgram.getProgram_ID(),"mvMat");
        cam.getProjMatrix().loadUniform(cursorProgram.getProgram_ID(),"projMat");

        cursor.getModelViewMatrix().sendUniformMat4f(cursor.getModelViewMatrix());
        cam.getProjMatrix().sendUniformMat4f(cam.getProjMatrix());

        cursor.drawObject();
    }


    /**
     * spawnSingleCursor wird hier fuer beide Cursor angewendet
     * Vor der Zeichnung werden die Cursor translatiert zu der Position der Haende,welche in der Kamera erkannt wird
     * @param cam die Kamera des Eventlisteners
     */
    public void spawnCursor(Camera cam)
    {
        processVideo();
        cursorLeft.getModelMatrix().translate((float)vidProc.getNormedCenterLeft().x+0.75f,(float)vidProc.getNormedCenterLeft().y+3f,9);
        cursorRight.getModelMatrix().translate((float)vidProc.getNormedCenterRight().x+0.75f,(float)vidProc.getNormedCenterRight().y+3f,9);
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUseProgram(cursorProgram.getProgram_ID());
        cursorRight.loadVAO();
        spawnSingleCursor(cam, cursorRight);
        cursorLeft.loadVAO();
        spawnSingleCursor(cam,cursorLeft);
        gl.glBindVertexArray(0);
    }

    /** Kollisionsabfrage der Bloecke falls Block auf Cursor antrifft */
    private boolean checkCollision(GraphicObject block)
    {
        float[]  bMat = block.getModelMatrix().getMatrix();
        float[] cL = cursorLeft.getModelMatrix().getMatrix();
        float[] cR = cursorRight.getModelMatrix().getMatrix();
        // Y-Achsenvergleich Block vs left Cursor( Skalierungen der Y-Achse werden dabei mit beachtet)
        // Cursor obere Bereich groesser als untere Blockbereich && obere Blockbereich kleiner als untere Cursorbereich
        if(block.getIterator() > 0.975)
        {
            if(bMat[7]-bMat[5]/2 < cL[7]+cL[5]/2 && bMat[7]+bMat[5]/2 > cL[7])
            {
                //X-Achsenvergleich
                if(bMat[3]-bMat[0]/2 < cL[3]+cL[0]/2 && bMat[3]+bMat[0]/2 > cL[3]-cL[0]/2)
                {
                    block.setIterator(0);
                    block.setDestroyed(true);
                    score.addPoints(100);
                    return true;
                }
            }
            // Y-Achsenvergleich Block vs right Cursor( Skalierungen der Y-Achse werden dabei mit beachtet)
            // Cursor obere Bereich groesser als untere Blockbereich && obere Blockbereich kleiner als untere Cursorbereich
            if(bMat[7]-bMat[5]/2 < cR[7]+cR[5]/2 && bMat[7]+bMat[5]/2 > cR[7])
            {
                //X-Achsenvergleich
                if(bMat[3]-bMat[0]/2 < cR[3]+cR[0]/2 && bMat[3]+bMat[0]/2 > cR[3]-cR[0]/2)
                {
                    block.setIterator(0);
                    block.setDestroyed(true);
                    score.addPoints(100);
                    return true;
                }
            }
            return false;
        }
        return false;
        }

    /**
     * Tesselierte Flaeche,Cubemap gezeichnet
     * @param cam Viewmatrix der Kamera wird genutzt um MV Matrizen zu erstellen
     */
    public void spawnBackgroundObjs(Camera cam)
    {
        backgroundObj.spawnGridSurface(cam);
     //   backgroundObj.spawnDots(cam);
        backgroundObj.spawnBackground(cam);
    }

    /**  Die HUD (Lebensanzeige, Spezialfaehigkeiten) werden hier initialisiert bzw geladen */
    public void spawnhud()
    {
        hud.spawnHeart();
        hud.spawnBomb();
        hud.spawnStar();
        hud.spawnUhr();
    }

    /**  Hier wird die HUD gezeichnet*/
    public  void drawhud()
    {
        hud.drawHeart(user.getLife());
        hud.drawStar();
        hud.drawBomb();
        hud.drawUhr();
    }

    /**Uberprueft Fingeranzahl die in der Kamera zu sehen sind
     * Fingeranzahl bestimmt die Aktivierung des Powerups
     */
    public void checkFinger()
    {
        switch(user.getFingers()) // falls Finger erkannt wird
        {
            case 1:
                if(hud.isBombSpawned()) // Wenn Bombe noch aktiviert werden kann
                {
                    hud.setBombSpawned(false);  // Alle Bloecke aus Objektcontainer entfernt um "komplette" zerstoerung zu simluieren
                    for(GraphicObject block : objects)
                    {
                        block.setIterator(0);
                        block.setDestroyed(true);
                    }
                    bombMode = true;
                }
                break;
            case 2:
                if(hud.isUhrSpawned()) // Wenn Uhr noch aktiviert werden kann
                {
                    hud.setUhrSpawned(false);  // Bloecke bewegen sich langsamer (geringerer Translationswert)
                    saveMoveIterator = moveIterator;
                    changeBlockSpeed(0.0015f);
                }
                break;
            case 3:
                if(hud.isStarSpawned()) // Wenn Star noch aktiviert werden kann
                {
                    hud.setStarSpawned(false); // Dann fuehr diesen Block aus
                    score.setAdd(2);
                }
                break;
            default:  user.getFingers();
        }
        if(!hud.isUhrSpawned() && hud.getUhrTimer() <= 300) // Falls Uhr aktiv ist wird Timer pro frame Iteriert ( 300 = 6 sek)
        {
            if(hud.getUhrTimer() == 300)  moveIterator = saveMoveIterator;
            hud.uhrTimerIterate();
        }
        if(!hud.isStarSpawned() && hud.getStarTimer() <= 300) // Falls Star aktiv ist wird Timer pro frame Iteriert ( 300 = 6 sek)
        {
            // Punktiteration in ihren normale Zustand gesetzt nach 6 sekunden
             if(hud.getStarTimer() == 300) score.setAdd(1);
             hud.starTimerIterate();
        }
    }


    /**
     * Loescht alle VBO,VAO,Programs dieser Klasse
     */
    public void deleteAllBuffer()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        cursorRight.deleteBuffers();
        cursorLeft.deleteBuffers();
        backgroundObj.deleteAllVisBuffers();
        for(GraphicObject block : objectContainer )
        {
            block.deleteBuffers();
        }

        gl.glDeleteProgram(blockProgram.getProgram_ID());
        gl.glDeleteProgram(blockProgram_destroyed.getProgram_ID());
        hud.deleteAllHUDBuffers();
    }

    public int getLife() { return user.getLife(); }

    public void setScore(ScorePoints score) { this.score = score; }

    /**
     * Die Klasse resettet bestimmte Werte,um einen Spielneustart zu ermoeglichen
     */
    public void reset()
    {
        for(GraphicObject obj : objectContainer)
        {
            obj.setIterator(0);
            obj.setSpawned(false);
            obj.setDestroyed(false);
        }
        moveIterator = 0.003f;
        user.reset();
        objects.clear();
    }
}
