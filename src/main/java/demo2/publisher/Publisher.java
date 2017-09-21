package demo2.publisher;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

public interface Publisher<Integer> extends Flow.Publisher<Integer> {

    void subscribe(List<Flow.Subscriber<? super Integer>> subscribers);

    void subscribe(Flow.Subscriber<? super Integer> subscriber);

    void close();

    CompletableFuture<List<Flow.Subscriber<? super java.lang.Integer>>> isClosing();

    Set<Flow.Subscriber<? super Integer>> getAllSubscriber();

    List<Flow.Subscriber<? super Integer>> getSubscribers();

    boolean isSubscribed(Flow.Subscriber<? super Integer> subscriber);

}
