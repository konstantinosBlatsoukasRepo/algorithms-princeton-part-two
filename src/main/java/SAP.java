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
    private static final int ROOT_INDEX = 38003;
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
        Iterable<Integer> vPathToRoot = calculatePathToRoot(vBfs);
        if (vPathToRoot == null || !vPathToRoot.iterator().hasNext()) {
            return -1;
        }

        Iterable<Integer> wPathToRoot = calculatePathToRoot(wBfs);
        if (wPathToRoot == null || !wPathToRoot.iterator().hasNext()) {
            return -1;
        }

        List<Integer> commonAncestors = getCommonAncestors(vPathToRoot, wPathToRoot);
        if (commonAncestors == null || commonAncestors.isEmpty()) {
            return -1;
        }

        int electedAncestor = getElectedAncestor(vBfs, commonAncestors);
        return electedAncestor;
    }

    private Iterable<Integer> calculatePathToRoot(BreadthFirstDirectedPaths bfs) {
        Iterable<Integer> pathToRoot = null;
        if (bfs.hasPathTo(ROOT_INDEX)) {
            pathToRoot = bfs.pathTo(ROOT_INDEX);
        }
        return pathToRoot;
    }

    private List<Integer> getCommonAncestors(Iterable<Integer> vPathToRoot, Iterable<Integer> wPathToRoot) {
        List<Integer> vConvertedPath = new ArrayList<>();
        for (Integer synsetIdV : vPathToRoot) {
            vConvertedPath.add(synsetIdV);
        }

        List<Integer> wConvertedPath = new ArrayList<>();
        for (Integer synsetIdW : wPathToRoot) {
            wConvertedPath.add(synsetIdW);
        }

        Set<Integer> commonAncestors = new HashSet<>();
        for (Integer synsetIdW : wPathToRoot) {
            for (Integer synsetIdV : vPathToRoot) {
                if (synsetIdW.equals(synsetIdV)) {
                    commonAncestors.add(synsetIdV);
                }
            }
        }

        List<Integer> commons = new ArrayList<>();
        commons.addAll(commonAncestors);
        return commons;
    }

    private int getElectedAncestor(BreadthFirstDirectedPaths vBfs, List<Integer> commonAncestors) {
        int minimumCost = Integer.MAX_VALUE;
        int electedAncestor = 0;
        for (Integer commonAncestor : commonAncestors) {
            int distanceToCommon = vBfs.distTo(commonAncestor);
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
