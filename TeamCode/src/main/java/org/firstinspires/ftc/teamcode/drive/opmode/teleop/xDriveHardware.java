package org.firstinspires.ftc.teamcode.drive.opmode.teleop;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class xDriveHardware{

    // Declare Drive Motor Variables
    //public DcMotor leftFront = null; // removed leftFront Because did not have expansion hub on at the time and needed to test
    public DcMotor rightFront = null;
    public DcMotor leftBack = null;
    public DcMotor rightBack = null;

    //Arm motor variable for spool
    public DcMotor armMotor = null;

    //Place holder variable for when a servo is added to robot
    public Servo hook = null;

    // Converting MotorTicks, Gear Ratio, Spool/ Wheel Diameter to Counts Per Inch for Encoder
    public static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    public static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    public static final double     SPOOL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (SPOOL_DIAMETER_INCHES * 3.1415);
    // Spools Turn speed
    public static final double     TURN_SPEED              = 0.7;
    // Stage Length of linear slide stage in inches
    public int stageLength = 20;

    HardwareMap hwMap  = null;

    // variable to represent the arm stage 0-3
    public int stage = 0;

    // variable to represent stage 0
    public int stage0;

    // Constructor
    public xDriveHardware(){

    }

    public void init(HardwareMap ahwMap){

        hwMap = ahwMap;

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        /*leftFront = hwMap.get(DcMotor.class, "leftFront");
        Note: This Motor was Commented out for test purposes only remove when done
         */
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        leftBack = hwMap.get(DcMotor.class, "leftBack");
        rightBack = hwMap.get(DcMotor.class, "rightBack");

        armMotor = hwMap.get(DcMotor.class, "armMotor");

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);




        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips

        //leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        armMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Set all powers to 0
        setAllMotorPowers(0);

    }
    /*Method sets each Drive Motors Powers  */
    public void setDriveMotorPower(double RFPower, double LBPower, double RBPower){// removed leftFront Because did not have expansion hub on at the time and needed to test
        //leftFront.setPower(LFPower);
        rightFront.setPower(RFPower);
        leftBack.setPower(LBPower);
        rightBack.setPower(RBPower);
    }

    // Sets all the Drive Motor Powers
    public void setAllDrivePower(double allPower){
        setDriveMotorPower(allPower, allPower, allPower);// removed leftFront Because did not have expansion hub on at the time and needed to test
    }

    //Sets every MotorPower
    public void setAllMotorPowers(double allPower){
        setAllDrivePower(allPower);
        armMotor.setPower(allPower);
    }


    // Moves the arm to the inches passed as parameters
    public void ArmToPosition(double speed, double Inches){
        int newTarget;


        // Creates Motors new Target Position then sets its target to new position
        newTarget = armMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
        armMotor.setTargetPosition(newTarget);

        //Sets arm motor to run to target position
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Sets Arm Power to speed param passed
        armMotor.setPower(speed);

    }

    // Method to increase the Arm for each stage check using a button
    public void ArmStageIncrease(boolean button) {
        // Checks if the button then adds one to the stage
        // checks what stage the stage is on then multiples the length but that amount
        if (button) {

            stage++;

            if (stage == 1){
                ArmToPosition(TURN_SPEED, stage*stageLength);
            }

            else if (stage == 2){
                ArmToPosition(TURN_SPEED, stage*stageLength);
            }

            else if (stage == 3){
                ArmToPosition(TURN_SPEED, stage*stageLength);
            }

            //Makes sure the Stage never is greater then 3
            if (stage > 3){
                stage = 3;
            }

        }
    }

    // Same method But decreases the Stage
    public void ArmStageDecrease(boolean button){
        // Make sure the Stage is not zero and rewinding
        if (button){
            stage--;
            // Sets Turn Speed to Negative so motor rewinds
            ArmToPosition(-TURN_SPEED, -stageLength);
        }
    }


    public void ArmSetStage0(boolean button){
        stage0 = armMotor.getCurrentPosition();

        if (button){
            stage = 0;
            armMotor.setTargetPosition(stage0);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
}
