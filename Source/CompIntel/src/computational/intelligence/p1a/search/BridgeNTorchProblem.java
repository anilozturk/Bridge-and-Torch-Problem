package computational.intelligence.p1a.search;

import java.util.SortedMap;
import java.util.function.BiConsumer;

public class BridgeNTorchProblem {

  public final static int goalTime = 15;

  public final static int ASCII_A = 65;

  private int[] times;

  private Node start;

  private Node end;

  @SuppressWarnings("unused")
  private BridgeNTorchProblem() {}

  public BridgeNTorchProblem(SortedMap<Character, Integer> map) {
    this.times = new int[map.size()];
    map.forEach(new BiConsumer<Character, Integer>() {

      int i = 0;

      @Override
      public void accept(Character t, Integer u) {
        BridgeNTorchProblem.this.times[this.i] = u;
        count();
      }

      private void count() {
        this.i++;
      }
    });

    this.start = new Node(map.size(), new boolean[] {true, true, true, true}, true);
    this.end = new Node(map.size(), new boolean[] {false, false, false, false}, false);
  }

  public void solve() {
    try {
      final AStarSearchAlgorithm algorithm =
          new AStarSearchAlgorithm(this.times, this.start, this.end);
      algorithm.printPath();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
