import org.opencv.core.Core;
/**
 * @author Patrick Pavlenko
 * @version 14.10.2019
 * Programm laeuft mit 50 FPS
 * Starter Klasse fuehrt das ganze Programmkonstrukt aus bei Initialisierung der Starter Klasse
 */
public class Main {

    public static void main(String[] args)
    {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
            Starter start = new Starter();

    }
}
