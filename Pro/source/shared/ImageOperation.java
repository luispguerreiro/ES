package shared;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class ImageOperation implements Serializable {

	private static final long serialVersionUID = 1L;

	static private byte[] img;

	public static byte[] imageToByteArray(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ImageIO.write(image, "png", baos);

		baos.flush();

		img = baos.toByteArray();

		baos.close();
		return img;
	}

	public static BufferedImage byteArrayToImage(byte[] img) throws IOException {

		InputStream in = new ByteArrayInputStream(img);

		BufferedImage bImageFromConvert = ImageIO.read(in);

		return bImageFromConvert;
	}

	public static void main(String[] args) throws IOException {
		BufferedImage originalImage = ImageIO.read(new File("Superman.png"));

		byte[] baos = new byte[30000];
		baos = imageToByteArray(originalImage);
		System.out.println(baos.toString());

	}

	public static BufferedImage rotate90(BufferedImage src) {
		int width = src.getWidth();
		int height = src.getHeight();

		BufferedImage dest = new BufferedImage(height, width, src.getType());

		Graphics2D graphics2D = dest.createGraphics();
		graphics2D.translate((height - width) / 2, (height - width) / 2);
		graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
		graphics2D.drawRenderedImage(src, null);

		return dest;
	}

	public static BufferedImage rotate180(BufferedImage src) {
		src = rotate90(src);
		src = rotate90(src);
		return src;
	}

	public static BufferedImage rotate270(BufferedImage src) {
		src = rotate90(src);
		src = rotate90(src);
		src = rotate90(src);
		return src;
	}
}
