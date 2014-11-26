import org.json.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Queue;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Load information about a level from a JSON file and store it
 */
public class Level
{
	Game game; // need access to global data

	/** major axis: x. This is to fit with the standard convention of (x,y)
		access by tiles[x][y] (zero indexed of course). (0,0) is located in the top left
	 */
	public Tile[][] tiles;
	public Tile[] topRow;
	/**
	 * indexed by the column that they are tied with, however they have independent positioning data and are not spatially tied to these columns
	 */
	public Candle[] candles;

	public Image person;
	// number of pixels from the top of the person to the top of the straight pipe
	// tile to be placed by their mouth
	// NOTE: if anyone has a better name I'm all ears, but for now its called Pp
	int Pp;
	public Image cake;
	// number of pixels from the top of the cake graphic to place candles
	int candleGoodZoneTop;
	int candleGoodZoneBottom;


	public Level(Game setGame, String levelFile)
	{
		game = setGame;
		loadFromString(readFileToString(levelFile));
	}

	public int getXRes() { return tiles.length; }
	// this is unsafe, but who cares. Just don't call it on an empty level!
	// this also assumes that each of the 'columns' are the same length. (as they should be)
	public int getYRes() { return tiles[0].length; }

	public Tile getTileAt(int x, int y) { return tiles[x][y]; }
	public Tile getTileAt(TilePos p) { return tiles[p.x][p.y]; }


	public void updateAfterMove()
	{
		updateTileActiveStatus();
	}



	public boolean[] getAdjacentConnections(TilePos p)
	{
		boolean[] adjacency = new boolean[4];
		Tile t = getTileAt(p);

		
		// [top, right, bottom, left]
		adjacency[0] = t.top &&		(p.y > 0) &&		getTileAt(p.posAbove()).bottom;
		adjacency[1] = t.right &&	(p.x < getXRes()-1) &&	getTileAt(p.posToRight()).left;
		adjacency[2] = t.bottom &&	(p.y < getYRes()-1) &&	getTileAt(p.posBelow()).top;
		adjacency[3] = t.left &&	(p.x > 0) &&		getTileAt(p.posToLeft()).right;
		
		return adjacency;
	}


	/** generate a search tree for the tiles using breadth-first search. Stop when every reachable node has been visited
	 */
	public void updateTileActiveStatus()
	{
		Queue<TilePos> frontier = new LinkedList<TilePos>();
		HashSet<TilePos> visited = new HashSet<TilePos>();

		// generate an entire search tree
		for(int x = 0; x < getXRes(); x++)
		{
			// doing two things at once, set all the activity states to false
			for(int y = 0; y < getYRes(); y++)
			{
				getTileAt(x,y).active = false;
			}

			TilePos t = new TilePos(x,0);
			// note: this is different to what is returned by getAdjacentConnections, if connectible edge faces upwards on top row: true
			if(getTileAt(t).top) // don't add if not connected to top
			{
				visited.add(t);
				getTileAt(t).active = true;
				frontier.add(t);
			}
		}

		while(!frontier.isEmpty())
		{
			TilePos t = frontier.poll();

			// this function already checks for edges, so no need to repeat checks
			boolean[] adj = getAdjacentConnections(t);
			
			if(adj[0]) // top
			{
				TilePos above = t.posAbove();
				if(!visited.contains(above))
				{
					visited.add(above);
					frontier.add(above);
					getTileAt(above).active = true;
				}
			}
			if(adj[1]) // right
			{
				TilePos right = t.posToRight();
				if(!visited.contains(right))
				{
					visited.add(right);
					frontier.add(right);
					getTileAt(right).active = true;
				}
			}
			if(adj[2]) // below
			{
				TilePos below = t.posBelow();
				if(!visited.contains(below))
				{
					visited.add(below);
					frontier.add(below);
					getTileAt(below).active = true;
				}
			}
			if(adj[3]) // left
			{
				TilePos left = t.posToLeft();
				if(!visited.contains(left))
				{
					visited.add(left);
					frontier.add(left);
					getTileAt(left).active = true;
				}
			}
		}
		// could return the visited set, however this is now unnecessary for most
		// use cases
	}




// LEVEL LOADING CODE BELOW

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
	

