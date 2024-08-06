#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Process structure
struct Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int waitingTime;
    int turnaroundTime;
};

// Function prototypes
struct Process* readProcessesFromFile(char *filename);
void FCFS(struct Process *processes, int numProcesses);
void SJF(struct Process *processes, int numProcesses);

int main(int argc, char *argv[]) {
    if (argc != 3) {
        printf("Usage: %s <filename> <algorithm>\n", argv[0]);
        exit(1);
    }

    char *filename = argv[1];
    char *algorithm = argv[2];

    struct Process *processes = readProcessesFromFile(filename);

    if (strcmp(algorithm, "FCFS") == 0) {
        FCFS(processes, processes[0].pid);
    } else if (strcmp(algorithm, "SJF") == 0) {
        SJF(processes, processes[0].pid);
    } else {
        printf("Invalid algorithm. Use FCFS or SJF.\n");
        exit(1);
    }

    free(processes);
    return 0;
}

// Read process details from file
struct Process* readProcessesFromFile(char *filename) {
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        printf("File not found: %s\n", filename);
        exit(1);
    }

    int numProcesses;
    fscanf(file, "%d", &numProcesses);

    struct Process *processes = (struct Process*)malloc(numProcesses * sizeof(struct Process));

    for (int i = 0; i < numProcesses; i++) {
        fscanf(file, "%d %d", &processes[i].arrivalTime, &processes[i].burstTime);
        processes[i].pid = i + 1;
    }

    fclose(file);
    return processes;
}

// First-Come, First-Served scheduling algorithm
void FCFS(struct Process *processes, int numProcesses) {
    int completionTime = 0;
    double totalWaitingTime = 0, totalTurnaroundTime = 0;

    for (int i = 0; i < numProcesses; i++) {
        if (processes[i].arrivalTime > completionTime) {
            completionTime = processes[i].arrivalTime;
        }
        processes[i].waitingTime = completionTime - processes[i].arrivalTime;
        processes[i].turnaroundTime = processes[i].waitingTime + processes[i].burstTime;
        completionTime += processes[i].burstTime;
        totalWaitingTime += processes[i].waitingTime;
        totalTurnaroundTime += processes[i].turnaroundTime;
    }

    printf("Order of Execution: ");
    for (int i = 0; i < numProcesses; i++) {
        printf("P%d -> ", processes[i].pid);
    }
    printf("\n");
    printf("Average Waiting Time: %.2f\n", totalWaitingTime / numProcesses);
    printf("Average Turnaround Time: %.2f\n", totalTurnaroundTime / numProcesses);
}

// Shortest Job First scheduling algorithm
void SJF(struct Process *processes, int numProcesses) {
    // Sort processes based on burst time
    for (int i = 0; i < numProcesses - 1; i++) {
        for (int j = 0; j < numProcesses - i - 1; j++) {
            if (processes[j].burstTime > processes[j + 1].burstTime) {
                // Swap
                struct Process temp = processes[j];
                processes[j] = processes[j + 1];
                processes[j + 1] = temp;
            }
        }
    }

    int completionTime = 0;
    double totalWaitingTime = 0, totalTurnaroundTime = 0;

    for (int i = 0; i < numProcesses; i++) {
        if (processes[i].arrivalTime > completionTime) {
            completionTime = processes[i].arrivalTime;
        }
        processes[i].waitingTime = completionTime - processes[i].arrivalTime;
        processes[i].turnaroundTime = processes[i].waitingTime + processes[i].burstTime;
        completionTime += processes[i].burstTime;
        totalWaitingTime += processes[i].waitingTime;
        totalTurnaroundTime += processes[i].turnaroundTime;
    }

    printf("Order of Execution: ");
    for (int i = 0; i < numProcesses; i++) {
        printf("P%d -> ", processes[i].pid);
    }
    printf("\n");
    printf("Average Waiting Time: %.2f\n", totalWaitingTime / numProcesses);
    printf("Average Turnaround Time: %.2f\n", totalTurnaroundTime / numProcesses);
}
