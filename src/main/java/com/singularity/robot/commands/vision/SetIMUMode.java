/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.vision;

import com.singularity.robot.subsystems.vision.LimelightVision;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetIMUMode extends InstantCommand {
    private final LimelightVision vision;
    private final int index;

    public SetIMUMode(int IMUIndex) {
        vision = LimelightVision.getInstance();
        index = IMUIndex;
    }

    @Override
    public void initialize() {
        super.initialize();
        vision.setIMUMode(index);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
