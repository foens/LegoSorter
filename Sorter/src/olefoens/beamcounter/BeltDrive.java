package olefoens.beamcounter;

import lejos.nxt.NXTRegulatedMotor;

import java.util.ArrayList;
import java.util.List;

public class BeltDrive
{
	private List<BeltMotorDescription> motors = new ArrayList<BeltMotorDescription>();

	public BeltDrive()
	{
	}

	public void addBeltMotor(BeltMotorDescription beltMotor)
	{
		motors.add(beltMotor);
	}

	public void forward()
	{
		for(BeltMotorDescription motorDescription : motors)
		{
			NXTRegulatedMotor m = motorDescription.motor;
			if(motorDescription.direction == DriveDirection.FORWARD)
				m.forward();
			else
				m.backward();
		}
	}

	public void stop()
	{
		for(BeltMotorDescription motorDescription : motors)
		{
			motorDescription.motor.stop();
		}
	}
}
