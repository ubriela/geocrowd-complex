/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package complextask_maxflow;

/**
 *http://web.mit.edu/~ecprice/acm/acm08/MaxFlow.java
 * 
 * @author dkh
 */
//Fattest path network flow algorithm using an adjacency matrix.
//
// Running time: O(|E|^2 log (|V| * U), where U is the largest
//               capacity of any edge.  If you replace the 'fattest
//               path' search with a minimum number of edges search,
//               the running time becomes O(|E|^2 |V|).
//
// INPUT: cap -- a matrix such that cap[i][j] is the capacity of
//               a directed edge from node i to node j
//
//               * Note that it is legitimate to create an i->j
//                 edge without a corresponding j->i edge.
//
//               * Note that for an undirected edge, set
//                 both cap[i][j] and cap[j][i] to the capacity of
//                 the undirected edge.
//
//        source -- starting node
//        sink -- ending node
//
// OUTPUT: value of maximum flow; also, the flow[][] matrix will
//         contain both positive and negative integers -- if you
//         want the actual flow assignments, look at the
//         *positive* flow values only.
//
// To use this, create a MaxFlow object, and call it like this:
//
//   MaxFlow nf;
//   int maxflow = nf.getMaxFlow(cap,source,sink);

import java.util.*;

public class MaxFlow {
    boolean found[];
    int N, cap[][], flow[][], dad[], dist[];

    boolean searchFattest(int source, int sink) {
	Arrays.fill(found, false);
	Arrays.fill(dist, 0);
	dist[source] = Integer.MAX_VALUE / 2;
        while (source != N) {
            int best = N;
            found[source] = true;
            if (source == sink) break;
            for (int k = 0; k < N; k++) {
		if (found[k]) continue;
		int possible = Math.min(cap[source][k] - flow[source][k], dist[source]);
		if (dist[k] < possible) {
		    dist[k] = possible;
		    dad[k] = source; 
		}
                if (dist[k] > dist[best]) best = k;
	    }
            source = best;
        }
        return found[sink];
    }

    boolean searchShortest(int source, int sink) {
	Arrays.fill(found, false);
	Arrays.fill(dist, Integer.MAX_VALUE/2);
	dist[source] = 0;
        while (source != N) {
            int best = N;
            found[source] = true;
            if (source == sink) break;
            for (int k = 0; k < N; k++) {
		if (found[k]) continue;
                if (cap[source][k] - flow[source][k] > 0) {
                    if (dist[k] > dist[source] + 1){
                        dist[k] = dist[source] + 1;
                        dad[k] = source;
                    }
                }
                if (dist[k] < dist[best]) best = k;
	    }
            source = best;
        }
        return found[sink];
    }

    public int[] getMaxFlow(int cap[][],int cost[][], int source, int sink) {
        this.cap = cap;
        N = cap.length;
        found = new boolean[N];
        flow = new int[N][N];
        dist = new int[N+1];
        dad = new int[N];
    
        int totflow = 0;
        int totcost = 0;
        while (searchFattest(source, sink)) {
            int amt = Integer.MAX_VALUE;
            for (int x = sink; x != source; x = dad[x])
                amt = Math.min(amt, cap[dad[x]][x] - flow[dad[x]][x]);
            for (int x = sink; x != source; x = dad[x]) {
                flow[dad[x]][x] += amt;
                flow[x][dad[x]] -= amt;
                totcost += amt*cost[dad[x]][x];
            }
            totflow += amt;
            
        }

        return new int[]{ totflow, totcost};
    }
}
