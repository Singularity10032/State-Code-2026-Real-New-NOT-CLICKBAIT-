/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.vision;

import com.singularity.robot.constants.Settings;

public class ResetLimelightIMU extends SetIMUMode {
    public ResetLimelightIMU() {
        super(Settings.Vision.RESET_IMU_INDEX);
    }
}