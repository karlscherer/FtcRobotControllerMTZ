package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.mtzConstantsCS.defaultArmExtensionPower;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.defaultArmPower;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.leftClawBoxPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.leftClawMaxOpenPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.rightClawBoxPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.rightClawMaxOpenPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.sampleDetectionPosition;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.ticksPerDegreeArm;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.ticksPerDegreeTurnChassis;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.ticksPerInchExtension;
import static org.firstinspires.ftc.teamcode.mtzConstantsCS.ticksPerRevolution1150;

import android.graphics.Color;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.io.IOException;
import java.util.List;

@Autonomous(name ="Auto Controls Test", group = "Bottom")
//@Disabled

/*************************
 * This class is intended to be a sub class to run the robot autonomously.
 * The following methods are available to super classes:
 *
 *      autoPaths  specify different path strings to use and whether the arm needs support
 *
 * v100 Copied from Last Year
 * v101 Updated paths from Meet 1 & added logging
 * v102 Cleaned Up Code
 * v103 Lucy worked on path for drop and parking at practice
 * v104 Fine tuned paths and added right side to Shannon & Lucy
 * v105 Competition Updates during meet 2
 * v106 Adding in Gyro
 * v107 Correcting Gyro at 30Jan2023 Practice
 * v108 From 2023
 * v109 Added paths for Center Stage without sensing
 * v110
 *
 *******************/

public class AutoControlsMTZ_test extends LinearOpMode {


    /**************
     *
     * Modify these speeds to help with diagnosing drive errors
     *
     **************/
    private static final double defaultDriveSpeed = 0.1;
    private static final double defaultTurnSpeed = 0.1;
    private static final int defaultPauseTime = 2300;

    /**********************
     * These variables are the constants in path commands
     **********************/
    private static final double ticksPerRevolution = ticksPerRevolution1150;
    private static final double gearReduction = 1.0;
    private static final double wheelDiameterInches = 4.0;
    private static final double pi = 3.1415;
    private static final double conversionTicksToInches = (ticksPerRevolution * gearReduction) / (pi * wheelDiameterInches);
    private static final double armDistanceAdjustment = 39.4;
    private static final double strafeDistanceAdjustment = 1.15;

    private static final double driveDistanceAdjustment = .85;
    private int allianceReverser = 1;


    public int armOdometer=0;
    public int extendOdometer=0;


    /******************
     * Declare the gyro
     */


    // The IMU sensor object
    BNO055IMU imuForDisplay;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    private double currAngle = 0.0;


    /*****************
     * Declare motor & servo objects
     ****************/
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private DcMotor armExtension;
    private DcMotor flywheel;
    //private Servo claw;
    private Servo leftClaw;
    private Servo rightClaw;
    private ColorSensor leftColorSensor;
    private ColorSensor rightColorSensor;


    /**************
     * Sampling variables
     */

    int randomNumberResult = 0;

    /***********
     * Lights Control Declarations
     ***********/

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    /*********************
     * Start TensorFlow Set-up
     */

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    /**
     * The variable to store our instance of the TensorFlow Object Detection processor.
     */
    private TfodProcessor tfod;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    //End TensorFlow Set-up



    @Override

