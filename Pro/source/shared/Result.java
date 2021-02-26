package shared;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {

	private static final long serialVersionUID = -2571354142115605713L;

	private String imagePath;
	private List<Rectangle> matches;

	public Result(String imagePath, List<Rectangle> matches) {
		this.imagePath = imagePath;
		this.matches = matches;
	}

	public String getImagePath() {

		return imagePath;
	}

	public List<Rectangle> getMatches() {
		return matches;
	}
}
