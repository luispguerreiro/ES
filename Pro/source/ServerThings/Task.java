package ServerThings;
import java.io.Serializable;

public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	private DealWithClient serverThread;

	private byte[] image;
	private String imagePath;
	private byte[] logo;
	private String procura;

	public Task(DealWithClient serverThread, String imagePath, byte[] image, byte[] logo, String procura) {
		this.serverThread = serverThread;
		this.imagePath = imagePath;
		this.image = image;
		this.logo = logo;
		this.procura = procura;
	}

	public String getImagePath() {
		return imagePath;
	}

	public DealWithClient getServerthread() {
		return serverThread;
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

	public SimpleTask getSimpleTask() {
		return new SimpleTask(imagePath, image, logo, procura);
	}

}
