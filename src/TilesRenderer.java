import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/** manages the rendering of a grid of tiles
*/
public class TilesRenderer extends JPanel
{
	private LogicalTiles logicalTiles;
	private int tileSize;
	private HashMap<Tile, Image> tileImages;
	

	public TilesRenderer(LogicalTiles setLogicalTiles, int setTileSize)
	{
		super(true/*isDoubleBuffered*/);

		
		logicalTiles = setLogicalTiles;
		tileSize = setTileSize;

		tileImages = new HashMap<Tile, Image>();

		// right angle pipes
		Image currentImage = new Image("res/right-angle-empty.jpg");
		tileImages.put(new Tile(false, true, true, false), currentImage);
		currentImage = currentImage.getRotatedCopy();
		tileImages.put(new Tile(false, false, true, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		tileImages.put(new Tile(true, false, false, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		tileImages.put(new Tile(true, true, false, false), currentImage);

		// straight pipes
		currentImage = new Image("res/straight-empty.jpg");
		tileImages.put(new Tile(true, false, true, false), currentImage);
		currentImage = currentImage.getRotatedCopy();
		tileImages.put(new Tile(false, true, false, true), currentImage);

		// cross pipe
		currentImage = new Image("res/cross-empty.jpg");
		tileImages.put(new Tile(true, true, true, true), currentImage);


		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(640, 480));
		setDoubleBuffered(true);
	}

	/** draw the tiles
	*/
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		for(int row = 0; row < logicalTiles.tiles.length; row++)
		{
			int cols = logicalTiles.tiles[0].length;
			for(int col = 0; col < cols; col++)
			{
				drawTile(row, col, g2);
			}
		}
		
	}

	/** draw a single tile
	*/
	private void drawTile(int tileRow, int tileCol, Graphics2D g2)
	{
		Tile t = logicalTiles.tiles[tileRow][tileCol];
		Image i = tileImages.get(t);
		
		int x = tileCol * tileSize;
		int y = tileRow * tileSize;

		g2.drawImage(i.getImage(), x, y, null);
	}
}
