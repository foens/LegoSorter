package olefoens.beamcounter;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class BeamHoleCounter implements Runnable
{
	private static final long BRICK_END_TIME_THRESHOLD_IN_MS = 200;
	private static final int LIGHT_THRESHOLD = 550;

	private final LightSensor sensor;
	private final BeltDrive beltDrive;
	private int brickCount = 0;
	private BeamListener listener;

	public BeamHoleCounter(LightSensor floodLight, LightSensor sensor, BeltDrive beltDrive)
	{
		this.sensor = sensor;
		this.beltDrive = beltDrive;

		floodLight.setFloodlight(true);
		this.sensor.setFloodlight(false);
		waitForFloodLightOn();
	}

	public void setListener(BeamListener listener)
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

	public void run()
	{
		beltDrive.forward();
		int lightBeamBrokenCount = 0;
		boolean stateAboveLightThreshold = true;

		LCD.drawString("Light sensed: ", 0, 0);
		LCD.drawString("Bricks: ", 0, 2);
		LCD.drawString("Holes : ", 0, 3);
		drawCounts(lightBeamBrokenCount);

		long lastTimeLightBelowThreshold = System.currentTimeMillis();
		try
		{
			while(true)
			{
				Thread.sleep(10);

				if(lastTimeLightBelowThreshold + BRICK_END_TIME_THRESHOLD_IN_MS < System.currentTimeMillis() && lightBeamBrokenCount >= 1)
				{
					brickCount++;
					int holesInBrick = lightBeamBrokenCount - 1;
					drawCounts(holesInBrick);
					listener.beamFound(holesInBrick);
					lightBeamBrokenCount = 0;
				}
				int lightValue = getLightValue();
				LCD.drawInt(lightValue, 3, 3, 1);

				if(lightValue > LIGHT_THRESHOLD && !stateAboveLightThreshold)
				{
					stateAboveLightThreshold = true;
					if(lightBeamBrokenCount != 1)
						playLightBeamBrokenSound();
				} else if(lightValue < LIGHT_THRESHOLD && stateAboveLightThreshold)
				{
					lightBeamBrokenCount++;
					lastTimeLightBelowThreshold = System.currentTimeMillis();
					stateAboveLightThreshold = false;
				}
			}
		} catch(InterruptedException e)
		{
			beltDrive.stop();
		}
	}

	private void playLightBeamBrokenSound()
	{
		int volume = Sound.getVolume();
		Sound.setVolume(50);
		Sound.playTone(4000, 50, 100);
		Sound.setVolume(volume);
	}

	private void drawCounts(int holesInBrick)
	{
		LCD.drawInt(brickCount, 2, 9, 2);
		LCD.drawInt(holesInBrick, 2, 9, 3);
	}
}