/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.util.vision;

import edu.wpi.first.math.geometry.Pose3d;

public class AprilTag {
    
    private final int id;
    private final Pose3d location;

    public AprilTag(int id, Pose3d location) {
        this.id = id;
        this.location = location;
    }

    public int getID() {
        return id;
    }

    public Pose3d getLocation() {
        return location;
    }

}