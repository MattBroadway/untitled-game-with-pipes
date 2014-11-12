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
	private HashMap<Tile, Image> inactiveImages;
	private HashMap<Tile, Image> activeImages;
	

	/** initialise a TilesRenderer for a game object
	 * @param game the game object to create the renderer for
	 * @param setTileSize the size (width and height) of a single tile in pixels
	 */
	public TilesRenderer(Game game, int setTileSize)
	{
		super(true/*isDoubleBuffered*/);

		this.game = game;
		tileSize = setTileSize;

		inactiveImages = new HashMap<Tile, Image>();
		activeImages = new HashMap<Tile, Image>();

		// right angle pipes
		Image currentImage = new Image("res/right-angle-inactive.jpg");
		inactiveImages.put(new Tile(false, true, true, false), currentImage);
		currentImage = currentImage.getRotatedCopy();
		inactiveImages.put(new Tile(false, false, true, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		inactiveImages.put(new Tile(true, false, false, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		inactiveImages.put(new Tile(true, true, false, false), currentImage);

		// straight pipes
		currentImage = new Image("res/straight-inactive.jpg");
		inactiveImages.put(new Tile(true, false, true, false), currentImage);
		currentImage = currentImage.getRotatedCopy();
		inactiveImages.put(new Tile(false, true, false, true), currentImage);

		// cross pipe
		currentImage = new Image("res/cross-inactive.jpg");
		inactiveImages.put(new Tile(true, true, true, true), currentImage);

		// T pipe
		currentImage = new Image("res/T-inactive.jpg");
		inactiveImages.put(new Tile(false, true, true, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		inactiveImages.put(new Tile(true, false, true, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		inactiveImages.put(new Tile(true, true, false, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		inactiveImages.put(new Tile(true, true, true, false), currentImage);





		// right angle pipes
		currentImage = new Image("res/right-angle-active.jpg");
		activeImages.put(new Tile(false, true, true, false), currentImage);
		currentImage = currentImage.getRotatedCopy();
		activeImages.put(new Tile(false, false, true, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		activeImages.put(new Tile(true, false, false, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		activeImages.put(new Tile(true, true, false, false), currentImage);

		// straight pipes
		currentImage = new Image("res/straight-active.jpg");
		activeImages.put(new Tile(true, false, true, false), currentImage);
		currentImage = currentImage.getRotatedCopy();
		activeImages.put(new Tile(false, true, false, true), currentImage);

		// cross pipe
		currentImage = new Image("res/cross-active.jpg");
		activeImages.put(new Tile(true, true, true, true), currentImage);

		// T pipe
		currentImage = new Image("res/T-active.jpg");
		activeImages.put(new Tile(false, true, true, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		activeImages.put(new Tile(true, false, true, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		activeImages.put(new Tile(true, true, false, true), currentImage);
		currentImage = currentImage.getRotatedCopy();
		activeImages.put(new Tile(true, true, true, false), currentImage);



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
		clearScreen(g);
		drawTiles(g);
		drawScore(g);
	}

	private void drawTiles(Graphics g)
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

	private void clearScreen(Graphics g)
	{	
		Dimension scrSize = game.w.getSize();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, (int)scrSize.getWidth(), (int)scrSize.getHeight());
	}

	private void drawScore(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawString("Time passed: " + game.getTimePassed(), 30, 30);
	}

	/** draw a single tile (the row and col refer to the tile's position in the logicalTiles object
	*/
	private void drawTile(int tileRow, int tileCol, Graphics2D g2)
	{
		Tile t = game.tiles.tiles[tileRow][tileCol];
		boolean active = game.tiles.activeTiles.contains(new LogicalTiles.TilePos(tileRow, tileCol));

		Image i = null;
		if(active)
		{
			i = activeImages.get(t);
		}
		else
		{
			i = inactiveImages.get(t);
		}
		
		int x = left + tileCol * tileSize;
		int y = top + tileRow * tileSize;

		g2.drawImage(i.getImage(), x, y, null);
	}
}
