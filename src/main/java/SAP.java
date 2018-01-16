import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kon on 10/1/2018.
 */
public class SAP {

    private Digraph dGraph;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        dGraph = new Digraph(G);
    }

    // length of shortest ancestral patjh between v and w; -1 if no such path
    public int length(int v, int w) {
        int minLengthVertex = ancestor(v, w);
        if (minLengthVertex == -1) {
            return minLengthVertex;
        }
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(dGraph, w);
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(dGraph, v);
        return wBfs.distTo(minLengthVertex) + vBfs.distTo(minLengthVertex);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(dGraph, v);
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(dGraph, w);
        int electedAncestor = calculateAncestor(vBfs, wBfs);
        return electedAncestor;
    }

    private int calculateAncestor(BreadthFirstDirectedPaths vBfs, BreadthFirstDirectedPaths wBfs) {
        List<Integer> commonAncestors = getCommonAncestors(vBfs, wBfs);
        if (commonAncestors == null || commonAncestors.isEmpty()) {
            return -1;
        }
        int electedAncestor = getElectedAncestor(vBfs, wBfs, commonAncestors);
        return electedAncestor;
    }

    private List<Integer> getCommonAncestors(BreadthFirstDirectedPaths vBfs, BreadthFirstDirectedPaths wBfs) {
        int totalVertices = dGraph.V();
        List<Integer> commons = new ArrayList<>();
        for (int currentVertex = 0; currentVertex < totalVertices - 1; currentVertex++) {
            if (vBfs.hasPathTo(currentVertex) && wBfs.hasPathTo(currentVertex)) {
                commons.add(currentVertex);
            }
        }
        return commons;
    }

    private int getElectedAncestor(BreadthFirstDirectedPaths vBfs, BreadthFirstDirectedPaths wBfs, List<Integer> commonAncestors) {
        int minimumCost = Integer.MAX_VALUE;
        int electedAncestor = 0;
        for (Integer commonAncestor : commonAncestors) {
            int distanceToCommonV = vBfs.distTo(commonAncestor);
            int distanceToCommonW = wBfs.distTo(commonAncestor);
            int distanceToCommon = distanceToCommonV;
            if (distanceToCommonW <= distanceToCommonV) {
                distanceToCommon = distanceToCommonW;
            }
            if(distanceToCommon < minimumCost) {
                minimumCost = distanceToCommon;
                electedAncestor = commonAncestor;
            }
        }
        return electedAncestor;
    }
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int minLengthVertex = ancestor(v, w);
        if (minLengthVertex == -1) {
            return minLengthVertex;
        }
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(dGraph, w);
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(dGraph, v);
        return wBfs.distTo(minLengthVertex) + vBfs.distTo(minLengthVertex);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(dGraph, v);
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(dGraph, w);
        int electedAncestor = calculateAncestor(vBfs, wBfs);
        return electedAncestor;
    }

    public static void main(String[] args) {

    }
}
