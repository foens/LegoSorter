package olefoens.sorter;

import lejos.nxt.*;
import lejos.nxt.addon.RCXLightSensor;
import lejos.util.Delay;
import olefoens.axlesorter.AxleSorter;
import olefoens.beamcounter.*;
import olefoens.communication.ClientRole;
import olefoens.communication.Communicator;

import java.io.IOException;

public class Main
{
	/**
	 * If you want to run the Sorter without bluetooth communication, set this to true
	 */
	public static final boolean NO_MASTER_MODE = false;

	public static void main(String[] args) throws InterruptedException
	{
		Button.ESCAPE.addButtonListener(new ButtonListener()
		{
			public void buttonPressed(Button b){ System.exit(0);}
			public void buttonReleased(Button b){}
		});

		LightSensor floodLight = new LightSensor(SensorPort.S1);
		LightSensor sensor = new LightSensor(SensorPort.S2);

		BeltDrive beltDrive = new BeltDrive();
		beltDrive.addBeltMotor(new BeltMotorDescription(Motor.B, DriveDirection.FORWARD));
		beltDrive.addBeltMotor(new BeltMotorDescription(Motor.C, DriveDirection.REVERSE));

		try
		{
			Communicator master = NO_MASTER_MODE ? null : Communicator.acceptConnectionFromMaster(ClientRole.Sorter);
			BeamHoleCounter bhc = new BeamHoleCounter(floodLight, sensor, beltDrive);
			AxleSorter axleSorter = new AxleSorter(Motor.A, new RCXLightSensor(SensorPort.S3), new RCXLightSensor(SensorPort.S4));

			BeamAxleSorter beamAxleSorter = new BeamAxleSorter(bhc, master, axleSorter);
			if(NO_MASTER_MODE)
				beamAxleSorter.stopAndStartSorters();
			else
				beamAxleSorter.startSystem();

		} catch(IOException e)
		{
			LCD.clear();
			LCD.drawString("IOException!", 0, 0);
			Delay.msDelay(10000);
		}
	}
}
