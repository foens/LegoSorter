package olefoens.communication;

public class Packet
{
	public final PacketType type;
	public final Object content;

	public Packet(PacketType type, Object content)
	{
		this.type = type;
		this.content = content;
	}
}
