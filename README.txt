*** This repository is the implementation of the following papers: ***

1) Hung Dang, Tuan Nguyen, and Hien To, Maximum Complex Task Assignment: Towards Tasks Correlation in Spatial Crowdsourcing, In Proceedings of International Conference on Information Integration and Web-based Applications and Services (IIWAS 2013), No 77, Pages 77-81, Vienna, Austria , 2-4 December 2013

Related studies:

https://github.com/ubriela/geocrowd-priv-dynamic

https://github.com/ubriela/geocrowd-priv

https://github.com/ubriela/geocrowd-priv-demo

--------------------------------------------------------------

ORIGINAL LIST OF NODE WOULD BE
 + source
 + complex task
 + sub task
 + worker
 + sink

--------------------------------------------------------------


TRANSFORMED MATRIX
 + new source
 + old source
 + complex task
 + sub task
 + worker
 + old sink
 + new sink

--------------------------------------------------------------

UPDATED EDGES BY THE TRANSFORMATION
 + New source -> complex task: Max_Sub_Task
 + Old Source -> new Sink: number of complex task * Max_Sub_Task
 + old sink to old source: infinity
 + complex task -> new sink: Max_Sub_Task - its # subtask


--------------------------------------------------------------

EDGES CONNECTING SUBTASK -> WORKER:
 Need a method to check whether a task is in MBR of the worker (minW_Lat < T_Lat < maxW_Lat && minW_Lng < T_Lng < maxW_Lng)
	 if   + true => set capacity = 1;
     		 + else => capacity = 0

// the method is implemented in Task class.

--------------------------------------------------------------
RETURNING NUMBER OF COMPLEXTASK AND SUBTASK ASSIGNED:
	+ #Complex Task: Total flow / Max_Sub_Task
 	+ #Sub Task: if we assgin COST of SubTask->Worker = 1, and all the other edges = 0, TotalCost = #SubTask

--------------------------------------------------------------
READING DATA FOR WORKER AND TASK
 + source: D:/YELP DATA SET/Worker - Task
 + Task Format: Lat-Lng, file index, location density, expertise
 + Worker Format: Id, Lat-Lng, MaxT, MBR, expertise set.

 + Processing: 
	- Task: for the simplest manner, we just pay attention to Lat-Lng and expertise. Expertise is used to group task into complex task
	- Worker: we only use Lat-Lng, MaxT and MBR

