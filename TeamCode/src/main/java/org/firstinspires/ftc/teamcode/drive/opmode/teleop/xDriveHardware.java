package org.firstinspires.ftc.teamcode.drive.opmode.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class xDriveHardware{

    // Declare Drive Motor Variaibles
    public DcMotor leftFront = null; // removed leftFront Because did not have expansion hub on at the time and needed to test
    public DcMotor rightFront = null;
    public DcMotor leftBack = null;
    public DcMotor rightBack = null;

    //Arm motor variable for spool
    public DcMotor armMotor = null;

    //Servo on robot that rotates the claw up or down
    public Servo claw = null;

    public int startPosition = 0;

    // Converting MotorTicks, Gear Ratio, Spool/ Wheel Diameter to Counts Per Inch for Encoder
    public static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    public static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    public static final double     SPOOL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (SPOOL_DIAMETER_INCHES * 3.1415);

    // Spools Turn speed
    public static double     TURN_SPEED              = 0.7;

    // Stage Length of linear slide stage in inches


    HardwareMap hwMap  = null;



    // Constructor
    public xDriveHardware(){

    }

    public void init(HardwareMap ahwMap){

        hwMap = ahwMap;

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftFront = hwMap.get(DcMotor.class, "leftFront");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        leftBack = hwMap.get(DcMotor.class, "leftBack");
        rightBack = hwMap.get(DcMotor.class, "rightBack");

        armMotor = hwMap.get(DcMotor.class, "armMotor");


        claw = hwMap.get(Servo.class, "claw");

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        startPosition = armMotor.getCurrentPosition();


        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        armMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Set all powers to 0
        setAllMotorPowers(0);

    }
    /*Method sets each Drive Motors Powers  */
    public void setDriveMotorPower(double LFPower, double RFPower, double LBPower, double RBPower){// removed leftFront Because did not have expansion hub on at the time and needed to test
        leftFront.setPower(LFPower);
        rightFront.setPower(RFPower);
        leftBack.setPower(LBPower);
        rightBack.setPower(RBPower);
    }

    // Sets all the Drive Motor Powers
    public void setAllDrivePower(double allPower){
        setDriveMotorPower(allPower, allPower, allPower, allPower);// removed leftFront Because did not have expansion hub on at the time and needed to test
    }

    //Sets every MotorPower
    public void setAllMotorPowers(double allPower){
        setAllDrivePower(allPower);
        armMotor.setPower(allPower);
    }

    // Moves the arm to the inches passed, Note:Sets the position to the inches Example-- If 20 is passed moves it to 20 then if 30 moves it to 30
    public void ArmToPosition(double speed, double Inches){
        int newTarget;


        // Creates Motors new Target Position then sets its target to new position
        newTarget = startPosition + (int) (Inches * COUNTS_PER_INCH);
        armMotor.setTargetPosition(newTarget);

        //Sets arm motor to run to target position
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Sets Arm Power to speed param passed
        armMotor.setPower(speed);

    }



}