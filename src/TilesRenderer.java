import javax.swing.*;
import java.awt.*;

/** manages the rendering of a grid of tiles
*/
public class TilesRenderer extends JPanel
{
	private LogicalTiles logicalTiles;
	private int tileSize;
	

	public TilesRenderer(LogicalTiles setLogicalTiles, int setTileSize)
	{
		super(true/*isDoubleBuffered*/);

		
		logicalTiles = setLogicalTiles;
		tileSize = setTileSize;

		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(640, 480));
		setDoubleBuffered(true);
	}

	/** draw the tiles
	*/
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		Rectangle box = new Rectangle(100, 100, 200, 100);
		
		g2.setColor(Color.RED);
		g2.fill(box);
	}

	/** draw a single tile
	*/
	private void drawTile(int tileRow, int tileCol)
	{
		
	}
}
