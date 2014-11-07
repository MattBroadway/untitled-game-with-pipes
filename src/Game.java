/** manages the logical state of the game
*/
public class Game
{
	public LogicalTiles tiles;
	public TilesInputListener tilesInput;
	public MainWindow w;

	public final String[] levelFiles = {
		"res/lvl/01.json"
	};

	public Game()
	{

	}

	public void start()
	{
		w = new MainWindow(this, "Pipes Game", 30/*FPS*/);
		w.tiles.setPos(100, 100);

		loadLevel(0);
	}

	public void loadLevel(int level)
	{
		tiles = new LogicalTiles(levelFiles[level]);
		tilesInput = new TilesInputListener(this);
		w.tiles.refreshGeometry();
	}

	/** update the state of the game (1 unit of time has passed)
	*/
	public void tick()
	{
		// Game logic goes here
	}

	public void handleTileClicked(int row, int col)
	{
		tiles.tiles[row][col].rotateCW();
	}
}

