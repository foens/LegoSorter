package olefoens.sorter;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import olefoens.axlesorter.AxleListener;
import olefoens.axlesorter.AxleSorter;
import olefoens.beamcounter.BeamHoleCounter;
import olefoens.beamcounter.BeamListener;
import olefoens.communication.Communicator;
import olefoens.communication.LegoBrickType;
import olefoens.communication.LegoItemDescription;
import olefoens.communication.Packet;

import java.io.IOException;

public class BeamAxleSorter implements BeamListener, Runnable, AxleListener
{
	private final BeamHoleCounter beamHoleCounter;
	private final Communicator master;
	private final AxleSorter axleSorter;
	private Thread beamHoleThread;
	private Thread axleThread;

	public BeamAxleSorter(BeamHoleCounter beamHoleCounter, Communicator master, AxleSorter axleSorter)
	{
		this.beamHoleCounter = beamHoleCounter;
		this.master = master;
		this.axleSorter = axleSorter;

		beamHoleCounter.setListener(this);
		axleSorter.setListener(this);
	}

	public void startSystem()
	{
		run();
	}

	@Override
	public void beamFound(int holes)
	{
		try
		{
			if(!Main.NO_MASTER_MODE)
				master.sendLegoDescription(new LegoItemDescription(LegoBrickType.Beam, holes));
		} catch(IOException e)
		{
			stopSystemFromException(e);
		}
	}

	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				Packet packet = master.readPacket();
				switch(packet.type)
				{
					case Reset:
						stopAndStartSorters();
						break;

					case Resume:
						stopAndStartSorters();
						break;

					case Stop:
						stopSorters();
						break;

					default:
						stopSystemFromException(new RuntimeException("Packet type unknown"));
						break;
				}
			}
		} catch(IOException e)
		{
			stopSystemFromException(e);
		}
	}

	public void stopAndStartSorters()
	{
		stopSorters();
		beamHoleThread = new Thread(beamHoleCounter);
		beamHoleThread.start();
		axleThread = new Thread(axleSorter);
		axleThread.start();
	}

	private void stopSorters()
	{
		stopThread(beamHoleThread);
		beamHoleThread = null;
		stopThread(axleThread);
		axleThread = null;
	}

	private void stopThread(Thread thread)
	{
		if(thread != null)
		{
			thread.interrupt();
			try
			{
				thread.join();
			} catch(InterruptedException e)
			{
				stopSystemFromException(e);
			}
		}
	}

	private void stopSystemFromException(Exception e)
	{
		LCD.clear();
		LCD.drawString("EXCEPTION", 0, 0);
		LCD.drawString(e.getClass().toString(), 0, 1);
		LCD.drawString(e.getMessage(), 0, 2);
		stopSorters();
		Delay.msDelay(10000);
		throw new RuntimeException(e);
	}

	@Override
	public void axleDetected(final int length)
	{
		try
		{
			if(!Main.NO_MASTER_MODE)
				master.sendLegoDescription(new LegoItemDescription(LegoBrickType.Axle, length));

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					for(int i = 0; i<length; i++)
					{
						int volume = Sound.getVolume();
						Sound.setVolume(50);
						Sound.playTone(4000, 50, 100);
						Sound.setVolume(volume);
						Delay.msDelay(250);
					}
				}
			}).start();
		} catch(IOException e)
		{
			stopSystemFromException(e);
		}
	}
}
