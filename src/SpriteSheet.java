import java.util.HashMap;
import java.util.HashSet;

public class SpriteSheet
{
	public HashMap<Tile, Image> tiles;
	public HashMap<Candle.Type, Image> unlitCandles;
	public HashMap<Candle.Type, Image> litCandles;
	public Image cursor;
	

	public SpriteSheet(String loadName)
	{
		loadByName(loadName);
	}

	public Image getTileImage(Tile t)
	{
		return tiles.get(t);
	}
	public Image getCandleImage(Candle c)
	{
		if(c.lit)
		{
			return litCandles.get(c.type);
		}
		else
		{
			return unlitCandles.get(c.type);
		}
	}



// LOADING CODE BELOW
	/**
	 * name is used as the subdirectory in tilesets/ to look in
	 */
	public void loadByName(String name)
	{
		tiles = new HashMap<Tile, Image>();
		unlitCandles = new HashMap<Candle.Type, Image>();
		litCandles = new HashMap<Candle.Type, Image>();

		String baseDir = "res/tiles/"+name+"/"; // this is bad. But it's ok for now
		// could (should?) test for this
		//if(Files.notExists(baseDir)) { throw new RuntimeException(); }


		cursor = new Image(baseDir + "cursor.png");

		loadTileByName(baseDir, "right-angle", new boolean[]{false, true, true, false});
		loadTileByName(baseDir, "straight", new boolean[]{true, false, true, false});
		loadTileByName(baseDir, "cross", new boolean[]{true, true, true, true});
		loadTileByName(baseDir, "T", new boolean[]{false, true, true, true});

		loadCandle(baseDir, "normal-candle", Candle.Type.NORMAL);
		loadCandle(baseDir, "trick-candle", Candle.Type.TRICK);
		loadCandle(baseDir, "TNT-candle", Candle.Type.TNT);
		loadCandle(baseDir, "kindle-candle", Candle.Type.KINDLE);
	}

	private void loadTileByName(String baseDir, String name, boolean[] con)
	{
		loadTileRotations(
				new Image(baseDir + name + "-inactive.jpg"),
				new Tile(con[0], con[1], con[2], con[3], false));
		loadTileRotations(
				new Image(baseDir + name + "-active.jpg"),
				new Tile(con[0], con[1], con[2], con[3], true));
	}
	private void loadCandle(String baseDir, String name, Candle.Type type)
	{
		Image i = new Image(baseDir + name + "-unlit.jpg");
		unlitCandles.put(type, i);
		i = new Image(baseDir + name + "-lit.jpg");
		litCandles.put(type, i);
	}

	/** load multiple rotations of a tile at once
	 * @param i the base image
	 * @param t the base tile
	 */
	private void loadTileRotations(Image i, Tile t)
	{
		// not the most optimal of solutions, but it will greatly simplify tile loading
		HashSet<Tile> addedTiles = new HashSet<Tile>();
		tiles.put(t, i);

		for(int angle = 0; angle < 360; angle += 90)
		{
			t = new Tile(t);
			t.rotateCW();

			i = i.getRotatedCopy();

			if(addedTiles.contains(t)) { continue; }

			tiles.put(t, i);
			addedTiles.add(t);
		}
	}
}

