package demo2;

import base.*;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Publisher extends SubmissionPublisher<Integer> {

    private static final String LOG_MESSAGE_FORMAT = "demo2.Publisher ----> [%s] %s%n";
    private final PublisherCore publisherCore;

    public static Publisher create(Executor executor,int corePoolSize, int maxBufferCapacity, long period, TimeUnit unit,int maxItemToPublish) {
        return new Publisher(executor,corePoolSize, maxBufferCapacity, period, unit, maxItemToPublish);
    }

    Publisher(Executor executor, int corePoolSize, int maxBufferCapacity, long period, TimeUnit unit, int maxItemToPublish) {
        super(executor, maxBufferCapacity);

        final AtomicInteger aInt = new AtomicInteger(0);

        publisherCore = PublisherCore.create(
                corePoolSize,
                period,
                unit,
                () -> {

                    Integer item = aInt.incrementAndGet();
                    Logger.printf(LOG_MESSAGE_FORMAT, "publishing item: " + item + " ...");

                    submit(item);

                    Logger.printf(LOG_MESSAGE_FORMAT, "estimateMaximumLag: " + super.estimateMaximumLag());
                    Logger.printf(LOG_MESSAGE_FORMAT, "estimateMinimumDemand: " + super.estimateMinimumDemand());

                    if (item == maxItemToPublish)
                        close();
                });
    }

    public void subscribe(List<Flow.Subscriber<? super Integer>> subscribers) {
        subscribers
            .stream()
            .forEach(it -> {
                super.subscribe(it);
            });
    }

    @Override
    public void subscribe(Flow.Subscriber<? super Integer> subscriber) {
        super.subscribe(subscriber);
    }

    public void close() {
        Logger.printf("shutting down...\n");

        getSubscribers().stream()
                        .filter(subscriber -> subscriber != null)
                        .forEach(subscriber -> {
                            Logger.printf(LOG_MESSAGE_FORMAT,"Subscriber " + ((Subscriber)subscriber).getName() + " isSubscribed(): " + isSubscribed(subscriber));
                        });

        publisherCore.close();

        super.close();

        Logger.printf("Finalize\n");

    }

}
