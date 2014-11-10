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


	/** initialise logicalTiles given the number of rows and columns in the grid
	*/
	public LogicalTiles(int setRows, int setCols)
	{
		rows = setRows;
		cols = setCols;
		tiles = new Tile[rows][cols];
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
	public void loadTilesFromJSONFile(String filename)
	{
		loadTilesFromJSONString(readFileToString(JSONFile));
	}
	/** initialise logicalTiles from a JSON level string
	 * reads the 'tiles' attribute in the JSON object
	*/
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
