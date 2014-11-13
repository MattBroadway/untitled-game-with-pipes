import java.awt.event.*;
import javax.swing.SwingUtilities;

/**
 * class that listens for key events and mouse events over
 * the area of a tiles grid and adjusts the game state in response
 */
public class TilesInputListener implements MouseListener, KeyListener
{
	
	private Game game;
	
	public TilesInputListener(Game game)
	{
		this.game = game;
		game.w.tiles.addMouseListener(this);
		game.w.addKeyListener(this);
	}
	
	/**
	 * This event is fired when the mouse is clicked. It reports
	 * the event to the game object to handle
	 */
	public void mouseClicked(MouseEvent e)
	{
		boolean leftClick = SwingUtilities.isLeftMouseButton(e);
		int row = yCoordToRow(e.getY());
		int col = xCoordToCol(e.getX());
		if(row != -1 && col != -1)
		{
			System.out.println("tile: [row:"+row+" col:"+col+"] clicked");
			game.setCursor(row, col);
			if (leftClick) game.rotateACW();
			else game.rotateCW();
		}
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	
	public void keyPressed(KeyEvent e)
	{
		System.out.println("key pressed: " + e.getKeyText(e.getKeyCode()));
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_LEFT:  game.moveCursor(0, -1); break;
			case KeyEvent.VK_RIGHT: game.moveCursor(0, 1);  break;
			case KeyEvent.VK_UP:    game.moveCursor(-1, 0); break;
			case KeyEvent.VK_DOWN:  game.moveCursor(1, 0);  break;
			
			case KeyEvent.VK_A:
			case KeyEvent.VK_HOME:
				game.rotateACW();
				break;
				
			case KeyEvent.VK_S:
			case KeyEvent.VK_END:
				game.rotateCW();
				break;
				
			// case KeyEvent.VK_P: // TODO: pause
			// case KeyEvent.VK_M: // TODO: mute
		}
	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	

	/**
	 * convert x coordinate (pixels) to a column in the logicalTiles object
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
	/**
	 * convert y coordinate (pixels) to a row in the logicalTiles object
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
