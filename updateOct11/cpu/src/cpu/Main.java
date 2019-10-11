package cpu;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class Main {
	

	double PID_COUNTER = 0; 
	double CPU_TIME = 0;
	double WAITCPU_TIME = 0;
	double TURNAROUND = 0;
	boolean ISFULL = false; //keeps track of if the PTable is full
	double TOTALPID = 0;
	double BURST = 0;
	double ARRIVAL = 0;
	double REMAINING = 0;
	int INDEX = 0;
	int CREATEP = 0;
	double A_COUNTER = 0;
	double TIME = 0;
	boolean isDone = false;
	int AVG_QUEUE = 0;
	ArrayList<DataCollected> FCFSData = new ArrayList<DataCollected>();
	ArrayList<DataCollected> SRTData = new ArrayList<DataCollected>();
	ArrayList<DataCollected> RR10Data = new ArrayList<DataCollected>();
	ArrayList<DataCollected> RR200Data = new ArrayList<DataCollected>();
	Random rand = new Random();
	
	public Main(String[] args) {
		//TODO Store values in another object.
		//to be read for excel with all values of each lambda
		/*
		for(int i = 1; i < 31; i++) {
			FCFS(new ArrayList<Process>(), i);
			FCFSData.add(new DataCollected(CPU_TIME, WAITCPU_TIME, TURNAROUND, TIME, AVG_QUEUE));
			System.out.println("For FCFS " + i + ","
					+ "\nTurnaround CPU_TIME is: " + TURNAROUND + ", "
					+ "\nWaiting CPU_TIME is: " + WAITCPU_TIME);
			System.out.println("The actual total CPU_TIME is: " + CPU_TIME + ", \nPID is: " + TOTALPID + "Average Q = " + AVG_QUEUE);
		}
		
		for(int i = 1; i < 31; i++) {
			SRT(new ArrayList<Process>(), i);
			SRTData.add(new DataCollected(CPU_TIME, WAITCPU_TIME, TURNAROUND, TIME, AVG_QUEUE));
			System.out.println("For STF "+i+", the total is: " + TIME + ", "
					+ "\nTurnaround CPU_TIME is: " + TURNAROUND + ", \nWaiting CPU_TIME is: " + WAITCPU_TIME);
			System.out.println("The actual total CPU_TIME is: " + CPU_TIME + ", and PID is: " + TOTALPID + "Average Q = " + AVG_QUEUE);
		}*/
		/*
		for(int i = 1; i < 31; i++) {
			RR(new ArrayList<Process>(), .01, i);
			RR10Data.add(new DataCollected(CPU_TIME, WAITCPU_TIME, TURNAROUND, TIME, AVG_QUEUE));
			System.out.println("For RR 10, #" + i + " the total is: " + CPU_TIME + ", "
					+ "Turnaround CPU_TIME is: " + TURNAROUND + ", Waiting CPU_TIME is: " + WAITCPU_TIME);
			System.out.println("The actual total CPU_TIME is: " + CPU_TIME + ", and PID is: " + TOTALPID + "Average Q = " + AVG_QUEUE);
			reset();
		}
		*/
		for(int i = 1; i < 31; i++) {
			RR(new ArrayList<Process>(), .2, i);
			RR200Data.add(new DataCollected(CPU_TIME, WAITCPU_TIME, TURNAROUND, TIME, AVG_QUEUE));
			System.out.println("For RR 200, #" + i + " the total is: " + CPU_TIME + ", "
					+ "Turnaround CPU_TIME is: " + TURNAROUND + ", Waiting CPU_TIME is: " + WAITCPU_TIME);
			System.out.println("The actual total CPU_TIME is: " + CPU_TIME + ", and PID is: " + TOTALPID + "Average Q = " + AVG_QUEUE);
		}
	}

	void createProcess(ArrayList<Process> PTable, int lambda) 
	{
		for(int i = 0; i < lambda; i++) {
			
//			//makes sure that the table doesn't exceed 10000
//			if (PID_COUNTER >= 10000){
//				ISFULL = true;
//				return;
//			}
			
			double arrival = TIME + (genexp(lambda));
			double PID = PID_COUNTER++;
			double random = new Random().nextDouble();
			double burst = .05 + (random * (.07 - .05));
			String state = "ready";
			double remaining = burst;
			
			TOTALPID++;
		
			PTable.add(new Process(PID, arrival, burst, state, remaining));
		}
	}
	
	double genexp(double lambda)
	{
		double u;
		double x;
		x = 0;
		while (x == 0)
			{
				u = Math.abs(rand.nextDouble());
				x = (-1/lambda)*Math.log(u);
			}
		return x;
	}
	
	int sortByArrival(ArrayList<Process> PTable) {
		int index = 0;
		for(int i = 0; i < PTable.size(); i++) {
			if(PTable.get(i).getArrival() < PTable.get(index).getArrival() && PTable.get(i).getState() == "ready")
				index = i;
		}
		
		return index;
	}

	//returns the index of the process with the least remaining CPU_TIME from a process table
	int getShortestP(ArrayList<Process> PTable) {
		int index = 0;
		for(int i = 0; i < PTable.size(); i++) {
			if(PTable.get(i).getRemaining() <= PTable.get(index).getRemaining() && PTable.get(i).getState() == "ready") {
				index = i;
			}
		}
		return index;
	}

	//function to reset global variables so that we can reuse them
	void reset() 
	{
		PID_COUNTER = 0; 
		CPU_TIME = 0;
		WAITCPU_TIME = 0;
		TURNAROUND = 0;
//		TOTAL = 0;
		TOTALPID = 0;
		ISFULL = false;
		BURST = 0;
		ARRIVAL = 0;
		REMAINING = 0;
		CREATEP = 0;
		INDEX = 0;
		A_COUNTER = 0;
		TIME = 0;
		AVG_QUEUE = 0;
	}

	//THIS IS WORKING
	void FCFS(ArrayList<Process> PTable, int lambda)
	{
		
		reset();
//		Timer t = new Timer(100, updateClockAction);
		createProcess(PTable, lambda);
		int terminated = 0;
		while (terminated < 10000) {
			int arrivalIndex = sortByArrival(PTable);
			//this is just for clarity in the code
			ARRIVAL = PTable.get(arrivalIndex).getArrival();
			BURST = PTable.get(arrivalIndex).getBurst();
			TIME+=BURST;
			
			CPU_TIME += BURST;
//			System.out.println(terminated+" ARRIVAL:" + ARRIVAL + " TIME: " + TIME+ "BURST: " + BURST);
//			System.out.println("WAITIME: " + WAITCPU_TIME);
			WAITCPU_TIME += TIME - ARRIVAL - BURST;
//			if(WAITCPU_TIME < 0) WAITCPU_TIME = 0;
			
			TURNAROUND += TIME - ARRIVAL + BURST;
//			System.out.println(BURST);
			PTable.remove(arrivalIndex);
			terminated++;
			if(terminated % 100 == 0)
			{
				AVG_QUEUE += PTable.size();
			}
//			if(!ISFULL) {
			createProcess(PTable, lambda);
			TIME++;
//			}
		}
		AVG_QUEUE /= 100;
	}
    
	void SRT(ArrayList<Process> PTable, int lambda) 
	{
		//reset the global variables
		reset();
		
		//fills the table with some elements so the table wont be empty to begin with
		//for(int i = 0; i < 10; i++) 
		//createProcess(PTable, 1);
		
		createProcess(PTable, lambda);
		
		int terminated = 0;
		
		//main loop, repeats until 10000 processes are done
		while(terminated < 10000)
		{
			
			//returns the element with the shortest remaining CPU_TIME
			INDEX = getShortestP(PTable);

			//System.out.println(INDEX);
			//System.out.println("Step 1");
			//this is just for clarity in the code
			ARRIVAL = PTable.get(INDEX).getArrival();
			REMAINING = PTable.get(INDEX).getRemaining();
			BURST = PTable.get(INDEX).getBurst();

			//System.out.println(REMAINING);
			
			//TODO FIX THE ALGORITHM FOR SRT
			
//			System.out.println("Step 2");
			if(REMAINING < 0.1) {
				//adds to CPU_TIME the amount of CPU_TIME the process was in the CPU
				CPU_TIME += BURST;
				
				WAITCPU_TIME += TIME - ARRIVAL - BURST;
				TURNAROUND += TIME - ARRIVAL + BURST;
				
				PTable.get(INDEX).setRemaining(0);
				terminated++;
				if(terminated % 100 == 0)
				{
					AVG_QUEUE += PTable.size();
				}
				//removes element index from the table
				PTable.remove(INDEX);
			}

			else PTable.get(INDEX).setRemaining(REMAINING-.1);
//			subtracts q from the remaining CPU_TIME of the element
//			System.out.println("Step 3");

//			if(ISFULL != true)
			createProcess(PTable, lambda);

			TIME++;
		}
		AVG_QUEUE /= 100;

		for(int i = 0; i < PTable.size(); i++){
			ARRIVAL = PTable.get(i).getArrival();
			REMAINING = PTable.get(i).getRemaining();
			BURST = PTable.get(i).getBurst();
			CPU_TIME += BURST - REMAINING;
			WAITCPU_TIME += TIME - ARRIVAL - (BURST - REMAINING);
			TURNAROUND += TIME - ARRIVAL + BURST - REMAINING;
		}
	}
	
	//GOOD AND DONE MY GUY
	void RR(ArrayList<Process> PTable, double q, int lambda) 
	{
		//resets global variables so we can run the functions one after the other
		reset();
		
		//lets us know if all elements of the array are done in RR
		//boolean isDone = false;
		int terminated = 0;
		
		//fills the table with some elements so the table wont be empty to begin with
//		for(int i = 0; i < 10; i++) 
//			createProcess(PTable, 10);
		
		createProcess(PTable, lambda);
		
		//loops over the array until there are no more elements that need to access the CPU
		while(terminated < 10000)//was !isDone
		{
			
			
			//this is just for clarity in the code
			ARRIVAL = PTable.get(INDEX).getArrival();
			REMAINING = PTable.get(INDEX).getRemaining();
			
			if(REMAINING <= q) 
			{
				System.out.println("ASdADs");
				CPU_TIME += REMAINING;
				WAITCPU_TIME += TIME - ARRIVAL - REMAINING;
				TURNAROUND += WAITCPU_TIME + BURST;
//				CREATEP += REMAINING;
				PTable.remove(INDEX);
				terminated++;

				//takes the average # of elements in the queue
				if(terminated % 100 == 0)
					AVG_QUEUE += PTable.size();
			}
			
			else {
				CPU_TIME += q;
//				TURNAROUND += q;
				
				//subtracts q from the remaining CPU_TIME
				PTable.get(INDEX).setRemaining(REMAINING - q);
//				CREATEP += q;
			}
			
			/*
			if(terminated == 10000)
				break;
			*/
			//creates a process every 6 seconds
//			while(CREATEP >= 6 && !ISFULL) {
				createProcess(PTable, lambda);
				TIME++;
//				CREATEP -= 6;
//			}
			
			//loop to make sure that the array is not done
//			for(int i = 1; i < PTable.size(); i++) {
//				if(PTable.get(i).getState() == "ready") {
//					isDone = false;
//					break;
//				}
//			}
				
			//incr index while making sure it loops back to the start
			INDEX = (INDEX + 1) % 10000;
		}
		AVG_QUEUE /= 100;
	}
}
