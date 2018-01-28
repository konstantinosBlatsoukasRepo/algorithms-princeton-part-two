package wordnet;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kon on 10/1/2018.
 */
public class SAP {

    private final Digraph dGraph;

    public SAP(Digraph G) {
        dGraph = new Digraph(G);
    }

    public int length(int v, int w) {
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(dGraph, w);
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(dGraph, v);

        List<Integer> commonVertices = getCommonVertices(wBfs, vBfs);
        if (commonVertices.isEmpty()) {
            return -1;
        }

        int minLengthVertex = getBestVertex(wBfs, vBfs, commonVertices);
        return wBfs.distTo(minLengthVertex) + vBfs.distTo(minLengthVertex);
    }

    public int ancestor(int v, int w) {
        int bestAncestor = v;
        if (v != w) {
            BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(dGraph, v);
            BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(dGraph, w);
            bestAncestor = calculateAncestor(vBfs, wBfs);
        }
        return bestAncestor;
    }

    private int calculateAncestor(BreadthFirstDirectedPaths vBfs, BreadthFirstDirectedPaths wBfs) {
        List<Integer> commonAncestors = getCommonVertices(vBfs, wBfs);
        if (commonAncestors == null || commonAncestors.isEmpty()) {
            return -1;
        }
        int bestVertex = getBestVertex(vBfs, wBfs, commonAncestors);
        return bestVertex;
    }

    private List<Integer> getCommonVertices(BreadthFirstDirectedPaths vBfs, BreadthFirstDirectedPaths wBfs) {
        int totalVertices = dGraph.V();
        List<Integer> commons = new ArrayList<>();
        for (int currentVertex = 0; currentVertex < totalVertices; currentVertex++) {
            if (vBfs.hasPathTo(currentVertex) && wBfs.hasPathTo(currentVertex)) {
                commons.add(currentVertex);
            }
        }
        return commons;
    }

    private int getBestVertex(BreadthFirstDirectedPaths vBfs, BreadthFirstDirectedPaths wBfs, List<Integer> commonVertices) {
        int minimumCost = Integer.MAX_VALUE;
        int bestVertex = 0;
        for (Integer commonVertex : commonVertices) {
            int distanceToCommonV = vBfs.distTo(commonVertex);
            int distanceToCommonW = wBfs.distTo(commonVertex);
            int distanceToCommon = distanceToCommonV + distanceToCommonW;
            if(distanceToCommon < minimumCost) {
                minimumCost = distanceToCommon;
                bestVertex = commonVertex;
            }
        }
        return bestVertex;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(dGraph, w);
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(dGraph, v);

        List<Integer> commonVertices = getCommonVertices(wBfs, vBfs);
        if (commonVertices.isEmpty()) {
            return -1;
        }

        int minLengthVertex = getBestVertex(wBfs, vBfs, commonVertices);
        return wBfs.distTo(minLengthVertex) + vBfs.distTo(minLengthVertex);
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(dGraph, v);
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(dGraph, w);
        int electedAncestor = calculateAncestor(vBfs, wBfs);
        return electedAncestor;
    }

    public static void main(String[] args) {
        In in = new In("digraph1.txt");
        Digraph digraph = new Digraph(in);
        SAP sap = new SAP(digraph);
        System.out.println(sap.length(3, 11));
        System.out.println(sap.ancestor(3, 11));
    }
}
