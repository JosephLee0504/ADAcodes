package Week1;

import java.util.ArrayList;

public class LamportBakeryAlgorithm extends Thread {

	// static vars
	private static int globalNumber = 1;
	static ArrayList<LamportBakeryAlgorithm> allThreads = new ArrayList<LamportBakeryAlgorithm>();

	// object vars
	private String name;
	private boolean choosing = false;
	private int number = 0;

	public LamportBakeryAlgorithm(String name) {
		this.name = name;
		System.out.println("New thread created with the name:" + name);
		LamportBakeryAlgorithm.allThreads.add(this);
	}

	private void setChoosing(boolean flag) {
		this.choosing = flag;
	}

	private boolean isChoosing() {
		return this.choosing;
	}

	private void setNewNumber() {
		this.number = LamportBakeryAlgorithm.globalNumber++;
	}

	private void resetNumber() {
		this.number = 0;
	}

	private int getNumber() {
		return this.number;
	}

	private static ArrayList<LamportBakeryAlgorithm> getAllThreads() {
		return LamportBakeryAlgorithm.allThreads;
	}

	public void run() {
		System.out.println("Thread " + name + " is running");
		try {
			Thread.sleep((long) (Math.random() * 1000));
			this.acquireLock();
			this.criticalSection();
			this.releaseLock();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void criticalSection() {
		for (int i = 0; i < 10; i++) {
			System.out.println("Thread " + name + " is outputting: " + Math.random());
			try {
				Thread.sleep((long) (Math.random() * 1000));
			} catch (Exception ex) {
				System.out.println("Thread: " + name + " was interrupted by exception:" + ex);
			}
		}
	}

	private void acquireLock() throws Exception {
		// choosing[t] <- true
		this.setChoosing(true);
		// assign a number to thread t one larger than currently assigned maximum
		this.setNewNumber();
		// choosing[t] <- false
		this.setChoosing(false);
		// for each thread s do
		for (LamportBakeryAlgorithm thread : LamportBakeryAlgorithm.getAllThreads()) {
			// while choosing[s] do 6 delay -> thread s has not chosen a number 7
			if (thread == this)
				continue;
			while (thread.isChoosing()) {
				Thread.sleep(1000);
			}
			// while (number [s] 6= 0 and number [s] < number [t]) 8 or (number [s]=number
			while (((thread.getNumber() != 0) && (thread.getNumber() < this.getNumber()))
					|| ((thread.getNumber() == this.getNumber()) && (thread.getName().compareTo(this.getName()) > 0))) {
				Thread.sleep(1000);
			}
		}

	}

	private void releaseLock() {
		this.resetNumber();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			(new LamportBakeryAlgorithm("Thread" + (i + 1))).start();
		}

	}
}