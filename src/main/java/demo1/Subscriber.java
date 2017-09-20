package demo1;

import base.Logger;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by vicboma on 19/09/17.
 */
public class Subscriber implements Flow.Subscriber<String> {

    private static final String LOG_MESSAGE_FORMAT = "demo1.Subscriber -> [%s] %s%n";
    private long iterations = 0;
    private Flow.Subscription subscription;
    private AtomicLong count;

    public static Subscriber create(long iterations) {
        return new Subscriber(iterations);
    }

    public Subscriber(long n) {
        this.iterations = n;
        count = new AtomicLong(iterations);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        Logger.printf(LOG_MESSAGE_FORMAT,"Subscribed");
        this.subscription = subscription;
        Logger.printf(LOG_MESSAGE_FORMAT,"Requesting %d new items...", iterations);
        subscription.request(iterations);
    }

    @Override
    public void onNext(String item) {
        if (item != null) {
            Logger.printf(LOG_MESSAGE_FORMAT,item);
            final long _count = count.decrementAndGet();

            if (_count == 0) {
                Logger.printf(LOG_MESSAGE_FORMAT,"Cancelling subscription...");
                subscription.cancel();
            }

        } else {
            Logger.printf(LOG_MESSAGE_FORMAT,"Null Item!");
        }
    }

    @Override
    public void onComplete() {
        Logger.printf(LOG_MESSAGE_FORMAT,"onComplete(): There is no remaining item in demo1.Processor.");
    }

    @Override
    public void onError(Throwable t) {
        Logger.printf(LOG_MESSAGE_FORMAT,"Error >> %s", t);
    }

}
