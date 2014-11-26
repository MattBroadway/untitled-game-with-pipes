import java.awt.event.*;

import javax.swing.SwingUtilities;

/**
 * class that listens for key events and mouse events over
 * the area of a tiles grid and adjusts the game state in response
 */
public class InputListener implements MouseListener, KeyListener
{
	
	private Game game;
	
	public InputListener(Game game)
	{
		this.game = game;
		game.w.r.addMouseListener(this);
		game.w.addKeyListener(this);
	}
	
	/**
	 * This event is fired when the mouse is clicked. It reports
	 * the event to the game object to handle
	 */
	public void mouseClicked(MouseEvent e)
	{
		boolean leftClick = SwingUtilities.isLeftMouseButton(e);
		int x = screenXToTileX(e.getX());
		int y = screenYToTileY(e.getY());
		if(x != -1 && y != -1)
		{
			System.out.println("tile: [x:"+x+" y:"+y+"] clicked");
			game.setCursor(x, y);
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
		System.out.println("key pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
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
	 * 
	 */
	public int screenXToTileX(int x)
	{
		Renderer.Layout.Geom tilesGeom = game.w.r.l.getTileGridGeom();
		int tilesXRes = game.lvl.getXRes();

		// floored
		int naiveCol = (int)(((x-tilesGeom.x) / (double)tilesGeom.width)*tilesXRes);
		
		if(naiveCol < 0 || naiveCol > tilesXRes)
		{
			return -1;
		}
		else
		{
			return naiveCol;
		}
	}
	/**
	 * 
	 */
	public int screenYToTileY(int y)
	{
		Renderer.Layout.Geom tilesGeom = game.w.r.l.getTileGridGeom();
		int tilesYRes = game.lvl.getYRes();

		// floored
		int naiveRow = (int)(((y-tilesGeom.y) / (double)tilesGeom.height)*tilesYRes);
		
		if(naiveRow < 0 || naiveRow > tilesYRes)
		{
			return -1;
		}
		else
		{
			return naiveRow;
		}
	}

}
