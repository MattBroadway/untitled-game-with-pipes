import java.io.BufferedReader;
import java.io.FileReader;
import org.json.*;
import java.util.Queue;
import java.util.HashSet;
import java.util.LinkedList;



/** manages the logical state of the tiles and provides an interface with manipulating them
*/
public class LogicalTiles
{
	public static class TilePos
	{
		public int row;
		public int col;

		public TilePos(int setRow, int setCol)
		{
			row = setRow;
			col = setCol;
		}
		
		@Override
		public String toString() { return "TilePos: (" + row + ", " + col + ")"; }

		@Override
		public int hashCode()
		{
			// no good for passwords, but I think this will work for our purposes
			// from: http://stackoverflow.com/a/11742657
			final int prime = 5171;
			return prime * (prime + row) + col;
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
			if(row != otherPos.row || col != otherPos.col)
			{
				return false;
			}
			return true;
		}
		
		public TilePos posAbove() { return new TilePos(row-1, col); }
		public TilePos posBelow() { return new TilePos(row+1, col); }
		public TilePos posToRight() { return new TilePos(row, col+1); }
		public TilePos posToLeft() { return new TilePos(row, col-1); }

	}


	/** 2D array of tiles
		row major (access like so: tiles[row][col])
	*/
	public Tile[][] tiles;
	/** whether there is a path from a given tile to the top row (where the air is coming from)
		//TODO: move this into tile object maybe...
	*/
	public HashSet<TilePos> activeTiles;
	private int rows;
	private int cols;


	/** initialise logicalTiles given the number of rows and columns in the grid
	*/
	public LogicalTiles(int setRows, int setCols)
	{
		rows = setRows;
		cols = setCols;
		tiles = new Tile[rows][cols];
		activeTiles = new HashSet<TilePos>();
	}
	/** initialise logicalTiles from a JSON level file
	 * reads the 'tiles' attribute in the JSON object
	*/
	public LogicalTiles(String JSONFile)
	{
		loadTilesFromJSONFile(JSONFile);
	}

	/** helper function to load files
	*/
	private static String readFileToString(String filename)
	{
		BufferedReader reader = null;
 
		String fileContents = "";
		try
		{
			String line;
 
			reader = new BufferedReader(new FileReader(filename));
 
			while((line = reader.readLine()) != null)
			{
				fileContents += line;
			}
 
		}
		catch(java.io.IOException e)
		{
			System.out.println("Failed to load level: " + filename);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(reader != null)
				{
					reader.close();
				}
			}
			catch(java.io.IOException e)
			{
				System.out.println("Failed to load level: " + filename);
				e.printStackTrace();
			}
		}

		return fileContents;

	}

	/** initialise logicalTiles from a JSON level file
	 * reads the 'tiles' attribute in the JSON object
	*/
	public void loadTilesFromJSONFile(String JSONFile)
	{
		loadTilesFromJSONString(readFileToString(JSONFile));
	}
	/** initialise logicalTiles from a JSON level string
	 * reads the 'tiles' attribute in the JSON object
	*/
	public void loadTilesFromJSONString(String JSON)
	{

		activeTiles = new HashSet<TilePos>();


		JSONObject o = new JSONObject(JSON);
		JSONArray tileArray = o.getJSONArray("tiles");
		rows = tileArray.length();
		cols = tileArray.getJSONArray(0).length();

		tiles = new Tile[rows][cols];

		for(int row = 0; row < rows; row++)
		{
			JSONArray tileRow = tileArray.getJSONArray(row);
			for(int col = 0; col < cols; col++)
			{
				JSONArray tileEntry = tileRow.getJSONArray(col);
				
				tiles[row][col] = new Tile(
					readBoolFrom(tileEntry, 0),
					readBoolFrom(tileEntry, 1),
					readBoolFrom(tileEntry, 2),
					readBoolFrom(tileEntry, 3)
				);
			}



		}
		
	}
	/** read a boolean attribute from a JSON array
	 * if an integer is found then it counts 0 to be false and everything else true
	 * @param arr the array to load from (eg of the form: [1,1,0,0] or [true,true,false,false])
	 * @param index the item from the array to load (zero indexed)
	 */
	public boolean readBoolFrom(JSONArray arr, int index)
	{
		boolean val = false;

		int valAsInt = arr.optInt(index, -1);
		if(valAsInt != -1) // -1 if not convertible to int
		{
			val = (valAsInt != 0); // might want to check if not one of: {0,1}
		}
		else
		{
			val = arr.getBoolean(index); // throws exception if not boolean
		}

		return val;
	}
	
	public int getRows() { return rows; }
	public int getCols() { return cols; }

	public Tile get(int row, int col) { return tiles[row][col]; }
	public Tile get(TilePos p) { return tiles[p.row][p.col]; }
	public Tile getAbove(int row, int col) { return tiles[row-1][col]; }
	public Tile getRightOf(int row, int col) { return tiles[row][col+1]; }
	public Tile getBelow(int row, int col) { return tiles[row+1][col]; }
	public Tile getLeftOf(int row, int col) { return tiles[row][col-1]; }


	/** call after each move
	*/
	public void updateActiveTiles()
	{
		activeTiles = getActiveTiles();
	}

	/** return connections with adjacent pipes
	 *	if this pipe cannot connect with an adjacent pipe with a connectible edge bordering this, that does not count
	 * @return [top, right, bottom, left]
	 */
	public boolean[] getAdjacentConnections(int row, int col)
	{
		boolean[] adjacency = new boolean[4];
		Tile t = get(row, col);

		
		// [top, right, bottom, left]
		adjacency[0] = t.top &&		(row > 0) &&		getAbove(row,col).bottom;
		adjacency[1] = t.right &&	(col < cols-1) &&	getRightOf(row,col).left;
		adjacency[2] = t.bottom &&	(row < rows-1) &&	getBelow(row,col).top;
		adjacency[3] = t.left &&	(col > 0) &&		getLeftOf(row,col).right;
		
		return adjacency;
	}

	/** generate a search tree for the tiles using breadth-first search. stop when every reachable node has been visited
	 */
	public HashSet<TilePos> getActiveTiles()
	{
		Queue<TilePos> frontier = new LinkedList<TilePos>();
		HashSet<TilePos> visited = new HashSet<TilePos>();

		// generate an entire search tree
		for(int col = 0; col < cols; col++)
		{
			TilePos t = new TilePos(0,col);
			// note: this is different to what is returned by getAdjacentConnections, if connectible edge faces upwards on top row: true
			if(get(t).top) // don't add if not connected to top
			{
				visited.add(t);
				frontier.add(t);
			}
		}

		while(!frontier.isEmpty())
		{
			TilePos t = frontier.poll();

			// this function already checks for edges, so no need to repeat checks
			boolean[] adj = getAdjacentConnections(t.row, t.col);
			
			if(adj[0]) // top
			{
				TilePos above = t.posAbove();
				if(!visited.contains(above))
				{
					visited.add(above);
					frontier.add(above);
				}
			}
			if(adj[1]) // right
			{
				TilePos right = t.posToRight();
				if(!visited.contains(right))
				{
					visited.add(right);
					frontier.add(right);
				}
			}
			if(adj[2]) // below
			{
				TilePos below = t.posBelow();
				if(!visited.contains(below))
				{
					visited.add(below);
					frontier.add(below);
				}
			}
			if(adj[3]) // left
			{
				TilePos left = t.posToLeft();
				if(!visited.contains(left))
				{
					visited.add(left);
					frontier.add(left);
				}
			}
		}

		return visited;
	}
}
