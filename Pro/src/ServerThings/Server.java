package ServerThings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private ServerSocket serverSocket;

	private List<String> procuras = new ArrayList<String>();;

	private ArrayList<DealWithClient> clientsList = new ArrayList<>();
	private ArrayList<DealWithWorker> workersList = new ArrayList<>();

	private BlockingQueue<Task> blockingQueue0;
	private BlockingQueue<Task> blockingQueue90;
	private BlockingQueue<Task> blockingQueue180;
	private BlockingQueue<Task> blockingQueue270;

	public Server(int port) {
		blockingQueue0 = new BlockingQueue<>();
		blockingQueue90 = new BlockingQueue<>();
		blockingQueue180 = new BlockingQueue<>();
		blockingQueue270 = new BlockingQueue<>();

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BlockingQueue<Task> getBlockingQueue(String procura) {
		if (procura.equals("Normal")) {
			return blockingQueue0;
		}

		if (procura.equals("90")) {
			return blockingQueue90;
		}

		if (procura.equals("180")) {
			return blockingQueue180;
		}
		if (procura.equals("270")) {
			return blockingQueue270;
		}

		return null;
	}

	private void init() throws IOException {
		System.out.println("lancou " + serverSocket + "\n Server operacional");

		while (true) {
			try {
				Socket unknown = serverSocket.accept();
				createServerThread(unknown);

			} catch (IOException e) {
				e.printStackTrace();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void createServerThread(Socket unknown) throws IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(unknown.getInputStream());
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(unknown.getOutputStream());
		String serverThreadType = (String) objectInputStream.readObject();

		if (serverThreadType.equals(DealWithClient.TYPE)) {
			System.out.println("New Client connected...");
			DealWithClient serverThreadClient = new DealWithClient(this, unknown);
			serverThreadClient.connectToClient(objectInputStream, objectOutputStream);
			serverThreadClient.start();
			clientsList.add(serverThreadClient);
		}
		if (serverThreadType.equals(DealWithWorker.TYPE)) {
			System.out.println("New Worker connected...");

			String procura = (String) objectInputStream.readObject();
			int count = 0;

			for (int t = 0; t < procuras.size(); t++) {
				if (procuras.get(t).equals(procura)) {
					count++;
				}
			}
			if (count == 0)
				procuras.add(procura);

			DealWithWorker serverThreadWorker = new DealWithWorker(this, unknown);
			serverThreadWorker.connectToWorker(objectInputStream, objectOutputStream);
			serverThreadWorker.start();
			workersList.add(serverThreadWorker);
		}
	}

	public List<String> getProcuras() {
		return procuras;
	}

	public ArrayList<DealWithClient> getClientsList() {
		return clientsList;
	}
	
	public static void main(String[] args) throws IOException {
		Server server = new Server(8080);
		server.init();
	}
}