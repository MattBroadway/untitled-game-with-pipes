import java.awt.Point;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.image.BufferStrategy;
import java.awt.Toolkit;
import java.awt.Color;

public class Main
{
	JFrame f;
	Point scrDim = new Point(600, 400);
	
	// To test graphics:
	double testX = 0, testY = 0;
	
	
	public static void main(String[] args)
	{
		new Main();
	}

	public Main()
	{
		// Initialize window
		f = new JFrame("Pipes yeah?");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(scrDim.x, scrDim.y);
		f.setVisible(true);
		f.createBufferStrategy(2);
		
		for(;;) // Loop forever
			{
			// Set up graphics
			BufferStrategy bf = f.getBufferStrategy();
			Graphics2D g = (Graphics2D)bf.getDrawGraphics();
	
			//Clear screen
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, scrDim.x, scrDim.y);
            g.setColor(Color.BLACK);

			logic();
			draw(g);

			// Show & dispose of graphics objects
			g.dispose();
			bf.show();
			Toolkit.getDefaultToolkit().sync();
		}
	}

	public void logic()
	{
		testX += 0.1;
		testY += 0.1;
	}

	public void draw(Graphics2D g)
	{
		g.drawRect((int)testX, (int)testY, 20, 20);
	}
}









