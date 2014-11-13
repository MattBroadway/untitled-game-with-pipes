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
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(filename)));
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
		// should this just let it throw an exception? otherwise no-one will know if they are
		// getting the name of a clip wrong
		if (clip == null) return;
		clip.start();
	}
} 

