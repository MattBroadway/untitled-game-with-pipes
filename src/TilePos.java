public class TilePos
{
	public int x;
	public int y;

	public TilePos(int setX, int setY)
	{
		x = setX;
		y = setY;
	}

	@Override
	public String toString() { return "TilePos: (" + x + ", " + y + ")"; }
	
	@Override
	public int hashCode()
	{
		// no good for passwords, but I think this will work for our purposes
		// from: http://stackoverflow.com/a/11742657
		final int prime = 5171;
		return prime * (prime + x) + y;
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
		TilePos otherPos = (TilePos)other;
		if(x != otherPos.x || y != otherPos.y)
		{
			return false;
		}
		return true;
	}
	
	public TilePos posAbove() { return new TilePos(x, y-1); }
	public TilePos posBelow() { return new TilePos(x, y+1); }
	public TilePos posToRight() { return new TilePos(x+1, y); }
	public TilePos posToLeft() { return new TilePos(x-1, y); }

}
