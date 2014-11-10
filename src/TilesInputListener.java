import java.awt.event.*;

/** class that listens for mouse events over the area of a tiles grid and reports which tiles are clicked
*/
public class TilesInputListener implements MouseListener
{
	/** needs access to the global state
	*/
	private Game game;

	/** initialise the listener for a game object
	*/
	public TilesInputListener(Game game)
	{
		this.game = game;
		game.w.tiles.addMouseListener(this);
	}

	
	/** This event is fired when the mouse is clicked. It reports the event to the game object to handle
	*/
	public void mouseClicked(MouseEvent e)
	{
		int row = yCoordToRow(e.getY());
		int col = xCoordToCol(e.getX());
		if(row != -1 && col != -1)
		{
			System.out.println("tile: [row:"+row+" col:"+col+"] clicked");
			game.handleTileClicked(row, col);
		}

	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	/** convert x coordinate (pixels) to a column in the logicalTiles object
	*/
	public int xCoordToCol(int x)
	{
		int left = game.w.tiles.left;
		int width = game.w.tiles.getTileSize();

		// I know integer division floors, I'm just making it explicit
		int naiveCol = (int)Math.floor((x-left)/(double)width);
		
		if(naiveCol < 0 || naiveCol > game.tiles.getCols())
		{
			return -1;
		}
		else
		{
			return naiveCol;
		}
	}
	/** convert y coordinate (pixels) to a row in the logicalTiles object
	*/
	public int yCoordToRow(int y)
	{
		int top = game.w.tiles.top;
		int height = game.w.tiles.getTileSize();

		// I know integer division floors, I'm just making it explicit
		int naiveRow = (int)Math.floor((y-top)/(double)height);
		
		if(naiveRow < 0 || naiveRow > game.tiles.getRows())
		{
			return -1;
		}
		else
		{
			return naiveRow;
		}
	}

}
