/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.auton.regular;

import com.singularity.robot.commands.handoff.HandoffRun;
import com.singularity.robot.commands.intake.IntakeDeploy;
import com.singularity.robot.commands.intake.IntakeStow;
import com.singularity.robot.commands.spindexer.SpindexerRun;
import com.singularity.robot.subsystems.superstructure.Superstructure;
import com.singularity.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

import com.pathplanner.lib.path.PathPlannerPath;

public class DepotAuton extends SequentialCommandGroup {
    
    public DepotAuton(PathPlannerPath... paths) {

        addCommands(

            // To Depot
            new IntakeDeploy().alongWith(
                CommandSwerveDrivetrain.getInstance().followPathCommand(paths[0])
            ),

            new IntakeStow().alongWith(
                CommandSwerveDrivetrain.getInstance().followPathCommand(paths[1])
            ),

            new WaitUntilCommand(() -> Superstructure.getInstance().atTolerance()),
            new SpindexerRun().alongWith(
                new HandoffRun()
            )
            // .until(() -> DriverStation.getMatchTime() < 2).andThen(
            //     new ParallelCommandGroup(
            //         new HandoffStop(),
            //         new SpindexerStop(),
            //         new ClimberDown()
            //     )
            // )

        );

    }

}
