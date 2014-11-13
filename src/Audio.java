import java.util.HashMap;
import java.io.File;
import javax.sound.sampled.*;

public class Audio
{
	private HashMap<String, Clip> clips = new HashMap<String, Clip>();
	
	public Audio()
	{
		
	}
	
	public void register(String name, String filename)
	{
		try
		{
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			//clip.addLineListener(() -> {
				// close the clip after it stops playing?
			//});
			clips.put(name, clip);
		}
		catch (Exception e)
		{
			System.out.println("Error loading audio clip: "+filename);
			e.printStackTrace();
		}
	}
	
	public void play(String name)
	{
		Clip clip = clips.get(name);
		clip.start();
	}
} 

