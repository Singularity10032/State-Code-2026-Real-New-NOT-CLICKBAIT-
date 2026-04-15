/************************ PROJECT TRIBECBOT *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.commands.superstructure;

import com.stuypulse.robot.subsystems.superstructure.Superstructure;
import com.stuypulse.robot.subsystems.superstructure.Superstructure.SuperstructureState;

import edu.wpi.first.wpilibj2.command.Command;

public class SuperstructureRightTrench extends Command {
    private final Superstructure superstructure;
    private final SuperstructureState state;
    private boolean stateReached = false;

    public SuperstructureRightTrench() {
        superstructure = Superstructure.getInstance();
        this.state = SuperstructureState.SHOOT_RIGHT_TRENCH;

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
        // Release the requirement so next command can take over
    }
}
