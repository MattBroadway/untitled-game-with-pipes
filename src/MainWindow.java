import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

/** The main window of the game
*/
public class MainWindow extends JFrame
{
	/** manages the rendering of the tiles
	*/
	public Renderer r;
	/** manages the frame rate and general timing of the game
	*/
	private Scheduler sc;
	/** stores global state about the game
	*/
	private Game game;

	/** repaints the window based on a timer
	*/
	public static class Scheduler implements ActionListener
	{
		private Timer timer;
		private MainWindow window;

		/** initialise the scheduler
		@param setFPS the frames per second to set the scheduler to follow
		@param setWindow the window object to call every 1/FPS seconds
		*/
		public Scheduler(int setFPS, MainWindow setWindow)
		{
			window = setWindow;

			int delay = 1000 / setFPS; // in milliseconds
			timer = new Timer(delay, this);
			timer.setRepeats(true);
		}
		/** start the scheduler (main loop)
		*/
		public void start() { timer.start(); }
		/** see MainWindow.tick()
		*/
		public void actionPerformed(ActionEvent e)
		{
			window.tick();
		}
	}

	/** initialise the window
	@param setTitleString set the window's title
	@param Game the game object to render
	*/
	public MainWindow(Game game, String setTitleString, int FPS)
	{
		super();
		
		this.game = game;

		setTitle(setTitleString);
		//setSize(640, 480); // change this to make it dynamic
		setResizable(false);
		setLocationRelativeTo(null); // centers the window on the screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setupScene();

		// repaints the window based on a timer
		sc = new Scheduler(FPS, this);
		sc.start();

		setVisible(true);
	}

	/** Create, setup and add content to be rendered
	*/
	public void setupScene()
	{
		r = new Renderer(game);
		add(r);
	}

	
	/** Update the game state and re-draw
	*/
	public void tick()
	{
		game.tick(); // update game state
		r.repaint();
		Toolkit.getDefaultToolkit().sync();
	}

	/**
	 * make maximum use of available screen space to accommodate the renderer
	 */
	public void resizeToRenderer()
	{
		// need to add in insets
		setResizable(true);
		Insets insets = getInsets();
		setSize(r.width+insets.left+insets.right, r.height+insets.top+insets.bottom);
		setResizable(false);
		setLocationRelativeTo(null); // centers the window on the screen
	}
}

