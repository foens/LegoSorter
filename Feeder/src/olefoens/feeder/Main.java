package olefoens.feeder;

import lejos.nxt.*;
import lejos.util.Delay;

public class Main implements Runnable
{
	private static final RejectArm arm = new RejectArm(Motor.C);
	private static final Elevator elevator = new Elevator(Motor.A);
	private static final NXTRegulatedMotor beltDrive = Motor.B;
	//private static final RCXLightSensor sensor = new RCXLightSensor(SensorPort.S1);
	private static final LightSensor sensor = new LightSensor(SensorPort.S1);

	public static void main(String[] args) throws Exception
	{
		Thread thread = new Thread(new Main());
		thread.setDaemon(true);
		thread.start();
		sensor.setFloodlight(true);
		LCD.drawString("Feeder", 0, 0);
		beltDrive.setSpeed(200);
		beltDrive.backward();
		for(int i = 0; i < 5; i++)
		{
			elevator.elevatorUp();
			elevator.elevatorDown();
			Delay.msDelay(3000);
		}
		Delay.msDelay(120000);
	}

	@Override
	public void run()
	{
		//arm.moveArmOut();
		int lightThreshold = 280;
		long started = -1;
		while(true)
		{
			int lightValue = sensor.getNormalizedLightValue();
			if(lightValue > lightThreshold && started == -1)
				started = System.currentTimeMillis();
			else if(lightValue < lightThreshold)
				started = -1;

			/*if(started != -1 && started + 1000 <= System.currentTimeMillis())
			{
				arm.moveArmBack();
				Delay.msDelay(3000);
				arm.moveArmOut();
				started = -1;
			}*/
			LCD.drawInt(lightValue, 5, 0, 1);
			LCD.drawInt(started == -1 ? (int) started : (int) (System.currentTimeMillis() - started), 9, 0, 2);
			//Delay.msDelay(10);
		}
	}
}
