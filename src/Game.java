import java.util.Arrays;

/** manages the logical state of the game
*/

public class Game
{
	public LogicalTiles tiles;
	public LogicalCandles candles;
	public TilesInputListener tilesInput;
	public MainWindow w;
	public int cursorX=0, cursorY=0;
	private double startTime;

	/** the levels of the game
	 */
	public final String[] levelFiles = {
		"res/lvl/01.json"
	};

	/** initialise the game
	 */
	public Game()
	{
		
	}

	/** start the game (create a window and load the first level)
	 */
	public void start()
	{
		w = new MainWindow(this, "Pipes Game", 30/*FPS*/);
		w.tiles.setPos(100, 100);
		
		// audio test
		Audio audio = new Audio();
		audio.register("intro", "res/sfx/trololo3.wav");
		
		loadLevel(0);
	}

	/** load a level from the levelFiles attribute (zero indexed)
	 */
	public void loadLevel(int level)
	{
		long loadStart = System.currentTimeMillis();

		tiles = new LogicalTiles(levelFiles[level]);
		candles = new LogicalCandles(levelFiles[level], tiles.getCols());
		tilesInput = new TilesInputListener(this);
		w.tiles.refreshGeometry(); // sets the total grid size calculated from the loaded level
		startTime = System.currentTimeMillis();	
		tiles.updateActiveTiles();

		long loadTime = System.currentTimeMillis() - loadStart;
		System.out.println("level " + level + " (" + levelFiles[level] + ") loaded in " + loadTime + "ms");
	}

	/** update the state of the game (1 unit of time has passed)
	 */
	public void tick()
	{
		boolean toBlow[] = getActiveBottomTiles();
		
		for (int i = 0; i < toBlow.length; i++)
		{
			if (toBlow[i]) {
				candles.candles[i].blow();
			}
		}
	}

	public void rotateCW()
	{
		tiles.get(cursorY, cursorX).rotateCW();
		tiles.updateActiveTiles();
	}
	
	public void rotateACW()
	{
		tiles.get(cursorY, cursorX).rotateACW();
		tiles.updateActiveTiles();
	}
	
	public void setCursor(int row, int col)
	{
		cursorX = col;
		if (cursorX >= tiles.getCols()) cursorX = 0;
		else if (cursorX < 0) cursorX = tiles.getCols()-1;
		
		cursorY = row;
		if (cursorY >= tiles.getRows()) cursorY = 0;
		else if (cursorY < 0) cursorY = tiles.getRows()-1;
	}
	
	public void moveCursor(int dy, int dx)
	{
		setCursor(cursorY + dy, cursorX + dx);
	}

	public double getTimePassed()
	{
		return (System.currentTimeMillis() - startTime)/1000;
	}
	
	public boolean[] getActiveBottomTiles()
	{
		boolean ret[] = new boolean[tiles.getCols()];
		
		for(int col = 0; col < tiles.getCols(); col++)
		{
			LogicalTiles.TilePos p = new LogicalTiles.TilePos(tiles.getRows()-1, col);
			ret[col] = tiles.activeTiles.contains(p) && tiles.get(p).bottom;
		}
		return ret;
	}
}
