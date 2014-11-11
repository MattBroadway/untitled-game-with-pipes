/** manages the logical state of the game
*/
public class Game
{
	public LogicalTiles tiles;
	public TilesInputListener tilesInput;
	public MainWindow w;

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
		tilesInput = new TilesInputListener(this);
		w.tiles.refreshGeometry(); // sets the total grid size calculated from the loaded level
	}

	/** update the state of the game (1 unit of time has passed)
	*/
	public void tick()
	{
	}

	/** handle a mouse click action (as reported by the TilesInputListener)
	 */
	public void handleTileClicked(int row, int col)
	{
		tiles.tiles[row][col].rotateCW();
		tiles.updateActiveTiles();
	}
}

