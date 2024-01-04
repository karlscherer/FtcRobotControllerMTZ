package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.mtzConstantsCS.MAX_AUTO_SPEED;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.MAX_AUTO_STRAFE;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.MAX_AUTO_TURN;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.SPEED_GAIN;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.STRAFE_GAIN;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.TURN_GAIN;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.USE_WEBCAM;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.armExtensionCollapsedLength;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.armExtensionInchesAtHome;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.armLengthDesired;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.armPivotHeight;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.armRotationDegreesAtHome;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.cameraBearingOffsetLeftTagLeftPixelLeftSide;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.cameraBearingOffsetRightTagRightPixelRightSide;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.defaultArmAssistLevel;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.defaultArmExtensionPower;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.defaultArmLowerPower;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.defaultArmPower;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.defaultDriveSpeed;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.defaultPauseTime;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.distanceBetweenScoopPositions;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.distanceBetweenValleys;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.driveBump;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.driveFastRatio;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.driveSlowRatio;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.endGameOver;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.endGameStart;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.endGameWarning;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.endGameWarning2;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.findStackDistance;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.findStackLevel;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.greenWarningTime;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.handAssistRideHeightAboveLevel;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.handAssistRideHeightDistance;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.handAssistRideHeightLevel;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.launcherReleasePosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.launcherSetPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.leftClawClosedPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.leftClawOpenPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.maxArmDegrees;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.maxArmExtensionInches;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.maxWristPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.minArmDegrees;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.minArmExtensionInches;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.minWristPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.prorate;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.redWarningTime;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.rightClawClosedPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.rightClawOpenPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.scoopStage;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.stackDistanceArray;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.stackDistanceAtHome;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.stackHeightAboveLevelArray;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.stackHeightOnLevelArray;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.stackLevelAtHome;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.strafeBump;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.ticksPerDegreeArm;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.ticksPerDegreeTurnChassis;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.ticksPerInchExtension;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.ticksPerInchWheelDrive;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.ticksPerInchWheelStrafe;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.turnBump;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.wristAdjustment;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.wristBump;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.wristConversionToServo;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.yellowWarningTime;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TeleOp(name="TeleMTZ_Drive_CS With Align Doesnt Work", group ="zz_bottom")

//@Disabled

/****
 * This class is intended to be a sub class to run the robot with the controllers.
 * The super classes may call out different controller maps to use
 *
 *
 * v100 Copied from Last Year
 * v101 Updates during meet 1
 * v102
 * v103
 * v104 Added Power Ratio to slow arms with wheels
 * v105
 * v106 Added Align To AprilTag
 *
 */

public class TeleMTZ_Drive_Controls_CSv_not_working_105_has_been_updated_after_this extends LinearOpMode {

    /********************************
     * Robot Configuration Flags
     ********************************/
    boolean accountForArmDrift;
    boolean hasChassisMotors;
    boolean hasAuxMotorsAndServos;
    boolean hasLightsHub;
    boolean wantAutoChassisControls;
    boolean leftClawRemainClosed = true;
    boolean rightClawRemainClosed = true;
    private int allianceReverser = 1;
    double armAssistLevel = defaultArmAssistLevel;
    double launcherPosition = launcherSetPosition;

    /********************************
     * Timer Variables
     ********************************/
    private ElapsedTime endGameTimer;

    boolean greenTimerElapsed;
    boolean yellowTimerElapsed;
    boolean redTimerElapsed;
    boolean endGameStartElapsed;
    double tempLightsTimer;

    /***********
     * Lights Control Declarations
     ***********/

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;
    RevBlinkinLedDriver.BlinkinPattern tempLightsPattern;

    /******************
     * April Tag Alignment Declarations
     */

    private static int DESIRED_TAG_ID = 0;     // Choose the tag you want to approach or set to -1 for ANY tag.
    private VisionPortal visionPortal;               // Used to manage the video source.
    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag

    /*************************
     * Motor & Servo Variables
     *************************/
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private DcMotor armExtension;
    private Servo leftClaw;
    private Servo rightClaw;
    private Servo wrist;
    private Servo planeLaunchTrigger;
    double drivePower;
    double blPower;
    double brPower;
    double flPower;
    double frPower;
    double powerRatio=1;

    boolean aboveLevel = false;
    boolean stackingDown;


    int tagAdditional = 0;

    int stackLevel = stackLevelAtHome;
    int stackDistance = stackDistanceAtHome;
    double armRotationDegrees = armRotationDegreesAtHome;
    double armExtensionInches = armExtensionInchesAtHome;
    double verticalDesired;
    double horizontalDesired;
    double stackDegreesDesired;
    String debugString ="none";

    double wristPositionDesired = wristConversionToServo(armRotationDegreesAtHome) + wristAdjustment;


    /*******
     * Add Controller Variables & Objects
     ********/


    /*************           Center Stage R1     Control Pad Map            **************/
// Assign Variables & Objects for Control Pads
    double chassisSpeedSlow;                             //Slow Speed
    mtzButtonBehavior chassisBumpLeftTurnStatus = new mtzButtonBehavior();         //Bump Left Turn




    mtzButtonBehavior chassisBumpForwardStatus = new mtzButtonBehavior();         //Bump Forward
    mtzButtonBehavior chassisBumpLeftStrafeStatus = new mtzButtonBehavior();         //Bump Left Strafe
    mtzButtonBehavior chassisBumpRightStrafeStatus = new mtzButtonBehavior();         //Bump Right Strafe
    mtzButtonBehavior chassisBumpBackStatus = new mtzButtonBehavior();         //Bump Backwards


