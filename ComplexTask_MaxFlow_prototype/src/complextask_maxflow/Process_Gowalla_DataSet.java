/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package complextask_maxflow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author dkh
 */
public class Process_Gowalla_DataSet {
    static ArrayList<Worker> Workers = new ArrayList<>();
    static ArrayList<Task> SubTasks = new ArrayList<>();
        static int total_ComplexTask = 0;
    static int total_subtask = 0;
    static int Max_SubTask_No = Integer.MIN_VALUE;
    static int Min_SubTask_No = Integer.MAX_VALUE;
    static Hashtable<Integer, Hashtable<Integer, Task>> Tasks = new Hashtable<>();
    
    public static void read_worker_gowalla(String filename) {

        try {
            FileReader f = new FileReader(filename);
            BufferedReader in = new BufferedReader(f);
            while (in.ready()) {

                String line = in.readLine();
                String[] parts = line.split(",");
                String id = parts[0];
                //  if (id.equals("T_CviPwQW0EnhISoBX3qpA") || id.equals("wEMn5U0mlTp3Td5n2ZgwKQ")
                //      || id.equals("V_NaUUcLx7e-hFRyGOsOBA") || id.equals("VIMwzph57BDFny0LAW_LGw")) {
                double lat = Double.parseDouble(parts[2]);
                double lng = Double.parseDouble(parts[3]);
                int maxT = Integer.parseInt(parts[4]);
                //String Min_Lat = parts[4].substring(1);
                double mbr_minLat = Double.parseDouble(parts[5].substring(1));
                double mbr_minLng = Double.parseDouble(parts[6]);
                double mbr_maxLat = Double.parseDouble(parts[7]);
                double mbr_maxLng = Double.parseDouble(parts[8].substring(0, parts[7].length() - 2));
                MBR mbr = new MBR(mbr_minLat, mbr_minLng, mbr_maxLat,
                        mbr_maxLng);
                Worker w = new Worker(id, lat, lng, maxT, mbr);
                Workers.add(w);
                
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
            while (in.ready()) {

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
     
}
