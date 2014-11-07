import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/** manages the rendering of a grid of tiles
*/
public class TilesRenderer extends JPanel
{
	private Game game;
	private int tileSize;
	public int top;
	public int left;
	private HashMap<Tile, Image> tileImages;
	

	public TilesRenderer(Game game, int setTileSize)
	{
		super(true/*isDoubleBuffered*/);

		this.game = game;
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
		setDoubleBuffered(true);
	}

	public void setPos(int setTop, int setLeft)
	{
		top = setTop;
		left = setLeft;
	}

	/** call after changing the size of the logical tiles grid
	*/
	public void refreshGeometry()
	{
		setPreferredSize(new Dimension(
			game.tiles.getRows()*tileSize,
			game.tiles.getCols()*tileSize));
	}

	public int getTileSize() { return tileSize; }

	/** draw the tiles
	*/
	public void paintComponent(Graphics g)
	{
		LogicalTiles logicalTiles = game.tiles;

		// may be the case before a level is loaded
		if(logicalTiles == null) { return; }

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
		Tile t = game.tiles.tiles[tileRow][tileCol];
		Image i = tileImages.get(t);
		
		int x = left + tileCol * tileSize;
		int y = top + tileRow * tileSize;

		g2.drawImage(i.getImage(), x, y, null);
	}
}
