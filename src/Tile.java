public class Tile
{
	public boolean top, right, bottom, left;

	Tile(boolean setTop, boolean setRight, boolean setBottom, boolean setLeft)
	{
		set(setTop, setRight, setBottom, setLeft);
	}
	public void set(boolean setTop, boolean setRight, boolean setBottom, boolean setLeft)
	{
		top = setTop;
		right = setRight;
		bottom = setBottom;
		left = setLeft;
	}
	@Override
	public int hashCode()
	{
		int result = 0;
		if(top) { result += 1<<0; }
		if(right) { result += 1<<1; }
		if(bottom) { result += 1<<2; }
		if(left) { result += 1<<3; }
		return result;
	}
	public void setByHash(int hash)
	{
		// bitwise operations in java are officially retarded
		top =	(hash & 1<<0) == 1;
		right =	(hash & 1<<1) == 1;
		bottom =(hash & 1<<2) == 1;
		left =	(hash & 1<<3) == 1;
	}
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
		{
			System.out.println("(this == other)");
			return true;
		}
		if(other == null)
		{
			System.out.println("(other == null)");
			return false;
		}
		if(getClass() != other.getClass())
		{
			System.out.println("(getClass() != other.getClass())");
			return false;
		}

		Tile otherTile = (Tile)other;
		if(hashCode() != otherTile.hashCode())
		{
			System.out.println("(hashCode() != other.hashCode())");
			return false;
		}
		return true;
	}

	/** rotate the tile clockwise 90 degrees
	*/
	public void rotateCW()
	{
		boolean oldTop = top;

		top = left;
		left = bottom;
		bottom = right;
		right = oldTop;
	}
	/** rotate the tile anti-clockwise 90 degrees
	*/
	public void rotateACW()
	{
		boolean oldTop = top;

		top = right;
		right = bottom;
		bottom = left;
		left = oldTop;
	}
}

