import java.util.Arrays;

/** manages the logical state of the game
*/

public class Game
{
	public LogicalTiles tiles;
        public LogicalCandles candles;
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
	}

	/** load a level from the levelFiles attribute (zero indexed)
	 */
	public void loadLevel(int level)
	{
		tiles = new LogicalTiles(levelFiles[level]);
                candles = new LogicalCandles(levelFiles[level], tiles.getCols());
		tilesInput = new TilesInputListener(this);
		w.tiles.refreshGeometry(); // sets the total grid size calculated from the loaded level
		startTime = System.currentTimeMillis();	
		tiles.updateActiveTiles();
	}

	/** update the state of the game (1 unit of time has passed)
	*/
	public void tick()
	{
		//System.out.println(Arrays.toString(getActiveCandles()));
            boolean toBlow[] = getActiveBottomTiles();
                
                for (int i = 0; i < toBlow.length; i++)
                {
                        if (toBlow[i]) {
                                candles.candles[i].blow();
                        }
                }
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


