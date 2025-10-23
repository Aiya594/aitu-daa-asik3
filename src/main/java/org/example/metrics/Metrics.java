package org.example.metrics;

public class Metrics {
    private long comparisons = 0;
    private long heapOpers = 0; //priority queue operations
    private long unions = 0;
    private long finds = 0;
    private long edgeAdditions = 0;

    public void incComparisons() { comparisons++; }
    public void addComparisons(long k) { comparisons += k; }
    public void incHeapOpers() { heapOpers++; }
    public void incUnions() { unions++; }
    public void incFinds() { finds++; }
    public void incEdgeAdditions() { edgeAdditions++; }

    public long totalOpers() {
        return comparisons + heapOpers + unions + finds + edgeAdditions;
    }

    @Override
    public String toString() {
        return String.format("comparisons=%d heapOperations=%d unions=%d finds=%d edgeAdds=%d totalOperations=%d",
                comparisons, heapOpers, unions, finds, edgeAdditions, totalOpers());
    }

}
