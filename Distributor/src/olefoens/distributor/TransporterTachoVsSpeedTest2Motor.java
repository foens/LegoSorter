package olefoens.distributor;

import lejos.nxt.Motor;
import lejos.robotics.RegulatedMotor;

public class TransporterTachoVsSpeedTest2Motor
{
    private static final int TEST_TRACK_ANGLE_LEN = 180; //280;
    private static final int TRACK_TRAVERSALS = 5;

    public static void run() throws InterruptedException
    {
        RegulatedMotor leftMotor = Motor.A;
        RegulatedMotor rightMotor = Motor.C;
        leftMotor.setSpeed((int)leftMotor.getMaxSpeed());
        rightMotor.setSpeed((int)rightMotor.getMaxSpeed());
        rightMotor.setAcceleration(1000);
        leftMotor.setAcceleration(1000);

        int curTraversal = 0;
        while (++curTraversal <= TRACK_TRAVERSALS)
        {
            leftMotor.rotate(-TEST_TRACK_ANGLE_LEN, true);
            rightMotor.rotate(-TEST_TRACK_ANGLE_LEN, true);
            while (leftMotor.isMoving() || rightMotor.isMoving())
                Thread.sleep(50);

            leftMotor.rotate(TEST_TRACK_ANGLE_LEN, true);
            rightMotor.rotate(TEST_TRACK_ANGLE_LEN, true);
            while (leftMotor.isMoving() || rightMotor.isMoving())
                Thread.sleep(50);
        }
    }
}
