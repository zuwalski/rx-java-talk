package batch;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import rx.Observable;
import rx.observables.ConnectableObservable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SparePartRelationMatrix {
    private final Random rnd = new Random();

    static class SparePartFrq implements Comparable<SparePartFrq> {
        final int partId;
        final int frq;

        public SparePartFrq(int partId, int frq) {
            this.partId = partId;
            this.frq = frq;
        }

        @Override
        public int compareTo(SparePartFrq o) {
            return Integer.compare(o.frq, frq);
        }

        @Override
        public String toString() {
            return "{partId=" + partId +
                    ", frq=" + frq +
                    '}';
        }
    }

    List<SparePartFrq> whatsRelated(long partId) {
        return IntStream.range(0, 5).mapToObj(i -> new SparePartFrq(rnd.nextInt(10000), rnd.nextInt(1000)))
                .sorted().collect(Collectors.toList());
    }

    @Test
    public void testWhatsRelated() {
        System.out.println(whatsRelated(1));
        System.out.println(whatsRelated(1));
        System.out.println(whatsRelated(1));
    }

    @Test
    public void whatsRelatedToMyBasket() {
        Observable<Integer> basketPartIds = Observable.just(2323, 232, 299, 477, 888);

        basketPartIds.map(this::whatsRelated)
                .flatMap(Observable::from)
                .groupBy(frq -> frq.partId)
                .flatMap(frqMap ->
                        frqMap.reduce(0, (a, b) -> a + b.frq)
                                .map(sum -> new SparePartFrq(frqMap.getKey(), sum))
                )
                .sorted()
                .take(10)
                .subscribe(System.out::println);
    }

    static class OrderLine {
        final int orderId;
        final int partId;
        final int qty;

        public OrderLine(int orderId, int partId, int qty) {
            this.orderId = orderId;
            this.partId = partId;
            this.qty = qty;
        }

        @Override
        public String toString() {
            return "{orderId=" + orderId +
                    ", partId=" + partId +
                    ", qty=" + qty +
                    '}';
        }
    }

    Observable<OrderLine> jdbcOrderLineCursor(int cnt) {
        return Observable.range(1, cnt)
                .flatMap(orderId ->
                        Observable.range(1, rnd.nextInt(5))
                                .map(i -> new OrderLine(Long.valueOf(orderId).intValue(), rnd.nextInt(10), 1 + rnd.nextInt(6)))
                );
    }

    @Test
    public void testjdbcOrderLineCursor() {
        jdbcOrderLineCursor(3)
                .subscribe(System.out::println);
    }

    @Test
    public void calcPartRelationMatrix() {
        ConnectableObservable<OrderLine> orderLines = jdbcOrderLineCursor(20).publish();

        orderLines.buffer(orderLines.distinct(orderLine -> orderLine.orderId))
                .flatMap(this::cartesian)
                .groupBy(pair -> pair.getLeft())
                .flatMap(partsInGroup ->
                        partsInGroup.groupBy(part2 -> part2.getRight())
                                .flatMap(part2group ->
                                        part2group.count().map(c -> new SparePartFrq(part2group.getKey(), c))
                                )
                                .sorted()
                                .take(5)
                                .toList()
                                .map(top5 -> Pair.of(partsInGroup.getKey(), top5))
                )
                .subscribe(System.out::println);

        orderLines.connect();
    }

    Observable<Pair<Integer, Integer>> cartesian(List<OrderLine> ls) {
        return Observable.create(s -> {
            s.onStart();
            for (OrderLine l1 : ls) {
                for (OrderLine l2 : ls) {
                    s.onNext(Pair.of(l1.partId, l2.partId));
                }
            }
            s.onCompleted();
        });
    }

    @Test
    public void testCartesian() {
        cartesian(Arrays.asList(new OrderLine(1, 1, 0), new OrderLine(1, 2, 0), new OrderLine(1, 3, 0)))
                .subscribe(System.out::println);
    }
}
