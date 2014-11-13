import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

/** manages the rendering of a grid of tiles
*/
public class TilesRenderer extends JPanel
{
	/**
	 * renderers need access to the global game state to update from the logical representation
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
	/** a mapping of logical tiles to images
	 * used to dynamically choose appropriate images for logical tiles
	 */
	// TODO: give these sensible names
	private HashMap<Tile, Image> inactiveImages;
	private HashMap<Tile, Image> activeImages;
	private HashMap<Candle.Type, Image> unlitImages;
	private HashMap<Candle.Type, Image> litImages;
	

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
		unlitImages = new HashMap<Candle.Type, Image>();
		litImages = new HashMap<Candle.Type, Image>();

		// right angle pipe
		loadTileRotations(new Image("res/tilesets/neater/right-angle-inactive.jpg"), new Tile(false, true, true, false), inactiveImages);

		// straight pipe
		loadTileRotations(new Image("res/tilesets/neater/straight-inactive.jpg"), new Tile(true, false, true, false), inactiveImages);

		// cross pipe
		loadTileRotations(new Image("res/tilesets/neater/cross-inactive.jpg"), new Tile(true, true, true, true), inactiveImages);

		// T pipe
		loadTileRotations(new Image("res/tilesets/neater/T-inactive.jpg"), new Tile(false, true, true, true), inactiveImages);
		
		// right angle pipe
		loadTileRotations(new Image("res/tilesets/neater/right-angle-active.jpg"), new Tile(false, true, true, false), activeImages);
		
		// straight pipe
		loadTileRotations(new Image("res/tilesets/neater/straight-active.jpg"), new Tile(true, false, true, false), activeImages);
		
		// cross pipe
		loadTileRotations(new Image("res/tilesets/neater/cross-active.jpg"), new Tile(true, true, true, true), activeImages);
		
		// T pipe
		loadTileRotations(new Image("res/tilesets/neater/T-active.jpg"), new Tile(false, true, true, true), activeImages);
		
		//unlit candles
		Image currentImage = new Image("res/normal-candle-unlit.jpg");
		unlitImages.put(Candle.Type.NORMAL, currentImage);
		currentImage = new Image("res/trick-candle-unlit.jpg");
		unlitImages.put(Candle.Type.TRICK, currentImage);
		currentImage = new Image("res/TNT-candle-unlit.jpg");
		unlitImages.put(Candle.Type.TRICK, currentImage);
		currentImage = new Image("res/kindle-candle-unlit.jpg");
		unlitImages.put(Candle.Type.KINDLE, currentImage);
		
		//lit candles
		currentImage = new Image("res/normal-candle-lit.jpg");
		litImages.put(Candle.Type.NORMAL, currentImage);
		currentImage = new Image("res/trick-candle-lit.jpg");
		litImages.put(Candle.Type.TRICK, currentImage);
		currentImage = new Image("res/TNT-candle-lit.jpg");
		litImages.put(Candle.Type.TNT, currentImage);
		currentImage = new Image("res/kindle-candle-lit.jpg");
		litImages.put(Candle.Type.KINDLE, currentImage);
		
		//Empty
		currentImage = new Image("res/empty.jpg");
		unlitImages.put(Candle.Type.EMPTY, currentImage);
		litImages.put(Candle.Type.EMPTY, currentImage);
		
		setBackground(Color.WHITE);
		setDoubleBuffered(true);
	}

	/** load multiple rotations of a tile at once
	 * @param i the base image
	 * @param t the base tile
	 * @param map the map to add the loaded tile to
	 */
	public void loadTileRotations(Image i, Tile t, HashMap<Tile, Image> map)
	{
		// not the most optimal of solutions, but it will greatly simplify tile loading
		HashSet<Tile> addedTiles = new HashSet<Tile>();
		map.put(t, i);

		for(int angle = 0; angle < 360; angle += 90)
		{
			t = new Tile(t);
			t.rotateCW();

			i = i.getRotatedCopy();

			if(addedTiles.contains(t)) { continue; }

			map.put(t, i);
			addedTiles.add(t);
		}
	}

	/** set the rendering position of the tiles grid
	 * @param setTop the new top of the tiles grid (pixels)
	 * @param setLeft the new left of the tiles grid (pixels)
	 */
	public void setPos(int setTop, int setLeft)
	{
		top = setTop;
		left = setLeft;
	}

	/** resizes the panel based on the size the tiles take up
	 * call after changing the size of the logical tiles grid
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
		// to make it clear what game.tiles is
		LogicalTiles logicalTiles = game.tiles;
		
		// may be the case before a level is loaded
		if(logicalTiles == null) { return; }
		
		Graphics2D g2 = (Graphics2D)g;
		
		int cols = logicalTiles.tiles[0].length;
		for(int row = 0; row < logicalTiles.tiles.length; row++)
		{
			for(int col = 0; col < cols; col++)
			{
				drawTile(row, col, g2);
			}
		}


		for (int col = 0; col < cols; col++)
		{
			drawCandle(col, g2);
		}
	}	
	
	private void clearScreen(Graphics g)
	{
		// note this is not the drawable size, this is the size of the entire window
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
	
	private void drawCandle(int col, Graphics2D g2)
	{
		Candle c = game.candles.candles[col];
		
		Image i = null;
		if(c.lit)
		{
			i = litImages.get(c.type);
		}
		else
		{
			i = unlitImages.get(c.type);
		}
		
		int x = left + col * tileSize;
		int y = top + game.tiles.getRows() * tileSize;

		g2.drawImage(i.getImage(), x, y, null);
	}
}
