package olefoens.distributor;

import lejos.robotics.RegulatedMotor;
import lejos.util.Delay;

public class SpeedVsPrecisionTest
{
    private static final int MEASURED_MAX_SPEED_AT_MAX_VOLTAGE = 885;
    private static final int MOTOR_DEFAULT_ACCEL = 6000;
    private static final int TEST_TRACK_ANGLE_LEN = 360 * 3;
    private static final int TEST_REPEATS = 3;

    public static void run(RegulatedMotor motor)
    {
        test(motor, MEASURED_MAX_SPEED_AT_MAX_VOLTAGE, MOTOR_DEFAULT_ACCEL);
        Delay.msDelay(5000);
        test(motor, MEASURED_MAX_SPEED_AT_MAX_VOLTAGE, MOTOR_DEFAULT_ACCEL / 2);
    }

    private static void test(RegulatedMotor motor, int speed, int acceleration)
    {
        motor.setSpeed(speed);
        motor.getMaxSpeed();
        motor.setAcceleration(acceleration);

        for (int i = 0; i < TEST_REPEATS; i++)
        {
            motor.rotate(-TEST_TRACK_ANGLE_LEN);
            motor.rotate(TEST_TRACK_ANGLE_LEN);
        }
    }
}
