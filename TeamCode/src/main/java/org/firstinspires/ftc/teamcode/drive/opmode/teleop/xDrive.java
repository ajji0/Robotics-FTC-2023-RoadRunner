package org.firstinspires.ftc.teamcode.drive.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//Declaring Teleop mode and name of mode
@TeleOp(name = "TeleOpMode: XDrive", group = "drive")
public class xDrive extends LinearOpMode {
    xDriveHardware robot = new xDriveHardware();
    private ElapsedTime runtime = new ElapsedTime();

    // variable to represent the arm stage 0-3
    public int stage = 0;


    //Stage Each Stage Length Increase or decrease these values depending on your height needed
    public int stage0Length = 0;
    public int stage1Length = 20;
    public int stage2Length = 30;
    public int stage3Legnth = 40;

    @Override
    public void runOpMode(){

        // Init the robot hardware map(Motors servos)
        robot.init(hardwareMap);

        // Saves the position of when linear slide is on the ground
        int stage0 = robot.armMotor.getCurrentPosition();

        // Update Driver Hub or Phone that the robot is ready to run
        telemetry.addData("Status:", "Ready to Run");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // Shows starting position of the arm
        telemetry.addData("Arm_Starting_at...:", "%7d", robot.armMotor.getCurrentPosition());
        telemetry.update();

        while (opModeIsActive()){

            // Gets the the imput of the gamepad and adjest the Motors power
            double max;

            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));


            //Divides the motor powers by the max
            if (max > 1.0)
            {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }

            // Uses the Method to set all motor powers then Ten Telemetry to see how much power each is getting
            robot.setDriveMotorPower(leftFrontPower, rightFrontPower, leftBackPower, rightBackPower); // removed leftFront Because did not have expansion hub on at the time and needed to test
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f", rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);

            // Uses the ArmStage Method to increase the stage if X is pressed and decrease if a is pressed
            telemetry.addData("Arm Motor Locat:", "%7d", robot.armMotor.getCurrentPosition());

            //First make sures the motor is not busy
            if (!robot.armMotor.isBusy()){
                // checks if the user hit the button and if its greater then 0 then goes down a stage
                if (gamepad1.a && stage > 0) {
                    stage--;
                }

                // Checks if the user has pressed gamepad button and goes up a stage
                if (gamepad1.x && stage < 3) {

                    //Add increase the stage
                    stage++;
                }


                if (stage == 0){
                    robot.armMotor.setTargetPosition(stage0);
                    robot.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                }

                //Checks for each stage and sets the robots height to that stage
                if (stage == 1) {
                    robot.ArmToPosition(robot.TURN_SPEED, stage1Length);
                }

                else if (stage == 2) {
                    robot.ArmToPosition(robot.TURN_SPEED, stage2Length);
                }

                else if (stage == 3) {
                    robot.ArmToPosition(robot.TURN_SPEED, stage3Legnth);
                }

            }

            //Same thing as the increase but decreases
            if (!robot.armMotor.isBusy()) {

            }
            //Sets Stage to 0 and sets the robot position to the original saved position
            if (gamepad1.b) {
                stage = 0;
                robot.ArmToPosition(robot.TURN_SPEED, stage0Length);
            }


            telemetry.addData("Stage", "%7d", stage);
            telemetry.update();

        }

    }
}
