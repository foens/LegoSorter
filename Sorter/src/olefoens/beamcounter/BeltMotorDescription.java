package olefoens.beamcounter;

import lejos.nxt.NXTRegulatedMotor;

public class BeltMotorDescription
{
	public final NXTRegulatedMotor motor;
	public final DriveDirection direction;

	public BeltMotorDescription(NXTRegulatedMotor motor, DriveDirection direction)
	{
		this.motor = motor;
		this.direction = direction;
	}
}