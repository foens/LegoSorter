package olefoens.communication;

public enum LegoBrickType
{
	Beam,
	Axle;

	public static LegoBrickType ownValueOf(String name)
	{
		for(LegoBrickType type : LegoBrickType.values())
		{
			if(type.name().equalsIgnoreCase(name))
				return type;
		}
		throw new RuntimeException(name + " not found");
	}
}
