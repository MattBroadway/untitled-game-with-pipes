/**
 * this class represents a logical Candle
 */
public class Candle
{
	public final Type type;
	public boolean lit;
	/**
	 * //TODO: could someone please document your intention for this attribute
	 * time until detonation? units?
	 */
	public int fuse;
	public int blownFromX; // the x coordinate of the tiles that blow on this candle

	public static enum Type
	{
		NORMAL(0),
		TRICK(1),
		TNT(2),
		KINDLE(3);
		
		public final int id;
		Type(int id) { this.id = id; }
	}
	public static int[] defaultFuse =
	{
		0, // NORMAL
		5, // TRICK
		15,// TNT
		0, // KINDLE
	};
	public static boolean[] defaultLit =
	{
		true,  // NORMAL
		true,  // TRICK
		true,  // TNT
		false, // KINDLE
	};

	/**
	 * Initialises a new candle, default values dependent on type
	 * @param type see Candle.Type docs
	 */
	public Candle(Type setType)
	{
		type = setType;
		fuse = defaultFuse[type.id];
		lit = defaultLit[type.id];
		blownFromX = -1;
	}
	
	/**
	 * Initialises a new candle, custom settings
	 * @param type see Candle.Type docs
	 * @param fuse set the fuse time (null for default)
	 * @param lit whether the candle is initially lit (null for default)
	 */
	public Candle(Type setType, Integer fuse, Boolean lit)
	{
		type = setType;
		
		fuse = (fuse==null) ? defaultFuse[type.id] : fuse;
		lit = (lit==null) ? defaultLit[type.id] : lit;
		blownFromX = -1;
	}

	
	public String toString()
	{
		return "Candle of Type: " + type + ", Fuse: " + fuse;
	}
	
	
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
		{
			return true;
		}
		if(other == null)
		{
			return false;
		}
		if(getClass() != other.getClass())
		{
			return false;
		}

		Candle otherCandle = (Candle)other;
		if(hashCode() != otherCandle.hashCode())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * looks at the type of candle this is and interacts with it accordingly
	 * NORMAL candles will go out once blown
	 * TRICK candles will go out once blown and relight after it's fuse time expires
	 * TNT will go out once blown
	 * KINDLE candles reverse their lit status when blown
	 */
	public void blow()
	{
		switch(type)
		{
			case NORMAL:
			{
				lit = false;
				break;
			}
			case TRICK:
			{
				lit = false;
				break;
			}
			case TNT:
			{
				lit = false;
				break;
			}
			case KINDLE :
			{
				lit = !lit;
				break;
			}
			default:
				throw new java.lang.UnsupportedOperationException();
		}
	}
	
}
