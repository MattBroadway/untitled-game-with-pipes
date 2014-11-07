import java.io.BufferedReader;
import java.io.FileReader;
import org.json.*;

/** manages the logical state of the tiles and provides an interface with manipulating them
*/
public class LogicalTiles
{
	
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

	/** 2D array of tiles
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
	public LogicalTiles(String JSONFile)
	{
		BufferedReader reader = null;
 
		String JSONString = "";
		try
		{
			String line;
 
			reader = new BufferedReader(new FileReader(JSONFile));
 
			while((line = reader.readLine()) != null)
			{
				JSONString += line;
			}
 
		}
		catch(java.io.IOException e)
		{
			System.out.println("Failed to load level: " + JSONFile);
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
				System.out.println("Failed to load level: " + JSONFile);
				e.printStackTrace();
			}
		}
		
		loadTilesFromJSONString(JSONString);
	}

	public void loadTilesFromJSONString(String JSON)
	{

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
	public boolean readBoolFrom(JSONArray arr, int index)
	{
		boolean val = false;

		int valAsInt = arr.optInt(index, -1);
		if(valAsInt != -1) // -1 if not convertible to int
		{
			val = (valAsInt == 1); // might want to check if not one of: {0,1}
		}
		else
		{
			val = arr.getBoolean(index); // throws exception if not boolean
		}

		return val;
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
