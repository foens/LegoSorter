package olefoens.communication;

public enum ClientRole
{
	Sorter,
	Distributor;

	public static ClientRole ownValueOf(String name)
	{
		for(ClientRole type : ClientRole.values())
		{
			if(type.name().equalsIgnoreCase(name))
				return type;
		}
		throw new RuntimeException(name + " not found");
	}
}
