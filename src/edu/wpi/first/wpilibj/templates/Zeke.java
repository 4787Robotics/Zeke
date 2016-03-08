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
    Joystick mechstick;
    DigitalOutput solenoidA;
    DigitalOutput solenoidB;
    
    
    // Joystick constants
    final int DRIVESTICK_USB = 1;
    final int MECHSTICK_USB = 2;
    int motorSwitch = 0;
    final double solenoidwindow = .05; //50ms, maybe will be variable later on
    final double DEADZONEY = .05;
    final double DEADZONEX = .05;
    double lastTime; //for test method
    
    // Motor constants
    final int FRONT_LEFT_MOTOR_PWM = 1;
    final int REAR_LEFT_MOTOR_PWM = 2;
    final int FRONT_RIGHT_MOTOR_PWM = 4;
    final int REAR_RIGHT_MOTOR_PWM = 3;
    final int SOLENOID_A_CHNL = 1;
    final int SOLENOID_B_CHNL = 2;
    final int TRIGGER_BTN = 1;
    final double TEST_TIMEOUT = .5;
    
    double x, y, expX, expY;
    
    // Motor controller configurations
    Jaguar frontLeft = new Jaguar(FRONT_LEFT_MOTOR_PWM);
    Jaguar rearLeft = new Jaguar(REAR_LEFT_MOTOR_PWM);
    Jaguar frontRight = new Jaguar(FRONT_RIGHT_MOTOR_PWM);
    Jaguar rearRight = new Jaguar(REAR_RIGHT_MOTOR_PWM);
    Jaguar[] motorList = {frontLeft, rearLeft, frontRight, rearRight};

    public Zeke() {
        //drive = new RobotDrive(frontRight, rearRight, frontLeft, rearLeft); //this is wrong
        drivestick = new Joystick(DRIVESTICK_USB);
        mechstick = new Joystick(MECHSTICK_USB);
        solenoidA = new DigitalOutput(SOLENOID_A_CHNL);
        solenoidB = new DigitalOutput(SOLENOID_B_CHNL);
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() { //No AI pod.. yet

    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
            //if(Math.abs(drivestick.getX()) > DEADZONE && Math.abs(drivestick.getY()) > DEADZONE)
            
             
            y = drivestick.getY();
            x = drivestick.getX();
            
            expX = 0;
            expY = 0;
            if(Math.abs(x)>DEADZONEX) 
                expX = x*x;
            else{
                expX=0;
            }
            if(Math.abs(y)>DEADZONEY)
                expY = y;
            else{
                expY=0;
            }
           
            frontLeft.set(expX - expY);
            rearLeft.set(expX - expY);
            frontRight.set(expX + expY);
            rearRight.set(expX + expY);
//            System.out.println(y + " : " + x);

            /*if (mechstick.getRawButton(1) || drivestick.getRawButton(1)) //ZEKE's nuclear arming system; both drivers must pull the trigger to fire
            {
                solenoidA.set(true);
//                solenoidB.set(true);
                Timer.delay(0.25); //del
                solenoidA.set(false);
//                solenoidB.set(false); //on, wait 20ms, off
                while (mechstick.getRawButton(1) || drivestick.getRawButton(1)) {Timer.delay(0.02);}
                Timer.delay(1); //delay 1 second for refilling in accumulator
                System.out.println("Both triggers pulled. Firing"); //reminder to replace || in condition with &&
            }*/
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
                if ((Timer.getFPGATimestamp() - lastTime) > TEST_TIMEOUT) {
                    motorList[motorSwitch].set(0);
                    lastTime = Timer.getFPGATimestamp();
                    motorSwitch = (motorSwitch + 1) % motorList.length;
                    System.out.println("Test Motor: " + motorSwitch);
                }
            }

            if (Math.abs(y) > DEADZONEY) {
                motorList[motorSwitch].set(y);
            }
        }
    }

}
//hold button 3 for bottom barrel
//hold button 2 for top barrel
//press trigger while holding one of those to fire
//solenoid should only open for 50 ms (configurable by perhaps the axis on the base of the joystick, different times open = diff power)

