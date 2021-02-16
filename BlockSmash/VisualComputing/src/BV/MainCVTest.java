package BV;

import org.opencv.core.Core;

/**
 * Main-Klasse zum Laden der "VideoProcessingTest"-Klasse und deren Bildverarbeitungsoperationen.
 *
 * @author David Waelsch
 * @version 15.11.2019
 */
public class MainCVTest {

    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new VideoProcessingTest();
    }
}
