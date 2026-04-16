/************************ PROJECT TRIBECBOT *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.commands.swerve;

import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;
import com.stuypulse.robot.subsystems.vision.LimelightVision;
import com.stuypulse.stuylib.control.angle.AngleController;
import com.stuypulse.stuylib.control.angle.feedback.AnglePIDController;
import com.stuypulse.stuylib.control.feedback.PIDController;
import com.stuypulse.robot.util.HolonomicController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.swerve.SwerveRequest;

/**
 * Command that drives the robot to a specific position using vision-corrected odometry and PID control.
 */
public class DriveToVisionPosition extends Command {
    
    /**
     * Constants for vision positioning
     */
    public interface VisionPositioningConstants {
        // Target shooting positions for vision-based positioning
        Pose2d VISION_SHOOTING_POSITION_1 = 
            new Pose2d(2.0, 2.5, Rotation2d.fromDegrees(0.0));
        
        Pose2d VISION_SHOOTING_POSITION_2 = 
            new Pose2d(2.0, 5.5, Rotation2d.fromDegrees(0.0));
        
        // Position movement tolerances
        double POSITION_TOLERANCE_METERS = 0.05; // 5cm
        double ROTATION_TOLERANCE_DEGREES = 2.0; // 2 degrees
        
        // Movement speed limits
        double MAX_LINEAR_VELOCITY_M_PER_S = 2.0;
        double MAX_ANGULAR_VELOCITY_RAD_PER_S = Math.PI;
        
        // Control gains for PID control
        double LINEAR_KP = 2.0;
        double ANGULAR_KP = 0.05;
    }
    
    /**
     * PID-based controller for vision positioning.
     * Uses full PID control for smooth, accurate movement to target position.
     */
    private static class VisionPositioningPIDController {
        private final HolonomicController holonomicController;
        
        public VisionPositioningPIDController(double xKp, double xKi, double xKd,
                                              double yKp, double yKi, double yKd,
                                              double angleKp, double angleKi, double angleKd) {
            // Create individual PID controllers for X, Y, and angle
            PIDController xController = new PIDController(xKp, xKi, xKd);
            PIDController yController = new PIDController(yKp, yKi, yKd);
            AngleController angleController = new AnglePIDController(angleKp, angleKi, angleKd);
            
            // Wrap them in a holonomic controller
            this.holonomicController = new HolonomicController(xController, yController, angleController);
        }
        
        public void update(Pose2d targetPose, Pose2d currentPose) {
            holonomicController.update(targetPose, currentPose);
        }
        
        public double getXVelocity() {
            return holonomicController.getOutput().vxMetersPerSecond;
        }
        
        public double getYVelocity() {
            return holonomicController.getOutput().vyMetersPerSecond;
        }
        
        public double getAngularVelocity() {
            return holonomicController.getOutput().omegaRadiansPerSecond;
        }
        
        public boolean isDone(double xTolerance, double yTolerance, double angleTolerance) {
            return holonomicController.isDone(xTolerance, yTolerance, angleTolerance);
        }
        
        public void reset() {
            // Controllers should be reset if needed
        }
    }
    
    private final CommandSwerveDrivetrain swerve;
    private final LimelightVision vision;
    private final Pose2d targetPose;
    private final VisionPositioningPIDController pidController;
    
    private boolean hasReachedPosition = false;

    /**
     * Command that drives the robot to a specific position using PID control
     * and vision-corrected odometry.
     * The swerve odometry is continuously updated by LimelightVision measurements.
     * @param targetPose The desired Pose2d(x, y, rotation) for the robot
     */
    public DriveToVisionPosition(Pose2d targetPose) {
        this.swerve = CommandSwerveDrivetrain.getInstance();
        this.vision = LimelightVision.getInstance();
        this.targetPose = targetPose;
        
        // Create PID controller with gains from VisionPositioningConstants
        this.pidController = new VisionPositioningPIDController(
            VisionPositioningConstants.LINEAR_KP, 0.0, 0.0,    // X: P only
            VisionPositioningConstants.LINEAR_KP, 0.0, 0.0,    // Y: P only
            VisionPositioningConstants.ANGULAR_KP, 0.0, 0.0    // Angle: P only
        );
        
        addRequirements(swerve);
    }

    @Override
    public void initialize() {
        // Check if Limelight is detecting AprilTags
        if (!vision.hasData()) {
            // No tags detected - display error and cancel command
            SmartDashboard.putString("Vision/DriveToVisionPosition", "ERROR: No AprilTags detected! Move robot into view of AprilTags.");
            cancel();  // Cancel this command immediately
            return;
        }
        
        hasReachedPosition = false;
        pidController.reset();
        SmartDashboard.putString("Vision/DriveToVisionPosition", "Starting - Moving to position");
    }

    @Override
    public void execute() {
        // Get current pose from swerve odometry (continuously corrected by Limelight vision)
        Pose2d currentPose = swerve.getPose();
        
        // Update PID controller with target and current pose
        pidController.update(targetPose, currentPose);
        
        // Get velocities from PID controller
        double xVel = pidController.getXVelocity();
        double yVel = pidController.getYVelocity();
        double rotVel = pidController.getAngularVelocity();
        
        // Clamp velocities to reasonable limits
        double maxLinearVel = VisionPositioningConstants.MAX_LINEAR_VELOCITY_M_PER_S;
        double maxAngularVel = VisionPositioningConstants.MAX_ANGULAR_VELOCITY_RAD_PER_S;
        
        double linearMagnitude = Math.sqrt(xVel * xVel + yVel * yVel);
        if (linearMagnitude > maxLinearVel) {
            xVel = (xVel / linearMagnitude) * maxLinearVel;
            yVel = (yVel / linearMagnitude) * maxLinearVel;
        }
        
        if (Math.abs(rotVel) > maxAngularVel) {
            rotVel = Math.signum(rotVel) * maxAngularVel;
        }
        
        // Send field-relative velocity command to swerve
        swerve.setControl(swerve.getFieldCentricSwerveRequest()
            .withVelocityX(xVel)
            .withVelocityY(yVel)
            .withRotationalRate(rotVel)
        );
        
        // Dashboard debugging
        SmartDashboard.putNumber("Vision/Target X", targetPose.getX());
        SmartDashboard.putNumber("Vision/Target Y", targetPose.getY());
        SmartDashboard.putNumber("Vision/Current X", currentPose.getX());
        SmartDashboard.putNumber("Vision/Current Y", currentPose.getY());
        SmartDashboard.putNumber("Vision/X Velocity", xVel);
        SmartDashboard.putNumber("Vision/Y Velocity", yVel);
        SmartDashboard.putNumber("Vision/Angular Velocity", rotVel);
        
        // Check if we've reached the target
        if (pidController.isDone(
            VisionPositioningConstants.POSITION_TOLERANCE_METERS,
            VisionPositioningConstants.POSITION_TOLERANCE_METERS,
            VisionPositioningConstants.ROTATION_TOLERANCE_DEGREES)) {
            hasReachedPosition = true;
        }
    }

    @Override
    public boolean isFinished() {
        return hasReachedPosition;
    }

    @Override
    public void end(boolean interrupted) {
        // Stop the swerve with brake mode
        swerve.setControl(new SwerveRequest.SwerveDriveBrake());
        SmartDashboard.putString("Vision/DriveToVisionPosition", 
            interrupted ? "Interrupted" : "Finished - Ready to Shoot");
    }
}
