/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package complextask_maxflow;

/**
 *
 * @author dkh
 */
public class Constant {

    public static String WorkerYELP = "D:\\YELP Data set\\complex task\\workers\\yelp_workers";
    public static String suffix = ".txt";
    public static String TaskYELP = "D:\\YELP Data set\\complex task\\tasks\\yelp_tasks";
    
    public static String TaskGOWALLA ="D:\\Gowalla\\task\\gowalla_tasks";
    public static String WorkerGOWALLA ="D:\\Gowalla\\worker\\gowalla_workers";
   
    public static int max_worker_per_instance = 25;
    public static int max_subtask_per_instance = 2000;
    public static int Gowalla_groups = 400;
    
    
    
    
    
    
    public static int Yelp_groups = 400;
    
    public static int avg_st = 3;
    
    public static int MaxT = 5;
    // the larger the group_values is, the smaller the MAX_ST/CT is
}
