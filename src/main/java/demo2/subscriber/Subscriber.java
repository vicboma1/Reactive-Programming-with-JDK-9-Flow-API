package demo2.subscriber;

import java.util.concurrent.Flow;

public interface Subscriber<T extends Object> extends Flow.Subscriber<T> {

    @Override
    void onSubscribe(Flow.Subscription subscription);

    @Override
    void onNext(Object item);

    boolean isCompleted();

    boolean isCancel();

    @Override
    void onComplete();

    @Override
    void onError(Throwable t);

    String getTittle();

    String getName();
}
