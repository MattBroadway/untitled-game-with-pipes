// maybe this is bad, change it later if so
import javax.swing.*;
import java.awt.*;


public class Main
{
	public static void main(String[] args)
	{
		// apparently running in another thread stops the GUI
		// from hanging sometimes
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				System.out.println("starting game...");
				Game game = new Game();

				game.start();
			}
		});

	}

}









