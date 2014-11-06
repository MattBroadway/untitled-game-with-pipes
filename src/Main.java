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
				System.out.println("Creating window");
				MainWindow w = new MainWindow("Pipes Game");

				// repaints the window based on a timer
				MainWindow.Scheduler sc = new MainWindow.Scheduler(30/*FPS*/, w);
				sc.start();
			}
		});

	}

}









