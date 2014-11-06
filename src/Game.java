/** manages the logical state of the game
*/
public class Game
{
	public LogicalTiles tiles;

	public Game()
	{
		tiles = new LogicalTiles(6, 6);
	}

	/** update the state of the game (1 unit of time has passed)
	*/
	public void tick()
	{
		// Game logic goes here
	}
}

