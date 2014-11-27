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
		public double getScaleFactor(double original, double stretched)
		{
			return stretched / original;
		}
		public double getScaled(double original, double scaleFactor)
		{
			return original * scaleFactor;
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
			public void debugDraw(Graphics2D g2)
			{
				Rectangle2D.Double r = new Rectangle2D.Double();
				r.x = x;
				r.y = y;
				r.width = width;
				r.height = height;
				g2.setColor(Color.GREEN);
				g2.draw(r);
				g2.setColor(Color.ORANGE);
				g2.drawString("(" + x + "," + y + "," + width + "," + height + ")", x+10, y + g2.getFontMetrics().getHeight());
			}
		}

		double My;
		double Mx;

		double Pa;
		double Pp;
			 
		double Rx;
		double Ry;
			 
		double Ca;
		double Cg;
		
		double candleScale;
		double candleAspect;
			 
		double B;

		Renderer r;

		public Layout(Renderer setR)
		{
			r = setR;
			B = 10; // can be set to any amount

			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Dimension screenDimension = env.getMaximumWindowBounds().getSize();
			JFrame frame = new JFrame();
			Insets insets = frame.getInsets();

			 My = screenDimension.height-insets.top-insets.bottom;
			 Mx = screenDimension.width-insets.left-insets.right;

			 // don't want to use the whole space
			 My *= 0.90;
			 Mx *= 0.90;

			// will be invalid for a short time until UpdateScale is called
			 // but don't call it here
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
			int originalPersonHeight = i.getHeight();
			Pa = getAspect(i.getWidth(),i.getHeight());
			Pp = r.game.lvl.Pp; // needs to be scaled with the person

			Rx = r.game.lvl.getXRes();
			Ry = r.game.lvl.getYRes();

			i = r.game.lvl.cake.getImage();
			Ca = getAspect(i.getWidth(), i.getHeight());
			
			// assuming every candle has the same resolution
			// this keeps the scale of the candles locked to the scale of the cake
			// candle height = candleScale * tileSize
			double initialTileSize = (double)r.game.ss.tiles.get(new Tile(true, true, true, true)).getImage().getHeight();
			i = r.game.ss.unlitCandles.get(Candle.Type.NORMAL).getImage();
			candleScale = (double)i.getHeight() / initialTileSize;
			candleAspect = getAspect(i.getWidth(), i.getHeight());
			Cg = Math.round((initialTileSize * candleScale - r.game.lvl.candleGoodZoneTop) * 1.8); // chosen some sensible multiple



			double h = My;
			while(true)
			{
				double personScale = getScaleFactor(originalPersonHeight, h - 2*B);
				Pp = Math.round(getScaled(Pp, personScale));
				double dtileSize = Math.round((h - 2*B - Cg - Pp) / (Rx*Ca + Ry + 1));
				double dwidth = Math.round(2*B + (Rx + 1)*dtileSize + getWidth(Pa, h-2*B));

				if(dwidth > Mx)
				{
					h = Math.round(h - 1); // could make the step larger. Solutions would be non-optimal however
				}
				else
				{
					// keep current tile size
					r.tileSize = (int)Math.floor(dtileSize);
					r.width = (int)Math.floor(dwidth);
					r.height = (int)Math.floor((int)(2*B) + (int)getHeight(Ca, Rx*r.tileSize) + (int)Cg + (int)(Ry+1)*r.tileSize + (int)Pp);
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
		public double getPersonScaleFactor()
		{
			Geom g = getPersonGeom();
			return getScaleFactor((double)r.game.lvl.person.getImage().getHeight(), (double)g.height);
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
		public double getCakeScaleFactor()
		{
			Geom g = getCakeGeom();
			return getScaleFactor((double)r.game.lvl.cake.getImage().getHeight(), (double)g.height);
		}
		public int getCandleHeight()
		{
			return (int)(r.tileSize * candleScale);
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
		
		public Geom getTopRowGeom()
		{
			Geom g = new Geom();
			g.x = (int)B;
			g.y = (int)(B+Pp);
			g.width = (int)((Rx +1)* r.tileSize);
			g.height = r.tileSize;
			return g;
		}
		public Geom getBorderGeom()
		{
			Geom g = new Geom();
			g.x = (int)B;
			g.y = (int)B;
			g.width = r.width - (int)(2*B);
			g.height = r.height - (int)(2*B);
			return g;
		}
		
		public void debugDraw(Graphics2D g2)
		{
			getPersonGeom().debugDraw(g2);
			getCakeGeom().debugDraw(g2);
			getTileGridGeom().debugDraw(g2);
			getTopRowGeom().debugDraw(g2);
			getBorderGeom().debugDraw(g2);
		}

	}

	/**
	 * renderers need access to the global game state to update from the logical representation
	 */
	private Game game;
	
	// whether to draw extra debug information
	boolean debugDraw;

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
		
		// disable later
		debugDraw = true;

		setBackground(Color.WHITE);
		setDoubleBuffered(true); // kind of redundant but it makes me happy :)
	}


	

	
	
	/** draw the scene
	*/
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		long startTime = System.currentTimeMillis();
		clearFrame(g2);
		drawTiles(g2);
		drawTopRow(g2);
		drawCake(g2);
		drawPerson(g2);
		drawCandles(g2);
		drawCursor(g2);
		drawScore(g2);
		long duration = System.currentTimeMillis() - startTime;
		if(debugDraw)
		{
			g2.setColor(Color.GREEN);
			// excludes debug drawing time, otherwise what would be the point
			g2.drawString("frame draw time: " + duration + "ms", (int)l.B+10, (int)l.B + 2 * g2.getFontMetrics().getHeight());
			l.debugDraw(g2);
		}
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
	private void drawTopRow(Graphics2D g2)
	{
		Layout.Geom g = l.getTopRowGeom();
		for(int x = 0; x < game.lvl.topRow.length; x++)
		{
			Tile t = game.lvl.topRow[x];
			Image i = game.ss.getTileImage(t);
			// x in pixels
			int xpx = g.x + x * tileSize;
			
			g2.drawImage(i.getImage(), xpx, g.y, tileSize, tileSize, null);
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
		Image i = game.ss.getCandleImage(c);
		
		Layout.Geom g = l.getCakeGeom();
		double scaleFactor = l.getCakeScaleFactor();
		
		int h = (int)l.getCandleHeight();
		int w = (int)l.getWidth(l.candleAspect, h);
		
		int xpx = g.x + (int)(((c.blownFromX + 0.5) * tileSize) - w / 2.0);
		int ypx = g.y + (int)l.getScaled(c.cakeY, scaleFactor) - h;
		

		g2.drawImage(i.getImage(), xpx, ypx, w, h, null);
	}
}
