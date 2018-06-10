package lesx.utils;

public class LesxPair<V, K> {
  private V first;
  private K second;

  public LesxPair(V first, K second) {
    this.first = first;
    this.second = second;
  }

  public V getFirst() {
    return first;
  }

  public K getSecond() {
    return second;
  }

}
