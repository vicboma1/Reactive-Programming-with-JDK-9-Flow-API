package demo1;

import java.util.concurrent.*;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.Executors.newSingleThreadExecutor;


/**
 * Created by vicboma on 19/09/17.
 */
public class Processor implements Flow.Processor<Integer, String> {

    private static final String LOG_MESSAGE_FORMAT = "demo1.Processor --> [%s] %s%n";

    public static Processor create(ExecutorService executor, long iterations) {
            return new Processor(executor, iterations);
        }

        private Finalizer finalizer;
        private Subscription publisherSubscription;
        private SubscriptionProcess subscription;
        private long iterations;
        private ConcurrentLinkedQueue<String> dataItems;
        private ExecutorService executor;

        public Processor(ExecutorService executor, long iterations) {
            this.iterations = iterations;
            dataItems = new ConcurrentLinkedQueue();
            this.executor = executor;
            this.finalizer = Finalizer.create();
        }

        @Override
        public void subscribe(Subscriber<? super String> subscriber) {
            subscription = new SubscriptionProcess(subscriber, executor);
            subscriber.onSubscribe(subscription);
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            Logger.printf(LOG_MESSAGE_FORMAT,"Subscribed...");
            publisherSubscription = subscription;
            requestItems();
        }

        private void requestItems() {
            Logger.printf(LOG_MESSAGE_FORMAT,"Requesting %d new items...", iterations);
            publisherSubscription.request(iterations);
        }

        @Override
        public void onNext(Integer item) {
            if (null == item)
                throw new NullPointerException();

            dataItems.add("item value = " + item * 10 + " after processing");
            Logger.printf(LOG_MESSAGE_FORMAT,"processing item: [" + item + "] ...");
        }

        @Override
        public void onComplete() {
            Logger.printf(LOG_MESSAGE_FORMAT,"Complete!");
        }

        @Override
        public void onError(Throwable t) {
            Logger.printf(LOG_MESSAGE_FORMAT,"Error >> %s", t);
        }


        private class SubscriptionProcess implements Subscription {

            private final ExecutorService executor;

            private Subscriber<? super String> subscriber;

            private AtomicBoolean isCanceled;

            public SubscriptionProcess(Subscriber<? super String> subscriber, ExecutorService executor) {
                this.executor = executor;
                this.subscriber = subscriber;
                isCanceled = new AtomicBoolean(false);
            }

            @Override
            public void request(long n) {
                if (isCanceled.get())
                    return;

                if (n < 0)
                    executor.execute(() -> subscriber.onError(new IllegalArgumentException()));
                else if (dataItems.size() > 0)
                    publishItems(n);
                else if (dataItems.size() == 0) {
                    subscriber.onComplete();
                }
            }

            private void publishItems(long n) {

                int remainItems = dataItems.size();

                if ((remainItems == n) || (remainItems > n)) {

                    for (int i = 0; i < n; i++) {
                        executor.execute(() -> {
                            subscriber.onNext(dataItems.poll());
                        });
                    }
                    Logger.printf(LOG_MESSAGE_FORMAT,"Remaining " + (dataItems.size() - n) + " items to be published to demo1.Subscriber!");
                } else if ((remainItems > 0) && (remainItems < n)) {

                    for (int i = 0; i < remainItems; i++) {
                        executor.execute(() -> {
                            subscriber.onNext(dataItems.poll());
                        });
                    }

                    subscriber.onComplete();

                } else {
                    Logger.printf(LOG_MESSAGE_FORMAT,"demo1.Processor contains no item!");
                }

            }

            @Override
            public void cancel() {
                isCanceled.set(true);

                shutdown();
                publisherSubscription.cancel();
            }

            private void shutdown() {
                Logger.printf(LOG_MESSAGE_FORMAT,"Shutdown executor...");
                executor.shutdown();
                Logger.printf(LOG_MESSAGE_FORMAT,"Shutdown complete.");
                finalizer.complete();
            }
        }
    }

