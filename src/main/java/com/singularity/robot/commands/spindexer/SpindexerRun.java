/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.spindexer;

import com.singularity.robot.subsystems.spindexer.Spindexer.SpindexerState;

public class SpindexerRun extends SpindexerSetState{
    public SpindexerRun(){
        super(SpindexerState.FORWARD);
    }
    
}
