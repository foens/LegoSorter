package olefoens.feeder;

import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;

public class RejectArm
{
	private final NXTRegulatedMotor arm;
	private static final int armToOutPositionAngles = 110;

	public RejectArm(NXTRegulatedMotor arm)
	{
		this.arm = arm;
		goToStartPosition();
	}

	private void goToStartPosition()
	{
		arm.setSpeed(60);
		arm.setStallThreshold(3, 5);
		arm.backward();
		arm.waitComplete();
		arm.flt();
		Delay.msDelay(100);
		arm.stop();
		Delay.msDelay(50);
		arm.flt();
		arm.rotate(-2);
		arm.setStallThreshold(25, 50);
		arm.setSpeed(arm.getMaxSpeed());
		arm.resetTachoCount();
		arm.flt();
	}

	public void moveArmOut()
	{
		arm.rotate(armToOutPositionAngles);
	}

	public void moveArmBack()
	{
		arm.rotate(-armToOutPositionAngles);
		arm.flt();
	}
}
