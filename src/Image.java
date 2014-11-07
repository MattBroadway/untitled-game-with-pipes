import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Image
{
	private BufferedImage data;

	public Image(String filename)
	{
		try
		{
			data = ImageIO.read(new File(filename));
		}
		catch(java.io.IOException e)
		{
			System.out.println("Failed to load image: " + filename);
		}
	}
	public Image(BufferedImage i)
	{
		data = i;
	}
	public BufferedImage getImage() { return data; }
	/** get a copy of the image rotated 90 degrees clockwise
	*/
	public Image getRotatedCopy() 
	{
		AffineTransform trans = new AffineTransform();
		trans.rotate(Math.PI/2, data.getWidth() / 2, data.getHeight() / 2);

		AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_BILINEAR);
		return new Image(op.filter(data, null));
	}
}
