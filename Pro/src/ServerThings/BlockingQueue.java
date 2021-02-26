package ServerThings;
import java.util.LinkedList;

public class BlockingQueue<T> {

	private LinkedList<T> lista = new LinkedList<T>();

	public BlockingQueue() {

	}

	public synchronized void offer(T e) {
		lista.add(e);
		notifyAll();
	} 

	public synchronized T poll() throws InterruptedException {
		while (size() == 0) {
			wait();
		}

		T e = lista.poll();
		return e;
	}

	public synchronized int size() {
		return lista.size();
	}

	public synchronized void clear() {
		lista.clear();
		notifyAll();
	}

}