    double driveStick2;                             //Drive 2
    double turnStick;                             //Turn
    double chassisSpeedFast;                             //High Speed
    mtzButtonBehavior chassisBumpRightTurnStatus = new mtzButtonBehavior();         //Bump Right Turn

    mtzButtonBehavior startButton1Status = new mtzButtonBehavior();         //Pad Select (A & B)


    mtzButtonBehavior aprilTagCenterStatus = new mtzButtonBehavior();         //Aim to Center AprilTag
    mtzButtonBehavior aprilTagLeftStatus = new mtzButtonBehavior();         //Aim to Left AprilTag
    mtzButtonBehavior aprilTagRightStatus = new mtzButtonBehavior();         //Aim to Right AprilTag
    mtzButtonBehavior planeLaunchStatus = new mtzButtonBehavior();         //Launch Plane


    double driveStick1;                             //Drive 1
    double strafeStick;                             //Strafe
    double leftClawClose;                             //Left Claw Close (Sticky)
    mtzButtonBehavior leftClawOpenStatus = new mtzButtonBehavior();         //Left Claw Open (Sticky)

    mtzButtonBehavior resetAdjustmentsStatus = new mtzButtonBehavior();         //Reset Adjustments
    mtzButtonBehavior handHomeStatus = new mtzButtonBehavior();         //Hand to Home

    mtzButtonBehavior levelUpStatus = new mtzButtonBehavior();         //Move Hand to Next Level Higher
    mtzButtonBehavior wristAdjustLessStatus = new mtzButtonBehavior();         //Slightly Decrease Wrist
    mtzButtonBehavior wristAdjustMoreStatus = new mtzButtonBehavior();         //Slightly Increase Wrist
    mtzButtonBehavior levelDownStatus = new mtzButtonBehavior();         //Move Hand to Next Level Lower


    double handVerticalStick;                             //Hand Vertical Move
    double handHorizontalStick;                             //Hand Horizontal Move
    double rightClawClose;                             //Right Claw Close (Sticky)
    mtzButtonBehavior rightClawOpenStatus = new mtzButtonBehavior();         //Right Claw Open (Sticky)

    mtzButtonBehavior startButton2Status = new mtzButtonBehavior();         //Pad Select (A & B)








    double handAssist;                             //Ride Height/Drop to 0

// End of Assignment Mapping
    /*************           End     Center Stage R1     Control Pad Map            **************/



    @Override

    //This is the default opMode call for generically running the opMode in this class directly from the phone without calling it from a super class
    public void runOpMode() throws InterruptedException{
        /******************************************************
         * These default settings will be used if THIS opMode is selected from the driver station.
         * Typically this opMode is not called since this class is used by super classes to call the controlRobot method with specific variables.
         * It is helpful to use this opMode for testing the controlRobot method
         *****************************************************/

        controlRobot("Red", "Center Stage R1", defaultDriveSpeed, true, true, true, true);
    }

