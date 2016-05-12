package computational.intelligence.p1a.search;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class Initializer {

  public static void main(String[] args) {

    final BridgeNTorchProblem problem = new BridgeNTorchProblem(produceConstraints());
    problem.solve();
  }

  public static SortedMap<Character, Integer> produceConstraints() {
    final SortedMap<Character, Integer> inputMap = new TreeMap<Character, Integer>();
    inputMap.put('A', 1);
    inputMap.put('B', 2);
    inputMap.put('C', 5);
    inputMap.put('D', 8);

    return Collections.unmodifiableSortedMap(inputMap);
  }
}
