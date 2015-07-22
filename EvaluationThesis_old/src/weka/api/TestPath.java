package weka.api;

import java.net.URL;

/**
 * Created by Giovanni Liva on 20/07/2015.
 */
public class TestPath {
    public static void main(String[] args) {
        URL location = TestPath.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println("PATH:" + location.getPath());
    }
}
