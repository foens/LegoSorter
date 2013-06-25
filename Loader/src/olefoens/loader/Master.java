package olefoens.loader;

import lejos.nxt.LCD;
import olefoens.communication.*;

import java.io.IOException;

public class Master
{
	private static final String DistributorDeviceName = "Distributor";
	private static final String BeamCounterDeviceName = "BeamCounter";

	private Communicator sorter;
	private Communicator distributor;

	public void waitForClientsToConnected() throws IOException
	{
		boolean toggle = false;
		while(sorter == null || distributor == null)
		{
			toggle = !toggle;
			LCD.clear();
			LCD.drawString("S " + (sorter != null ? "connected" : "no link"), 0, 0);
			LCD.drawString("D " + (distributor != null ? "connected" : "no link"), 0, 1);

			Communicator communicator;
			if(sorter == null && distributor == null)
				communicator = Communicator.connectToClient(toggle ? DistributorDeviceName : BeamCounterDeviceName);
			else if(sorter == null)
				communicator = Communicator.connectToClient(BeamCounterDeviceName);
			else
				communicator = Communicator.connectToClient(DistributorDeviceName);

			if(communicator == null)
				continue;

			Packet packet = communicator.readPacket();
			if(packet.type != PacketType.ClientRole)
				throw new RuntimeException();

			switch((ClientRole) packet.content)
			{
				case Distributor:
					distributor = communicator;
					break;

				case Sorter:
					sorter = communicator;
					break;
			}
		}
	}

	public void sendResetToClients() throws IOException
	{
		sorter.sendReset();
		distributor.sendReset();
	}

	public LegoItemDescription getSorterLegoDescription() throws IOException
	{
		Packet packet = sorter.readPacket();
		if(packet.type != PacketType.LegoDescription)
			throw new RuntimeException();

		return (LegoItemDescription) packet.content;
	}

	public void sorterStop() throws IOException
	{
		sorter.sendStop();
	}

	public void waitForDistributorReady() throws IOException
	{
		Packet packet = distributor.readPacket();
		if(packet.type != PacketType.Ready)
			throw new RuntimeException();
	}

	public void sendLegoDescriptionToDistributor(LegoItemDescription legoDescription) throws IOException
	{
		distributor.sendLegoDescription(legoDescription);
	}

	public void sendResumeToSorter() throws IOException
	{
		sorter.sendResume();
	}
}
