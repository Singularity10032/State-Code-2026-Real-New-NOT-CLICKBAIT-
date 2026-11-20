/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.vision;

import com.singularity.robot.subsystems.vision.LimelightVision;
import com.singularity.robot.subsystems.vision.LimelightVision.MegaTagMode;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetMegaTagMode extends InstantCommand {
    
    private MegaTagMode megaTagMode;

    public SetMegaTagMode(MegaTagMode mode) {
        this.megaTagMode = mode;
    }

    @Override
    public void initialize() {
        super.initialize();
        LimelightVision.getInstance().setMegaTagMode(megaTagMode);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}