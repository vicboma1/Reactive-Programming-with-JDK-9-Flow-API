package demo1;

import base.Logger;

import java.util.concurrent.*;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.LongStream;

import static java.util.concurrent.Executors.newSingleThreadExecutor;


/**
 * Created by vicboma on 19/09/17.
 */
public class Publisher implements Flow.Publisher<Integer> {

    public static Publisher create(ExecutorService executor ){
        return new Publisher(executor);
    }

    private static final String LOG_MESSAGE_FORMAT = "demo1.PublisherImpl --> [%s] %s%n";

    private ExecutorService executor;
    private Finalizer finalizer;

    public Publisher(ExecutorService executor){
        this.executor = executor;
        this.finalizer = Finalizer.create();
    }


    @Override
    public void subscribe(Subscriber<? super Integer> processor) {
        processor.onSubscribe(
                new SubscriptionPublisher(processor, executor)
        );
    }

    public void exitAsync() throws ExecutionException, InterruptedException {
        finalizer.get();
    }

    private class SubscriptionPublisher implements Subscription {

        private final ExecutorService executor;

        private Subscriber<? super Integer> processor;
        private AtomicBoolean isCanceled;

        public SubscriptionPublisher(Subscriber<? super Integer> subscriber, ExecutorService executor) {
            this.processor = subscriber;
            this.executor = executor;

            isCanceled = new AtomicBoolean(false);
        }

        @Override
        public void request(long n) {
            if (isCanceled.get())
                return;

            if (n < 0)
                executor.execute(() -> processor.onError(new IllegalArgumentException()));
            else
                publishItems(n);
        }

        @Override
        public void cancel() {
            isCanceled.set(true);
            shutdown();
        }

        private void publishItems(long n) {
            LongStream.rangeClosed(0,n)
                     .forEach(it -> {
                         //Warning with "it" inside callback
                        executor.execute(() -> {
                            Logger.printf(LOG_MESSAGE_FORMAT, "publish item: [" + it + "] ...");
                            processor.onNext((int)it);
                        });
                    });
            }
        }

        private void shutdown() {
            Logger.printf(LOG_MESSAGE_FORMAT,"Shutdown executor...");
            executor.shutdown();
            CompletableFuture.runAsync(() -> {
                Logger.printf(LOG_MESSAGE_FORMAT,"Shutdown complete.");
                finalizer.complete();
            });

        }

    }



