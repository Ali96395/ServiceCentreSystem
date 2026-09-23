import java.util.Random;
import java.util.Scanner;

/* =========================================================
   NUST SERVICE CENTRE SIMULATION - DSA521S GROUP PROJECT
   Single-file runnable demo covering Parts A, B, C, D
   ========================================================= */

public class ServiceCentreSystem {

    // =====================================================
    // STUDENT MODEL
    // =====================================================
    static class Student {
        String studentNo, name, serviceType;
        int estimatedServiceTime;

        Student(String studentNo, String name, String serviceType, int estimatedServiceTime) {
            this.studentNo = studentNo;
            this.name = name;
            this.serviceType = serviceType;
            this.estimatedServiceTime = estimatedServiceTime;
        }

        @Override
        public String toString() {
            return String.format("%-12s %-10s %-15s %3d min",
                    studentNo, name, serviceType, estimatedServiceTime);
        }
    }

    // =====================================================
    // PART A1 - QUEUE (hand-implemented)
    // =====================================================
    static class Queue {
        static class Node {
            Student data; Node next;
            Node(Student d) { data = d; }
        }
        Node front, rear;
        int size;

        boolean isEmpty() { return front == null; }

        void enqueue(Student s) {
            Node n = new Node(s);
            if (isEmpty()) front = rear = n;
            else { rear.next = n; rear = n; }
            size++;
        }

        Student dequeue() {
            if (isEmpty()) return null;
            Student s = front.data;
            front = front.next;
            if (front == null) rear = null;
            size--;
            return s;
        }

        Student peek() { return isEmpty() ? null : front.data; }

        void displayQueue() {
            if (isEmpty()) { System.out.println("  [Queue empty]"); return; }
            Node cur = front; int i = 1;
            while (cur != null) {
                System.out.println("  " + i++ + ". " + cur.data);
                cur = cur.next;
            }
        }
    }

    // =====================================================
    // PART A2 - SINGLY LINKED LIST (hand-implemented)
    // =====================================================
    static class SinglyLinkedList {
        static class Node {
            Student data; Node next;
            Node(Student d) { data = d; }
        }
        Node head; int size;

        void insertAtBeginning(Student s) {
            Node n = new Node(s); n.next = head; head = n; size++;
        }

        void insertAtEnd(Student s) {
            Node n = new Node(s);
            if (head == null) head = n;
            else { Node c = head; while (c.next != null) c = c.next; c.next = n; }
            size++;
        }

        boolean insertAtPosition(Student s, int pos) {
            if (pos < 1 || pos > size + 1) return false;
            if (pos == 1) { insertAtBeginning(s); return true; }
            Node n = new Node(s), c = head;
            for (int i = 1; i < pos - 1; i++) c = c.next;
            n.next = c.next; c.next = n; size++;
            return true;
        }

        boolean deleteStudent(String no) {
            if (head == null) return false;
            if (head.data.studentNo.equals(no)) { head = head.next; size--; return true; }
            Node c = head;
            while (c.next != null && !c.next.data.studentNo.equals(no)) c = c.next;
            if (c.next == null) return false;
            c.next = c.next.next; size--; return true;
        }

        Student searchStudent(String no) {
            Node c = head;
            while (c != null) {
                if (c.data.studentNo.equals(no)) return c.data;
                c = c.next;
            }
            return null;
        }

        void displayStudents() {
            if (head == null) { System.out.println("  [No records]"); return; }
            Node c = head; int i = 1;
            while (c != null) { System.out.println("  " + i++ + ". " + c.data); c = c.next; }
        }
    }

    // =====================================================
    // PART A3 - STACK (hand-implemented)
    // =====================================================
    static class Stack {
        int[] data; int top; int capacity;
        Stack(int c) { capacity = c; data = new int[c]; top = -1; }
        boolean isEmpty() { return top == -1; }
        void push(int v) {
            if (top == capacity - 1) throw new RuntimeException("Overflow");
            data[++top] = v;
        }
        int pop() {
            if (isEmpty()) throw new RuntimeException("Underflow");
            return data[top--];
        }
        void display() {
            if (isEmpty()) { System.out.print("[empty]"); return; }
            for (int i = 0; i <= top; i++) System.out.print(data[i] + (i < top ? " " : ""));
        }
    }

