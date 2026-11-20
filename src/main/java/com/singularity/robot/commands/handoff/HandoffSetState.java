/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.handoff;

import com.singularity.robot.subsystems.handoff.Handoff;
import com.singularity.robot.subsystems.handoff.Handoff.HandoffState;

import edu.wpi.first.wpilibj2.command.Command;

public class HandoffSetState extends Command {
    private final Handoff handoff;
    private HandoffState state;

    public HandoffSetState(HandoffState state) {
        this.handoff = Handoff.getInstance();
        this.state = state;
        addRequirements(handoff);
    }

    @Override
    public void initialize() {
        handoff.setState(state);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
