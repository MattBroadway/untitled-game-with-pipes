
/** manages the logical state of the game
*/

public class Game
{
	public InputListener input;
	public MainWindow w;
	public Level lvl;
	public SpriteSheet ss;

	// RF: for input manager
	public int cursorX=0, cursorY=0;
	private double startTime; // used to calculate time running

	/** the levels of the game
	 */
	public final String[] levelFiles = {
		"res/lvl/01.json",
		"res/lvl/02.json"
	};
	public int currentLevel;

	/** initialise the game
	 */
	public Game()
	{
		input = null;
		w = null;
		lvl = null;
		ss = null;
	}

	/** start the game (create a window and load the first level)
	 */
	public void start()
	{
		w = new MainWindow(this, "Pipes Game", 30/*FPS*/);
		input = new InputListener(this);
		
		loadLevel(1);
	}

	/** load a level from the levelFiles attribute (zero indexed)
	 */
	public void loadLevel(int level)
	{
		long loadStart = System.currentTimeMillis();

		lvl = new Level(this, levelFiles[level]);
		
		startTime = System.currentTimeMillis();	
		lvl.updateAfterMove();

		w.r.l.updateScale();
		w.resizeToRenderer();

		long loadTime = System.currentTimeMillis() - loadStart;
		System.out.println("level " + level + " (" + levelFiles[level] + ") loaded in " + loadTime + "ms");
		
		currentLevel = level;
	}

	/** update the state of the game (1 unit of time has passed)
	 */
	public void tick()
	{
		int bottomRow = lvl.getYRes()-1;
		for(int x = 0; x < lvl.getXRes(); x++)
		{
			Tile t = lvl.getTileAt(x, bottomRow);
			if(t.active && t.bottom)
			{
				lvl.candles[x].blow();
			}
		}
	}

	public void rotateCW()
	{
		lvl.getTileAt(cursorX, cursorY).rotateCW();
		lvl.updateAfterMove();
	}
	
	public void rotateACW()
	{
		lvl.getTileAt(cursorX, cursorY).rotateACW();
		lvl.updateAfterMove();
	}
	
	public void setCursor(int x, int y)
	{
		if(x != -1 && y != -1)
		{
			cursorX = x;
			cursorY = y;
		}
	}
	
	public void moveCursor(int dx, int dy)
	{
		cursorX += dx;
		cursorY += dy;

		if(cursorX >= lvl.getXRes()) cursorX = 0;
		else if(cursorX < 0) cursorX = lvl.getXRes()-1;

		if(cursorY >= lvl.getYRes()) cursorY = 0;
		else if(cursorY < 0) cursorY = lvl.getYRes()-1;
	}

	public double getTimePassed()
	{
		return (System.currentTimeMillis() - startTime)/1000;
	}
}
