package ServerThings;
import java.io.Serializable;
//class criada por o dealWithWorker nao "gostar" de receber uma socket juntamente com a task, nem necessita de a ter

public class SimpleTask implements Serializable {


	private static final long serialVersionUID = 1L;
	private byte[] image;
	private String imagePath;
	private byte[] logo;
	private String procura;

	public SimpleTask(String imagePath, byte[] image, byte[] logo, String procura) {
		this.imagePath = imagePath;
		this.image = image;
		this.logo = logo;
		this.procura = procura;
	}

	public String getImagePath() {
		return imagePath;
	}

	public byte[] getImage() {
		return image;
	}

	public byte[] getLogo() {
		return logo;
	}

	public String getProcura() {
		return procura;
	}
}
