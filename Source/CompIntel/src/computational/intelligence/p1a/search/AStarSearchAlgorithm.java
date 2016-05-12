package computational.intelligence.p1a.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * This algorithm is based on the pseudocode which is given at added link
 *
 * @link https://en.wikipedia.org/wiki/A*_search_algorithm
 *
 * @author anıl öztürk
 * @author asım zorlu
 */
public class AStarSearchAlgorithm {

  /**
   * This inner class is required for getting to Node which has minimum fScore value in the
   * PriorityQueue
   *
   * @author anıl öztürk
   * @author asım zorlu
   *
   */
  private class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
      if (AStarSearchAlgorithm.this.fScore.get(o1) > AStarSearchAlgorithm.this.fScore.get(o2)) {
        return 1;
      } else if (AStarSearchAlgorithm.this.fScore.get(o1) < AStarSearchAlgorithm.this.fScore
          .get(o2)) {
        return -1;
      } else {
        return 0;
      }
    }
  }

  private final Node start;

  private final Node end;

  private final Set<Node> closedSet = new HashSet<Node>();

  private final Queue<Node> openSet = new PriorityQueue<Node>(new NodeComparator());

  private final Map<Node, Node> cameFrom = new HashMap<Node, Node>();

  private final Map<Node, Integer> gScore = new HashMap<Node, Integer>();

  private final Map<Node, Integer> fScore = new HashMap<Node, Integer>();

  private final int[] passingTimes; // this array holds the crossing times on river of every people

  private final List<Node> path;

  public AStarSearchAlgorithm(int[] times, Node start, Node end) throws Exception {
    if (times == null || start == null || end == null) {
      throw new NullPointerException("Times//Start//End cannot be null");
    }
    this.passingTimes = times;
    this.start = start;
    this.end = end;

    this.openSet.add(this.start);

    this.gScore.put(start, 0);
    this.fScore.put(start, heuristicEstimate(start));

    this.path = run();
  }

  /**
   * This return the weight of edge that is between the given nodes.
   *
   * @param current
   * @param neighbour
   * @return
   */
  private Integer dist_between(Node current, Node neighbour) {
    final boolean[] cID = current.getId();
    final boolean[] nID = neighbour.getId();
    int max = 0;

    for (int i = 0; i < cID.length; i++) {
      if (cID[i] != nID[i]) {
        max = max > this.passingTimes[i] ? max : this.passingTimes[i];
      }
    }
    return max;
  }

  private List<Node> getNeighbours(Node from) {
    final List<Node> neighbours = new ArrayList<Node>();
    Node temp = null;
    try {
      temp = from.clone();
    } catch (final CloneNotSupportedException e) {
      e.printStackTrace();
    }
    if (from.hasTorch()) { // if the torch is at left side
      if (from.numOfPeople() == 4) {
        for (int i = 0; i < from.getId().length - 1; i++) {
          while (i < from.getId().length - 1 && !temp.getId()[i]) {
            i++;
          }

          if (i == from.getId().length - 1) { // if we passed the limit stop here.
            return neighbours;
          }

          for (int j = i + 1; j < from.getId().length; j++) {
            while (j < from.getId().length && !temp.getId()[j]) {
              j++;
            }

            if (j == from.getId().length) { // if we passed the limit stop here.
              break;
            }

            temp.getId()[i] = false;
            temp.getId()[j] = false;
            temp.setTorch(!from.hasTorch());
            if (this.fScore.get(temp) == null) { // for initializing
              this.fScore.put(temp, Integer.MAX_VALUE);
            }
            neighbours.add(temp);
            try {
              temp = from.clone();
            } catch (final CloneNotSupportedException e) {
              e.printStackTrace();
            }
          }
        }
      } else {
        for (int i = 0; i < from.getId().length; i++) {
          while (i < from.getId().length && !temp.getId()[i]) {
            i++;
          }

          if (i == from.getId().length) {
            return neighbours;
          }

          temp.getId()[i] = false;
          temp.setTorch(!from.hasTorch());
          if (this.fScore.get(temp) == null) {
            this.fScore.put(temp, Integer.MAX_VALUE);
          }
          neighbours.add(temp);
          try {
            temp = from.clone();
          } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
          }

          for (int j = i + 1; j < from.getId().length; j++) {
            while (j < from.getId().length && !temp.getId()[j]) {
              j++;
            }

            if (j == from.getId().length) {
              break;
            }

            temp.getId()[i] = false;
            temp.getId()[j] = false;
            temp.setTorch(!from.hasTorch());
            if (this.fScore.get(temp) == null) {
              this.fScore.put(temp, Integer.MAX_VALUE);
            }
            neighbours.add(temp);
            try {
              temp = from.clone();
            } catch (final CloneNotSupportedException e) {
              e.printStackTrace();
            }
          }
        }
      }
    } else { // if the torch is at right side
      for (int i = 0; i < from.getId().length; i++) {
        while (i < from.getId().length && temp.getId()[i]) {
          i++;
        }

        if (i == from.getId().length) {
          return neighbours;
        }

        temp.getId()[i] = true;
        temp.setTorch(!from.hasTorch());
        if (this.fScore.get(temp) == null) {
          this.fScore.put(temp, Integer.MAX_VALUE);
        }
        neighbours.add(temp);

        try {
          temp = from.clone();
        } catch (final CloneNotSupportedException e) {
          e.printStackTrace();
        }

        for (int j = i + 1; j < from.getId().length; j++) {
          while (j < from.getId().length && temp.getId()[j]) {
            j++;
          }

          if (j == from.getId().length) {
            break;
          }

          temp.getId()[i] = true;
          temp.getId()[j] = true;
          temp.setTorch(!from.hasTorch());
          if (this.fScore.get(temp) == null) {
            this.fScore.put(temp, Integer.MAX_VALUE);
          }
          neighbours.add(temp);

          try {
            temp = from.clone();
          } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return neighbours;
  }

  /**
   * This method computes the estimated time left for reach the end node. The idea is take the
   * quicker and slower person from the left and go across. When going back take the quicker person
   * and go across. This will continue until there is no people left on the left side of river.
   *
   * @param node
   * @return
   */
  private int heuristicEstimate(Node node) {
    Node temp = null;
    try {
      temp = node.clone();
    } catch (final CloneNotSupportedException e) {
      e.printStackTrace();
    }
    int timeEstimate = 0;
    int remaining = temp.numOfPeople();
    boolean torch = temp.hasTorch();
    while (remaining != 0) {
      int max = Integer.MIN_VALUE;
      int maxIndex = max;
      int min = Integer.MAX_VALUE;
      int minIndex = min;
      if (torch) {
        for (int i = 0; i < temp.getId().length; i++) {
          if (temp.getId()[i]) {
            if (max < this.passingTimes[i]) {
              max = this.passingTimes[i];
              maxIndex = i;
            }
            if (min > this.passingTimes[i]) {
              min = this.passingTimes[i];
              minIndex = i;
            }
          }
        }
        temp.getId()[maxIndex] = false;
        temp.getId()[minIndex] = false;
        timeEstimate += max;
        remaining -= 2;
        torch = !torch;
      } else {
        for (int i = 0; i < temp.getId().length; i++) {
          if (!temp.getId()[i]) {
            if (min > this.passingTimes[i]) {
              min = this.passingTimes[i];
              minIndex = i;
            }
          }
        }
        temp.getId()[minIndex] = true;
        timeEstimate += min;
        remaining += 1;
        torch = !torch;
      }
    }
    return timeEstimate;
  }

  public void printPath() {
    System.out.println(this.path.get(0).toString());
    for (int i = 1; i < this.path.size(); i++) {
      System.out.println(this.path.get(i).toString() + "--"
          + dist_between(this.path.get(i - 1), this.path.get(i)) + "min");
    }
  }

  /**
   * Draw the successful path and store it in totalPath.
   *
   * @param cameFrom
   * @param current
   * @return
   */
  private List<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
    final List<Node> totalPath = new ArrayList<>();
    totalPath.add(current);
    while (cameFrom.containsKey(current)) {
      current = cameFrom.get(current);
      totalPath.add(current);
    }
    Collections.reverse(totalPath);
    return totalPath;
  }

  /**
   * It is the main loop required for A* search algorithm. You can find detailed explanation at the
   * link which is given above
   *
   * @return
   * @throws Exception
   */
  private List<Node> run() throws Exception {
    while (!this.openSet.isEmpty()) {
      final Node current = this.openSet.peek();
      if (current.equals(this.end)) {
        return reconstructPath(this.cameFrom, this.end);
      }

      this.openSet.remove(current);
      this.closedSet.add(current);

      final List<Node> neighbours = getNeighbours(current);
      for (final Node neighbour : neighbours) {
        if (this.closedSet.contains(neighbour)) {
          continue;
        }

        final int tentative_gScore = this.gScore.get(current) + dist_between(current, neighbour);
        if (!this.openSet.contains(neighbour)) {
          this.openSet.add(neighbour);
        } else if (tentative_gScore >= this.gScore.get(neighbour)) {
          continue;
        }

        this.cameFrom.put(neighbour, current);
        this.gScore.put(neighbour, tentative_gScore);
        this.fScore.put(neighbour, this.gScore.get(neighbour) + heuristicEstimate(neighbour));
      }
    }
    throw new Exception("Algorithm cannot find the path");
  }
}
