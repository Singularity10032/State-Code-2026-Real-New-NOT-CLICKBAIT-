/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.superstructure;

import com.singularity.robot.subsystems.superstructure.Superstructure;
import com.singularity.robot.subsystems.superstructure.Superstructure.SuperstructureState;

import edu.wpi.first.wpilibj2.command.Command;

public class SuperstructureSetStateAndRelease extends Command {
    private final Superstructure superstructure;
    private final SuperstructureState state;
    private boolean stateReached = false;

    public SuperstructureSetStateAndRelease(SuperstructureState state) {
        superstructure = Superstructure.getInstance();
        this.state = state;

        addRequirements(superstructure);
    }

    @Override 
    public void execute() {
        superstructure.setState(state);
        // Once state is set, mark as reached so we can finish
        if (superstructure.getState() == state) {
            stateReached = true;
        }
    }

    @Override
    public boolean isFinished() {
        return stateReached;
    }

    @Override
    public void end(boolean interrupted) {
        // Don't release the requirement - let the next command take over
        // The subsystem requirement will be automatically released when command ends
    }
}