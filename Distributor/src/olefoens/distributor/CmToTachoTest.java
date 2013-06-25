package olefoens.distributor;

import lejos.robotics.RegulatedMotor;

public class CmToTachoTest
{
    private static final int TEST_TRACK_ANGLE_LEN = 375 * 3;

    public static void run(RegulatedMotor motor)
    {
        motor.setSpeed((int)motor.getMaxSpeed());
        motor.setAcceleration(3000);

        motor.rotate(-TEST_TRACK_ANGLE_LEN);
    }
}
