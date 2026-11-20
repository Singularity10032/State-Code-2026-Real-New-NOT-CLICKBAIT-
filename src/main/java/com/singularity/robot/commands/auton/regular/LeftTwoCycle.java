/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.auton.regular;

import com.singularity.robot.commands.handoff.HandoffRun;
import com.singularity.robot.commands.handoff.HandoffStop;
import com.singularity.robot.commands.intake.IntakeDeploy;
import com.singularity.robot.commands.intake.IntakeRunRollers;
import com.singularity.robot.commands.intake.IntakeStopRollers;
import com.singularity.robot.commands.spindexer.SpindexerRun;
import com.singularity.robot.commands.spindexer.SpindexerStop;
import com.singularity.robot.commands.superstructure.SuperstructureAutoInterpolation;
import com.singularity.robot.commands.superstructure.SuperstructureAutoInterpolationSOTM;
import com.singularity.robot.commands.superstructure.SuperstructureInterpolation;
import com.singularity.robot.commands.superstructure.SuperstructureSOTM;
import com.singularity.robot.subsystems.handoff.Handoff;
import com.singularity.robot.subsystems.superstructure.Superstructure;
import com.singularity.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

import com.pathplanner.lib.path.PathPlannerPath;

public class LeftTwoCycle extends SequentialCommandGroup {
    
    public LeftTwoCycle(PathPlannerPath... paths) {

        addCommands(

            // NZ Trip 1
            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[0]).alongWith(
                new WaitCommand(0.5).andThen(new IntakeDeploy())
            ),

            // Trip 1 To Score
            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[1]).alongWith(
                new SuperstructureAutoInterpolation()
            ),
            new SuperstructureSOTM(),
            new WaitUntilCommand(() -> Superstructure.getInstance().atTolerance()),
            new HandoffRun().andThen(
                new SpindexerRun()
            ).andThen(new WaitCommand(4.5)),
            new SuperstructureAutoInterpolation(),

            // NZ Trip 2
            new ParallelCommandGroup(
                CommandSwerveDrivetrain.getInstance().followPathCommand(paths[2]),
                new HandoffStop(),
                new SpindexerStop()
            ),
            new SuperstructureSOTM(),

            new ParallelCommandGroup(
                new WaitUntilCommand(() -> Superstructure.getInstance().atTolerance())
                // new SwerveClimbAlign()
            ),
            new HandoffRun().andThen(
                new SpindexerRun()
            ),
            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[3])
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
