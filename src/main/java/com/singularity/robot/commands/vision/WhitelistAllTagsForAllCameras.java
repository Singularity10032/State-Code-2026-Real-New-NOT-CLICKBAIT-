/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.vision;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class WhitelistAllTagsForAllCameras extends ParallelCommandGroup {
    public WhitelistAllTagsForAllCameras() {
        addCommands(
            new WhitelistAllTags("limelight-left"),
            new WhitelistAllTags("limelight-right"),
            new WhitelistAllTags("limelight-back")
        );
    }

}
