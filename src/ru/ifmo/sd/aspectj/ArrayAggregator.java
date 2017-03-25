package ru.ifmo.sd.aspectj;

public class ArrayAggregator {
    private int size;
    private int[] container;
    private int[] prefixSum;

    public ArrayAggregator(int size) {
        this.size = size;
        this.container = new int[size];
        initRandom();
        this.prefixSum = new int[size];
        calcPrefixSums();
    }

    public static void runner(ArrayAggregator aggregator) {
        aggregator.naiveSumOnRange(0, aggregator.size);
        aggregator.sumOnRange(0, aggregator.size);
        aggregator.averageSum(100);
        aggregator.naiveRecursiveSum(0, 3);
    }

    public long naiveSumOnRange(int l, int r) {
        long result = 0;
        for(int i = l; i < r; i++) {
            result += container[i];
        }
        return result;
    }

    public long sumOnRange(int l, int r) {
        return (r >= 1 ? prefixSum[r - 1] : 0) - (l >= 1 ? prefixSum[l - 1] : 0);
    }

    public double averageSum(int cnt) {
        long result = 0;
        for(int i = 0; i < cnt; i++) {
            int l = (int)(Math.random() * size);
            int r = (int)(Math.random() * size);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            result += naiveSumOnRange(l, r);
        }
        return ((double) result) / cnt;
    }

    public long naiveRecursiveSum(int l, int r) {
        if (l >= r) {
            return 0;
        } else if (l + 1 == r) {
            return container[l];
        } else {
            return container[l] + naiveRecursiveSum(l + 1, r);
        }
    }

    // private methods

    private void initRandom() {
        for(int i = 0; i < size; i++) {
            container[i] = (int)(Math.random() * size);
        }
    }

    private void calcPrefixSums() {
        prefixSum[0] = container[0];
        for(int i = 1; i < size; i++) {
            prefixSum[i] = prefixSum[i - 1] + container[i];
        }
    }
}
