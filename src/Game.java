/** manages the logical state of the game
*/
public class Game
{
	public LogicalTiles tiles;

	public final String[] levelFiles = {
		"res/lvl/01.json"
	};

	public Game()
	{
		loadLevel(0);
	}

	public void loadLevel(int level)
	{
		tiles = new LogicalTiles(levelFiles[level]);
	}

	/** update the state of the game (1 unit of time has passed)
	*/
	public void tick()
	{
		// Game logic goes here
	}
}

