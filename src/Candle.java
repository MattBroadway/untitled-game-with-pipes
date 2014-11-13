/**
 * this class represents a logical Candle
 */
public class Candle {

	public final Type type;
	public boolean lit;
	public int fuse;

	public static enum Type
	{
		EMPTY(0),
		NORMAL(1),
		TRICK(2),
		TNT(3),
		KINDLE(4);
		
		public final int id;
		Type(int id) { this.id = id; }
	}
	public static int[] defaultFuse =
	{
		0, // EMPTY
		0, // NORMAL
		5, // TRICK
		15,// TNT
		0, // KINDLE
	};
	public static boolean[] defaultLit =
	{
		false, // EMPTY
		true,  // NORMAL
		true,  // TRICK
		true,  // TNT
		false, // KINDLE
	};

	/**
	 * Initialises a new candle, default values dependent on type
	 * @param type see Candle.Type docs
	 */
	public Candle(Type type)
	{
		this.type = type;
		this.fuse = defaultFuse[type.id];
		this.lit = defaultLit[type.id];
	}
	
	/**
	 * Initialises a new candle, custom settings
	 * @param type see Candle.Type docs
	 * @param fuse set the fuse time (null for default)
	 * @param lit whether the candle is initially lit (null for default)
	 */
	public Candle(Type type, Integer fuse, Boolean lit)
	{
		this.type = type;
		
		this.fuse = (fuse==null) ? defaultFuse[type.id] : fuse;
		this.lit = (lit==null) ? defaultLit[type.id] : lit;
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
