/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Jordan
 * @description General purpose timeout class for operations that require cooldowns
 */
public class Timeout {

    private double lastTime;
    private double interval;
    private String debugName;

    public Timeout(double milliseconds) //give timeout length in millis
    {
        interval = milliseconds;
    }

    public Timeout(int milliseconds) {
        interval = (double) milliseconds;
    }
    
    public Timeout(double milliseconds, String name)
    {
        interval = milliseconds;
        debugName = name;
    }

    public boolean get() { //returns whether timeout is over
        double ret = Timer.getFPGATimestamp() - lastTime;
        if (debugName.equals("fireTO")){
            System.out.println("Time since last = " + ret);
            System.err.println(debugName + " Returned " + (ret > interval / 1000));
        }
        return ret > interval / 1000;
    }

    public void set() { //begin timeout
        lastTime = Timer.getFPGATimestamp();
    }
}
