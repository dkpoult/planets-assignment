package za.co.dinoko.assignment.davidPoulton.shared;

public class SortablePair<T extends Comparable<T>, U> implements Comparable<SortablePair<T, U>> {

    private T key;
    private U value;

    public SortablePair(T key, U value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public U getValue() {
        return value;
    }

    public void setValue(U value) {
        this.value = value;
    }

    @Override
    public int compareTo(SortablePair<T, U> o) {
        return this.key.compareTo(o.key);
    }
}
