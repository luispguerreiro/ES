package ServerThings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import shared.Join;
import shared.Result;

class DealWithClient extends Thread {

	public static final String TYPE = "Client";

	private Server server;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;

	private Barrier barrier;

	private List<Result> arrResults;

	public DealWithClient(Server server, Socket socket) {
		this.server = server;
	}

	public void connectToClient(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
		this.objectInputStream = objectInputStream;
		this.objectOutputStream = objectOutputStream;
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

			List<String> procuras = server.getProcuras();
			send(procuras);

			Join join = (Join) receive();

			barrier = new Barrier(join.getImages().size() * join.getProcura().size());
			arrResults = new ArrayList<>();

			byte[] logo = join.getLogo();

			for (int i = 0; i < join.getImages().size(); i++) {
				byte[] image = join.getImages().get(i);
				String path = join.getArrNames().get(i);

				for (String tipoProcura : join.getProcura()) {
					Task task = new Task(this, path, image, logo, tipoProcura);
					BlockingQueue<Task> blockingQueue = server.getBlockingQueue(tipoProcura);

					if (blockingQueue != null) {
						blockingQueue.offer(task);
					}
				}
			}
			barrier.waitResults();

			System.out.println("DWC: A enviar resultados " + arrResults);
			
			send(arrResults);
		}
	}

	public synchronized void addTaskResult(Result result) {
		arrResults.add(result);
		barrier.taskCompleted();
	}
}
