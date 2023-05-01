import java.util.ArrayList;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner get = new Scanner(System.in);
        ArrayList<Process> processes = new ArrayList<>();

        int n = get.nextInt(); get.nextLine();
        for (int i=0; i<n; i++) {
            String[] inp = get.nextLine().split(" ");
            processes.add(new Process(
                    inp[0],
                    Integer.parseInt(inp[1]),
                    Integer.parseInt(inp[2]),
                    Integer.parseInt(inp[3])
            ));
        }

        int timer = 0;
        Process currentProcess = null;
        BinaryHeap queue = new BinaryHeap();


        while (queue.size() != 0 || processes.size() != 0 || currentProcess != null) {
            String timeString = String.format("%1$3s", timer).replace(' ', '0');

            // masukkan proses ke waitQueue
            if (processes.size() > 0 && processes.get(0).arrivalTime <= timer) {
                System.out.println(timeString + " add " + processes.get(0).name);
                queue.insert(processes.get(0));
                processes.remove(0);
            }

            // jangan jalankan proses ketika waktu sama dengan 0
            if (timer == 0) {
                timer++;
                continue;
            }

            // pilih proses priority tertinggi
            if (queue.size() != 0) {
                if (currentProcess == null){
                    currentProcess = queue.get();
                    queue.remove();
                    System.out.println(timeString + " executing " + currentProcess.name);
                } else {
                    if (queue.get().compareTo(currentProcess) < 0){
                        Process oldProcess = currentProcess;
                        System.out.println(timeString + " " + currentProcess.name + " preempted");
                        currentProcess = queue.get();
                        queue.remove();
                        System.out.println(timeString + " executing " + currentProcess.name);
                        queue.insert(oldProcess);
                    }
                }
            }

            // jalankan proses terkini
            if (currentProcess != null) {
                if (currentProcess.burstTime > 0){
                    currentProcess.burstTime--;
                }
                if (currentProcess.burstTime == 0){
                    System.out.println(timeString + " " + currentProcess.name + " done");
                    currentProcess = null;
                }
            }

            timer++;
        }

        get.close();
    }
}

class Process implements Comparable<Process> {
    String name;
    int priority;
    int burstTime;
    int arrivalTime;

    public Process(String name, int priority, int burstTime, int arrivalTime) {
        this.name = name;
        this.priority = priority;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public int compareTo(Process o) {
        if (this.priority != o.priority){
            return Integer.compare(this.priority,o.priority);
        }else {
            return Integer.compare(this.arrivalTime,o.arrivalTime);
        }
    }
}



class BinaryHeap {
    ArrayList<Process> processes = new ArrayList<>();

    public void insert(Process process){
        processes.add(process);
        int index = processes.size() - 1;
        heapifyUp(index);
    }
    private void heapifyUp(int index) {
        if (index == 0){
            return;
        }
        int idxParent;
        if (index%2 == 0){
            idxParent = (index - 2)/2;
        }else {
            idxParent = (index - 1)/2;
        }
        Process newProses = processes.get(index);
        Process parent = processes.get(idxParent);

        if (newProses.compareTo(parent) < 0){
            processes.set(idxParent,newProses);
            processes.set(index,parent);
            heapifyUp(idxParent);
        }
    }
    public Process get(){
        if (processes == null){
            return null;
        }
        return processes.get(0);
    }
    public void remove(){
        if (processes == null){
            return;
        }
        Process root = processes.get(0);
        Process tail = processes.get(processes.size() - 1);

        processes.set(processes.size() - 1,root);
        processes.set(0,tail);

        processes.remove(processes.size() - 1);
        heapifyDown(0);

    }

    public int size(){
        return processes.size();
    }
    private void heapifyDown(int index) {
        int leftChildIndex = (index * 2) + 1;
        int rightChildIndex = (index * 2) + 2;
        int smallestChildIndex = index;

        if (leftChildIndex < processes.size()) {
            if (processes.get(leftChildIndex).compareTo(processes.get(smallestChildIndex)) < 0){
                smallestChildIndex = leftChildIndex;
            }
        }
        if (rightChildIndex < processes.size()) {
            if (processes.get(rightChildIndex).compareTo(processes.get(smallestChildIndex)) < 0){
                smallestChildIndex = rightChildIndex;
            }
        }

        if (smallestChildIndex != index) {
            Process temp = processes.get(index);
            processes.set(index, processes.get(smallestChildIndex));
            processes.set(smallestChildIndex, temp);
            heapifyDown(smallestChildIndex);
        }
    }
}
