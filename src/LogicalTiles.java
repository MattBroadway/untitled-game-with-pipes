/** manages the logical state of the tiles and provides an interface with manipulating them
*/
public class LogicalTiles
{
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

	/** a path of tiles that are joined
	*/
	public class Path
	{
		public class TilePos { public int row; public int col; }
		public TilePos[] tiles;

		/** get column that the path comes out at (or -1 if dead end)
		@param rowCount the number of rows on the board
		*/
		public int outCol(int rowCount)
		{
			if(tiles.length == 0) { return -1; }
			
			TilePos p = tiles[tiles.length-1];
			if(p.row == rowCount) // path makes it out
			{
				return p.col;
			}
			else // path does not make it out
			{
				return -1;
			}
		}
	}

	/** A matrix representation of a graph representation of the tiles
		row major (access like so: tiles[row][col])
	*/
	public Tile[][] tiles;
	private int rows;
	private int cols;


	public LogicalTiles(int setRows, int setCols)
	{
		rows = setRows;
		cols = setCols;
		tiles = new Tile[rows][cols];

		
		
	}
	

	/** get the paths from the top row
	*/
	public Path[] getPathsFrom(int col)
	{
		// use some search algorithm to traverse until all the paths out
		// are found

		// maybe use ArrayList<ArrayList<Path.TilePos>> while searching
		// then grab static arrays from them at the end
		return null;
	}
}
