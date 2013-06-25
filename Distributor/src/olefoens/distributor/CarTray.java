package olefoens.distributor;

import lejos.robotics.RegulatedMotor;

import java.lang.Thread;

public class CarTray
{
    private static final int MOTOR_DEFAULT_STALL_TACHO_ERROR = 50;
    private static final int MOTOR_DEFAULT_STALL_THRESHOLD_TIME_MS = 100;
    private static final int MOTOR_DEFAULT_SPEED = 360;
    private static final int STALL_WAIT_INTERVAL_MS = 50;
    private static final int GATE_UP_ROTATE_ANGLE = -60;
    private static final int GATE_RESET_TACHO_ERROR = 1;
    private static final int GATE_RESET_THRESHOLD_TIME_MS = 100;
    private static final int GATE_RESET_MOTOR_SPEED = 125;
    private static final int ROTATE_ANGLE_TO_AVOID_GATE_CLOSING_SOUND = -15;
    private RegulatedMotor gateMotor;

    public CarTray(RegulatedMotor gateMotor)
    {
        this.gateMotor = gateMotor;
        resetGateToStartPos();
    }

    public void releaseItem()
    {
        gateMotor.rotate(GATE_UP_ROTATE_ANGLE);
        gateMotor.rotate(-GATE_UP_ROTATE_ANGLE);
    }

    private void resetGateToStartPos()
    {
        setMotorValuesForGateReset();

        gateMotor.forward();
        waitForGateClosure();
        moveGateToAvoidMakingWeirdSoundWhenClosing();

        setDefaultMotorValues();
    }

    private void setMotorValuesForGateReset()
    {
        gateMotor.setStallThreshold(GATE_RESET_TACHO_ERROR, GATE_RESET_THRESHOLD_TIME_MS);
        gateMotor.setSpeed(GATE_RESET_MOTOR_SPEED);
    }

    private void setDefaultMotorValues()
    {
        gateMotor.setStallThreshold(MOTOR_DEFAULT_STALL_TACHO_ERROR, MOTOR_DEFAULT_STALL_THRESHOLD_TIME_MS);
        gateMotor.setSpeed(MOTOR_DEFAULT_SPEED);
    }

    private void moveGateToAvoidMakingWeirdSoundWhenClosing()
    {
        gateMotor.flt();
        gateMotor.setStallThreshold(MOTOR_DEFAULT_STALL_TACHO_ERROR, MOTOR_DEFAULT_STALL_THRESHOLD_TIME_MS);
        gateMotor.rotate(ROTATE_ANGLE_TO_AVOID_GATE_CLOSING_SOUND);
    }

    private void waitForGateClosure()
    {
        while (!gateMotor.isStalled())
        {
            try
            {
                Thread.sleep(STALL_WAIT_INTERVAL_MS);
            }
            catch (InterruptedException ignored)
            {}
        }
    }
}
