package demo2.subscriber;

import base.Logger;
import demo2.Generator;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

public class SubscriberImpl<T extends Object> implements Subscriber<T> {

    private String name;
    private boolean isCompleted;
    private boolean isCancel;

    private Flow.Subscription subscription;
    private AtomicInteger count;

    private int DEMAND = 0;

    public static Subscriber create(String _name,int n) {
        return new SubscriberImpl(_name,n);
    }

    SubscriberImpl(String _name,int n) {
        this.name = _name;
        this.DEMAND = n;
        this.count = new AtomicInteger(DEMAND);
        this.isCompleted = false;
        this.isCancel = false;
    }



    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        Logger.printf(getTittle(),"Subscribed...");
        this.subscription = subscription;

        request(DEMAND);
    }


    private void request(int n) {
        Logger.printf(getTittle(),"request new " + n + " items...");
        subscription.request(n);
    }

    @Override
    public void onNext(Object item) {
        Logger.printf(getTittle(),"itemValue: " + item);

        if (count.decrementAndGet() == 0) {
            if (Generator.nextBoolean()) {
                request(DEMAND);
                count.set(DEMAND);
            } else {
                Logger.printf(getTittle(),"Cancel subscribe...  ");
                subscription.cancel();
            }

        }

    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public boolean isCancel() {
        return isCancel;
    }

    @Override
    public void onComplete() {
        Logger.printf(getTittle(),"Complete! ");
        this.isCompleted = true;

    }

    @Override
    public void onError(Throwable t) {
        Logger.printf(getTittle(),"Error: " + t.getMessage());
        this.isCancel = true;
    }

    @Override
    public String getTittle() {
        return "demo2.subscriber.subscriber "+name+" -> [%s] %s%n";
    }

    @Override
    public String getName() {
        return name;
    }
}