    //This is the method that handles the controls
    public void controlRobot(String alliance, String controlPadMap, Double defaultDrivePower, Boolean runChassis, Boolean runAux, Boolean runLights, Boolean runAutos) throws InterruptedException {

        // Robot Configuration Flags
        hasChassisMotors = runChassis;
        hasAuxMotorsAndServos = runAux;
        hasLightsHub = runLights;
        wantAutoChassisControls = runAutos;
        // non-counterbalanced Spur Gear arm needs to account for arm drift
        accountForArmDrift = false;

        //Code written for Blue alliance and reverse turns if on Red alliance
        allianceReverser=1;
        if (alliance=="Red") {
            allianceReverser=-1;
        }

        /***********************
         * Modifiable variables
         **********************/

        /***************
         * reset Timer Variables to false
         ***************/
        greenTimerElapsed = false;
        yellowTimerElapsed = false;
        redTimerElapsed = false;
        endGameStartElapsed = false;

        /*************
         * Assign Lights Variables
         *************/
       if(hasLightsHub) {
           blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

           if (alliance=="Red") {
               pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_RED;
           } else {
               pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_BLUE;
           }
           blinkinLedDriver.setPattern(pattern);
       }

        /*******************************
         * Assign Motor & Servo Variables
         ******************************/
        if(hasChassisMotors){
            frontLeft = hardwareMap.dcMotor.get("frontLeft");
            frontRight = hardwareMap.dcMotor.get("frontRight");
            backLeft = hardwareMap.dcMotor.get("backLeft");
            backRight = hardwareMap.dcMotor.get("backRight");
            frontLeft.setDirection(DcMotor.Direction.REVERSE);
            backLeft.setDirection(DcMotor.Direction.REVERSE);
        }
        if(hasAuxMotorsAndServos){
            leftClaw = hardwareMap.servo.get("leftClaw");
            leftClaw.setDirection(Servo.Direction.REVERSE);
            rightClaw = hardwareMap.servo.get("rightClaw");
            wrist = hardwareMap.servo.get("wrist");
            planeLaunchTrigger = hardwareMap.servo.get("planeLaunchTrigger");
            arm = hardwareMap.dcMotor.get("arm");
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //arm.setDirection(DcMotor.Direction.REVERSE);
            armExtension = hardwareMap.dcMotor.get("armExtension");
            armExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            armExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            armExtension.setDirection(DcMotor.Direction.REVERSE);
            armExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }


        /**********************************
         * Do Not set positions on initialize since that counts as controlling the robot
         * and initialize would not be able to happen until the timer starts for driver controlled period
         **********************************/


        /***********************************************
         * Tell driver station that initialization is complete
         **********************************************/
        telemetry.log().clear();
        telemetry.update();
        telemetry.log().add("Initialized. Control Pad Map = "+controlPadMap+". Go "+alliance+" alliance");

        telemetry.log().add(greenWarningTime+", " +
                yellowWarningTime+", " +
                redWarningTime+", " +
                endGameStart
        );

/***************************************************
  * Initialize the AprilTag Reader
 ***************************************************/
        //Copied from our auto Align class
        boolean targetFound     = false;    // Set to true when an AprilTag target is detected
        double  drive           = 0;        // Desired forward power/speed (-1 to +1)
        double  strafe          = 0;        // Desired strafe power/speed (-1 to +1)
        double  turn            = 0;        // Desired turning power/speed (-1 to +1)

        // Initialize the Apriltag Detection process
        initAprilTag();


        if (USE_WEBCAM)
            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur

        // Wait for driver to press start
        //End Copy
/***************************************************
 * End Initialize the AprilTag Reader
 ***************************************************/


        /************* Press Play Button ***********************/

        waitForStart();

        /************ START ***************/

        if(hasLightsHub) {
            //Use a gentle pattern during the normal part of the match
            //pattern = RevBlinkinLedDriver.BlinkinPattern.SINELON_LAVA_PALETTE;
            pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
            blinkinLedDriver.setPattern(pattern);
        }
        //Start timer here since play was just pressed
        endGameTimer = new ElapsedTime();
        endGameTimer.reset();

        while (opModeIsActive()) {
            /**************************************************************
             *
             * TeleOp Loops From Here to the End of controlRobot
             *
             * Loops often to see if controls are still the same
             *
             ****************************************************************/

            /***********************
             * Gather Button Input *
             **********************/

            if (controlPadMap=="Center Stage R1") {

/*************           Center Stage R1     Controls Update Status           **************/
                chassisSpeedSlow = gamepad1.left_trigger;             //Slow Speed
                chassisBumpLeftTurnStatus.update(gamepad1.left_bumper);             //Bump Left Turn




                chassisBumpForwardStatus.update(gamepad1.dpad_up);             //Bump Forward
                chassisBumpLeftStrafeStatus.update(gamepad1.dpad_left);             //Bump Left Strafe
                chassisBumpRightStrafeStatus.update(gamepad1.dpad_right);             //Bump Right Strafe
                chassisBumpBackStatus.update(gamepad1.dpad_down);             //Bump Backwards


                driveStick2 = gamepad1.left_stick_y;             //Drive 2
                turnStick = gamepad1.left_stick_x;             //Turn
                chassisSpeedFast = gamepad1.right_trigger;             //High Speed
                chassisBumpRightTurnStatus.update(gamepad1.right_bumper);             //Bump Right Turn

                startButton1Status.update(gamepad1.start);             //Pad Select (A & B)


                aprilTagCenterStatus.update(gamepad1.y);             //Aim to Center AprilTag
                aprilTagLeftStatus.update(gamepad1.x);             //Aim to Left AprilTag
                aprilTagRightStatus.update(gamepad1.b);             //Aim to Right AprilTag
                planeLaunchStatus.update(gamepad1.a);             //Launch Plane


                driveStick1 = gamepad1.right_stick_y;             //Drive 1
                strafeStick = gamepad1.right_stick_x;             //Strafe
                leftClawClose = gamepad2.left_trigger;             //Left Claw Close (Sticky)
                leftClawOpenStatus.update(gamepad2.left_bumper);             //Left Claw Open (Sticky)

                resetAdjustmentsStatus.update(gamepad2.guide);             //Reset Adjustments
                handHomeStatus.update(gamepad2.back);             //Hand to Home

                levelUpStatus.update(gamepad2.dpad_up);             //Move Hand to Next Level Higher
                wristAdjustLessStatus.update(gamepad2.dpad_left);             //Slightly Decrease Wrist
                wristAdjustMoreStatus.update(gamepad2.dpad_right);             //Slightly Increase Wrist
                levelDownStatus.update(gamepad2.dpad_down);             //Move Hand to Next Level Lower


                handVerticalStick = gamepad2.left_stick_y;             //Hand Vertical Move
                handHorizontalStick = gamepad2.left_stick_x;             //Hand Horizontal Move
                rightClawClose = gamepad2.right_trigger;             //Right Claw Close (Sticky)
                rightClawOpenStatus.update(gamepad2.right_bumper);             //Right Claw Open (Sticky)

                startButton2Status.update(gamepad2.start);             //Pad Select (A & B)








                handAssist = gamepad2.right_stick_y;             //Ride Height/Drop to 0

/*************           End     Center Stage R1     Updates            **************/
        }
            else {

                /***********************************
                 * Control Pad Map Selection Error *
                 **********************************/
                telemetry.log().add("Error in Control Map Selection"); telemetry.update();
                if(hasLightsHub){pattern = RevBlinkinLedDriver.BlinkinPattern.SHOT_RED; blinkinLedDriver.setPattern(pattern);}
                waitForStart(); sleep(30000);
            }

            displayTelemetry();

            if(resetAdjustmentsStatus.clickedUp){
                stackLevelAtHome = stackLevel;
                stackDistanceAtHome = stackDistance;
                armRotationDegreesAtHome = armRotationDegrees;
                armExtensionInchesAtHome =  armExtensionInches;
            }

            /*************************
             * Chassis bump controls
             *************************/
            if(hasChassisMotors && wantAutoChassisControls) {
                if (chassisBumpForwardStatus.clickedDown) {
                    Drive(driveBump, .5, 0);
                }
                if (chassisBumpBackStatus.clickedDown) {
                    Drive(-driveBump, .5, 0);
                }
                if (chassisBumpLeftStrafeStatus.clickedDown) {
                    Strafe(strafeBump, .5, 0);
                }
                if (chassisBumpRightStrafeStatus.clickedDown) {
                    Strafe(-strafeBump, .5, 0);
                }
                if (chassisBumpLeftTurnStatus.clickedDown) {
                    Turn(-turnBump, .5, 0);
                }
                if (chassisBumpRightTurnStatus.clickedDown) {
                    Turn(turnBump, .5, 0);
                }
            }

            /**********************************************************************************************
             * Speed adjust with triggers                                                                 *
             * If the chassisSpeedFast trigger is pulled, the motor speed will increase a constant amount *
             * If the chassisSpeedSlow trigger is pulled, the motor speed will decrease a constant amount *
             * chassisSpeedFast overrides chassisSpeedSlow                                                *
             *********************************************************************************************/
            if (chassisSpeedFast > 0) {
                powerRatio = driveFastRatio;
            } else if (chassisSpeedSlow > 0) {
                powerRatio = driveSlowRatio;
            }
            drivePower = defaultDrivePower*powerRatio;

            /**************************
             * Chassis drive controls *
             *************************/

            turnStick = turnStick * .85; //Turns are too fast, was 1.0
            blPower = drivePower * ((-driveStick2 + -driveStick1 + strafeStick) - turnStick);
            brPower = drivePower * ((-driveStick2 + -driveStick1 - strafeStick) + turnStick);
            flPower = drivePower * ((-driveStick2 + -driveStick1 + strafeStick) + turnStick);
            frPower = drivePower * ((-driveStick2 + -driveStick1 - strafeStick) - turnStick);

            /**************************************
             * AlignToAprilTag
             **************************************/

            if(alliance == "Red") {
                tagAdditional = 3;
            }
            if(aprilTagLeftStatus.clickedDown){
                alignToAprilTag(1+tagAdditional,false,true,true);
            } else if(aprilTagCenterStatus.clickedDown){
                alignToAprilTag(2+tagAdditional,false,true,true);
            } else if (aprilTagRightStatus.clickedDown){
                alignToAprilTag(3+tagAdditional,true,true,true);
            }

            /**************************************
             * End AlignToAprilTag
             **************************************/

            else {
                //Set motors to run manually from controller
                backLeft.setPower(blPower);
                backRight.setPower(brPower);
                frontLeft.setPower(flPower);
                frontRight.setPower(frPower);
            }



            /*************************
             * Aux drive controls
             *************************/
            /*************
             * Arm Controls
             *************/

            if (handVerticalStick < 0) {
                arm.setPower(defaultArmPower * powerRatio * (-handVerticalStick));
            } else {
                arm.setPower(defaultArmLowerPower * powerRatio * (-handVerticalStick));
            }

            armExtension.setPower(handHorizontalStick*defaultArmExtensionPower * powerRatio);
            if (handVerticalStick!=0){
                stackLevel = -1;
            }
            if (handHorizontalStick!=0){
                stackDistance = -1;
            }

            //handAssist
            if(handAssist<=-0.9){
                //Ride Height Desired
                stackLevel = handAssistRideHeightLevel;
                stackDistance = handAssistRideHeightDistance;
                aboveLevel = handAssistRideHeightAboveLevel;
                goToStackPosition(false,stackLevel,stackDistance,aboveLevel);
            }
            if(handAssist>=0.9){
                // Zero Height Desired
                stackLevel = 0;
                stackDistance = 0;
                aboveLevel = false;
                goToStackPosition(false,stackLevel,stackDistance,aboveLevel);
            }


            /************************
             * Stacker Controls
             ***********************/
            armRotationDegrees = (arm.getCurrentPosition() / mtzConstantsCS.ticksPerDegreeArm) + armRotationDegreesAtHome;
            armExtensionInches = armExtension.getCurrentPosition() / ticksPerInchExtension - armExtensionInchesAtHome;


            if(levelUpStatus.clickedDown){
                stackingDown=false;
                if(stackLevel!=-1 && stackLevel < stackHeightOnLevelArray.length-1){
                    stackLevel++;
                    stackDistance=stackLevel;
                }
                goToStackPosition(stackingDown,stackLevel,stackDistance,aboveLevel);
            }
            if(levelDownStatus.clickedDown){
                stackingDown=true;
                if(stackLevel!=-1){
                    if(aboveLevel){
                        //aboveLevel = false;
                    } else if(stackLevel!=0){
                        stackLevel--;
                        stackDistance=stackLevel;
                        //aboveLevel = true;
                    }
                }
                goToStackPosition(stackingDown,stackLevel,stackDistance,aboveLevel);
            }

            /*************
             * Wrist Adjuster
             *************/
            if (wristAdjustLessStatus.clickedDown) {
                wristAdjustment = wristAdjustment - wristBump;
            } else if (wristAdjustMoreStatus.clickedDown) {
                wristAdjustment = wristAdjustment + wristBump;
            }

            /********************************************
             *
             * Wrist Angle Auto Set
             *
             * without stacker controls
             *
             ********************************************/
            if(armRotationDegrees>20 && armRotationDegrees<70) {//Deliver on the backdrop at the front of the robot
                wristPositionDesired = wristAutoLevelDeliverFront(armRotationDegrees) + wristAdjustment;
                scoopStage = 2;
                debugString = "Front "+wristPositionDesired+"";

            }
            else if(armRotationDegrees>70) {//Deliver over the top to the rear
                wristPositionDesired = wristAutoLevelDeliverRear(armRotationDegrees) + wristAdjustment;
                scoopStage = 3;
                debugString = "Rear "+ wristPositionDesired+"";

            }
            else {//Scoop
                wristPositionDesired = wristAutoLevelScoop(armRotationDegrees) + wristAdjustment;
                scoopStage = 1;
                debugString = "Scoop "+wristPositionDesired+"";

            }

            if(wristPositionDesired < minWristPosition){
                wristPositionDesired = minWristPosition;
            }

            if(wristPositionDesired > maxWristPosition){
                wristPositionDesired = maxWristPosition;
            }

            //Set wrist position
            wrist.setPosition(wristPositionDesired);

            /*************
             * Claw Controls
             *************/

            if(leftClawClose>0.95){leftClawRemainClosed = true; }
            if(leftClawOpenStatus.clickedDown){leftClawRemainClosed = false;}
            if(rightClawClose>0.95){rightClawRemainClosed = true; }
            if(rightClawOpenStatus.clickedDown){rightClawRemainClosed = false;}
            if (leftClawRemainClosed) {
                leftClaw.setPosition(leftClawClosedPosition);
            } else {
                //Close claw to prorated level of between open and closed position based on the current trigger value between open trigger and closed trigger
                leftClaw.setPosition(prorate(leftClawClose,0,1,leftClawOpenPosition,leftClawClosedPosition));
            }
            if (rightClawRemainClosed) {
                rightClaw.setPosition(rightClawClosedPosition);
            } else {
                //Close claw to prorated level of between open and closed position based on the current trigger value between open trigger and closed trigger
                rightClaw.setPosition(prorate(rightClawClose,0,1,rightClawOpenPosition,rightClawClosedPosition));
            }


            /********************
             *
             * Plane Launcher
             *
             *******************/
            //launcher position starts as set when it is initialized above
            //If the launch button is pressed, the launch position should change to released
            //by using a variable to store the desired position, it can retain the position for each iteration of the loop
            if(planeLaunchStatus.clickedDown){
                launcherPosition = launcherReleasePosition;
            }
            planeLaunchTrigger.setPosition(launcherPosition);





            /*********************************
             * Check if timer has elapsed
             *********************************/


            if(hasLightsHub) {
                //Check for End Timer First
                if (endGameTimer.seconds() < tempLightsTimer){
                    blinkinLedDriver.setPattern(tempLightsPattern);
                } else if (endGameTimer.seconds() > endGameOver) {
                    endGameStartElapsed = true;
                    pattern = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
                    blinkinLedDriver.setPattern(pattern);
                } else if (endGameTimer.seconds() > endGameWarning2) {
                    endGameStartElapsed = true;
                    pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_RED;
                    blinkinLedDriver.setPattern(pattern);
                } else if (endGameTimer.seconds() > endGameWarning) {
                    endGameStartElapsed = true;
                    pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
                    blinkinLedDriver.setPattern(pattern);
                } else if (endGameTimer.seconds() > endGameStart) {
                    endGameStartElapsed = true;
                    pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
                    blinkinLedDriver.setPattern(pattern);
                } else if (endGameTimer.seconds() > redWarningTime) { //Then check for red
                    redTimerElapsed = true;
                    pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
                    blinkinLedDriver.setPattern(pattern);
                } else if (endGameTimer.seconds() > yellowWarningTime) { //Then check for yellow
                    yellowTimerElapsed = true;
                    pattern = RevBlinkinLedDriver.BlinkinPattern.YELLOW;
                    blinkinLedDriver.setPattern(pattern);
                } else if (endGameTimer.seconds() > greenWarningTime) { //Then check for green
                    greenTimerElapsed = true;
                    pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
                    blinkinLedDriver.setPattern(pattern);
                }

                //Stop supporting arm 5 seconds after end game
                if (endGameTimer.seconds() > endGameOver + 5) {
                    armAssistLevel = 0;
                }
            }
        }
    }

