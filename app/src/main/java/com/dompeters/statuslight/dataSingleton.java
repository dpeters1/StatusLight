package com.dompeters.statuslight;

/**
 * Created by Dominic on 2017-09-22.
 */

public class dataSingleton {
    public int ledMode = 1;
    public int maxBrightness = 100;
    public String ipAddr;
    private static final dataSingleton holder = new dataSingleton();

    public static dataSingleton getInstance() {return holder;}
}
