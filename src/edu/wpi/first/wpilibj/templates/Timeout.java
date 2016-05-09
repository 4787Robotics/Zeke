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
 * @description General purpose timeout class for operations that require
 */
public class Timeout {

    private double lastTime;
    private double interval;

    public Timeout(double milliseconds) //give timeout length in millis
    {
        interval = milliseconds;
    }

    public Timeout(int milliseconds) {
        interval = (double) milliseconds;
    }

    public boolean get() {
        return Timer.getFPGATimestamp() - lastTime > interval / 1000;
    }

    public void set() {
        lastTime = Timer.getFPGATimestamp();
    }
}
