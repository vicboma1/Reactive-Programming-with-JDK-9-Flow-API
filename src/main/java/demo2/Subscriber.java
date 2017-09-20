package demo2;

import base.Logger;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

public class Subscriber<T extends Object> implements Flow.Subscriber<T> {

    private String name;

    private Flow.Subscription subscription;
    private AtomicInteger count;

    private int DEMAND = 0;

    public static Subscriber create(String _name,int n) {
        return new Subscriber(_name,n);
    }

    Subscriber(String _name,int n) {
        name = _name;
        this.DEMAND = n;
        count = new AtomicInteger(DEMAND);
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
    public void onComplete() {
        Logger.printf(getTittle(),"Complete! ");
    }

    @Override
    public void onError(Throwable t) {
        Logger.printf(getTittle(),"Error: " + t.getMessage());
    }

    public String getTittle() {
        return "demo2.Subscriber "+name+" -> [%s] %s%n";
    }

    public String getName() {
        return name;
    }
}