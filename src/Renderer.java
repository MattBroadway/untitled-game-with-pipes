import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;


/** manages the rendering of a grid of tiles
*/
public class Renderer extends JPanel
{
	public class Layout
	{
		public double getWidth(double aspect, double height)
		{
			return height / aspect;
		}
		public double getHeight(double aspect, double width)
		{
			return width * aspect;
		}
		public double getAspect(double width, double height)
		{
			return height / width;
		}

		public class Geom
		{
			public int x;
			public int y;
			public int width;
			public int height;
			public Geom() { x = 0; y = 0; width = 0; height = 0;}
			public Geom(int setX, int setY, int setWidth, int setHeight)
			{ x = setX; y = setY; width = setWidth; height = setHeight; }
		}

		double My;
		double Mx;

		double Pa;
		double Pp;
			 
		double Rx;
		double Ry;
			 
		double Ca;
		double Cg;
			 
		double B;

		Renderer r;

		public Layout(Renderer setR)
		{
			r = setR;
			B = 20;

			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Dimension screenDimension = env.getMaximumWindowBounds().getSize();
			JFrame frame = new JFrame();
			Insets insets = frame.getInsets();

			 My = screenDimension.height-insets.top-insets.bottom;
			 Mx = screenDimension.width-insets.left-insets.right;

			 // don't want to use the whole space
			 My *= 0.95;
			 Mx *= 0.95;

			// will be invalid for a short time
		}





		/** resizes the panel based on the size the tiles take up
		 * call after changing the size of the logical tiles grid
		 */
		public void updateScale()
		{
			/*
				key (needs a better explanation):
					P = person
					C = cake
					M = maximum
					R = resolution (units: tiles)
					B = border

					a = aspect ratio
					p = pixels (meaning various
					g = gap (fixed?)
			*/
			BufferedImage i = r.game.lvl.person.getImage();
			Pa = getAspect(i.getTileWidth(),i.getTileHeight());
			Pp = 90; // for now

			Rx = r.game.lvl.getXRes();
			Ry = r.game.lvl.getYRes();

			i = r.game.lvl.cake.getImage();
			Ca = getAspect(i.getTileWidth(),i.getTileHeight()); // for now
			Cg = 40; // for now. This entire variable will go



			double h = My;
			while(true)
			{
				double dtileSize = (h - 2*B - Cg - Pp) / (Rx*Ca + Ry + 1);
				double dwidth = 2*B + (Rx + 1)*dtileSize + (h / Pa);

				if(dwidth > Mx)
				{
					h -= 1; // could make the step larger. Solutions would be non-optimal however
				}
				else
				{
					// keep current tile size
					r.tileSize = (int)Math.floor(dtileSize);
					r.width = (int)Math.floor(dwidth);
					r.height = (int)Math.floor(2*B + Rx*dtileSize*Ca + Cg + (Ry+1)*dtileSize + Pp);
					break;
				}
			}

		}

		public Geom getPersonGeom()
		{
			Geom g = new Geom();
			g.y = (int)B;
			g.height = r.height - (int)(2*B);
			g.width = (int)getWidth(Pa, g.height);
			g.x = r.width - (int)B - g.width;
			return g;
		}

		public Geom getCakeGeom()
		{
			Geom g = new Geom();
			g.x = (int)B;
			g.y = (int)Math.floor(B + Pp + (Ry + 1)*r.tileSize + Cg);
			Geom Pg = getPersonGeom();
			g.width = r.width - (int)(2*B) - r.tileSize - Pg.width;
			g.height = r.height - (int)B - g.y;
			return g;
		}

		public Geom getTileGridGeom()
		{
			Geom g = new Geom();
			g.x = (int)B;
			g.y = (int)(B + Pp + r.tileSize);
			g.width = (int)(Rx * r.tileSize);
			g.height = (int)(Ry * r.tileSize);
			return g;
		}


	}

	/**
	 * renderers need access to the global game state to update from the logical representation
	 */
	private Game game;

	Layout l;

	int tileSize;
	int width;
	int height;
	

	/** initialise a TilesRenderer for a game object
	 * @param game the game object to create the renderer for
	 */
	public Renderer(Game game)
	{
		super(true/*isDoubleBuffered*/);

		this.game = game;
		l = new Layout(this);

		setBackground(Color.WHITE);
		setDoubleBuffered(true); // kind of redundant but it makes me happy :)
	}


	

	
	
	/** draw the scene
	*/
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		clearFrame(g2);
		drawTiles(g2);
		drawCake(g2);
		drawPerson(g2);
		drawCandles(g2);
		drawCursor(g2);
		drawScore(g2);
	}

	private void clearFrame(Graphics2D g2)
	{
		Rectangle2D.Double r = new Rectangle2D.Double(0,0,width,height);
		g2.setColor(Color.WHITE);
		g2.fill(r);
	}

	private void drawTiles(Graphics2D g2)
	{
		if(game.lvl == null) { return; } // this actually is the case before the level has loaded

		Layout.Geom tileGridGeom = l.getTileGridGeom();
		for(int x = 0; x < game.lvl.getXRes(); x++)
		{
			for(int y = 0; y < game.lvl.getYRes(); y++)
			{
				drawTile(x, y, tileGridGeom, g2);
			}
		}
	}
	public void drawCake(Graphics2D g2)
	{
		Layout.Geom g = l.getCakeGeom();
		g2.drawImage(game.lvl.cake.getImage(), g.x, g.y, g.width, g.height, null);
	}
	public void drawPerson(Graphics2D g2)
	{
		Layout.Geom g = l.getPersonGeom();
		g2.drawImage(game.lvl.person.getImage(), g.x, g.y, g.width, g.height, null);
	}
	public void drawCandles(Graphics2D g2)
	{
		for(int index = 0; index < game.lvl.candles.length; index++)
		{
			drawCandle(index, g2);
		}
	}
	private void drawScore(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawString("Time passed: " + game.getTimePassed(), 30, 30);
	}
	

	/** draw a single tile (the row and col refer to the tile's position in the logicalTiles object
	*/
	private void drawTile(int x, int y, Layout.Geom g, Graphics2D g2)
	{
		Tile t = game.lvl.getTileAt(x,y);
		
		Image i = game.ss.getTileImage(t);
		// x in pixels
		int xpx = g.x + x * tileSize;
		// y in pixels
		int ypx= g.y + y * tileSize;
		
		g2.drawImage(i.getImage(), xpx, ypx, tileSize, tileSize, null);
	}
	
	private void drawCursor(Graphics2D g2)
	{
		Layout.Geom g = l.getTileGridGeom();
		int x = g.x + game.cursorX * tileSize;
		int y = g.y + game.cursorY * tileSize;
		g2.drawImage(game.ss.cursor.getImage(), x, y, tileSize, tileSize, null);
	}
	
	private void drawCandle(int index, Graphics2D g2)
	{
		Candle c = game.lvl.candles[index];
		
		// might be an idea to cache this. It won't change often
		Image i = game.ss.getCandleImage(c);
		
		int xpx = /*left*/ + c.blownFromX * 64;
		int ypx = /*top*/ + game.lvl.getXRes() * 64;

		g2.drawImage(i.getImage(), xpx, ypx, null);
	}
}
