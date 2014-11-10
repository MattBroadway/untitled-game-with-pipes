import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/** manages the rendering of a grid of tiles
*/
public class TilesRenderer extends JPanel
{
	/** renderers need access to the global game state to update from the logical representation
	*/
	private Game game;
	/** the size of the tile images
	 * TODO: (currently no checking is in place to enforce)
	*/
	private int tileSize;
	/** the top of the tile grid
	*/
	public int top;
	/** the left of the tile grid
	*/
	public int left;
	/** a mapping of logical tiles -&gt; images
	 * used to dynamically choose appropriate images for logical tiles
	*/
	private HashMap<Tile, Image> tileImages;
	

	/** initialise a TilesRenderer for a game object
	 * @param game the game object to create the renderer for
	 * @param setTileSize the size (width and height) of a single tile in pixels
	 */
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

	/** set the rendering position of the tiles grid
	 * @param setTop the new top of the tiles grid
	 * @param setLeft the new left of the tiles grid
	 */
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

	/** draw a single tile (the row and col refer to the tile's position in the logicalTiles object
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
