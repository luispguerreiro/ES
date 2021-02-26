package shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Join implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<byte[]> images;
	private byte[] logo;
	private ArrayList<String> procura;
	private ArrayList<String> arrNames;

	public Join(ArrayList<byte[]> images, byte[] logo, ArrayList<String> procura, ArrayList<String> arrNames) {
		this.images = images;
		this.logo = logo;
		this.procura = procura;
		this.arrNames = arrNames;
	}

	public ArrayList<byte[]> getImages() {
		return images;
	}

	public byte[] getLogo() {
		return logo;
	}

	public ArrayList<String> getProcura() {
		return procura;
	}

	public ArrayList<String> getArrNames() {
		return arrNames;
	}
}
