import javax.swing.*;
import java.awt.*;

/** manages the rendering of a grid of tiles
*/
public class TilesRenderer extends JPanel
{
	private LogicalTiles logicalTiles;
	private int tileSize;
	private Image[] tileImages;
	

	public TilesRenderer(LogicalTiles setLogicalTiles, int setTileSize)
	{
		super(true/*isDoubleBuffered*/);

		
		logicalTiles = setLogicalTiles;
		tileSize = setTileSize;

		int imageCount = 1/*all*/ + 2/*straight*/ + 4/*adjacent*/;
		tileImages = new Image[imageCount];
		tileImages[0] = new Image("res/adjacent-empty.jpg");

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
		g2.drawImage(tileImages[0].getImage(), 200, 200, null);
	}

	/** draw a single tile
	*/
	private void drawTile(int tileRow, int tileCol)
	{
		
	}
}
