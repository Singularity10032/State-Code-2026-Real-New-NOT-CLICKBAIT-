/************************ PROJECT TRIBECBOT *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.commands.vision;

import com.stuypulse.robot.subsystems.vision.LimelightVision;
import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * Whitelists only the tags used for vision-based shooting positions (11, 2, 18, 27).
 * This improves vision accuracy by filtering out other field markers.
 * 
 * Tags:
 * - 11, 2: Blue alliance hub tags
 * - 18, 27: Red alliance hub tags
 */
public class WhitelistVisionShootingTags extends InstantCommand {
    private final int[] VISION_SHOOTING_TAGS = {2, 11, 18, 27};

    public WhitelistVisionShootingTags() {
        // Get vision subsystem but don't require it (instant command)
    }

    @Override
    public void initialize() {
        LimelightVision vision = LimelightVision.getInstance();
        
        // Whitelist tags for each Limelight camera
        vision.setTagWhitelist(VISION_SHOOTING_TAGS, "limelight-left");
        vision.setTagWhitelist(VISION_SHOOTING_TAGS, "limelight-right");
        vision.setTagWhitelist(VISION_SHOOTING_TAGS, "limelight-back");
    }
}
