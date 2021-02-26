package ServerThings;

public class Barrier {

	private int taskSentCounter;
	private int taskCompleteCounter;

	public Barrier(int taskSentCounter) {
		this.taskSentCounter = taskSentCounter;
	}

	public synchronized void waitResults() {
		while (taskSentCounter != taskCompleteCounter) {
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void taskCompleted() {
		taskCompleteCounter++;
		notify();
	}
}
