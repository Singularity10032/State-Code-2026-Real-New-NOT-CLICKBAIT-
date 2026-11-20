/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.turret;

import com.singularity.robot.subsystems.superstructure.turret.Turret;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ZeroTurret extends InstantCommand {

    private final Turret turret;

    public ZeroTurret() {
        this.turret = Turret.getInstance();
        addRequirements(turret);
    }

    @Override
    public void initialize() {
        turret.zeroEncoders();
        turret.seedTurret();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}