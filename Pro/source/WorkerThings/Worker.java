package WorkerThings;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import ServerThings.SimpleTask;
import shared.ImageOperation;
import shared.Result;
import shared.SearchEngine;

public class Worker extends Thread {

	private Socket infoServerSocket;

	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	private String host;
	private int port;
	private String procura;

	private Thread thread;

	public Worker(String host, int port, String procura) {
		this.host = host;
		this.port = port;
		this.procura = procura;

		thread = new Thread(this);
	}

	private void init() {
		boolean fail = false;
		do {
			try {
				fail = false;
				infoServerSocket = new Socket(host, port);
				objectOutputStream = new ObjectOutputStream(infoServerSocket.getOutputStream());
				objectInputStream = new ObjectInputStream(infoServerSocket.getInputStream());

				send("Worker");
				
				send(procura);
				send(procura);
				
				thread.start();

			} catch (IOException e) {
				fail = true;
				System.out.println("Erro: Ligação ao servidor recusada");
				try {
					sleep(1000);
				} catch (InterruptedException f) {
					f.printStackTrace();
				}
			}
		} while (fail);

	}

	public Object receive() {
		try {
			return objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void send(Object object) {
		try {
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			objectOutputStream.reset();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {

			SimpleTask simpleTask = (SimpleTask) receive();

			try {
				List<Rectangle> matches = getMatches(simpleTask);

				System.out.println(simpleTask.getImagePath() + " Total matches: " + matches.size());

				send(new Result(simpleTask.getImagePath(), matches));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private List<Rectangle> getMatches(SimpleTask task) throws IOException {

		BufferedImage big = ImageOperation.byteArrayToImage(task.getImage());
		BufferedImage logo = ImageOperation.byteArrayToImage(task.getLogo());

		if(procura.equals("90")) {
			logo = ImageOperation.rotate90(logo);
			
		} else if(procura.equals("180")) {
			logo = ImageOperation.rotate180(logo);
		}  
		if(procura.equals("270")){
			logo = ImageOperation.rotate270(logo);
		}

		List<Rectangle> matches = SearchEngine.match(big, logo);
		
		return matches;
	}

	public static void main(String[] args) {
		Worker worker = new Worker(args[0], Integer.parseInt(args[1]), args[2]);
		worker.init();
	}
}
