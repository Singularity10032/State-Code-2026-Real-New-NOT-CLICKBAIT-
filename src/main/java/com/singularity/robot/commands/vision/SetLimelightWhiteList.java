/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.vision;

import com.singularity.robot.constants.Field;
import com.singularity.robot.subsystems.vision.LimelightVision;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetLimelightWhiteList extends InstantCommand {
    private LimelightVision vision;
    private int[] whitelistedArray;
    private String limelightName;

    public SetLimelightWhiteList(int[] whitelistedArray, String limelightName) {
        vision = LimelightVision.getInstance();
        this.whitelistedArray = whitelistedArray;
        this.limelightName = limelightName;
    }

    public SetLimelightWhiteList(String limelightName) {
        vision = LimelightVision.getInstance();
        this.whitelistedArray = Field.ALL_TAGS;
        this.limelightName = limelightName;
    }

    @Override
    public void initialize() {
        vision.setTagWhitelist(whitelistedArray, limelightName);
    }
    
    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}