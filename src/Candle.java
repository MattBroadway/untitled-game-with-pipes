/**
 * this class represents a logical Candle
 */
public class Candle {

	private final int type;
	private boolean lit;
	private int fuse;
	public static final int EMPTY = 0;
	public static final int NORMAL = 1;
	public static final int TRICK = 2;
	public static final int TNT = 3;
	public static final int KINDLE = 4;
	
	/**
	 * Initialises a new candle, defaulted
	 * @param type 
	 */
	public Candle(int type)
	{
		this.type = type;
		this.fuse = 0;
		if (type == KINDLE || type == EMPTY)
		{
			lit = false;
		}
		else
		{
			lit = true;
		}
	}
	
	/**
	 * Initialises a new candle, custom fuse time
	 * @param type
	 * @param fuse 
	 */
	public Candle(int type, int fuse)
	{
		this.type = type;
		this.fuse = fuse;
		if (type == KINDLE || type == EMPTY)
		{
			lit = false;
		}
		else
		{
			lit = true;
		}
	}
	
	/**
	 * Initialises a new candle, custom initially lit status
	 * @param type
	 * @param lit 
	 */
	Candle(int type, boolean lit)
	{
		this.type = type;
		this.lit = lit;
		fuse = 0;
	}
	
	public boolean getLit()
	{
		return lit;
	}
	
	public String toString()
	{
		return "Type: " + type + ", Fuse: " + fuse;
	}
	
	public void setLit(boolean lit)
	{
		this.lit = lit;
	}
	
	@Override
	public int hashCode()
	{
		int hash = 10 * type;
		if(lit)
		{
			hash++;
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object other)
	{
		return this.hashCode() == other.hashCode();
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
				if (lit)
				{
					lit = false;
				}
				break;
			case TRICK:
				if (lit)
				{
					lit = false;
				}
				break;
			case TNT:
				if (lit)
				{
					lit = false;
				}
				break;
			case KINDLE :
				lit = !lit;
				break;
				
		}
	}
	
}
