package olefoens.axlesorter;

import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.addon.RCXLightSensor;
import lejos.util.Delay;

public class AxleSorter implements Runnable
{
	private static final int LIGHT_THRESHOLD = 550;
	private static final float TIME_PER_AXLE_LENGTH = 293;

	private final NXTRegulatedMotor motor;
	private final RCXLightSensor sensor;

	private AxleListener listener;

	public AxleSorter(NXTRegulatedMotor motor, RCXLightSensor floodLight, RCXLightSensor sensor)
	{
		this.motor = motor;
		this.sensor = sensor;

		floodLight.setFloodlight(true);
		sensor.setFloodlight(false);
		waitForFloodLightOn();
	}

	public void setListener(AxleListener listener)
	{
		this.listener = listener;
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

	@Override
	public void run()
	{
		motor.forward();

		LCD.drawString("Light sensed: ", 0, 5);
		LCD.drawString("Dark: ", 0, 7);

		try
		{
			boolean axleDetected = false;
			long axleStartTime = 0;
			while(true)
			{
				int lightValue = getLightValue();
				LCD.drawInt(lightValue, 3, 3, 6);

				if(lightValue < LIGHT_THRESHOLD && !axleDetected)
				{
					axleDetected = true;
					axleStartTime = System.currentTimeMillis();
				}

				if(lightValue > LIGHT_THRESHOLD && axleDetected)
				{
					long axleEndTime = System.currentTimeMillis();
					long axleTransitionTime = axleEndTime - axleStartTime;
					LCD.drawInt((int) axleTransitionTime, 4, 6, 7);
					axleDetected = false;
					int length = Math.round(axleTransitionTime / TIME_PER_AXLE_LENGTH);
					LCD.drawInt(length, 4, 11, 7);
					listener.axleDetected(length);
				}

				Thread.sleep(5);
			}
		} catch(InterruptedException e)
		{
			motor.stop();
			motor.flt();
		}
	}
}
