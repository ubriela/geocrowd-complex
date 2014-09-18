/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package complextask_maxflow;


/**
 *
 * @author dkh
 */
public class Worker {

    private String userID;
    private double lat;
    private double lng;
    private int maxTaskNo;
    private MBR mbr;
    
    public Worker(String id, double lt, double ln, MBR m ) {
		userID = id;
		lat = lt;
		lng = ln;
		maxTaskNo = 1;
		mbr = new MBR(m);
		
	}
	
	

	public Worker(String id, double lt, double ln, int maxT, MBR m) {
		userID = id;
		lat = lt;
		lng = ln;
		maxTaskNo = maxT;
		mbr = new MBR(m);
		
	}
	

	public int getMaxTaskNo() {
		return maxTaskNo;
	}

	public String getUserID() {
		return userID;
	}

	public void incMaxTaskNo() {
		maxTaskNo++;
	}

	public MBR getMBR() {
		return mbr;
	}

	public double getLatitude() {
		return lat;
	}

	public double getLongitude() {
		return lng;
	}
}
