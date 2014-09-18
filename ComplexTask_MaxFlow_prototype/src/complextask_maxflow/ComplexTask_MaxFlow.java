/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * offset_Task = Total_ComplexTask + 2;
 * offset_Worker = offset_Task + total_subtask;
 * n = offset_Worker + total_Worker + 2;
 * INPUT MATRIX - INDEX
 *  + NEW SOURCE: 0
 *  + OLD SOURCE: 1
 *  + COMPLEX TASK: 2 -> Total_ComplexTask + 1;
 *  + SUB TASK: offset_Task -> offset_Worker -1
 *  + WORKER: offset_Worker -> n-3
 *  + OLD SINK: n-2
 *  + NEW SINK: n-1
 * 
 * offset_Task = Total_ComplexTask + 2;
 * offset_Worker = offset_Task + Total_SubTask;
 */
package complextask_maxflow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import jxl.*;
import jxl.write.*;
import jxl.write.Boolean;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

/**
 *
 * @author dkh
 */
public class ComplexTask_MaxFlow {

    /**
     * @param args the command line arguments
     */
    static int total_ComplexTask = 0;
    static int total_subtask = 0;
    static int total_Worker = 0;
    static int offset_Task;
    static int offset_Worker;
    static int total_node;
    static int AVG_SubTask_No = 0;
    static int Max_SubTask_No = Integer.MIN_VALUE;
    static int Min_SubTask_No = Integer.MAX_VALUE;
    static Hashtable<Integer, Hashtable<Integer, Task>> Tasks = new Hashtable<>();
    static ArrayList<Worker> Workers = new ArrayList<>();
    static ArrayList<Task> SubTasks = new ArrayList<>();
    static int[][] cap;
    static int[][] cost_st;
    static int[][] cost_ct;
    static int[][] cost_travelmax;
    static int[][] cost_travelmin;
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) {


        try {
            File exlFile = new File("D:/write_test.xls");
            WritableWorkbook writableWorkbook = Workbook
                    .createWorkbook(exlFile);
            Label label = new Label(0, 0, "#ComplexTask");
            Label label1 = new Label(0, 1, "#Max ST / CT");
            Label label2 = new Label(0, 2, "#CT assigned");
            Label label3 = new Label(0, 3, "#ST assigned");
            Label label4 = new Label(0, 4, "Max Distance");
            Label label5 = new Label(0, 5, "Avg Distance");
            Label label6 = new Label(0, 6, "Min Distance");

            WritableSheet writableSheet = writableWorkbook.createSheet(
                    "Sheet1", 0);
            writableSheet.addCell(label);
            writableSheet.addCell(label1);
            writableSheet.addCell(label2);
            writableSheet.addCell(label3);
            writableSheet.addCell(label4);
            writableSheet.addCell(label5);
            writableSheet.addCell(label6);
            int k = 0;
            for (int i = 1; i < 3; i++) {
                for (int j = 4; j < 6; j++) {

                    k++;
                    Max_SubTask_No = Integer.MIN_VALUE;
                    total_ComplexTask = 0;
                    total_subtask = 0;
                    total_Worker = 0;
                    Tasks = new Hashtable<>();
                    SubTasks = new ArrayList<>();
                    Workers = new ArrayList<>();

                    System.out.println("Testing time " + k + "\n");
                    int t[] = Execute(i, j);

                    System.out.println("------------------------------------------\n");

                    result.delete(0, result.length());
                    Number num = new Number(k, 0, total_ComplexTask);
                    Number num2 = new Number(k, 1, Max_SubTask_No);
                    Number num3 = new Number(k, 2, Math.abs(t[0]));
                    Number num4 = new Number(k, 3, t[1]);
                    Number num5 = new Number(k, 4, Math.abs(t[2]));
                    Number num6 = new Number(k, 5, t[3]);
                    Number num7 = new Number(k, 6, t[4]);

                    writableSheet.addCell(num);
                    writableSheet.addCell(num2);
                    writableSheet.addCell(num3);
                    writableSheet.addCell(num4);
                    writableSheet.addCell(num5);
                    writableSheet.addCell(num6);
                    writableSheet.addCell(num7);
                }

            }
            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

// <editor-fold defaultstate="collapsed" desc="EXECUTE THE PROGRAM">
    public static int[] Execute(int t_file_index, int w_file_index) {
        int[] t = new int[5];

        input_from_dataset(t_file_index, w_file_index, "gowalla");
        print_input_statistic();
        Initiate_Capacity_and_Cost_Matrixes();
        Capacity_Matrix_Assignment();
        double startTime = System.nanoTime();
        result.append("#Complex Task assigned: ");
        t[0] = execute_phase(cap, cost_ct);
        result.append("#SubTask assigned: ");
        t[1] =0;// execute_phase(cap, cost_st);

        //result.append("Max Distance: ");
        t[2] = 0;//execute_phase(cap, cost_travelmax);

        MaxFlow f = new MaxFlow();
        int r[] = f.getMaxFlow(cap, cost_travelmin, 0, total_node - 1);
        result.append("Average Distance: " + Math.abs(r[1]) + "(flow values: " + r[0] + ")\n");
        t[3] = Math.abs(r[1]);

        result.append("Min Distance: ");
        t[4] = execute_phase(cap, cost_travelmin);






        double endTime = System.nanoTime();
        System.out.println(result.toString());
        Utils.printTime((endTime - startTime) / 1000000000.0);
        return t;

    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="EXCECUTE PHASE">
    public static int execute_phase(int[][] capacity, int[][] cost) {
        MinCostMaxFlow flow = new MinCostMaxFlow();
        int ret[] = flow.getMaxFlow(capacity, cost, 0, total_node - 1);
        result.append(Math.abs(ret[1]) + "(flow values: " + ret[0] + ")\n");
        return ret[1];
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="INITIATE INPUT MATRIXES">
    public static void Initiate_Capacity_and_Cost_Matrixes() {
        if (total_node > 0) {
            cap = new int[total_node][total_node];
            cost_st = new int[total_node][total_node];
            cost_ct = new int[total_node][total_node];
            cost_travelmax = new int[total_node][total_node];
            cost_travelmin = new int[total_node][total_node];

        } else {
            System.out.println("Read input first");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="ASSIGN VALUES FOR CAPACITY MATRIX">
    public static void Capacity_Matrix_Assignment() {
        edge_NewSource_ComplexTask();
        edge_OldSource_NewSink();
        edge_ComplexTask_SubTask();
        edge_ComplexTask_NewSink();
        edge_SubTask_Worker();
        edge_Worker_OldSink();
        edge_OldSink_OldSource();
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="SET WEIGHTS FOR EDGES CONNECTING NEW SOURCE AND COMPLEX TASK">
    public static void edge_NewSource_ComplexTask() {
        if (cap != null) {
            for (int j = 2; j < offset_Task; j++) {
                cap[0][j] = Max_SubTask_No;
                //cost_ct[0][j] = 1;

            }
        } else {
            System.out.println("edge_NewSource_ComplexTask: Capacity Matrix is null");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="SET WEIGHTS FOR EDGES CONNECTING OLD SOURCE AND NEW SINK">
    public static void edge_OldSource_NewSink() {
        if (total_node > 0) {
            cap[1][total_node - 1] = Max_SubTask_No * total_ComplexTask;
        } else {
            System.out.println("edge_OldSource_NewSink: read input first");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="SET WEIGHTS/COST FOR EDGES CONNECTING COMPLEX TASK AND SUB TASK">
    public static void edge_ComplexTask_SubTask() {
        if (cap != null) {
            int subtask_start_index = offset_Task;
            int subtask_end = offset_Task;
            Iterator complextask = Tasks.keySet().iterator();
            int complex_task_index = 2;
            while (complextask.hasNext()) {
                int group = Integer.parseInt(complextask.next().toString());
                int s = Tasks.get(group).size();
                subtask_end += s;
                for (int i = subtask_start_index; i < subtask_end; i++) {
                    cap[complex_task_index][i] = 1;
                }
                //cost_ct[complex_task_index][subtask_start_index] = -1;
                cost_ct[complex_task_index][subtask_end - 1] = -1;

                complex_task_index++;
                subtask_start_index = subtask_end;
            }
        } else {
            System.out.println("edge_ComplexTask_SubTask: capacity matrix is not initiate");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="SET WEIGHTS FOR EDGES CONNECTING COMPLEX TASK AND NEW SINK">
    public static void edge_ComplexTask_NewSink() {
        if (cap != null) {
            int complex_task_index = 2;
            Iterator complextask = Tasks.keySet().iterator();

            while (complextask.hasNext()) {
                int group = Integer.parseInt(complextask.next().toString());
                int s = Tasks.get(group).size();
                cap[complex_task_index][total_node - 1] = Max_SubTask_No - s;
                complex_task_index++;
            }
        } else {
            System.out.println("edge_ComplexTask_NewSink: Capacity Matrix is not initiated");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="EXTRACT A LIST OF SUBTASK FROM COMPLEX TASK HASH TABLE">
    public static void Extract_SubTasks() {
        if (cap != null) {
            Iterator complextask = Tasks.keySet().iterator();
            while (complextask.hasNext()) {
                int group = Integer.parseInt(complextask.next().toString());
                Iterator sub_task = Tasks.get(group).keySet().iterator();
                while (sub_task.hasNext()) {
                    int st = Integer.parseInt(sub_task.next().toString());
                    SubTasks.add(Tasks.get(group).get(st));
                }
            }

        } else {
            System.out.println("Extract_SubTasks: Capacity Matrix is not initiated");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="SET WEIGHTS FOR EDGES CONNECTING SUB TASK AND WORKER">
    public static void edge_SubTask_Worker() {
        if (cap != null) {
            Extract_SubTasks();
            for (int st = 0; st < SubTasks.size(); st++) {
                for (int w = 0; w < Workers.size(); w++) {
                    if (SubTasks.get(st).isCoveredByMBR(Workers.get(w).getMBR())) {
                        cap[offset_Task + st][offset_Worker + w] = 1;
                        cost_st[offset_Task + st][offset_Worker + w] = 1;
                        double d = distance(SubTasks.get(st).getLat(), SubTasks.get(st).getLng(),
                                Workers.get(w).getLatitude(), Workers.get(w).getLongitude(), 'K');
                        cost_travelmax[offset_Task + st][offset_Worker + w] = (int) (d * (-1) * 1000);
                        cost_travelmin[offset_Task + st][offset_Worker + w] = (int) (d * 1000);
                    }
                }
            }
        } else {
            System.out.println("edge_SubTask_Worker: Capacity Matrix is not initiated");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="SET WEIGHTS FOR EDGES CONNECTING WORKER AND OLD SINK">
    public static void edge_Worker_OldSink() {
        for (int i = 0; i < Workers.size(); i++) {
            cap[offset_Worker + i][total_node - 2] = Workers.get(i).getMaxTaskNo();
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="SET WEIGHTS FOR EDGES CONNECTING OLD SINK AND OLD SOURCE">
    public static void edge_OldSink_OldSource() {
        if (cap != null) {
            cap[total_node - 2][1] = Integer.MAX_VALUE;
        } else {
            System.out.println("edge_OldSink_OldSource: Capactity Matrix is not initiated");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="READ DATASET TO GENERATE INPUT">
    public static void input_from_dataset(int t_index, int w_index, String dataset) {
        if (dataset.equalsIgnoreCase("yelp")) {
            read_task_yelp(Constant.TaskYELP + t_index + Constant.suffix);
            read_worker_yelp(Constant.WorkerYELP + w_index + Constant.suffix);

        } else if (dataset.equalsIgnoreCase("gowalla")) {
            read_worker_gowalla(Constant.WorkerGOWALLA + t_index + Constant.suffix);
            read_task_gowalla(Constant.TaskGOWALLA + w_index + Constant.suffix);
        }
        offset_Task = total_ComplexTask + 2;
        offset_Worker = offset_Task + total_subtask;
        total_node = total_ComplexTask + total_subtask + total_Worker + 4;
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="GENERATE INPUT FROM YELP DATASET">
    public static void read_task_yelp(String filename) {
        try {
            int tasks_no = 0;
            Random generator = new Random();
            FileReader f = new FileReader(filename);
            BufferedReader in = new BufferedReader(f);
            while (in.ready() && tasks_no < Constant.max_subtask_per_instance) {

                String line = in.readLine();
                String[] parts = line.split(",");
                double lat = Double.parseDouble(parts[0]);
                double lng = Double.parseDouble(parts[1]);
                //int group = Integer.parseInt(parts[4]);
                int group = generator.nextInt(Constant.Yelp_groups);
                //omit the following if to get back to normal version
                // the if statement is to restraint number of input for the sake of simplicity in testing
                //   if (group == 21 || group == 25 || group == 39 || group == 133 || group == 98) {
                Task t = new Task(lat, lng);
                if (Tasks.containsKey(group)) {
                    int temp_index = Tasks.get(group).size();
                    Tasks.get(group).put(temp_index, t);
                } else {
                    Hashtable<Integer, Task> temp_ht = new Hashtable<>();
                    temp_ht.put(0, t);
                    Tasks.put(group, temp_ht);
                }
                tasks_no++;
                //}
            }
            total_subtask = tasks_no;
            total_ComplexTask = Tasks.size();
            Iterator complextask = Tasks.keySet().iterator();
            while (complextask.hasNext()) {
                int group = Integer.parseInt(complextask.next().toString());
                int s = Tasks.get(group).size();
                if (s > Max_SubTask_No) {
                    Max_SubTask_No = s;
                }
                if (s < Min_SubTask_No) {
                    Min_SubTask_No = s;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void read_worker_yelp(String filename) {

        try {
            Random generator = new Random();
            FileReader f = new FileReader(filename);
            BufferedReader in = new BufferedReader(f);
            while (in.ready() && Workers.size() < Constant.max_worker_per_instance) {

                String line = in.readLine();
                String[] parts = line.split(",");
                String id = parts[0];
                //  if (id.equals("T_CviPwQW0EnhISoBX3qpA") || id.equals("wEMn5U0mlTp3Td5n2ZgwKQ")
                //      || id.equals("V_NaUUcLx7e-hFRyGOsOBA") || id.equals("VIMwzph57BDFny0LAW_LGw")) {
                double lat = Double.parseDouble(parts[1]);
                double lng = Double.parseDouble(parts[2]);
                //int maxT = Constant.MaxT + generator.nextInt(8);
                int maxT = Integer.parseInt(parts[3]);
                //String Min_Lat = parts[4].substring(1);
                double mbr_minLat = Double.parseDouble(parts[4].substring(1));
                double mbr_minLng = Double.parseDouble(parts[5]);
                double mbr_maxLat = Double.parseDouble(parts[6]);
                double mbr_maxLng = Double.parseDouble(parts[7].substring(0, parts[7].length() - 2));
                MBR mbr = new MBR(mbr_minLat, mbr_minLng, mbr_maxLat,
                        mbr_maxLng);
                Worker w = new Worker(id, lat, lng, maxT, mbr);
                Workers.add(w);
                total_Worker = Workers.size();
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="GENERATE INPUT FROM GOWALLA DATASET">
    public static void read_worker_gowalla(String filename) {

        try {
            Random generator = new Random();
            FileReader f = new FileReader(filename);
            BufferedReader in = new BufferedReader(f);
            while (in.ready() && Workers.size() < Constant.max_worker_per_instance) {

                String line = in.readLine();
                String[] parts = line.split(",");
                String id = parts[0];
                //  if (id.equals("T_CviPwQW0EnhISoBX3qpA") || id.equals("wEMn5U0mlTp3Td5n2ZgwKQ")
                //      || id.equals("V_NaUUcLx7e-hFRyGOsOBA") || id.equals("VIMwzph57BDFny0LAW_LGw")) {
                double lat = Double.parseDouble(parts[2]);
                double lng = Double.parseDouble(parts[3]);

                int maxT = Constant.MaxT + generator.nextInt(8);
                //String Min_Lat = parts[4].substring(1);
                double mbr_minLat = Double.parseDouble(parts[5].substring(1));
                double mbr_minLng = Double.parseDouble(parts[6]);
                double mbr_maxLat = Double.parseDouble(parts[7]);
                double mbr_maxLng = Double.parseDouble(parts[8].substring(0, parts[8].length() - 2));
                MBR mbr = new MBR(mbr_minLat, mbr_minLng, mbr_maxLat,
                        mbr_maxLng);
                Worker w = new Worker(id, lat, lng, maxT, mbr);
                Workers.add(w);
                total_Worker = Workers.size();
                //}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void read_task_gowalla(String filename) {
        try {
            Random generator = new Random();
            int tasks_no = 0;
            FileReader f = new FileReader(filename);
            BufferedReader in = new BufferedReader(f);
            while (in.ready() && tasks_no < Constant.max_subtask_per_instance) {

                String line = in.readLine();
                String[] parts = line.split(",");
                double lat = Double.parseDouble(parts[0]);
                double lng = Double.parseDouble(parts[1]);
                int group = generator.nextInt(Constant.Gowalla_groups);
                //omit the following if to get back to normal version
                // the if statement is to restraint number of input for the sake of simplicity in testing
                //   if (group == 21 || group == 25 || group == 39 || group == 133 || group == 98) {
                Task t = new Task(lat, lng);
                if (Tasks.containsKey(group)) {
                    int temp_index = Tasks.get(group).size();
                    Tasks.get(group).put(temp_index, t);
                } else {
                    Hashtable<Integer, Task> temp_ht = new Hashtable<>();
                    temp_ht.put(0, t);
                    Tasks.put(group, temp_ht);
                }
                tasks_no++;
                //}
            }
            total_subtask = tasks_no;
            total_ComplexTask = Tasks.size();
            System.out.println(Tasks.size());
            Iterator complextask = Tasks.keySet().iterator();
            while (complextask.hasNext()) {
                int group = Integer.parseInt(complextask.next().toString());
                int s = Tasks.get(group).size();
                if (s > Max_SubTask_No) {
                    Max_SubTask_No = s;
                }
                if (s < Min_SubTask_No) {
                    Min_SubTask_No = s;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="PRINT OUT STATISTICS OF THE INPUT">
    public static void print_input_statistic() {
        StringBuilder sb = new StringBuilder();
        sb.append("#Complex Tasks (CT): " + total_ComplexTask + "\n");
        sb.append("#SubTasks (ST): " + total_subtask + "\n");
        sb.append("#Worker: " + total_Worker + "\n");
        sb.append("MAX ST/CT: " + Max_SubTask_No + "\n");
        sb.append("MIN ST/CT: " + Min_SubTask_No + "\n");
        /* sb.append("Minimum subtasks of a complex task: " + Min_SubTask_No + "\n\n");

        
         sb.append("Total node: " + total_node + "\n\n");

         sb.append("New Source index: 0" + "\n");
         sb.append("Old Source index: 1" + "\n");
         sb.append("Complex Task index: 2->" + (total_ComplexTask + 1) + "\n");
         sb.append("Sub Task index: " + offset_Task + "->" + (offset_Worker - 1) + "\n");
         sb.append("Worker index: " + offset_Worker + "->" + (total_node - 3) + "\n");
         sb.append("Old Sink index: " + (total_node - 2) + "\n");
         sb.append("New Sink index: " + (total_node - 1) + "\n");*/

        System.out.println(sb.toString());

    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="PRINT OUT CAPACITY MATRIX">
    public static void print_capacity_matrix() {
        for (int i = 0; i < cap.length; i++) {

            if (i == 0) {
                System.out.println("New Source");
            }
            if (i == 1) {
                System.out.println("Old Source");
            }
            if (i == 2) {
                System.out.println("ComplexTask");
            }
            if (i == offset_Task) {
                System.out.println("Sub Task");
            }
            if (i == offset_Worker) {
                System.out.println("Worker");
            }
            if (i == total_node - 2) {
                System.out.println("Old Sink");
            }
            if (i == total_node - 1) {
                System.out.println("New Sink");
            }
            for (int j = 0; j < cap.length; j++) {


                System.out.print(cap[i][j] + "\t\t");
            }
            System.out.println();
        }

    }
// </editor-fold>

    private static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
