/** This class represents a logical tile
*/
public class Tile
{
	/** these attributes refer to the edges of the tile which are 'connectible'
	*/
	public boolean top, right, bottom, left;

	/** initialise a tile by passing booleans
	*/
	Tile(boolean setTop, boolean setRight, boolean setBottom, boolean setLeft)
	{
		set(setTop, setRight, setBottom, setLeft);
	}
	/** set a tile by passing booleans
	*/
	public void set(boolean setTop, boolean setRight, boolean setBottom, boolean setLeft)
	{
		top = setTop;
		right = setRight;
		bottom = setBottom;
		left = setLeft;
	}
	/** Use a bitmask to represent the boolean attributes
	 * ie.
	 * bit 1 = top
	 * bit 2 = right
	 * bit 3 = bottom
	 * bit 4 = left
	 * @return the hash of the tile object
	 */
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
	/** set a tile by passing a bit mask ( see hashCode() )
	*/
	public void setByHash(int hash)
	{
		// bitwise operations in java are officially retarded
		top =	(hash & 1<<0) == 1;
		right =	(hash & 1<<1) == 1;
		bottom =(hash & 1<<2) == 1;
		left =	(hash & 1<<3) == 1;
	}
	/** test whether two tiles are logically equivalent
	 * equivalence is determined like so:
	 * 	- same object =&gt; true
	 *	- one is null =&gt; false
	 *	- different classes =&gt; false
	 *	- hashes differ =&gt; false
	 *	- otherwise equivalent (true)
	 * @param other the object to test equivalence with
	 * @return whether the calling object and 'other' are equivalent
	 */
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

		Tile otherTile = (Tile)other;
		if(hashCode() != otherTile.hashCode())
		{
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

