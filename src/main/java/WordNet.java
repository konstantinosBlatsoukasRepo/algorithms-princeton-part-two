import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kon on 14/1/2018.
 */
public class WordNet {

    private Map<Integer, Synset> synsetsObj = new HashMap<>();
    private Set<String> nouns = new HashSet<>();
    private Map<String, List<Integer>> wordSynsetIds = new HashMap<>();
    private Digraph wordNetGraph;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        readSynsets(synsets);
        createGraphFromTxt(hypernyms);
        nouns = extractWordNetNouns();
        sap = new SAP(wordNetGraph);

        DirectedCycle cycle = new DirectedCycle(wordNetGraph);
        if (cycle.hasCycle() || !isRootedDAG(wordNetGraph)) {
            throw new IllegalArgumentException("The input does not correspond to a rooted DAG!");
        }
    }

    private boolean isRootedDAG(Digraph g) {
        int roots = 0;
        for (int i = 0; i < g.V(); i++) {
            if (!g.adj(i).iterator().hasNext()) {
                roots++;
                if (roots > 1) {
                    return false;
                }
            }
        }
        return roots == 1;
    }

    private void readSynsets(String synsets) {
        In file = new In(synsets);
        while (file.hasNextLine()) {
            String[] values = file.readLine().split(",");

            int synsetId = Integer.parseInt(values[0]);
            String[] nouns = values[1].split(" ");
            String description = values[2];

            Synset currentSynset = new Synset();
            currentSynset.setNouns(Arrays.asList(nouns));
            currentSynset.setDescription(description);

            synsetsObj.put(synsetId, currentSynset);
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns;
    }

    private Set<String> extractWordNetNouns() {
        for (Map.Entry<Integer, Synset> entry : synsetsObj.entrySet()) {
            Integer synsetId = entry.getKey();
            List<String> currentNouns = entry.getValue().getNouns();
            for (String currentNoun : currentNouns) {
                nouns.add(currentNoun);
                List<Integer> synsetIds = new ArrayList<>();
                if(wordSynsetIds.containsKey(currentNoun)) {
                    synsetIds = wordSynsetIds.get(currentNoun);
                }
                synsetIds.add(synsetId);
                wordSynsetIds.put(currentNoun, synsetIds);
            }
        }
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nouns.contains(word);
    }

    private void createGraphFromTxt(String fileName) {
        wordNetGraph = new Digraph(synsetsObj.size());
        In file = new In(fileName);
        while (file.hasNextLine()) {
            String[] values = file.readLine().split(",");
            int from = Integer.parseInt(values[0]);
            for (int i = 0; i < values.length; i++) {
                int vertexId = Integer.parseInt(values[i]);
                if (vertexId != from) {
                    wordNetGraph.addEdge(from, vertexId);
                }
            }
        }
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        Iterable<Integer> nounAVertices = wordSynsetIds.get(nounA);
        Iterable<Integer> nounBVertices = wordSynsetIds.get(nounB);
        int distance = sap.length(nounAVertices, nounBVertices);
        return distance;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        Iterable<Integer> nounAVertices = wordSynsetIds.get(nounA);
        Iterable<Integer> nounBVertices = wordSynsetIds.get(nounB);
        int commonAncestorId = sap.ancestor(nounAVertices, nounBVertices);

        Synset synset = synsetsObj.get(commonAncestorId);
        List<String> synsetNouns = synset.getNouns();

        StringBuilder stringBuilder = new StringBuilder();
        for (String currentSynset : synsetNouns) {
            stringBuilder.append(currentSynset).append(" ");
        }

        String joinedString = stringBuilder.toString().toString();
        return joinedString.substring(0, joinedString.length() - 1);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
        int distance = wordNet.distance("Black_Plague", "black_marlin");
        System.out.println("distance = " + distance);
    }

    private class Synset {
        List<String> nouns = new ArrayList<>();
        String description;

        public List<String> getNouns() {
            return nouns;
        }

        public void setNouns(List<String> nouns) {
            this.nouns = nouns;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
