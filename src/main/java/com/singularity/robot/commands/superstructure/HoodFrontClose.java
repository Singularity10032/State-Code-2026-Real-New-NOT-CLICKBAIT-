/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.superstructure;

import com.singularity.robot.subsystems.superstructure.hood.Hood;
import com.singularity.robot.subsystems.superstructure.hood.Hood.HoodState;

import edu.wpi.first.wpilibj2.command.Command;

public class HoodFrontClose extends Command {
    private final Hood hood;

    public HoodFrontClose() {
        hood = Hood.getInstance();
        addRequirements(hood);
    }

    @Override 
    public void execute() {
        hood.setState(HoodState.HUB_DIRECT_FRONT);
    }

    @Override
    public boolean isFinished() {
        return false; // Runs until cancelled (when button is released)
    }

    @Override
    public void end(boolean interrupted) {
        hood.setState(HoodState.STOW); // Return to stow when button is released
    }
}
