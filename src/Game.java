/** manages the logical state of the game
*/
public class Game
{
	public LogicalTiles tiles;

	public Game()
	{
		tiles = new LogicalTiles(6, 6);
		for(int row = 0; row < tiles.tiles.length; row++)
		{
			int cols = tiles.tiles[0].length;
			for(int col = 0; col < cols; col++)
			{
				tiles.tiles[row][col] = new Tile(true, true, false, false);
			}
		}
	}

	/** update the state of the game (1 unit of time has passed)
	*/
	public void tick()
	{
		// Game logic goes here
	}
}

