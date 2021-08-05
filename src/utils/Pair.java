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

}
