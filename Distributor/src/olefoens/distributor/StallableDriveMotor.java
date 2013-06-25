package olefoens.distributor;

import lejos.nxt.*;
import lejos.util.Delay;

/**
 * A motor that is able to drive a certain direction and then stop when it
 * becomes stalled.
 */
public class StallableDriveMotor extends NXTRegulatedMotor implements Runnable
{
    private static final int WAIT_FOR_DRIVE_STALL_INTERVAL_MS = 25;
    private boolean hasDriveStalled;

    public StallableDriveMotor(TachoMotorPort port)
    {
        super(port);
    }

    public void forwardUntilStalledAsync()
    {
        hasDriveStalled = false;
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        suspendRegulation();
        tachoPort.controlMotor(30, BasicMotorPort.FORWARD);

        int oldTaco = getTachoCount();
        while (true)
        {
            Delay.msDelay(50);
            if (oldTaco == getTachoCount())
                break;
            oldTaco = getTachoCount();
        }

        resetTachoCount();
        flt();
        hasDriveStalled = true;
    }

    public void waitForDriveStall()
    {
        while (!hasDriveStalled)
            Delay.msDelay(WAIT_FOR_DRIVE_STALL_INTERVAL_MS);
    }
}

