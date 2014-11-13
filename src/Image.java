import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

/** This class holds a BufferedImage and adds functionality to load from a file and get a rotated copy
	(Note this name could conflict with java.awt.Image, so don't use that)
*/
public class Image
{
	/** The image data
	*/
	private BufferedImage data;
	
	/** Load from a file
	 * According to the javax.imageio documentation, these formats are supported for reading:
	 *	- JPEG
	 *	- PNG
	 *	- BMP
	 *	- WBMP
	 *	- GIF
	 *
	 * @param filename the file path to load from
	 */
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
	/** Load from another buffered image
	 * this is used to create the copy when rotating
	 * @param i the image to load from
	 */
	public Image(BufferedImage i)
	{
		data = i;
	}
	
	/** Get the BufferedImage from the Image
	 * @return the BufferedImage object
	 */
	public BufferedImage getImage() { return data; }
	/** get a copy of the image rotated 90 degrees clockwise
	 * @return a new Image object, rotated 90 degrees clockwise
	*/
	public Image getRotatedCopy() 
	{
		AffineTransform trans = new AffineTransform();
		trans.rotate(Math.PI/2, data.getWidth() / 2, data.getHeight() / 2);
		
		AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_BILINEAR);
		return new Image(op.filter(data, null));
	}
}