	/**
	 * Load the level from a JSON source file
	 * @param source the JSON source code for the level
	 */
	public void loadFromString(String source)
	{
		// in the future, could elegantly only load new required assets as specified
		// by the level file
		game.ss = new SpriteSheet("neater"); 

		JSONObject file = new JSONObject(source);

		loadCake(file.getString("cake"));
		loadPerson(file.getString("person"));

		JSONArray topRowArray = file.getJSONArray("top-row");
		JSONArray tilesArray = file.getJSONArray("tiles");
		JSONArray candlesArray = file.getJSONArray("candles");

		loadFromTopRowAttribute(topRowArray);
		loadFromTilesAttribute(tilesArray);
		loadFromCandlesAttribute(candlesArray);

	}
	private void loadFromTopRowAttribute(JSONArray topRowArray)
	{
		topRow = new Tile[topRowArray.length()];
		for(int x = 0; x < topRow.length; x++)
		{
			JSONArray tileEntry = topRowArray.getJSONArray(x);
			
			// this isn't great for cache optimisation, but then neither is
			// Java in general...
			topRow[x] = new Tile(
				readBoolFrom(tileEntry, 0),
				readBoolFrom(tileEntry, 1),
				readBoolFrom(tileEntry, 2),
				readBoolFrom(tileEntry, 3)
			);
		}
	}
	private void loadPerson(String filename)
	{
		person = new Image("res/people/"+filename);
		Pp = Integer.parseInt(readFileToString("res/people/"+filename+".Pp"));
	}
	private void loadCake(String filename)
	{
		cake = new Image("res/cakes/"+filename);
		String[] boundaries = readFileToString("res/cakes/"+filename+".boundaries").split(",");
		candleGoodZoneTop = Integer.parseInt(boundaries[0]);
		candleGoodZoneBottom = Integer.parseInt(boundaries[1]);
	}
	private void loadFromTilesAttribute(JSONArray tilesAttrib)
	{
		int yRes = tilesAttrib.length(); // outer arrays descend down the file and so are the y axis
		int xRes = tilesAttrib.getJSONArray(0).length(); // inner arrays go across the file and so are the x axis

		tiles = new Tile[xRes][yRes];
		
		for(int y = 0; y < yRes; y++)
		{
			JSONArray tileRow = tilesAttrib.getJSONArray(y);
			for(int x = 0; x < xRes; x++)
			{
				JSONArray tileEntry = tileRow.getJSONArray(x);
				
				// this isn't great for cache optimisation, but then neither is
				// Java in general...
				tiles[x][y] = new Tile(
					readBoolFrom(tileEntry, 0),
					readBoolFrom(tileEntry, 1),
					readBoolFrom(tileEntry, 2),
					readBoolFrom(tileEntry, 3)
				);
			}
		}
	}
	private void loadFromCandlesAttribute(JSONArray candlesAttrib)
	{
		candles = new Candle[candlesAttrib.length()];
		
		for(int index = 0; index < candles.length; index++)
		{
			JSONObject candleEntry = candlesAttrib.getJSONObject(index);

			Candle.Type t = Candle.Type.valueOf(candleEntry.getString("type"));

			// these become null if not present, in which case the constructor
			// chooses appropriate default values
			Integer fuse = candleEntry.has("fuse") ? candleEntry.getInt("fuse") : null;
			Boolean lit = candleEntry.has("lit") ? candleEntry.getBoolean("lit") : null;

			candles[index] = new Candle(t, fuse, lit);
		}
	}


	/** read a boolean attribute from a JSON array
	 * if an integer is found then it counts 0 to be false and everything else true
	 * @param arr the array to load from (eg of the form: [1,1,0,0] or [true,true,false,false])
	 * @param index the item from the array to load (zero indexed)
	 */
	private boolean readBoolFrom(JSONArray arr, int index)
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

}
