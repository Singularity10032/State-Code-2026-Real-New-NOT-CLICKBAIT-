/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.auton.regular;

import com.singularity.robot.commands.handoff.HandoffStop;
import com.singularity.robot.commands.spindexer.SpindexerStop;
import com.singularity.robot.commands.superstructure.SuperstructureSetStateAndRelease;
import com.singularity.robot.commands.superstructure.SuperstructureStow;
import com.singularity.robot.subsystems.handoff.Handoff;
import com.singularity.robot.subsystems.handoff.Handoff.HandoffState;
import com.singularity.robot.subsystems.spindexer.Spindexer;
import com.singularity.robot.subsystems.spindexer.Spindexer.SpindexerState;
import com.singularity.robot.subsystems.superstructure.Superstructure.SuperstructureState;
import com.singularity.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import com.pathplanner.lib.path.PathPlannerPath;

public class PreshootFromTrench extends SequentialCommandGroup {
    
    public PreshootFromTrench(PathPlannerPath... paths) {

        addCommands(

            // Follow path to hub/tower
            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[0]),

            // Shoot sequence - run for 5 seconds
            new ParallelCommandGroup(
                new SuperstructureSetStateAndRelease(SuperstructureState.SHOOT_HUB_DIRECT_FRONT),
                new RunCommand(
                    () -> Handoff.getInstance().setState(HandoffState.FORWARD),
                    Handoff.getInstance()),
                new RunCommand(
                    () -> Spindexer.getInstance().setState(SpindexerState.FORWARD),
                    Spindexer.getInstance())
            ).withTimeout(5.0),

            // Stop everything
            new SpindexerStop()
                .alongWith(new SuperstructureStow())
                .alongWith(new HandoffStop())

        );

    }

}