    /**************
     *
     * The method below runs when this specific opMode is selected to run.
     * Typically this is used for testing versions of this opMode
     * since the super classes will only call the unversioned file
     *
     * A typical path is used for reference between versions
     *
     **************/
    public void runOpMode() throws InterruptedException {
        try {
            autoPaths("Red","default",false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**************
     *
     * The autoPaths method is called from super classes to specify the alliance and path
     *
     **************/
    public void autoPaths(String alliance,String pathToRun,Boolean supportArm) throws InterruptedException, IOException {

        Logging.setup();
        Logging.log("Starting AutoPaths method");
        Logging.log("Path to Run: " + pathToRun);

        /************
         * Assign gyro
         */
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        imuForDisplay = hardwareMap.get(BNO055IMU.class, "imu");
        imuForDisplay.initialize(parameters);


        /**************
         *
         * Assign motors and servos
         *
         */
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftColorSensor = hardwareMap.get(ColorSensor.class, "sensor_color");
        rightColorSensor = hardwareMap.get(ColorSensor.class, "sensor_color2");

        leftClaw = hardwareMap.servo.get("leftClaw");
        rightClaw = hardwareMap.servo.get("rightClaw");
        leftClaw.setDirection(Servo.Direction.REVERSE);
        //rightClaw.setDirection(Servo.Direction.REVERSE);
        arm = hardwareMap.dcMotor.get("arm");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //arm.setDirection(DcMotor.Direction.REVERSE);
        armExtension = hardwareMap.dcMotor.get("armExtension");
        armExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armExtension.setDirection(DcMotor.Direction.REVERSE);
        armExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armOdometer=0;



        /*************
         * Set Lights Variables to the color for the alliance
         *************/
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        if (alliance=="Blue") {
            pattern = RevBlinkinLedDriver.BlinkinPattern.BREATH_BLUE;
        } else if (alliance=="Red") {
            pattern = RevBlinkinLedDriver.BlinkinPattern.BREATH_RED;
        }
        blinkinLedDriver.setPattern(pattern);


        /********
         * Movement starts here on initialize
         */


        //This was commented out in the code that was running in meet 1
        //Leaving it in to see if the arm behaves better after being reset
        StopAndResetAllEncoders();

        leftClaw.setPosition(leftClawBoxPosition);
        rightClaw.setPosition(rightClawBoxPosition);
        //}
        telemetry.log().clear();
        telemetry.update();
        telemetry.log().add(pathToRun+" Initialized. Go "+alliance+" alliance");

        //Paths written for Blue alliance and reverse turns if on Red alliance
        allianceReverser=1;
        if (alliance=="Blue") {
            allianceReverser=-1;
        }
        /************************************************************
         * ******************************************************** *
         * ******************************************************** *
         *
         * Paths            Paths            Paths          Paths   *
         *
         * ******************************************************** *
         * ******************************************************** *
         ************************************************************/

        /*******************
         * Default Path
         ******************/
        if (pathToRun=="default"){
            pathToRun="Backdrop Sample";
        }

        /*****************************************************************************
         * Coding Instructions
         *
         * Write paths for Blue alliance and apply reverser on turns and strafes
         *
         ****************************************************************************/


        if (pathToRun == "Backdrop" || pathToRun == "Audience" || pathToRun == "Backdrop Sample" || pathToRun == "Audience Sample"){
            /*****************
             * Path Branch 4 *
             ****************/
            Logging.log("Running Path Branch 4");
            /************************************
             * Path set up -- Add to each path
             ***********************************/
            //Robot Setup Notes
            telemetry.log().add("Line up notes should be entered in. ");
            waitForStart();

            sleep(1000);

            double ExtraDistance = 0;
            if (pathToRun == "Audience"){
                ExtraDistance = 40;
            }
            if (pathToRun == "Backdrop Sample" || pathToRun == "Audience Sample"){ //Need to Sample
                randomNumberResult = determinePixelLocation(); //Find
                Drive(16, defaultDriveSpeed, defaultPauseTime); //Drive forward to spike area
                if (randomNumberResult==1) {
                    Turn(-30,defaultDriveSpeed,defaultPauseTime);
                    Drive(32, defaultDriveSpeed, defaultPauseTime); //Drive forward to center spike
                    Drive(-32, defaultDriveSpeed, defaultPauseTime); //Drop pixel and backup close to original position
                    Turn(30,defaultDriveSpeed,defaultPauseTime);
                }
                if (randomNumberResult==2) {
                    Drive(32, defaultDriveSpeed, defaultPauseTime); //Drive forward to center spike
                    Drive(-32, defaultDriveSpeed, defaultPauseTime); //Drop pixel and backup close to original position
                }
                if (randomNumberResult==3) {
                    Turn(30,defaultDriveSpeed,defaultPauseTime);
                    Drive(32, defaultDriveSpeed, defaultPauseTime); //Drive forward to center spike
                    Drive(-32, defaultDriveSpeed, defaultPauseTime); //Drop pixel and backup close to original position
                    Turn(-30,defaultDriveSpeed,defaultPauseTime);
                }

            } else {
                Drive(48, defaultDriveSpeed, defaultPauseTime); //Drive forward to center spike
                Drive(-32, defaultDriveSpeed, defaultPauseTime); //Drop pixel and backup close to original position
            }
            Turn(90,defaultTurnSpeed, defaultPauseTime); //Turn towards the backdrop
            Strafe(15*allianceReverser, defaultDriveSpeed, defaultPauseTime); //straighten up on the wall
            Strafe(-6*allianceReverser, defaultDriveSpeed, defaultPauseTime); //move away from the wall
            Drive(24 + ExtraDistance, defaultDriveSpeed, defaultPauseTime); //Travel back to backdrop area
            Strafe(-20*allianceReverser, defaultDriveSpeed, defaultPauseTime); //slide in front of backdrop

            //slide over in front of the april tag for the drop

            int aprilTagSpacing = 8; //set a distance between AprilTags on the backdrop
            //The 2 alliances have different order of numbers from the wall to the center
            int backdropAprilTagTargetOffset = 0;
            if (allianceReverser==1){
                backdropAprilTagTargetOffset =randomNumberResult*aprilTagSpacing;
            } else {
                backdropAprilTagTargetOffset =(4-randomNumberResult)*aprilTagSpacing;
            }

            Strafe(backdropAprilTagTargetOffset*allianceReverser,defaultDriveSpeed,defaultPauseTime); //Slide over the offset amount

            //Deliver Pixel

            //align to aprilTag corresponding to random number result and alliance (1-3 Blue, 4-6 Red)

            //Need aligning code


            RaiseArm(10,defaultPauseTime); //Raise arm

            ExtendArm(5,defaultArmExtensionPower,defaultPauseTime); //extend arm
            leftClaw.setPosition(leftClawMaxOpenPosition); //release left pixel
            rightClaw.setPosition(rightClawMaxOpenPosition); //release left pixel
            RaiseArm(1,defaultPauseTime); //raise arm a little more
            ReturnExtension(); //retract arm
            RaiseArm(-11,defaultPauseTime);//lower arm

            //Park

            Drive(-4, defaultDriveSpeed, defaultPauseTime); //Back up a little
            if(pathToRun=="Audience" || pathToRun=="Audience Sample"){
                Strafe(backdropAprilTagTargetOffset+26 * allianceReverser, defaultDriveSpeed, defaultPauseTime); //Slide to wall
            } else {
                Strafe(backdropAprilTagTargetOffset-40 * allianceReverser, defaultDriveSpeed, defaultPauseTime); //Slide away from wall towards center
            }
            Drive(14, defaultDriveSpeed, defaultPauseTime);//Forward to park area
            ReturnArm(); //Lower arm to floor

        }

        else if (pathToRun == "ArmTest") {
            /*****************
             * Path Branch 4 *
             ****************/
            Logging.log("Running Path Branch 4");
            /************************************
             * Path set up -- Add to each path
             ***********************************/
            //Robot Setup Notes
            telemetry.log().add("Line up notes should be entered in. ");
            waitForStart();
            //arm.setPower();

            RaiseArm(1, defaultPauseTime);
            sleep(20000);


        }

        else if (pathToRun=="Calibrate") {
            /************************************
             * Path set up -- Add to each path
             ***********************************/
            //Robot Setup Notes
            telemetry.log().add("Robot Raises Arm 10, Moves Forward 24, then Left 24, then Rotate Left 180");
            waitForStart();
            /************
             * Path Start
             ************/
            RaiseArm(10,2000);
            Drive(24,defaultDriveSpeed,5000);
            Strafe(-24,defaultDriveSpeed,10000);
            Turn(-180,defaultTurnSpeed,0);
            /************
             * Path End *
             ***********/
        }
        //Path Selection Error
        else {
            /************************************
             * Path Selection Error
             ***********************************/
            //Robot Setup Notes
            telemetry.log().add("Error in Path Selection");
            telemetry.update();
            if (alliance=="Blue") {
                pattern = RevBlinkinLedDriver.BlinkinPattern.SHOT_BLUE;
            } else if (alliance=="Red") {
                pattern = RevBlinkinLedDriver.BlinkinPattern.SHOT_RED;
            }
            blinkinLedDriver.setPattern(pattern);
            waitForStart();
            /************
             * Path Start
             ************/
            sleep(30000);

            /************
             * Path End *
             ***********/
        }

        // End of Paths
        sleep(30000); //Allow the timer to run to the end so that nothing else happens before the timer is up
    }


    /**********************
     * Path Methods
     **********************/
    public void goToFoundationfromWall(int allianceReverser) throws InterruptedException{

        //Align Hooks With Foundation
        Drive(24, defaultDriveSpeed, defaultPauseTime);
        Strafe(allianceReverser * -12, defaultDriveSpeed, defaultPauseTime);
        Drive(5, defaultDriveSpeed, defaultPauseTime);
    }
    public void moveFoundation (int allianceReverser) throws InterruptedException{

        //Hook Foundation
        //HooksDown();

        //Move Foundation to Build Zone
        Drive(-20, 0.2, defaultPauseTime);
        Turn(allianceReverser * 40, 0.2, defaultPauseTime);
        Drive(5, -0.2, defaultPauseTime);
        Turn(allianceReverser * 80, 0.2, defaultPauseTime);
        Strafe(allianceReverser * 5, 0.2, defaultPauseTime);
        Drive(12, 0.1, defaultPauseTime);

        //Unhook Foundation
        //HooksUp();
    }
    public void foundationToAudienceDepot(int allianceReverser) throws InterruptedException {
        /***
         * Travel to Audience
         * Forwards 10
         * Turn Audience to Bridge 90° Fast
         * Backwards 24
         * Strafe towards audience Fast with tweak towards wall
         * Strafe towards audience slow for 6
         */

        Drive(18, defaultDriveSpeed, defaultPauseTime);
        Turn(allianceReverser * -90, defaultTurnSpeed, defaultPauseTime);
        Drive(-30, defaultDriveSpeed, defaultPauseTime);
        Strafe(allianceReverser * -4*24, defaultDriveSpeed*2, defaultPauseTime);
        Drive(-12, defaultDriveSpeed, defaultPauseTime);
        Drive(10, defaultDriveSpeed, defaultPauseTime);
        Strafe(allianceReverser * -18, defaultDriveSpeed/2, defaultPauseTime);
        Strafe(allianceReverser * 8, defaultDriveSpeed/2, defaultPauseTime);

    }
    public void quarryToMovedFoundation (int allianceReverser) throws InterruptedException{
        /*********
         * Turn bridge to Building Site 90° Fast
         * Strafe towards Wall 24 Fast
         * Forward with tweak towards Wall for 96
         * Strafe Bridge and forward 6
         */
        Turn(allianceReverser*-90,defaultTurnSpeed,defaultPauseTime);
        Strafe(allianceReverser*24,defaultDriveSpeed,defaultPauseTime);
        Drive(3*24, defaultDriveSpeed*2, defaultPauseTime);
        Turn(allianceReverser*20,defaultTurnSpeed,defaultPauseTime);
        Drive(24, defaultDriveSpeed*2, defaultPauseTime);
    }
    public void grabSkyStone(int allianceReverser) throws InterruptedException {
        //Angle towards skystone
        //CloseClaw();
        //claw.setPosition(mtzConstants.clawClosedPosition);
        //Wait for it to close
        sleep(1000);
        //Raise Arm
        RaiseArm(4,defaultPauseTime);
    }

    /**********************
     * Sampling Methods
     **********************/

    public int determinePixelLocation () throws InterruptedException {
        int pixelLocation = 0;
        int pixelTimer = 10000;
        if (opModeIsActive()) {
            while (opModeIsActive() && pixelTimer>0) {

                telemetryTfod();

                // Push telemetry to the Driver Station.
                telemetry.update();

                // Save CPU resources; can resume streaming when needed.
                if (gamepad1.dpad_down) {
                    visionPortal.stopStreaming();
                } else if (gamepad1.dpad_up) {
                    visionPortal.resumeStreaming();
                }
                List<Recognition> currentRecognitions = tfod.getRecognitions();
                telemetry.addData("# Objects Detected", currentRecognitions.size());

                // Step through the list of recognitions and display info for each one.
                for (Recognition recognition : currentRecognitions) {
                    double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
                    double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

                    telemetry.addData(""," ");
                    telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                    telemetry.addData("- Position", "%.0f / %.0f", x, y);
                    telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
                    if (x<100) {
                        pixelLocation = 1;
                    }
                    else if (x>=100 && x<200){
                        pixelLocation = 2;
                    }
                    else if (x>200){
                        pixelLocation = 3;
                    }
                }   // end for() loop

                // Share the CPU.
                sleep(20);
                pixelTimer=pixelTimer-20;
            }
        }

        // Save more CPU resources when camera is no longer needed.
        visionPortal.close();

        return pixelLocation;
    }


    /**********************
     * Motion Methods
     **********************/

    public void Drive(double distance, double motorPower, int pause) throws InterruptedException {
       if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            DriveByInches(distance);
            RunDriveToPosition();
            DrivePower(motorPower);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }
    public void Strafe(double rightDistance, double power, int pause) throws InterruptedException {
        //Left is positive
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            StrafeByInches(rightDistance);
            RunDriveToPosition();
            DrivePower(power);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }
    public void Turn(int rightDegrees, double power, int pause) throws InterruptedException {
        //Left is negative
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            TurnByAngle(rightDegrees);
            RunDriveToPosition();
            DrivePower(power);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }
    public void RaiseArmByDegrees(double degrees, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            RaiseByInches (degrees);
            ArmPower(defaultArmPower);
        }
        ArmPower(0);
        Thread.sleep(pause);

    }

    public void RaiseArm(int distance, int pause) throws InterruptedException {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Set the target rotations for the motor
        RaiseByInches(distance);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ArmPower(defaultArmPower);
        while (arm.isBusy()) {
            DisplayArmTelemetry();
        }
    }
    public void ReturnArm() throws InterruptedException {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Set the target rotations for the motor
        arm.setTargetPosition(-armOdometer);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ArmPower(defaultArmPower/2);
        while (arm.isBusy()) {
            DisplayArmTelemetry();
        }
        armOdometer=0;
    }
    public void ExtendArm(double additionalExtension, double power,int pause) throws InterruptedException {
        if (opModeIsActive()) {
            armExtension.setTargetPosition((int) ((armExtension.getCurrentPosition() + additionalExtension) * ticksPerInchExtension));
            armExtension.setPower(power);
            while (arm.isBusy() || armExtension.isBusy()) {
                DisplayArmTelemetry();
            }
            armExtension.setPower(0);
        }
        Thread.sleep(pause);
    }
    public void ReturnExtension() throws InterruptedException {
        if (opModeIsActive()) {
            armExtension.setTargetPosition(0);
            armExtension.setPower(defaultArmExtensionPower/2);
            while (arm.isBusy() || armExtension.isBusy()) {
                DisplayArmTelemetry();
            }
            armExtension.setPower(0);
        }
        extendOdometer=0;
    }
    public void lightForward() throws InterruptedException{

        //This is not making all of the wheels turn in the same direction and so it is commented out
        //frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //DrivePower(-0.1);

        //Substituting this instead
        Drive(1,0.1,defaultPauseTime);
    }

    public void DriveGyro(double inches, double motorPower, int pause) throws InterruptedException {
        /****
         * inches: how far the robot is supposed to travel
         * power: how strong the motors should be targeting
         *
         */

        double targetDistance = inches * conversionTicksToInches*driveDistanceAdjustment;
        int i = 0;
        double splitDistance;
        angles = imuForDisplay.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double originalHeading = angles.firstAngle;

        //double orientationGain = 1*(10^-15);
        double orientationGain = 0.00000001;
        int stepJump = 100;
        while(opModeIsActive()) {
            while (targetDistance > 0) {
                if (targetDistance > stepJump) {
                    //split the target distance up so error can be adjusted
                    if (i < 3) {
                        splitDistance = stepJump - 20;
                    } else {
                        splitDistance = stepJump;
                    }
                } else {
                    //use up all of the target distance and go slow
                    splitDistance = (targetDistance);
                    // Reduce the motor speeds on each motor
                    motorPower = 0.2 * motorPower;
                }


                //Tell the motors the intended distance
                frontLeft.setTargetPosition((int) (splitDistance));
                frontRight.setTargetPosition((int) (splitDistance));
                backLeft.setTargetPosition((int) (splitDistance));
                backRight.setTargetPosition((int) (splitDistance));

                // Only drive to the distance for one motor,
                // otherwise the other motors will still want to finish their count
                // and throw the orientation wacky at the end

                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                /*
                frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                 */

                //Get the current orientation
                angles = imuForDisplay.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

                //CCW is Positive for the orientation
                double orientationError = originalHeading - angles.firstAngle;

                //Set the motor power based on the error noted on the imu with gain

                //Subtract Error on Left
                frontLeft.setPower(motorPower - (orientationError * orientationGain));
                backLeft.setPower(motorPower - (orientationError * orientationGain));

                //Add error on right
                frontRight.setPower(motorPower + (orientationError * orientationGain));
                backRight.setPower(motorPower + (orientationError * orientationGain));


                //increase the counters for the next iteration of the loop
                targetDistance = targetDistance - splitDistance;
                double frontLeftInches = frontLeft.getCurrentPosition();
                double frontRightInches = frontRight.getCurrentPosition();
                double backLeftInches = backLeft.getCurrentPosition();
                double backRightInches = backRight.getCurrentPosition();

                while (frontLeft.isBusy()) {
                    /*
                    telemetry.clear();

                    telemetry.addLine()
                            .addData("Front Left Ticks ", (int) frontLeftInches + "   Power: " + "%.1f", frontLeft.getPower());
                    telemetry.addLine()
                            .addData("Front Right Ticks: ", (int) frontRightInches + "   Power: " + "%.1f", frontRight.getPower());
                    telemetry.addLine()
                            .addData("Back Left Ticks: ", (int) backLeftInches + "   Power: " + "%.1f", backLeft.getPower());
                    telemetry.addLine()
                            .addData("Back Right Ticks: ", (int) backRightInches + "   Power: " + "%.1f", backRight.getPower());


                     */
                    telemetry.addData("Target Distance: ", targetDistance);
                    telemetry.addData("Original Heading: ", originalHeading);
                    telemetry.addData("Orientation Error: ", orientationError);
                    telemetry.addData("Loop Iteration: ", i);
                    telemetry.update();
                }
                //DisplayDriveTelemetry();
                //StopAndResetDriveEncoders();
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);

                //wait for humans to read
                sleep(1000);

                i = i + 1;
            }
            Thread.sleep(pause);
        }
    }

    /**********************
     * Encoder Methods
     **********************/

    public void StopAndResetAllEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void StopAndResetDriveEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void StopAndResetArmEncoder() {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void RunDriveToPosition() {
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void RunArmToPosition() {
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /*********************************
     * Distance Calculation Methods
     ********************************/

    public void DriveByInches(double distance) {
        double correctedDistance = (distance*(driveDistanceAdjustment));
        frontLeft.setTargetPosition((int)(correctedDistance * conversionTicksToInches));
        frontRight.setTargetPosition((int)(correctedDistance * conversionTicksToInches));
        backLeft.setTargetPosition((int)(-1 * correctedDistance * conversionTicksToInches));
        backRight.setTargetPosition((int)(-1 * correctedDistance * conversionTicksToInches));
    }
    public void StrafeByInches(double distance) {
        double correctedDistance = distance*(strafeDistanceAdjustment);
        frontLeft.setTargetPosition((int)(correctedDistance * conversionTicksToInches));
        frontRight.setTargetPosition((int)(-correctedDistance * conversionTicksToInches));
        backLeft.setTargetPosition((int)(correctedDistance * conversionTicksToInches));
        backRight.setTargetPosition((int)(-correctedDistance * conversionTicksToInches));
    }
    public void TurnByAngle(double degrees) {
        frontLeft.setTargetPosition((int)(degrees * ticksPerDegreeTurnChassis));
        frontRight.setTargetPosition((int)(-degrees * ticksPerDegreeTurnChassis));
        backLeft.setTargetPosition((int)(-degrees * ticksPerDegreeTurnChassis));
        backRight.setTargetPosition((int)(degrees * ticksPerDegreeTurnChassis));
    }
    public void RaiseByInches(double distance) {
        int correctedDistance = (int) (distance * (armDistanceAdjustment));
        arm.setTargetPosition(correctedDistance);
        armOdometer=armOdometer+correctedDistance;
    }
    public void raiseByDegrees(double degrees) {
        int correctedDistance = (int)(degrees * ticksPerDegreeArm);
        arm.setTargetPosition(correctedDistance);
        armOdometer=armOdometer+correctedDistance;
    }



    /**********************
     * Power Methods
     **********************/

    public void DrivePower(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }
    public void ArmPower(double power) {
        arm.setPower(power);
    }

    /******
     * TensorFlow Methods
     **********/

    /**
     * Initialize the TensorFlow Object Detection processor.
     */
    private void initTfod() {

        // Create the TensorFlow processor the easy way.
        tfod = TfodProcessor.easyCreateWithDefaults();

        // Create the vision portal the easy way.
        if (USE_WEBCAM) {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    hardwareMap.get(WebcamName.class, "Webcam 1"), tfod);
        } else {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    BuiltinCameraDirection.BACK, tfod);
        }

    }   // end method initTfod()

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop

    }   // end method telemetryTfod()


    //End TensorFlow Methods



    /**********************
     * Telemetry Methods
     **********************/

    public void DisplayDriveTelemetry() {
        double frontLeftInches = frontLeft.getCurrentPosition() / conversionTicksToInches;
        double frontRightInches = frontRight.getCurrentPosition() / conversionTicksToInches;
        double backLeftInches = backLeft.getCurrentPosition() / conversionTicksToInches;
        double backRightInches = backRight.getCurrentPosition() / conversionTicksToInches;
        telemetry.clear();
        telemetry.addLine()
                .addData("Front Left Inches ", (int) frontLeftInches + "   Power: " + "%.1f", frontLeft.getPower());
        telemetry.addLine()
                .addData("Front Right Inches: ", (int) frontRightInches + "   Power: " + "%.1f", frontRight.getPower());
        telemetry.addLine()
                .addData("Back Left Inches: ", (int) backLeftInches + "   Power: " + "%.1f", backLeft.getPower());
        telemetry.addLine()
                .addData("Back Right Inches: ", (int) backRightInches + "   Power: " + "%.1f", backRight.getPower());
        angles=imuForDisplay.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double heading = angles.firstAngle;
        telemetry.addData("Heading: ",angles.firstAngle);
        telemetry.addData("Roll: ",angles.secondAngle);
        telemetry.addData("Pitch: ",angles.thirdAngle);
        telemetry.update();
    }
    public void DisplayArmTelemetry() {
        double armDegrees = arm.getCurrentPosition() / ticksPerDegreeArm;
        double frontRightInches = frontRight.getCurrentPosition() / conversionTicksToInches;
        double backLeftInches = backLeft.getCurrentPosition() / conversionTicksToInches;
        double backRightInches = backRight.getCurrentPosition() / conversionTicksToInches;
        telemetry.clear();
        telemetry.addLine()
                .addData("Arm Degrees ", (int) armDegrees + "   Power: " + "%.1f", arm.getPower());
        telemetry.update();
    }
    //End of Class
}
