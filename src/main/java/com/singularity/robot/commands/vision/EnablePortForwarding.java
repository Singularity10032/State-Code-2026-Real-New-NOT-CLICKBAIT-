/*********************** SINGULARITY FRC 10032 ***********************/
/* Copyright (c) 2026 Singularity (FRC 10032). All rights reserved. */
/* Use of this source code is governed by an MIT-style license      */
/* that can be found in the repository LICENSE file.                */
/********************************************************************/
package com.singularity.robot.commands.vision;

import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class EnablePortForwarding extends InstantCommand {
    private String hostname;

    public EnablePortForwarding(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public void initialize() {
        PortForwarder.remove(5801);
        PortForwarder.add(5801, hostname, 5801);
    }
    
    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
