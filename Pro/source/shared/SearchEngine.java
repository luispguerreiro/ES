package shared;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class SearchEngine implements Serializable {

	private static final long serialVersionUID = 1461957255796589339L;

	public static ArrayList<Rectangle> match(BufferedImage big, BufferedImage logo) {
		ArrayList<Rectangle> matches = new ArrayList<>();
		for (int i = 0; i <= big.getWidth() - logo.getWidth(); i++) {
			here: for (int j = 0; j <= big.getHeight() - logo.getHeight(); j++) {
				for (int m = 0; m < logo.getWidth(); m++) {
					for (int n = 0; n < logo.getHeight(); n++) {
						if (big.getRGB(i + m, j + n) != logo.getRGB(m, n)) {
							continue here;
						}
					}
				}
				matches.add(new Rectangle(i, j, logo.getWidth(), logo.getHeight()));
			}
		}
		return matches;
	}

//	public static void paintSquare(BufferedImage imagem, BufferedImage logo) {
//
//		ArrayList<Rectangle> matches = SearchEngine.match(imagem, logo);
//		for (int i = 0; i < matches.size(); i++) {
//			if (matches.size() == 0)
//				System.out.println("No Matches");
//			else
//				System.out.println(matches.get(i).x + "," + matches.get(i).y);
//
//			Graphics2D g2d = imagem.createGraphics();
//			g2d.setColor(Color.RED);
//			g2d.drawRect(matches.get(i).x, matches.get(i).y, matches.get(i).width, matches.get(i).height);
//			g2d.dispose();
//		}
//
//	}

}
