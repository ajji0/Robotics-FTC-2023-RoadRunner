package org.firstinspires.ftc.teamcode.drive.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

//Declaring Teleop mode and name of mode
@TeleOp(name = "TeleOpMode: XDrive", group = "drive")
public class xDrive extends LinearOpMode {
    xDriveHardware robot = new xDriveHardware();
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode(){

        // Init the robot hardware map(Motors servos)
        robot.init(hardwareMap);

        // Update Driver Hub or Phone that the robot is ready to run
        telemetry.addData("Status:", "Ready to Run");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // Shows starting postition of the arm
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
            robot.setDriveMotorPower(rightFrontPower, leftBackPower, rightBackPower); // removed leftFront Because did not have expansion hub on at the time and needed to test
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f", rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);

            // Uses the ArmStage Method to increase the stage if X is pressed and decrease if a is pressed
            telemetry.addData("Arm Motor Locat:", "%7d", robot.armMotor.getCurrentPosition());

            robot.ArmStageIncrease(gamepad1.x, 20);
            robot.ArmStageDecrease(gamepad1.a, 20);

            telemetry.addData("Stage", "%7d", robot.stage);
            telemetry.update();
        }
    }
}