    /*******************************
     * End of Control Robot Method
     ******************************/

    /****************************************************************************
     * Motion Methods                                                           *
     * These methods cause motion in the robot when called by the program above *
     ***************************************************************************/

    /*****************
     * Drive
     *
     * Propels the robot forward or backward a certain number of inches with all 4 wheels turning the same direction
     *
     * @param distance inches
     * @param motorPower
     * @param pause
     * @throws InterruptedException
     */
    public void Drive(double distance, double motorPower, int pause) throws InterruptedException {
        if(hasChassisMotors) {
            if (opModeIsActive()) {
                StopAndResetDriveEncoders();
                DriveByInches(distance);
                RunDriveToPosition();
                DrivePower(motorPower);
                while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                    DisplayDriveTelemetry();
                }
                Thread.sleep(pause);
            }
        }
    }

    /***********
     * Strafe
     *
     * Propels the robot sideways a certain number of inches
     *
     * @param leftDistance inches
     * @param power
     * @param pause
     * @throws InterruptedException
     */
    public void Strafe(double leftDistance, double power, int pause) throws InterruptedException {
        if(hasChassisMotors) {
            //Left is positive
            if (opModeIsActive()) {
                StopAndResetDriveEncoders();
                StrafeByInches(leftDistance);
                RunDriveToPosition();
                DrivePower(power);
                while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                    DisplayDriveTelemetry();
                }
                Thread.sleep(pause);
            }
        }
    }

    /*******
     * Turn
     *
     * Rotates the robot about the center of the 4 wheels
     * Left is negative degrees
     *
     * @param rightDegrees
     * @param power
     * @param pause
     * @throws InterruptedException
     */
    public void Turn(double rightDegrees, double power, int pause) throws InterruptedException {
        if(hasChassisMotors) {
            //Left is negative
            if (opModeIsActive()) {
                StopAndResetDriveEncoders();
                TurnByAngle(rightDegrees);
                RunDriveToPosition();
                DrivePower(power);
                while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                    DisplayDriveTelemetry();
                }
                Thread.sleep(pause);
            }
        }
    }

    //Arm Methods

    public void goToStackPosition(boolean stackingDown,int stackLevel,int stackDistance,boolean aboveLevel) throws InterruptedException {
        double vertDesired, horDesired, degreesDesired, vertRequired,armSpeed;
        if (stackLevel<0){
            stackLevel = findStackLevel();
            if(stackingDown){
                aboveLevel = true;
            }
        }
        if (stackDistance<0){
            stackDistance = findStackDistance();
        }
        if(stackLevel>stackHeightOnLevelArray.length-1){
            stackLevel = stackHeightOnLevelArray.length-1;
        }
        if(stackDistance>stackDistanceArray.length-1){
            stackDistance = stackDistanceArray.length-1;
        }
        if (stackLevel >= 0 && stackDistance >= 0) {
            // Check if the stone is getting set down on a level and go slow if so
            if (aboveLevel) {
                vertDesired = stackHeightAboveLevelArray[stackLevel];
                armSpeed = mtzConstantsCS.defaultArmLowerPower;
            } else {
                vertDesired = stackHeightOnLevelArray[stackLevel];
                armSpeed = mtzConstantsCS.defaultArmLowerPower / 3;
            }
            if (!stackingDown) {
                armSpeed = mtzConstantsCS.defaultArmPower;
            }

            vertRequired = vertDesired - armPivotHeight;
            horDesired = stackDistanceArray[stackDistance];

            degreesDesired = Math.toDegrees(Math.asin((vertRequired) / (armLengthDesired(horDesired, vertDesired))));

            //Stay in the max & min
            armExtensionInches = armLengthDesired(horDesired, vertDesired) - armExtensionCollapsedLength;
            if(armExtensionInches < minArmExtensionInches){
                armExtensionInches = minArmExtensionInches;
            } else if(armExtensionInches > maxArmExtensionInches){
                armExtensionInches = maxArmExtensionInches;
            }
            if(degreesDesired < minArmDegrees){
                degreesDesired = minArmDegrees;
            } else if(degreesDesired > maxArmDegrees){
                degreesDesired = maxArmDegrees;
            }
            stackDegreesDesired = degreesDesired;
            // Set the target positions to run to
            raiseByDegrees(degreesDesired);
            //wristPositionDesired = wristAutoLevel(degreesDesired);
            horizontalDesired = horDesired;
            verticalDesired = vertDesired;
            if(hasAuxMotorsAndServos) {
                armExtension.setTargetPosition((int) ((armExtensionInches - armExtensionInchesAtHome) * ticksPerInchExtension));

                if (opModeIsActive()) {
                    // Turn motors on to let them reach the target if the stop button hasn't been pressed
                    arm.setPower(armSpeed);
                    armExtension.setPower(defaultArmExtensionPower);
                    //Wrist Position is set once so it doesn't try to go to 2 different positions each loop iteration
                    // wrist.setPosition(wristPositionDesired);
                    while (arm.isBusy() || armExtension.isBusy()) {
                        DisplayArmTelemetry();
                    }
                }

            }
            Thread.sleep(defaultPauseTime);
        }
    }

    public void RaiseArm(double degrees, double power,int pause) throws InterruptedException {
        if(hasAuxMotorsAndServos) {
            if (opModeIsActive()) {
                raiseByDegrees(degrees);
                ArmPower(power);
                while (arm.isBusy() || armExtension.isBusy()) {
                    DisplayArmTelemetry();
                }
            }
            Thread.sleep(pause);
        }
    }
    public void LowerArm(double degrees, double power, int pause) throws InterruptedException {
        if(hasAuxMotorsAndServos) {
            if (opModeIsActive()) {
                raiseByDegrees(-degrees);
                ArmPower(power);
                while (arm.isBusy() || armExtension.isBusy()) {
                    DisplayArmTelemetry();
                }
            }
            Thread.sleep(pause);
        }
    }
    public double wristAutoLevelScoop(double armAngle){
        return wristConversionToServo(55 - armAngle);
    }
    public double wristAutoLevelDeliverFront(double armAngle){
        return wristConversionToServo(140 - armAngle);
    }
    public double wristAutoLevelDeliverRear(double armAngle){
        return wristConversionToServo(armAngle + 30);
    }

    public void ExtendArm(double desiredArmLength, double power,int pause) throws InterruptedException {
        if (opModeIsActive()) {
            armExtensionInches = desiredArmLength - armExtensionCollapsedLength;
            if(armExtensionInches<minArmExtensionInches){
                armExtensionInches=minArmExtensionInches;
            } else if(armExtensionInches>maxArmExtensionInches){
                armExtensionInches=maxArmExtensionInches;
            }
            if(hasAuxMotorsAndServos) {
                armExtension.setTargetPosition((int) (armExtensionInches * ticksPerInchExtension));
                armExtension.setPower(power);

                while (arm.isBusy() || armExtension.isBusy()) {
                    DisplayArmTelemetry();
                }
            }
        }
        Thread.sleep(pause);
    }

