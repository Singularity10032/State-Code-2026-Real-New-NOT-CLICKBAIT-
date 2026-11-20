/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.turret;

import com.singularity.robot.subsystems.superstructure.turret.Turret.TurretState;

public class TurretShoot extends TurretSetState {
    public TurretShoot() {
        super(TurretState.SHOOT);
    }
}