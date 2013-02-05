package multithreading;

import java.util.ArrayList;
import java.util.List;

public class Multithread1 extends Thread {
	
	private int threadId;
	
	static List<Integer> listOut = new ArrayList<Integer>();
	static List<Integer> listIn = new ArrayList<Integer>();
	
	public Multithread1(int threadId) {
		this.threadId = threadId;
	}
		
	
	public void run() {        
        int firstInt = listIn.get(threadId);
        int secondInt = listIn.get(threadId+1);
        System.out.println("(id="+threadId+") firstInt=" + firstInt + " - secondInt="+ secondInt);
        listOut.add(firstInt*10);
	}

    public static void main(String args[]) throws InterruptedException {   	
    	for (int i=0; i<10; i++)
    		listIn.add(i);   
    	
    	List<Thread> threads = new ArrayList<Thread>();
    	for (int i=0; i<10; i =i+2) {
    		Thread t = new Multithread1(i);
    		t.start();
    		threads.add(t);
    	}
    	
    	for (Thread t : threads)
    		t.join();
    	
    	
    	for (int i=0; i<10; i++)
    		System.out.println("(" + i + ")" + listOut.get(i));
    }

}
