/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.hood;

import com.singularity.robot.subsystems.superstructure.hood.Hood;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SeedHoodRelativeEncoderAtUpperHardstop extends InstantCommand {
    private final Hood hood;

    public SeedHoodRelativeEncoderAtUpperHardstop() {
        this.hood = Hood.getInstance();

        addRequirements(hood);
    }

    @Override
    public void initialize() {
        hood.seedHoodAtUpperHardStop();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
