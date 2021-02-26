package ClientThings;

import java.io.IOException;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import shared.Result;


//Espero que resulte

public class Client implements Runnable {
	private Socket infoServerSocket;

	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	private String host;
	private int port;

	private Thread thread;

	private LogoFinderWindow window;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;

		thread = new Thread(this);
	}

	private void init() {
		try {
			infoServerSocket = new Socket(host, port);
			objectOutputStream = new ObjectOutputStream(infoServerSocket.getOutputStream());
			objectInputStream = new ObjectInputStream(infoServerSocket.getInputStream());

			send("Client");

			thread.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setWindow(LogoFinderWindow window) {
		this.window = window;
	}

	public void send(Object message) {
		try {
			System.out.println("Client " + Thread.currentThread().getName() + ": send -> " + message);
			objectOutputStream.writeObject(message);
			objectOutputStream.flush();
			objectOutputStream.reset();
			
			
			//Testes

		} catch (IOException e) {
			e.printStackTrace();
		}
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

	@Override
	public void run() {
		while (true) {
			List<String> arrWorkers = (List<String>) receive();

			window.setWorkers(arrWorkers);
			
			List<Result> arrResults = (List<Result>) receive();
			
			System.out.println("C: Recebi" + arrResults);

			window.setResults(arrResults);
		}
	}

	public void close() {
		try {
			infoServerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		Client client = new Client("127.0.0.1", 8080);
		LogoFinderWindow window = new LogoFinderWindow("LogoFinder App");
		client.setWindow(window);
		window.setClient(client);
		window.open(1200, 650);
		client.init();
	}

}
