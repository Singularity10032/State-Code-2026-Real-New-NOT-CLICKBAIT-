/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.vision;

import com.singularity.robot.Robot;
import com.singularity.robot.constants.Field;

public class WhitelistTowerTags extends SetLimelightWhiteList {
    public WhitelistTowerTags (String limelightName) {
        super(Robot.isBlue() ? Field.BLUE_TOWER_TAG_IDS: Field.RED_TOWER_TAG_IDS, limelightName);
    }
}