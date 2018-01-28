package wordnet;

/**
 * Created by k.blatsoukas on 1/15/2018.
 */
public class Outcast {

  private WordNet wordnet;

  public Outcast(WordNet wordnet) {
    this.wordnet = wordnet;
  }

  public String outcast(String[] nouns) {
    String maxItem = null;
    int outcastDistance = 0;
    for (int i = 0; i < nouns.length; i++) {
      String outerNoun = nouns[i];
      int newMax = 0;

      for (int j = 0; j < nouns.length; j++) {
        String innerNoun = nouns[j];

        if (i != j) {
          newMax += wordnet.distance(outerNoun, innerNoun);
        }

        if (newMax > outcastDistance) {
          maxItem = outerNoun;
        }

      }
    }
    return maxItem;
  }

  public static void main(String[] args) {
    WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
    Outcast outcast = new Outcast(wordNet);

    String[] nouns = new String[]{"horse", "zebra", "cat", "bear", "table"};
    String outcome = outcast.outcast(nouns);
    System.out.println("outcome = " + outcome);

    String[] nounsSec = "water soda bed orange_juice milk apple_juice tea coffee".split(" ");
    String outcomeSec = outcast.outcast(nounsSec);
    System.out.println("outcomeSec = " + outcomeSec);

    String[] nounsThird = "apple pear peach banana lime lemon blueberry strawberry mango watermelon potato".split(" ");
    String outcomeThird = outcast.outcast(nounsThird);
    System.out.println("outcomeThird = " + outcomeThird);

  }
}