    static int evaluatePostfix(String expr) {
        Stack st = new Stack(100);
        String[] tokens = expr.trim().split("\\s+");
        System.out.println("\n--- Postfix Trace: " + expr + " ---");
        for (String t : tokens) {
            if (isNumber(t)) {
                st.push(Integer.parseInt(t));
                System.out.printf("Push %-3s -> Stack: ", t);
                st.display(); System.out.println();
            } else {
                int b = st.pop(), a = st.pop();
                int r = apply(a, b, t);
                st.push(r);
                System.out.printf("Op %-3s ( %d %s %d = %d ) -> Stack: ", t, a, t, b, r);
                st.display(); System.out.println();
            }
        }
        int result = st.pop();
        System.out.println("Final Result = " + result);
        return result;
    }
    static boolean isNumber(String s) {
        try { Integer.parseInt(s); return true; }
        catch (Exception e) { return false; }
    }
    static int apply(int a, int b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "x": case "*": return a * b;
            case "/": case "÷": if (b == 0) throw new ArithmeticException("÷0"); return a / b;
            default: throw new IllegalArgumentException("Bad op: " + op);
        }
    }

    // =====================================================
    // PART B - SORTING ALGORITHMS (no built-ins)
    // =====================================================
    static long selectionSort(int[] a) {
        long comps = 0;
        for (int i = 0; i < a.length - 1; i++) {
            int m = i;
            for (int j = i + 1; j < a.length; j++) {
                comps++;
                if (a[j] < a[m]) m = j;
            }
            int t = a[m]; a[m] = a[i]; a[i] = t;
        }
        return comps;
    }

    static long insertionSort(int[] a) {
        long comps = 0;
        for (int i = 1; i < a.length; i++) {
            int key = a[i], j = i - 1;
            while (j >= 0) {
                comps++;
                if (a[j] > key) { a[j + 1] = a[j]; j--; }
                else break;
            }
            a[j + 1] = key;
        }
        return comps;
    }

    static long mergeSort(int[] a) {
        long[] c = new long[1];
        mergeSortHelper(a, 0, a.length - 1, c);
        return c[0];
    }
    static void mergeSortHelper(int[] a, int l, int r, long[] c) {
        if (l >= r) return;
        int m = (l + r) / 2;
        mergeSortHelper(a, l, m, c);
        mergeSortHelper(a, m + 1, r, c);
        merge(a, l, m, r, c);
    }
    static void merge(int[] a, int l, int m, int r, long[] c) {
        int n1 = m - l + 1, n2 = r - m;
        int[] L = new int[n1], R = new int[n2];
        for (int i = 0; i < n1; i++) L[i] = a[l + i];
        for (int j = 0; j < n2; j++) R[j] = a[m + 1 + j];
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) { c[0]++; if (L[i] <= R[j]) a[k++] = L[i++]; else a[k++] = R[j++]; }
        while (i < n1) a[k++] = L[i++];
        while (j < n2) a[k++] = R[j++];
    }

    static long quickSort(int[] a) {
        long[] c = new long[1];
        quickSortHelper(a, 0, a.length - 1, c);
        return c[0];
    }
    static void quickSortHelper(int[] a, int lo, int hi, long[] c) {
        if (lo < hi) {
            int p = partition(a, lo, hi, c);
            quickSortHelper(a, lo, p - 1, c);
            quickSortHelper(a, p + 1, hi, c);
        }
    }
    static int partition(int[] a, int lo, int hi, long[] c) {
        int pivot = a[hi], i = lo - 1;
        for (int j = lo; j < hi; j++) {
            c[0]++;
            if (a[j] <= pivot) { i++; int t = a[i]; a[i] = a[j]; a[j] = t; }
        }
        int t = a[i + 1]; a[i + 1] = a[hi]; a[hi] = t;
        return i + 1;
    }

    // =====================================================
    // PART A4 - ARRAY STATISTICS
    // =====================================================
    static void arrayStats(int[] times) {
        if (times.length == 0) { System.out.println("  [No data]"); return; }
        int total = 0, high = times[0], low = times[0], over10 = 0;
        for (int t : times) {
            total += t;
            if (t > high) high = t;
            if (t < low) low = t;
            if (t > 10) over10++;
        }
        double avg = (double) total / times.length;
        System.out.println("  Total students served : " + times.length);
        System.out.println("  Total service time    : " + total + " min");
        System.out.printf ("  Average service time  : %.2f min%n", avg);
        System.out.println("  Highest service time  : " + high + " min");
        System.out.println("  Lowest service time   : " + low + " min");
        System.out.println("  Services > 10 minutes : " + over10);
    }

    // =====================================================
    // HELPERS
    // =====================================================
    static String arr(int[] a) { return java.util.Arrays.toString(a); }

    static void banner(String title) {
        System.out.println("\n=================================================");
        System.out.println("  " + title);
        System.out.println("=================================================");
    }

    // =====================================================
    // DEMO RUNNERS
    // =====================================================
    static void demoQueue() {
        banner("PART A1 - QUEUE DEMONSTRATION");
        Queue q = new Queue();
        Student[] arrivals = {
            new Student("221045678", "Maria",   "Registration", 12),
            new Student("222034512", "Tomas",   "Student Card",  5),
            new Student("223041876", "Ndapewa", "Fees",          8),
            new Student("221067341", "Simon",   "Documents",     4),
            new Student("221099900", "Anna",    "Academic",      7),
            new Student("221088877", "Petrus",  "Registration", 15)
        };
        System.out.println("\n>> Enqueueing 6 students in arrival order:");
        for (Student s : arrivals) {
            q.enqueue(s);
            System.out.println("   + " + s);
        }
        System.out.println("\n>> Waiting queue:");
        q.displayQueue();
        System.out.println("\n>> peek() first in line: " + q.peek());

        System.out.println("\n>> Serving 3 students (dequeue):");
        for (int i = 0; i < 3; i++) {
            Student served = q.dequeue();
            System.out.println("   - Served: " + served);
        }
        System.out.println("\n>> Remaining queue:");
        q.displayQueue();
        System.out.println("  isEmpty()? " + q.isEmpty());
    }

    static void demoLinkedList() {
        banner("PART A2 - SINGLY LINKED LIST DEMONSTRATION");
        SinglyLinkedList list = new SinglyLinkedList();

        System.out.println("\n>> Insert at END: Maria, Tomas");
        list.insertAtEnd(new Student("221045678", "Maria", "Registration", 12));
        list.insertAtEnd(new Student("222034512", "Tomas", "Student Card",  5));

        System.out.println(">> Insert at BEGINNING: Ndapewa");
        list.insertAtBeginning(new Student("223041876", "Ndapewa", "Fees", 8));

        System.out.println(">> Insert at POSITION 2: Simon");
        list.insertAtPosition(new Student("221067341", "Simon", "Documents", 4), 2);

        System.out.println("\n>> Linked list now:");
        list.displayStudents();

        System.out.println("\n>> Search for 222034512:");
        Student found = list.searchStudent("222034512");
        System.out.println("   " + (found != null ? "FOUND: " + found : "Not found"));

        System.out.println("\n>> Delete 221045678 (Maria):");
        System.out.println("   delete returned: " + list.deleteStudent("221045678"));

        System.out.println("\n>> Linked list after deletion:");
        list.displayStudents();
    }

    static void demoStack() {
        banner("PART A3 - POSTFIX EXPRESSION EVALUATION");
        evaluatePostfix("5 3 + 2 *");
        evaluatePostfix("10 2 / 3 +");
        evaluatePostfix("4 5 + 6 2 - *");
    }

    static void demoStats() {
        banner("PART A4 - DAILY SERVICE STATISTICS");
        int[] times = {12, 5, 8, 4, 7, 15, 10, 3, 20, 6, 11, 9};
        System.out.println("  Service times recorded: " + arr(times));
        arrayStats(times);
    }

    static void demoSorting() {
        banner("PART B - SORTING ALGORITHMS DEMONSTRATION");
        int[] base = {17, 5, 23, 8, 14, 3, 11, 20, 6, 9};
        System.out.println("  Original array: " + arr(base));

        int[] s1 = base.clone();
        long c1 = selectionSort(s1);
        System.out.println("\n  Selection Sort  -> " + arr(s1) + "  (comparisons = " + c1 + ")");

        int[] s2 = base.clone();
        long c2 = insertionSort(s2);
        System.out.println("  Insertion Sort  -> " + arr(s2) + "  (comparisons = " + c2 + ")");

        int[] s3 = base.clone();
        long c3 = mergeSort(s3);
        System.out.println("  Merge Sort      -> " + arr(s3) + "  (comparisons = " + c3 + ")");

        int[] s4 = base.clone();
        long c4 = quickSort(s4);
        System.out.println("  Quick Sort      -> " + arr(s4) + "  (comparisons = " + c4 + ")");
    }

    static void demoExperiment() {
        banner("PART C - ALGORITHM EXPERIMENT");
        int[] sizes = {20, 50, 100, 500};
        Random rand = new Random(42);

        System.out.printf("%-16s %-6s %-14s %-15s%n",
                "Algorithm", "Size", "Comparisons", "Time (ns)");
        System.out.println("-----------------------------------------------------");

        for (int size : sizes) {
            int[] original = new int[size];
            for (int i = 0; i < size; i++) original[i] = rand.nextInt(1000);

            runExperiment("Selection Sort", 1, original);
            runExperiment("Insertion Sort", 2, original);
            runExperiment("Merge Sort",     3, original);
            runExperiment("Quick Sort",     4, original);
        }

        // Almost-sorted test (Part C question 3)
        System.out.println("\n--- Almost-Sorted Array (size 100) ---");
        System.out.printf("%-16s %-14s %-15s%n", "Algorithm", "Comparisons", "Time (ns)");
        int[] almost = new int[100];
        for (int i = 0; i < 100; i++) almost[i] = i;
        almost[10] = 50; almost[50] = 10;

        runExperiment("Selection Sort", 1, almost);
        runExperiment("Insertion Sort", 2, almost);
        runExperiment("Merge Sort",     3, almost);
        runExperiment("Quick Sort",     4, almost);
    }

    static void runExperiment(String name, int type, int[] src) {
        int[] copy = src.clone();
        long comps = 0;
        long start = System.nanoTime();
        switch (type) {
            case 1: comps = selectionSort(copy); break;
            case 2: comps = insertionSort(copy); break;
            case 3: comps = mergeSort(copy);     break;
            case 4: comps = quickSort(copy);     break;
        }
        long end = System.nanoTime();
        System.out.printf("%-16s %-6d %-14d %-15d%n", name, src.length, comps, (end - start));
    }

    static void demoIntegrated() {
        banner("PART D - INTEGRATED SERVICE-CENTRE SYSTEM (AUTOMATED WALKTHROUGH)");
        Queue q = new Queue();
        SinglyLinkedList records = new SinglyLinkedList();
        int[] statsStore = new int[100];
        int statsCount = 0;

        System.out.println("\n[Menu Option 1] Add students to waiting queue...");
        Student[] arrivals = {
            new Student("221045678", "Maria",   "Registration", 12),
            new Student("222034512", "Tomas",   "Student Card",  5),
            new Student("223041876", "Ndapewa", "Fees",          8),
            new Student("221067341", "Simon",   "Documents",     4)
        };
        for (Student s : arrivals) { q.enqueue(s); System.out.println("   + " + s); }

        System.out.println("\n[Menu Option 3] Display waiting students:");
        q.displayQueue();

        System.out.println("\n[Menu Option 2] Serve 2 students:");
        for (int i = 0; i < 2; i++) {
            Student s = q.dequeue();
            System.out.println("   Served: " + s);
            records.insertAtEnd(s);
            statsStore[statsCount++] = s.estimatedServiceTime;
        }

        System.out.println("\n[Menu Option 4] Add one more record to Linked List:");
        Student extra = new Student("221099900", "Anna", "Academic", 7);
        records.insertAtEnd(extra);
        statsStore[statsCount++] = extra.estimatedServiceTime;
        System.out.println("   + " + extra);

        System.out.println("\n[Menu Option 5] Display Linked-List records:");
        records.displayStudents();

        System.out.println("\n[Menu Option 6] Search record 223041876:");
        Student found = records.searchStudent("223041876");
        System.out.println("   " + (found != null ? "FOUND: " + found : "Not found"));

        System.out.println("\n[Menu Option 7] Remove record 221045678:");
        System.out.println("   Removed? " + records.deleteStudent("221045678"));
        System.out.println("   Records now:");
        records.displayStudents();

        System.out.println("\n[Menu Option 8] Daily statistics:");
        int[] realStats = new int[statsCount];
        System.arraycopy(statsStore, 0, realStats, 0, statsCount);
        System.out.println("   Service times: " + arr(realStats));
        arrayStats(realStats);

        System.out.println("\n[Menu Option 9] Sort service times (Quick Sort):");
        int[] sorted = realStats.clone();
        quickSort(sorted);
        System.out.println("   Original: " + arr(realStats));
        System.out.println("   Sorted  : " + arr(sorted));
    }

    // =====================================================
    // MAIN
    // =====================================================
    public static void main(String[] args) {
        System.out.println("#################################################");
        System.out.println("#   NUST CAMPUS SERVICE CENTRE SIMULATION       #");
        System.out.println("#   DSA521S - Data Structures & Algorithms 1    #");
        System.out.println("#   Group Mini-Project 2026                     #");
        System.out.println("#################################################");

        demoQueue();
        demoLinkedList();
        demoStack();
        demoStats();
        demoSorting();
        demoExperiment();
        demoIntegrated();

        // Optional: interactive menu
        if (args.length > 0 && args[0].equalsIgnoreCase("menu")) {
            runInteractiveMenu();
        } else {
            System.out.println("\n=================================================");
            System.out.println("  ALL DEMONSTRATIONS COMPLETE");
            System.out.println("  Run with argument 'menu' for interactive mode:");
            System.out.println("      java ServiceCentreSystem menu");
            System.out.println("=================================================");
        }
    }

    // =====================================================
    // OPTIONAL INTERACTIVE MENU (Part D live mode)
    // =====================================================
    static void runInteractiveMenu() {
        Scanner sc = new Scanner(System.in);
        Queue q = new Queue();
        SinglyLinkedList records = new SinglyLinkedList();
        int[] store = new int[200];
        int count = 0;
        int choice;

        do {
            System.out.println("\n===== CAMPUS SERVICE CENTRE =====");
            System.out.println("1.  Add student to waiting queue");
            System.out.println("2.  Serve next student");
            System.out.println("3.  Display waiting students");
            System.out.println("4.  Add student service record");
            System.out.println("5.  Display student records");
            System.out.println("6.  Search student record");
            System.out.println("7.  Remove student record");
            System.out.println("8.  Display daily statistics");
            System.out.println("9.  Sort service times");
            System.out.println("10. Run sorting experiment");
            System.out.println("11. Exit");
            System.out.print("Select option: ");

            while (!sc.hasNextInt()) { sc.next(); System.out.print("Enter a number: "); }
            choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1: {
                    System.out.print("Student No: ");      String no   = sc.nextLine();
                    System.out.print("Name: ");            String nm   = sc.nextLine();
                    System.out.print("Service Type: ");    String tp   = sc.nextLine();
                    System.out.print("Estimated Time: ");  int tm      = Integer.parseInt(sc.nextLine());
                    q.enqueue(new Student(no, nm, tp, tm));
                    System.out.println("Added to queue.");
                    break;
                }
                case 2: {
                    Student s = q.dequeue();
                    if (s == null) System.out.println("Queue empty.");
                    else {
                        System.out.println("Served: " + s);
                        records.insertAtEnd(s);
                        if (count < store.length) store[count++] = s.estimatedServiceTime;
                    }
                    break;
                }
                case 3: q.displayQueue(); break;
                case 4: {
                    System.out.print("Student No: ");      String no   = sc.nextLine();
                    System.out.print("Name: ");            String nm   = sc.nextLine();
                    System.out.print("Service Type: ");    String tp   = sc.nextLine();
                    System.out.print("Estimated Time: ");  int tm      = Integer.parseInt(sc.nextLine());
                    records.insertAtEnd(new Student(no, nm, tp, tm));
                    break;
                }
                case 5: records.displayStudents(); break;
                case 6: {
                    System.out.print("Student No: ");
                    Student s = records.searchStudent(sc.nextLine());
                    System.out.println(s == null ? "Not found." : "Found: " + s);
                    break;
                }
                case 7: {
                    System.out.print("Student No: ");
                    System.out.println(records.deleteStudent(sc.nextLine()) ? "Removed." : "Not found.");
                    break;
                }
                case 8: {
                    int[] real = new int[count];
                    System.arraycopy(store, 0, real, 0, count);
                    arrayStats(real);
                    break;
                }
                case 9: {
                    int[] real = new int[count];
                    System.arraycopy(store, 0, real, 0, count);
                    if (real.length == 0) { System.out.println("No data."); break; }
                    System.out.println("Original: " + arr(real));
                    quickSort(real);
                    System.out.println("Sorted  : " + arr(real));
                    break;
                }
                case 10: demoExperiment(); break;
                case 11: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid option.");
            }
        } while (choice != 11);
        sc.close();
    }
}