package olefoens.loader;

import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.addon.RCXLightSensor;
import lejos.util.Delay;

public class Loader
{
	private static final int LIGHT_THRESHOLD = 550;
	private static final int BELT_DRIVE_FORWARD_EJECT_TIME_MS_PER_LENGTH = 100;

	private final RCXLightSensor sensor;
	private final NXTRegulatedMotor beltDrive;

	public Loader(RCXLightSensor emitter, RCXLightSensor sensor, NXTRegulatedMotor beltDrive)
	{
		this.sensor = sensor;
		this.beltDrive = beltDrive;

		emitter.setFloodlight(true);
		sensor.setFloodlight(false);
		waitForFloodLightOn();
	}

	private void waitForFloodLightOn()
	{
		LCD.drawString("Waiting for", 3, 5);
		LCD.drawString("Floodlight ON", 2, 6);
		while(getLightValue() < LIGHT_THRESHOLD)
		{
			Delay.msDelay(5);
		}
		LCD.clear();
	}

	private int getLightValue()
	{
		return sensor.getNormalizedLightValue();
	}

	private void beltDriveForward()
	{
		 beltDrive.backward();
	}

	public void runUntilItemInFrontOfLight()
	{
		LCD.clear();
		beltDriveForward();
		LCD.drawString("Waiting for", 3, 3);
		LCD.drawString("Item", 5, 4);
		while(getLightValue() > LIGHT_THRESHOLD)
		{
			Delay.msDelay(5);
		}
		beltDrive.stop();
		beltDrive.flt();
	}

	public void ejectItem(int length)
	{
		beltDriveForward();
		Delay.msDelay(BELT_DRIVE_FORWARD_EJECT_TIME_MS_PER_LENGTH * length);
		beltDrive.stop();
		beltDrive.flt();
	}
}
