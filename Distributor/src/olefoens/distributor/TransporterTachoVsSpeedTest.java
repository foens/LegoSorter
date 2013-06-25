package olefoens.distributor;

import lejos.nxt.Motor;
import lejos.robotics.RegulatedMotor;

public class TransporterTachoVsSpeedTest
{
    private static final int TEST_TRACK_ANGLE_LEN = 280*3;
    private static final int TRACK_TRAVERSALS = 10;

    public static void run() throws InterruptedException
    {
        RegulatedMotor motor = Motor.C;
        motor.setSpeed((int)motor.getMaxSpeed());
        motor.setAcceleration(1800);

        int curTraversal = 0;
        while (++curTraversal <= TRACK_TRAVERSALS)
        {
            motor.rotate(-TEST_TRACK_ANGLE_LEN);
            motor.rotate(TEST_TRACK_ANGLE_LEN);
        }
    }
}
