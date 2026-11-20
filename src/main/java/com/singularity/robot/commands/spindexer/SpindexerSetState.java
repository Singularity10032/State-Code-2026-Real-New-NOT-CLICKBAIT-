/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.spindexer;

import com.singularity.robot.subsystems.spindexer.Spindexer;
import com.singularity.robot.subsystems.spindexer.Spindexer.SpindexerState;

import edu.wpi.first.wpilibj2.command.Command;

public class SpindexerSetState extends Command {
    private final Spindexer spindexer;
    private SpindexerState state;

    public SpindexerSetState(SpindexerState state) {
        this.spindexer = Spindexer.getInstance();
        this.state = state;
        addRequirements(spindexer);
    }

    @Override
    public void initialize() {
        spindexer.setState(state);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}