/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.vision;

import com.singularity.robot.constants.Field;
import com.singularity.robot.subsystems.vision.LimelightVision;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetLimelightBlackList extends InstantCommand {
    private LimelightVision vision;
    private int[] blacklistedArray;
    private String limelightName;

    public SetLimelightBlackList(int[] blacklistedArray, String limelightName) {
        vision = LimelightVision.getInstance();
        this.blacklistedArray = blacklistedArray ;
        this.limelightName = limelightName;
    }

    public SetLimelightBlackList(String limelightName) {
        vision = LimelightVision.getInstance();
        this.blacklistedArray = Field.ALL_TAGS;
        this.limelightName = limelightName;
    }

    @Override
    public void initialize() {
        vision.setTagWhitelist(new int[]{516}, limelightName);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}