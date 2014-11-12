import java.util.Arrays;

/** manages the logical state of the game
*/

public class Game
{
	public LogicalTiles tiles;
	public TilesInputListener tilesInput;
	public MainWindow w;
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

		loadLevel(0);
		tiles.updateActiveTiles();
	}

	/** load a level from the levelFiles attribute (zero indexed)
	 */
	public void loadLevel(int level)
	{
		tiles = new LogicalTiles(levelFiles[level]);
		tilesInput = new TilesInputListener(this);
		w.tiles.refreshGeometry(); // sets the total grid size calculated from the loaded level
		startTime = System.currentTimeMillis();	
	}

	/** update the state of the game (1 unit of time has passed)
	*/
	public void tick()
	{
		System.out.println(Arrays.toString(getActiveCandles()));
	}

	/** handle a mouse click action (as reported by the TilesInputListener)
	 */
	public void handleTileClicked(int row, int col, boolean leftClick)
	{
		if(leftClick)
			tiles.tiles[row][col].rotateCW();
		else
			tiles.tiles[row][col].rotateACW();

		tiles.updateActiveTiles();
	}

	public double getTimePassed()
	{
		return (System.currentTimeMillis() - startTime)/1000;
	}

	public boolean[] getActiveCandles()
	{
		boolean ret[] = new boolean[tiles.tiles[0].length];
		
		for(LogicalTiles.TilePos tp : tiles.activeTiles)
			if(tp.row == tiles.tiles.length)
				ret[tp.col] = true;

		return ret;
	}
}

