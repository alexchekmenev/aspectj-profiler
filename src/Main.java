import ru.ifmo.sd.aspectj.ArrayAggregator;

public class Main {

    public static final int SIZE = 10_000_000;

    public static void main(String[] args) {
        ArrayAggregator aggregator = new ArrayAggregator(SIZE);
        ArrayAggregator.runner(aggregator);
    }
}
