/**
 * VISION-BASED SHOOTING POSITION SETUP
 * =====================================
 * 
 * GOAL:
 * Press right button → Robot moves to specific position using vision → Robot shoots
 * Works with AprilTags 11, 2, 18, 27 (hub tags)
 * 
 * 
 * WHAT WAS CREATED:
 * ================
 * 
 * 1. DriveToVisionPosition.java (NEW COMMAND)
 *    - Moves the robot to a target Pose2d using proportional control
 *    - Uses vision-corrected odometry from LimelightVision
 *    - Automatically finishes when robot reaches target position
 *    - Then allows next commands (shooting) to execute
 *    
 * 2. Settings.VisionShootingPositions (NEW CONSTANTS)
 *    - VISION_SHOOTING_POSITION_1: Target position for tag-based shooting
 *    - VISION_SHOOTING_POSITION_2: Alternative position (if needed)
 *    - Tunable control gains (LINEAR_KP, ANGULAR_KP)
 *    - Configurable movement tolerances
 *    
 * 3. WhitelistVisionShootingTags.java (OPTIONAL COMMAND)
 *    - Filters Limelight to only see tags 2, 11, 18, 27
 *    - Improves vision accuracy by ignoring other markers
 *    - Can be called before starting vision positioning routine
 *    
 * 4. RobotContainer.java (UPDATED)
 *    - Right button now uses: DriveToVisionPosition → then shoots
 *    - Replaces old manual Right Trench scoring
 *    
 * 
 * HOW THE VISION PROCESS WORKS:
 * =============================
 * 
 * 1. LimelightVision continuously detects AprilTags (2, 11, 18, 27)
 * 2. Converts detections → Robot pose on field
 * 3. Sends pose to swerve odometry: swerve.addVisionMeasurement()
 * 4. Swerve fuses vision + gyro + encoders = accurate pose
 * 
 * When right button is pressed:
 * 
 * 5. DriveToVisionPosition reads current pose from swerve
 * 6. Calculates error to target position
 * 7. Sends proportional velocity commands to swerve
 * 8. Limelight keeps correcting odometry as robot moves
 * 9. Robot reaches target → Command finishes
 * 10. Shooting commands execute (hood, handoff, spindexer)
 * 
 * 
 * TUNING PARAMETERS (in Settings.VisionShootingPositions):
 * ========================================================
 * 
 * POSITION_TOLERANCE_METERS = 0.05
 *   - How close robot needs to be to target position (5cm)
 *   - Smaller = more precise, but slower to achieve
 *   - Larger = faster convergence, less accurate
 *   
 * ROTATION_TOLERANCE_DEGREES = 2.0
 *   - How close robot needs to be to target rotation (2 degrees)
 *   
 * LINEAR_KP = 2.0
 *   - Proportional gain for movement speed
 *   - Higher = faster movement, but may oscillate
 *   - Lower = slower movement, smoother
 *   
 * ANGULAR_KP = 0.05
 *   - Proportional gain for rotation
 *   - Controls how aggressively robot rotates to target angle
 *   
 * MAX_LINEAR_VELOCITY_M_PER_S = 2.0
 *   - Speed limit for movement (2 m/s)
 *   
 * MAX_ANGULAR_VELOCITY_RAD_PER_S = π
 *   - Rotation speed limit (π rad/s ≈ 180°/sec)
 * 
 * 
 * NEXT STEPS FOR TESTING:
 * =======================
 * 
 * 1. MEASURE THE EXACT POSITION:
 *    - Place robot at desired shooting position
 *    - Read coordinates from swerve odometry (SmartDashboard)
 *    - Update VISION_SHOOTING_POSITION_1 in Settings.java
 *    - (This is the exact position where you want to shoot from)
 *    
 * 2. ENABLE TAG WHITELISTING (OPTIONAL):
 *    - In RobotContainer configureElasticButtons(), add:
 *      SmartDashboard.putData("Robot/Whitelist Vision Shooting Tags", 
 *        new WhitelistVisionShootingTags());
 *    - Click button on dashboard before running vision positioning
 *    
 * 3. TEST THE POSITIONING:
 *    - Place robot in view of tags 2, 11, 18, 27
 *    - Press right button
 *    - Robot should move to exact position
 *    - Check SmartDashboard for distance/error values
 *    
 * 4. FINE-TUNE GAINS IF NEEDED:
 *    - If robot overshoots: decrease LINEAR_KP/ANGULAR_KP
 *    - If robot moves too slowly: increase LINEAR_KP/ANGULAR_KP
 *    - If oscillating: decrease gains and/or increase tolerances
 *    
 * 5. VERIFY SHOOTING:
 *    - Once positioned, hood/shooter should be ready
 *    - Driver can manually shoot or script it
 *    
 * 
 * FILE LOCATIONS:
 * ===============
 * 
 * DriveToVisionPosition.java
 * → src/main/java/com/singularity/robot/commands/swerve/
 * 
 * Settings.VisionShootingPositions
 * → src/main/java/com/singularity/robot/constants/Settings.java
 *   (bottom of file, before closing brace)
 * 
 * WhitelistVisionShootingTags.java
 * → src/main/java/com/singularity/robot/commands/vision/
 * 
 * RobotContainer.java changes
 * → Right button binding (around line 272)
 * → Import statement (top of file)
 * 
 * 
 * IMPORTANT NOTES:
 * ================
 * 
 * ✓ Vision system is NOT overwritten - it still corrects odometry
 * ✓ Turret/Hood/Shooter work independently - no conflicts
 * ✓ Multiple commands can run in parallel (movement + LEDs)
 * ✓ Robot will stay in brake mode once positioning is done
 * ✓ All subsystems remain enabled/disabled as configured
 * 
 * 
 * DEBUGGING:
 * ==========
 * 
 * SmartDashboard outputs (when running):
 * - Vision/Target X, Y: Where robot is trying to go
 * - Vision/Current X, Y: Where robot actually is
 * - Vision/Distance to Target: How far from position
 * - Vision/Rotation Error (deg): How far from target rotation
 * - Vision/DriveToVisionPosition: Status (Starting, Finished, Interrupted)
 * 
 * If robot doesn't move:
 * 1. Check that Limelights detect the AprilTags (vision enabled?)
 * 2. Verify swerve odometry is being updated on dashboard
 * 3. Confirm right button is properly bound in RobotContainer
 * 4. Check if DriveToVisionPosition command is being imported
 * 
 */