//Encoder Methods

    public void StopAndResetAllEncoders() {
        if(hasChassisMotors) {
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if(hasAuxMotorsAndServos){
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }
    public void StopAndResetDriveEncoders() {
        if(hasChassisMotors) {
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }
    public void RunDriveToPosition() {
        if(hasChassisMotors) {
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
    public void RunArmToPosition() {
        if(hasAuxMotorsAndServos) {
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
    public void RunArm() {
        if(hasAuxMotorsAndServos) {
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }


    //End of Encoder Methods

//Distance Calculation Methods

    public void DriveByInches(double distance) {
        if(hasChassisMotors) {
            frontLeft.setTargetPosition((int) (distance * ticksPerInchWheelDrive));
            frontRight.setTargetPosition((int) (distance * ticksPerInchWheelDrive));
            backLeft.setTargetPosition((int) (-1 * distance * ticksPerInchWheelDrive));
            backRight.setTargetPosition((int) (-1 * distance * ticksPerInchWheelDrive));
        }
    }

    public void StrafeByInches(double distance) {
        if(hasChassisMotors) {
            frontLeft.setTargetPosition((int) (distance * ticksPerInchWheelStrafe));
            frontRight.setTargetPosition((int) (-distance * ticksPerInchWheelStrafe));
            backLeft.setTargetPosition((int) (distance * ticksPerInchWheelStrafe));
            backRight.setTargetPosition((int) (-distance * ticksPerInchWheelStrafe));
        }
    }

    public void TurnByAngle(double degrees) {
        if(hasChassisMotors) {
            frontLeft.setTargetPosition((int) (degrees * ticksPerDegreeTurnChassis));
            frontRight.setTargetPosition((int) (-degrees * ticksPerDegreeTurnChassis));
            backLeft.setTargetPosition((int) (-degrees * ticksPerDegreeTurnChassis));
            backRight.setTargetPosition((int) (degrees * ticksPerDegreeTurnChassis));
        }
    }
    public void raiseByDegrees(double degrees) {
        if(hasAuxMotorsAndServos){
            arm.setTargetPosition((int)((degrees + armRotationDegreesAtHome) * mtzConstantsCS.ticksPerDegreeArm));
        }
    }

    //End of distance calculation methods

//Power Methods

    public void DrivePower(double power) {
        if(hasChassisMotors) {
            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);
        }
    }
    public void ArmPower(double power) {
        if(hasAuxMotorsAndServos) {
            arm.setPower(power);
        }
    }
//End Power Methods

    /*******************************
     * Moves Robot for AutoAlign
     * @param x strafe
     * @param y Drive
     * @param yaw Turn
     */
    public void moveRobot(double x, double y, double yaw) {
        // Calculate wheel powers.
        double leftFrontPower    =  x -y -yaw;
        double rightFrontPower   =  x +y +yaw;
        double leftBackPower     =  x +y -yaw;
        double rightBackPower    =  x -y +yaw;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // Send powers to the wheels.
        frontLeft.setPower(leftFrontPower);
        frontRight.setPower(rightFrontPower);
        backLeft.setPower(leftBackPower);
        backRight.setPower(rightBackPower);
    }

    /**************************************
     * alignToAprilTag
     * @param tagID
     * @param leftOfCamera
     * @param leftOfMountain
     * @param leftPixelInScoop
     * @throws InterruptedException
     */
    public void alignToAprilTag(int tagID, boolean leftOfCamera,boolean leftOfMountain, boolean leftPixelInScoop) throws InterruptedException {

        boolean targetFound     = false;    // Set to true when an AprilTag target is detected
        double  drive           = 0;        // Desired forward power/speed (-1 to +1)
        double  strafe          = 0;        // Desired strafe power/speed (-1 to +1)
        double  turn            = 0;        // Desired turning power/speed (-1 to +1)
        boolean stillAligning = true;
        double sweepCounter = 0;
        double maxSweep = 5;
        int sweepDirection = 1;
        double headingOffset = 0;
        DESIRED_TAG_ID = tagID;
        targetFound = false;
        desiredTag  = null;
        if (leftOfCamera){
            headingOffset = cameraBearingOffsetLeftTagLeftPixelLeftSide;
            if(!leftOfMountain) {
                headingOffset = headingOffset + distanceBetweenValleys;
            }

            if(!leftPixelInScoop){
                headingOffset = headingOffset - distanceBetweenScoopPositions;
            }
        } else {
            DESIRED_TAG_ID = tagID+1;
            headingOffset = cameraBearingOffsetRightTagRightPixelRightSide;
            if(leftOfMountain) {
                headingOffset = headingOffset - distanceBetweenValleys;
            }

            if(leftPixelInScoop){
                headingOffset = headingOffset + distanceBetweenScoopPositions;
            }
        }

        while (aprilTagCenterStatus.isDown || aprilTagLeftStatus.isDown || aprilTagRightStatus.isDown)   // Loop to find the tag and drive to it
        {
            // Step through the list of detected tags and look for a matching tag
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                // Look to see if we have size info on this tag.
                if (detection.metadata != null) {
                    //  Check to see if we want to track towards this tag.
                    if ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID)) {
                        // Yes, we want to use this tag.
                        targetFound = true;
                        desiredTag = detection;
                        break;  // don't look any further.
                    } else {
                        // This tag is in the library, but we do not want to track it right now.
                        telemetry.addData("Skipping", "Tag ID %d is not desired", detection.id);
                    }
                } else {
                    // This tag is NOT in the library, so we don't have enough information to track to it.
                    telemetry.addData("Unknown", "Tag ID %d is not in TagLibrary", detection.id);
                }
            }

            // Tell the driver what we see, and what to do.
            if (targetFound) {
                telemetry.addData("\n>","HOLD Left-Bumper to Drive to Target\n");
                telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
                telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);
                telemetry.addData("Bearing","%3.0f degrees", desiredTag.ftcPose.bearing);
                telemetry.addData("Yaw","%3.0f degrees", desiredTag.ftcPose.yaw);
            } else {
                telemetry.addData("\n>","Sweeping\n");
            }

            // If we have found the desired target, Drive to target Automatically .
            if (targetFound) {

                // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
                double  rangeError      = (desiredTag.ftcPose.range - mtzConstantsCS.backdropAprilTagDESIRED_DISTANCE);
                double  headingError    = desiredTag.ftcPose.bearing + mtzConstantsCS.cameraBearingOffsetLeftTagLeftPixelLeftSide;
                double  yawError        = desiredTag.ftcPose.yaw;

                if (rangeError <mtzConstantsCS.alignConfidence && headingError<mtzConstantsCS.alignConfidence && yawError <mtzConstantsCS.alignConfidence){
                    return;
                }
                // Use the speed and turn "gains" to calculate how we want the robot to move.
                drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
                turn   = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
                strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

                telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
            } else {

                // turn to find tag
                if (Math.abs(sweepCounter)<maxSweep) {
                    turn = -.1 * sweepDirection;
                    sweepCounter = sweepCounter + sweepDirection;
                }
                else {
                    sweepDirection = -1 * sweepDirection;
                    turn = -.1 * sweepDirection;
                    sweepCounter = sweepCounter + sweepDirection;
                }
                telemetry.addData("Manual","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
            }
            telemetry.update();

            // Apply desired axes motions to the drivetrain.
            moveRobot(drive, strafe, turn);
            sleep(10);
        }
        telemetry.addLine("Something went wrong with the alignment");
        return;
    }


    //Telemetry Methods
    public void displayTelemetry() {
        telemetry.clearAll();
        telemetry.addLine()
                .addData("Timer: ", endGameTimer.toString());
        telemetry.addLine()
                .addData("Blinkin: ", pattern);
        telemetry.addLine()
                .addData("Default Arm: ", defaultArmPower);
        telemetry.addLine()
                .addData("Arm Power: ", arm.getPower());
        telemetry.addLine()
                .addData("Arm Extension Power: ", armExtension.getPower());
        telemetry.addLine()
                .addData("Arm Rotation: ", armRotationDegrees);
        telemetry.addLine()
                .addData("leftClaw: ", leftClaw.getPosition());
        telemetry.addLine()
                .addData("rightClaw: ", rightClaw.getPosition());
        telemetry.addLine()
                .addData("Scoop Stage: ", scoopStage);
        telemetry.addLine()
                .addData("wrist: ", wrist.getPosition());
        telemetry.addLine()
                .addData("Plane Launch: ", planeLaunchTrigger.getPosition());
        telemetry.addLine()
                .addData("Debug String: ", debugString);


        if(hasChassisMotors) {
            telemetry.addLine()
                    .addData("Front Left Power: ", frontLeft.getPower());
            telemetry.addLine()
                    .addData("Front Right Power: ", frontRight.getPower());
            telemetry.addLine()
                    .addData("Back Left Power: ", backLeft.getPower());
            telemetry.addLine()
                    .addData("Back Right Power: ", backRight.getPower());
        }
        telemetry.update();
    }
    public void DisplayDriveTelemetry() {
        if(hasChassisMotors) {
            double frontLeftInches = frontLeft.getCurrentPosition() / ticksPerInchWheelDrive;
            double frontRightInches = frontRight.getCurrentPosition() / ticksPerInchWheelDrive;
            double backLeftInches = backLeft.getCurrentPosition() / ticksPerInchWheelDrive;
            double backRightInches = backRight.getCurrentPosition() / ticksPerInchWheelDrive;
            telemetry.clear();
            telemetry.addLine()
                    .addData("F Left : ", (int) frontLeftInches + "in, Power: " + "%.1f", frontLeft.getPower());
            telemetry.addLine()
                    .addData("F Right: ", (int) frontRightInches + "in, Power: " + "%.1f", frontRight.getPower());
            telemetry.addLine()
                    .addData("B Left : ", (int) backLeftInches + "in, Power: " + "%.1f", backLeft.getPower());
            telemetry.addLine()
                    .addData("B Right: ", (int) backRightInches + "in, Power: " + "%.1f", backRight.getPower());
            telemetry.addLine()
                    .addData("Arm: ", (int) arm.getCurrentPosition()/ticksPerDegreeArm + "deg, Power: " + "%.1f", arm.getPower());
            telemetry.update();
        }
        if(hasAuxMotorsAndServos){

        }

    }
    public void DisplayArmTelemetry() {
        if(hasAuxMotorsAndServos) {
            double armDegrees = arm.getCurrentPosition() / mtzConstantsCS.ticksPerDegreeArm;
            telemetry.clear();
            telemetry.addLine()
                    .addData("Arm Degrees ", (int) armDegrees + "  Power: " + "%.1f", arm.getPower());
            telemetry.addLine()
                    .addData("Arm Ext Inches ", (int) armExtensionInches + "  Power: " + "%.1f", armExtension.getPower());
            telemetry.update();
        }
    }
    //End of Telemetry Methods

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {
        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(2);

        // Create the vision portal by using a builder.
        if (USE_WEBCAM) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();
        } else {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(aprilTag)
                    .build();
        }
    }

    /*
     Manually set the camera gain and exposure.
     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
    */
    private void    setManualExposure(int exposureMS, int gain) {
        // Wait for the camera to be open, then use the controls

        if (visionPortal == null) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                sleep(20);
            }
            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }

        // Set camera controls unless we are stopping.
        if (!isStopRequested())
        {
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                sleep(50);
            }
            exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
            sleep(20);
            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            sleep(20);
        }
    }
    //End of Class
}