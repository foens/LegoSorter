package olefoens.loader;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.RCXLightSensor;
import lejos.util.Delay;
import olefoens.communication.LegoItemDescription;

import java.io.File;
import java.io.IOException;

public class Main
{
	public static void main(String[] args) throws InterruptedException
	{
		try
		{
			Sound.setVolume(100);
			Loader loader = new Loader(new RCXLightSensor(SensorPort.S1), new RCXLightSensor(SensorPort.S2), Motor.A);
			Master master = new Master();
			master.waitForClientsToConnected();
			master.sendResetToClients();

			while(true)
			{
				loader.runUntilItemInFrontOfLight();
				LCD.clear();
				LCD.drawString("Item received", 0, 0);
				Sound.playSample(new File("hmm.wav"));
				master.sorterStop();
				LegoItemDescription legoDescription = master.getSorterLegoDescription();
				Sound.playSample(new File("ahh.wav"));
				LCD.drawString(legoDescription.type.name(), 0, 1);
				LCD.drawString("Length: ", 0, 2);
				LCD.drawInt(legoDescription.length, 3, 8, 2);
				master.waitForDistributorReady();
				LCD.clear();
				LCD.drawString("Ejecting", 0, 0);
				loader.ejectItem(legoDescription.length);
				LCD.clear();
				LCD.drawString("Item ejected", 0, 0);
				master.sendLegoDescriptionToDistributor(legoDescription);
				master.sendResumeToSorter();
			}
		} catch(IOException e)
		{
			LCD.clear();
			LCD.drawString("IOException!", 0, 0);
			Delay.msDelay(10000);
		}
	}
}
