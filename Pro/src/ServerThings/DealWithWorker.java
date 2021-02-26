package ServerThings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.Result;

public class DealWithWorker extends Thread {

	public static final String TYPE = "Worker";

	private Server server;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;

	private String procura;

	public DealWithWorker(Server server, Socket socket) {
		this.server = server;
	}

	public void connectToWorker(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
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

		
		procura = (String) receive();
		BlockingQueue<Task> blockingQueue = server.getBlockingQueue(procura);

		if (blockingQueue != null) {
			try {
				while (true) {
					Task task = blockingQueue.poll();

					SimpleTask simpleTask = task.getSimpleTask();
					send(simpleTask);

					Result result = (Result) receive();
					task.getServerthread().addTaskResult(result);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	
}
