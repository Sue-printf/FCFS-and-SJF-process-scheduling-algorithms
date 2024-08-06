//Description of the Program
/*
* This Java program has FCFS and SJF process scheduling algorithms.
* It reads the number of processes, their arrival times and burst times from a file.
* It analyzes the algorithm (FCFS or SJF) selected, determines the order of execution,
* waiting time, and turnaround time for each process, and finally presents these measures.
*
* Usage: java SchedulingAlgorithms <filename> <algorithm>
* Example: java SchedulingAlgorithms processes.txt FCFS
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class SchedulingAlgorithms{

    static class Process {
        int pid;
        int arrivalTime;
        int burstTime;
        int waitingTime;
        int turnaroundTime;

        public Process(int pid, int arrivalTime, int burstTime) {
            this.pid = pid;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
        }
    }

    public static void main(String[] args) {
        // Check if the correct number of arguments is provided
        if (args.length !=  2) {
            System.out.println("Usage: java SchedulingAlgorithms <filename> <algorithm>");
            System.exit(1);
        }

        String filename = args[0];
        String algorithm = args[1];

        // Read process details from the file
        Process[] processes = readProcessesFromFile(filename);

        // Perform scheduling based on the chosen algorithm
        if (algorithm.equalsIgnoreCase("FCFS")) {
            FCFS(processes);
        } else if (algorithm.equalsIgnoreCase("SJF")) {
            SJF(processes);
        } else {
            System.out.println("Invalid algorithm. Use FCFS or SJF.");
            System.exit(1);
        }
    }

    // Read process details from the file
    private static Process[] readProcessesFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            int numProcesses = scanner.nextInt();
            Process[] processes = new Process[numProcesses];

            for (int i =  0; i < numProcesses; i++) {
                int arrivalTime = scanner.nextInt();
                int burstTime = scanner.nextInt();
                processes[i] = new Process(i +  1, arrivalTime, burstTime);
            }

            return processes;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            System.exit(1);
        }

        return null;
    }

    // First Come First Serve (FCFS) Scheduling Algorithm
    private static void FCFS(Process[] processes) {
        Arrays.sort(processes, Comparator.comparingInt(a -> a.arrivalTime));

        int[] waitingTime = new int[processes.length];
        int[] turnaroundTime = new int[processes.length];
        int[] completionTime = new int[processes.length];

        waitingTime[0] =  0;
        for (int i =  1; i < processes.length; i++) {
            waitingTime[i] = completionTime[i -  1] - processes[i].arrivalTime;
        }

        for (int i =  0; i < processes.length; i++) {
            completionTime[i] = waitingTime[i] + processes[i].burstTime;
            turnaroundTime[i] = completionTime[i] - processes[i].arrivalTime;
        }

        System.out.print("Order of Execution: ");
        for (Process p : processes) {
            System.out.print("P" + p.pid + " -> ");
        }
        System.out.println();

        double totalWaitingTime = Arrays.stream(waitingTime).sum();
        double totalTurnaroundTime = Arrays.stream(turnaroundTime).sum();
        System.out.printf("Average Waiting Time: %.2f\n", totalWaitingTime / processes.length);
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaroundTime / processes.length);
    }

    // Shortest Job First (SJF) Scheduling Algorithm
    private static void SJF(Process[] processes) {
        Arrays.sort(processes, Comparator.comparingInt(a -> a.burstTime));

        int[] waitingTime = new int[processes.length];
        int[] turnaroundTime = new int[processes.length];
        int[] completionTime = new int[processes.length];

        waitingTime[0] =  0;
        for (int i =  1; i < processes.length; i++) {
            waitingTime[i] = completionTime[i -  1] - processes[i].arrivalTime;
        }

        for (int i =  0; i < processes.length; i++) {
            completionTime[i] = waitingTime[i] + processes[i].burstTime;
            turnaroundTime[i] = completionTime[i] - processes[i].arrivalTime;
        }

        System.out.print("Order of Execution: ");
        for (Process p : processes) {
            System.out.print("P" + p.pid + " -> ");
        }
        System.out.println();

        double totalWaitingTime = Arrays.stream(waitingTime).sum();
        double totalTurnaroundTime = Arrays.stream(turnaroundTime).sum();
        System.out.printf("Average Waiting Time: %.2f\n", totalWaitingTime / processes.length);
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaroundTime / processes.length);
    }
}
