/************************ PROJECT TRIBECBOT *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.commands.swerve;

import com.stuypulse.robot.constants.Field;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;
import com.stuypulse.robot.subsystems.vision.LimelightVision;
import com.stuypulse.robot.subsystems.superstructure.hood.Hood;
import com.stuypulse.robot.subsystems.superstructure.hood.Hood.HoodState;
import com.stuypulse.robot.subsystems.superstructure.shooter.Shooter;
import com.stuypulse.robot.subsystems.superstructure.shooter.Shooter.ShooterState;
import com.stuypulse.robot.subsystems.handoff.Handoff;
import com.stuypulse.robot.subsystems.handoff.Handoff.HandoffState;
import com.stuypulse.robot.subsystems.spindexer.Spindexer;
import com.stuypulse.robot.subsystems.spindexer.Spindexer.SpindexerState;
import com.stuypulse.robot.util.superstructure.InterpolationCalculator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.swerve.SwerveRequest;

/**
 * Command that rotates the robot to face the hub and shoots using interpolated hood angle and RPM.
 * No turret required - the entire robot chassis rotates to aim at the hub.
 */
public class RobotFaceHubAndShoot extends Command {
    
    private static final double ROTATION_TOLERANCE_DEG = 2.0;
    
    private final CommandSwerveDrivetrain swerve;
    private final LimelightVision vision;
    private final Hood hood;
    private final Shooter shooter;
    private final Handoff handoff;
    private final Spindexer spindexer;
    
    private boolean isFacingHub = false;
    
    public RobotFaceHubAndShoot() {
        this.swerve = CommandSwerveDrivetrain.getInstance();
        this.vision = LimelightVision.getInstance();
        this.hood = Hood.getInstance();
        this.shooter = Shooter.getInstance();
        this.handoff = Handoff.getInstance();
        this.spindexer = Spindexer.getInstance();
        
        addRequirements(swerve, hood, shooter, handoff, spindexer);
    }
    
    @Override
    public void initialize() {
        // Check if Limelight is detecting AprilTags for position accuracy
        if (!vision.hasData()) {
            SmartDashboard.putString("RobotFaceHubAndShoot", "WARNING: No AprilTags detected! Using odometry only.");
        }
        
        isFacingHub = false;
        SmartDashboard.putString("RobotFaceHubAndShoot", "Starting - Rotating to hub and preparing shot");
    }
    
    @Override
    public void execute() {
        Pose2d robotPose = swerve.getPose();
        Pose2d hubCenter = Field.hubCenter;
        
        // Calculate angle to hub
        double dx = hubCenter.getX() - robotPose.getX();
        double dy = hubCenter.getY() - robotPose.getY();
        Rotation2d angleToHub = new Rotation2d(Math.atan2(dy, dx));
        
        // Calculate distance to hub
        double distanceToHub = robotPose.getTranslation().getDistance(hubCenter.getTranslation());
        
        // Check if robot is facing hub (within tolerance)
        double angleDifference = angleToHub.minus(robotPose.getRotation()).getDegrees();
        isFacingHub = Math.abs(angleDifference) < ROTATION_TOLERANCE_DEG;
        
        // Create swerve request: rotate to hub, no translation
        SwerveRequest.FieldCentricFacingAngle request = new SwerveRequest.FieldCentricFacingAngle()
            .withVelocityX(0)
            .withVelocityY(0)
            .withTargetDirection(angleToHub);
        
        swerve.setControl(request);
        
        // Get hood angle and RPM from interpolation based on distance
        InterpolationCalculator.InterpolatedShotInfo shotInfo = 
            InterpolationCalculator.interpolateShotInfo(robotPose, hubCenter);
        
        // Set hood to calculated angle
        hood.setState(HoodState.INTERPOLATION);
        
        // Set shooter to calculated RPM
        shooter.setState(ShooterState.SHOOT);
        
        // Set handoff and spindexer to forward when robot is facing hub
        if (isFacingHub) {
            handoff.setState(HandoffState.FORWARD);
            spindexer.setState(SpindexerState.FORWARD);
        } else {
            handoff.setState(HandoffState.STOP);
            spindexer.setState(SpindexerState.STOP);
        }
        
        // Dashboard output
        SmartDashboard.putNumber("RobotFaceHubAndShoot/Angle to Hub (deg)", angleToHub.getDegrees());
        SmartDashboard.putNumber("RobotFaceHubAndShoot/Distance to Hub (m)", distanceToHub);
        SmartDashboard.putNumber("RobotFaceHubAndShoot/Target Hood Angle (deg)", shotInfo.targetHoodAngle().getDegrees());
        SmartDashboard.putNumber("RobotFaceHubAndShoot/Target Shooter RPM", shotInfo.targetRPM());
        SmartDashboard.putBoolean("RobotFaceHubAndShoot/Facing Hub", isFacingHub);
        SmartDashboard.putString("RobotFaceHubAndShoot", isFacingHub ? "Ready to shoot!" : "Rotating to hub...");
    }
    
    @Override
    public void end(boolean interrupted) {
        swerve.setControl(new SwerveRequest.SwerveDriveBrake());
        hood.setState(HoodState.STOW);
        shooter.setState(ShooterState.STOP);
        handoff.setState(HandoffState.STOP);
        spindexer.setState(SpindexerState.STOP);
        SmartDashboard.putString("RobotFaceHubAndShoot", "Ended");
    }
    
    @Override
    public boolean isFinished() {
        return false;  // Command runs until button is released
    }
}
