/*----------------------------------------------------------------------------*/
 /* Copyright (c) FIRST 2008. All Rights Reserved.                             */
 /* Open Source Software - may be modified and shared by FRC teams. The code   */
 /* must be accompanied by the FIRST BSD license file in the root directory of */
 /* the project.                                                               */
 /*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.SerialPort;
import java.lang.Math;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Zeke extends SimpleRobot {

    // Variable initializations 
    RobotDrive drive;
    Joystick drivestick;
    DigitalOutput solenoidA;
    DigitalOutput solenoidB;
    SerialPort serial;
    Timeout fireTO;
    Timeout solenoidTO;
    Timeout testTO;

    // Joystick constants
    final int DRIVESTICK_USB = 1;
    int motorSwitch = 0;
    final double DEADZONEY = .05;
    final double DEADZONEX = .05;

    // Motor constants
    final int FRONT_LEFT_MOTOR_PWM = 1;
    final int REAR_LEFT_MOTOR_PWM = 2;
    final int FRONT_RIGHT_MOTOR_PWM = 6;
    final int REAR_RIGHT_MOTOR_PWM = 5;
    final int SOLENOID_A_CHNL = 1;
    final int SOLENOID_B_CHNL = 2;
    final int TRIGGER_BTN = 1;
    final double TEST_TIMEOUT = 500;    
    final double SOLENOID_WINDOW = 400;
    final double FIRE_TIMEOUT = 1000;

    boolean whichSolenoid = true;
    boolean hasFiredThisLoop = true;

    double x, y, expX, expY;

    // Motor controller configurations
    Jaguar frontLeft = new Jaguar(FRONT_LEFT_MOTOR_PWM);
    Jaguar rearLeft = new Jaguar(REAR_LEFT_MOTOR_PWM);
    Jaguar frontRight = new Jaguar(FRONT_RIGHT_MOTOR_PWM);
    Jaguar rearRight = new Jaguar(REAR_RIGHT_MOTOR_PWM);
    Jaguar[] motorList = {frontLeft, rearLeft, frontRight, rearRight};

    public Zeke() {
        drivestick = new Joystick(DRIVESTICK_USB);
        solenoidA = new DigitalOutput(SOLENOID_A_CHNL);
        solenoidB = new DigitalOutput(SOLENOID_B_CHNL);

        fireTO = new Timeout(FIRE_TIMEOUT);
        solenoidTO = new Timeout(SOLENOID_WINDOW);
        testTO = new Timeout(TEST_TIMEOUT);
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() { //No AI pod.. yet
        System.out.println("No autonomous mode implemented");
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
            y = drivestick.getY();
            x = drivestick.getX();

            expX = 0;
            expY = 0;
            if (Math.abs(x) > DEADZONEX) {
                expX = x * Math.abs(x);
                System.out.println(expX);
            } else {
                expX = 0;
            }
            if (Math.abs(y) > DEADZONEY) {
                expY = y;
                System.out.println(expX);
            } else {
                expY = 0;
            }

            frontLeft.set(expX - expY);
            rearLeft.set(expX - expY);
            frontRight.set(expX + expY);
            rearRight.set(expX + expY);    

            if (drivestick.getRawButton(1)) //ZEKE's nuclear arming system; both drivers must pull the trigger to fire
            {
                if (drivestick.getRawButton(5) && fireTO.get()) {
                    solenoidA.set(true);
                    fireTO.set();
                    solenoidTO.set();
                    System.out.println("Firing A");                    
                } else if (drivestick.getRawButton(3) && fireTO.get()) {
                    solenoidB.set(true);
                    fireTO.set();
                    solenoidTO.set();
                    System.out.println("Firing B");                    
                }
                if (solenoidTO.get()) {
                    solenoidA.set(false);
                    solenoidB.set(false);
                }

            }
            Timer.delay(0.005); //5ms
        }
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
        while (isTest() && isEnabled()) {
            y = drivestick.getY();
            if (drivestick.getRawButton(TRIGGER_BTN)) {
                if (testTO.get()) {
                    motorList[motorSwitch].set(0);                    
                    motorSwitch = (motorSwitch + 1) % motorList.length;
                    testTO.set();
                    System.out.println("Test Motor: " + motorSwitch);
                }
            }

            if (Math.abs(y) > DEADZONEY) {
                motorList[motorSwitch].set(y);
            }
        }
    }

    public void disabled() {
        solenoidA.set(false);
        solenoidB.set(false);
    }

}
//hold button 3 for bottom barrel
//hold button 2 for top barrel
//press trigger while holding one of those to fire
//solenoid should only open for 50 ms (configurable by perhaps the axis on the base of the joystick, different times open = diff power)

