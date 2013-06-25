package olefoens.communication;

public enum PacketType
{
	LegoDescription,
	Reset,
	Resume,
	Ready,
	ClientRole,
	Stop;

	public static PacketType ownValueOf(String name)
	{
		for(PacketType type : PacketType.values())
		{
			if(type.name().equalsIgnoreCase(name))
				return type;
		}
		throw new RuntimeException(name + " not found");
	}
}