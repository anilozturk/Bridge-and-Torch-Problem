package computational.intelligence.p1a.search;

import java.security.InvalidParameterException;

/**
 * This class hold the instant state for problem.
 *
 * @author anıl öztürk
 * @author asım zorlu
 */
public class Node {

  private final boolean[] id;

  private boolean torch;

  public Node(int size, boolean[] id, boolean side) {
    if (id.length != size) {
      throw new InvalidParameterException("id size is not matching with people number");
    }
    this.id = id;
    this.torch = side;
  }

  @Override
  protected Node clone() throws CloneNotSupportedException {
    final boolean[] cloneID = new boolean[this.id.length];
    for (int i = 0; i < cloneID.length; i++) {
      cloneID[i] = this.id[i];
    }

    return new Node(cloneID.length, cloneID, hasTorch());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Node) {
      if (this.torch != ((Node) obj).hasTorch()) {
        return false;
      }

      for (int i = 0; i < this.id.length; i++) {
        if (this.id[i] != ((Node) obj).getId()[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public boolean[] getId() {
    return this.id;
  }

  @Override
  public int hashCode() {
    int result = 1;
    final int c = 1;

    result += c * (this.torch ? 1 : 2);
    for (int i = 0; i < this.id.length; i++) {
      result += c * (this.id[i] ? i * 7 : i * 3);
    }
    return result * 37 + result;
  }

  public boolean hasTorch() {
    return this.torch;
  }

  public int numOfPeople() {
    int count = 0;
    for (int i = 0; i < this.id.length; i++) {
      if (this.id[i]) {
        count++;
      }
    }
    return count;
  }

  public void setTorch(boolean side) {
    this.torch = side;
  }

  @Override
  public String toString() {
    String res = "";
    for (int i = 0; i < this.id.length; i++) {
      if (this.id[i]) {
        res += (char) (BridgeNTorchProblem.ASCII_A + i);
      }
    }

    if (res.length() == 0) {
      return "GOAL";
    }
    return res;
  }
}
