/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package complextask_maxflow;

/**
 *
 * @author dkh
 */
public class Task {

    private double lat;
    private double lng;
    private int cost;
  

    public Task(double lt, double ln, int c) {
        lat = lt;
        lng = ln;
        cost = c;
       

    }

    public Task(double lt, double ln) {
        lat = lt;
        lng = ln;
        cost = 1;
        

    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
   

    public void print() {
        System.out.println("lat:" + lat + "   lng:" + lng + "   cost:" + cost);
    }

    public boolean isCoveredByMBR(MBR m) {
        if ((lat >= m.minLat) && (lat <= m.maxLat)
                && (lng >= m.minLng) && (lng <= m.maxLng)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCoveredByRange(double minLat_mbr, double minLng_mbr,
            double maxLat_mbr, double maxLng_mbr) {
        if ((lat >= minLat_mbr) && (lat <= maxLat_mbr) && (lng >= minLng_mbr)
                && (lng <= maxLng_mbr)) {
            return true;
        }
        return false;
    }
}
