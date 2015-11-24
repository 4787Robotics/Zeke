/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

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
    
    // Joystick constants
    final int DRIVESTICK_USB = 1;
    final int MECHSTICK_USB = 2;
    final double solenoidwindow = .05; //50ms, maybe will be variable later on
    final double DEADZONEY = .05;
    final double DEADZONEX = .15;
    
    
    // Motor constants
    final int FRONT_LEFT_MOTOR_PWM = 1;
    final int REAR_LEFT_MOTOR_PWM = 2;
    final int FRONT_RIGHT_MOTOR_PWM = 4;
    final int REAR_RIGHT_MOTOR_PWM = 3;
    
    double x, y, expX, expY;
    
    // Motor controller configurations
    Talon frontLeft = new Talon(FRONT_LEFT_MOTOR_PWM);
    Talon rearLeft = new Talon(REAR_LEFT_MOTOR_PWM);
    Talon frontRight = new Talon(FRONT_RIGHT_MOTOR_PWM);
    Talon rearRight = new Talon(REAR_RIGHT_MOTOR_PWM);

    public Zeke() {
        //drive = new RobotDrive(frontRight, rearRight, frontLeft, rearLeft); //this is wrong
        drivestick = new Joystick(DRIVESTICK_USB);
        mechstick = new Joystick(MECHSTICK_USB);
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
            if(Math.abs(x)>DEADZONEX) expX = x;
            if(Math.abs(y)>DEADZONEY) expY = y;
           
            frontLeft.set(expX - expY);
            rearLeft.set(expX - expY);
            frontRight.set(expX + expY);
            rearRight.set(expX + expY);
            System.out.println(y + " : " + x);

            if (mechstick.getRawButton(1) && drivestick.getRawButton(1)) //ZEKE's nuclear arming system; both drivers must pull the trigger to fire
            {
                if(mechstick.getRawButton(3))
                {
                    //top barrel
                }
                else if(mechstick.getRawButton(2))
                {
                    //bottom barrel
                }
            }
            Timer.delay(0.005); //5ms
        }
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
        
    }
}
//hold button 3 for bottom barrel
//hold button 2 for top barrel
//press trigger while holding one of those to fire
//solenoid should only open for 50 ms (configurable by perhaps the axis on the base of the joystick, different times open = diff power)

