package utils;

public class Pair<K, V> {

  private final K first;
  private final V second;

  public Pair(K first, V second) {
    this.first = first;
    this.second = second;
  }

  public K first() {
    return this.first;
  }

  public V second() {
    return this.second;
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Pair<?, ?>
        && ((Pair<?, ?>) other).first.equals(first)
        && ((Pair<?, ?>) other).second.equals(second);
  }

  @Override
  public int hashCode() {
    return first.hashCode() + second.hashCode();
  }

  @Override
  public String toString() {
    return String.format("%s$%s", second.toString(), first.toString());
  }
}
