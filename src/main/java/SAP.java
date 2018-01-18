import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kon on 10/1/2018.
 */
public class SAP {

    private Digraph dGraph;
    private Digraph biDirectional;

    public SAP(Digraph G) {
        dGraph = new Digraph(G);
        biDirectional = createBiDirectionalGraph(dGraph);
    }

    public int length(int v, int w) {
        int minLengthVertex = ancestor(v, w);
        if (minLengthVertex == -1) {
            return minLengthVertex;
        } else if (v == w) {
            return 0;
        }
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(biDirectional, w);
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(biDirectional, v);
        //TODO: calculate common paths
        return wBfs.distTo(minLengthVertex) + vBfs.distTo(minLengthVertex);
    }

    public int ancestor(int v, int w) {
        int electedAncestor = v;
        if (v != w) {
            BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(dGraph, v);
            BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(dGraph, w);
            electedAncestor = calculateAncestor(vBfs, wBfs);
        }
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
            int distanceToCommon = distanceToCommonV + distanceToCommonW;
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

    private int calculateTotalCommonPaths(BreadthFirstDirectedPaths vBfs, BreadthFirstDirectedPaths wBfs, int commonAncestor) {
        int totalCommonPaths = 0;
        Iterable<Integer> vPathToAncestor = vBfs.pathTo(commonAncestor);
        Iterable<Integer> wPathToAncestor = wBfs.pathTo(commonAncestor);

        int vSize = getIterableSize(vPathToAncestor);
        int wSize = getIterableSize(wPathToAncestor);



        return totalCommonPaths * 2;
    }

    private int getIterableSize(Iterable<Integer> pathToAncestor) {
        int totalVertices = 0;
        for (Integer currentVertex : pathToAncestor) {
            totalVertices++;
        }
        return totalVertices;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int minLengthVertex = ancestor(v, w);
        if (minLengthVertex == -1) {
            return minLengthVertex;
        }
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(biDirectional, w);
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(biDirectional, v);
        return wBfs.distTo(minLengthVertex) + vBfs.distTo(minLengthVertex);
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(dGraph, v);
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(dGraph, w);
        int electedAncestor = calculateAncestor(vBfs, wBfs);
        return electedAncestor;
    }

    private Digraph createBiDirectionalGraph(Digraph digraph) {
        Digraph resultedGraph = new Digraph(digraph);
        Digraph reversedGraph = digraph.reverse();
        for (int currentVertex = 0; currentVertex < resultedGraph.V() - 1; currentVertex++) {
            Iterable<Integer> reversedVertices = reversedGraph.adj(currentVertex);
            for (Integer adjacentVertex : reversedVertices) {
                resultedGraph.addEdge(currentVertex, adjacentVertex);
            }
        }
        return resultedGraph;
    }

    public static void main(String[] args) {
        In in = new In("digraph2.txt");
        Digraph digraph = new Digraph(in);
        SAP sap = new SAP(digraph);

        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(digraph, 3);

        Iterable<Integer> path = wBfs.pathTo(0);
        for (Integer vertexId : path) {
            System.out.println(vertexId);
        }
    }
}
