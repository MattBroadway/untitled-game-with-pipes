import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import org.json.JSONArray;
import org.json.JSONObject;

/** manages the logical state of the tiles and provides an interface with manipulating them
*/
public class LogicalCandles
{
	public Candle[] candles;
	// ideally this info should be in one place, not two places that
	// are kept in sync
	public int cols;
	
	/** initialise logicalTiles from a JSON level file
	 * reads the 'tiles' attribute in the JSON object
	*/
	public LogicalCandles(String JSONFile, int cols)
	{
		this.cols = cols;
		loadCandlesFromJSONFile(JSONFile);
	}
		
	/**
	 * returns a boolean array of whether of not each candle is lit
	 * @return stats the array mentioned above
	 */
	public boolean[] getCandlesStatus()
	{
		boolean[] stats = new boolean[cols];
		for (int i = 0; i < cols; i++) {
			stats[i] = candles[i].lit;
		}
		return stats;
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
	 * reads the "candles" attribute in the JSON object
	*/
	public void loadCandlesFromJSONFile(String JSONFile)
	{
		loadCandlesFromJSONString(readFileToString(JSONFile));
	}
		
	/** initialise logicalTiles from a JSON level string
	 * reads the "candles" attribute in the JSON object
	*/
	public void loadCandlesFromJSONString(String JSON)
	{
		JSONObject o = new JSONObject(JSON);
		JSONArray candleArray = o.getJSONArray("candles");
		
		candles = new Candle[cols];

		System.out.println(candleArray);
		
		
		for(int col = 0; col < cols; col++)
		{
		
			JSONObject candleEntry = candleArray.getJSONObject(col);

			Candle.Type t = Candle.Type.valueOf(candleEntry.getString("type"));

			// these become null if not present, in which case the constructor
			// chooses appropriate default values
			Integer fuse = candleEntry.has("fuse") ? candleEntry.getInt("fuse") : null;
			Boolean lit = candleEntry.has("lit") ? candleEntry.getBoolean("lit") : null;

			candles[col] = new Candle(t, fuse, lit);
		}		
	}
	
}
