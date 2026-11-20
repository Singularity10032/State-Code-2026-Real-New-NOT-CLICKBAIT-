/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.util;

import com.singularity.lib.control.Controller;
import com.singularity.lib.control.angle.AngleController;
import com.singularity.lib.math.Angle;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class HolonomicController {
    private Controller xController;
    private Controller yController;
    private AngleController angleController;

    public HolonomicController(Controller xController, Controller yController, AngleController angleController) {
        this.xController = xController;
        this.yController = yController;
        this.angleController = angleController;
    }

    public ChassisSpeeds update(Pose2d setpoint, Pose2d measurement) {
        xController.update(setpoint.getX(), measurement.getX());
        yController.update(setpoint.getY(), measurement.getY());
        angleController.update(
                Angle.fromRotation2d(setpoint.getRotation()),
                Angle.fromRotation2d(measurement.getRotation()));

        return getOutput();
    }

    public ChassisSpeeds getOutput() {
        return ChassisSpeeds.fromFieldRelativeSpeeds(
                xController.getOutput(),
                yController.getOutput(),
                angleController.getOutput(),
                angleController.getMeasurement().getRotation2d());
    }

    public ChassisSpeeds getError() {
        return ChassisSpeeds.fromFieldRelativeSpeeds(
                xController.getError(),
                yController.getError(),
                angleController.getError().toDegrees(),
                angleController.getMeasurement().getRotation2d());
    }

    public boolean isDone(double xToleranceMeters, double yToleranceMeters, double angleToleranceDegrees) {
        return xController.isDone(xToleranceMeters)
                && yController.isDone(yToleranceMeters)
                && angleController.isDoneDegrees(angleToleranceDegrees);
    }
}