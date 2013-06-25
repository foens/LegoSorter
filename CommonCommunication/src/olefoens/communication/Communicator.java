package olefoens.communication;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.util.Delay;

import javax.bluetooth.RemoteDevice;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Communicator
{
	private static final int CONNECTION_MODE = NXTConnection.RAW;
	private final DataInputStream dataInputStream;
	private final DataOutputStream dataOutputStream;

	public Communicator(DataInputStream dataInputStream, DataOutputStream dataOutputStream)
	{
		this.dataInputStream = dataInputStream;
		this.dataOutputStream = dataOutputStream;
	}

	public static Communicator connectToClient(String deviceName) throws IOException
	{
		RemoteDevice device = Bluetooth.getKnownDevice(deviceName);
		if(device == null)
		{
			LCD.clear();
			LCD.drawString("No device found", 0, 0);
			LCD.drawString(" for name", 0, 1);
			LCD.drawString(deviceName, 0, 2);
			LCD.drawString("Pair devices", 0, 3);
			LCD.drawString(" first", 0, 4);
			Delay.msDelay(10000);
			throw new RuntimeException();
		}

		LCD.clear();
		LCD.drawString("Connecting to", 0, 0);
		LCD.drawString(deviceName, 0, 1);

		BTConnection connection = Bluetooth.connect(device.getBluetoothAddress(), CONNECTION_MODE);
		if(connection == null)
			return null;

		return new Communicator(connection.openDataInputStream(), connection.openDataOutputStream());
	}

	private void sendClientRole(ClientRole clientRole) throws IOException
	{
		dataOutputStream.writeUTF(PacketType.ClientRole.name());
		dataOutputStream.writeUTF(clientRole.name());
		dataOutputStream.flush();
	}

	public static Communicator acceptConnectionFromMaster(ClientRole role) throws IOException
	{
		LCD.clear();
		LCD.drawString("Wating for ", 0, 0);
		LCD.drawString(" master to", 0, 1);
		LCD.drawString(" connect", 0, 2);
		BTConnection connection = Bluetooth.waitForConnection(0, CONNECTION_MODE);
		Communicator communicator = new Communicator(connection.openDataInputStream(), connection.openDataOutputStream());
		communicator.sendClientRole(role);
		LCD.clear();
		return communicator;
	}

	public void sendLegoDescription(LegoItemDescription description) throws IOException
	{
		dataOutputStream.writeUTF(PacketType.LegoDescription.name());
		dataOutputStream.writeUTF(description.type.name());
		dataOutputStream.writeInt(description.length);
		dataOutputStream.flush();
	}

	public void sendStop() throws IOException
	{
		dataOutputStream.writeUTF(PacketType.Stop.name());
		dataOutputStream.flush();
	}

	public void sendResume() throws IOException
	{
		dataOutputStream.writeUTF(PacketType.Resume.name());
		dataOutputStream.flush();
	}

	public void sendReady() throws IOException
	{
		dataOutputStream.writeUTF(PacketType.Ready.name());
		dataOutputStream.flush();
	}

	public void sendReset() throws IOException
	{
		dataOutputStream.writeUTF(PacketType.Reset.name());
		dataOutputStream.flush();
	}

	public Packet readPacket() throws IOException
	{
		PacketType packetType = PacketType.ownValueOf(dataInputStream.readUTF());
		switch(packetType)
		{
			case LegoDescription:
				LegoBrickType type = LegoBrickType.ownValueOf(dataInputStream.readUTF());
				int length = dataInputStream.readInt();
				return new Packet(packetType, new LegoItemDescription(type, length));

			case Stop:
			case Resume:
			case Reset:
			case Ready:
				return new Packet(packetType, null);

			case ClientRole:
				ClientRole clientRole = ClientRole.ownValueOf(dataInputStream.readUTF());
				return new Packet(packetType, clientRole);

			default:
				throw new RuntimeException();
		}
	}

	public boolean havePacketToRead() throws IOException
	{
		return dataInputStream.available() > 0;
	}
}
