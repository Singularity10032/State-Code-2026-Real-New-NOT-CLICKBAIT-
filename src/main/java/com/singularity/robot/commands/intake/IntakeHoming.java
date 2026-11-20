/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.intake;

import com.singularity.robot.subsystems.intake.Intake.PivotState;

public class IntakeHoming extends IntakeSetState {

    public IntakeHoming() { //TODO: ensure this works/overrides the Intake Deploy w/o conflicts
        super(PivotState.HOMING);
    }
    
}
