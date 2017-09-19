# Reactive Programming with JDK 9 Flow API

![](https://travis-ci.org/vicboma1/Reactive-Programming-with-JDK-9-Flow-API.svg?branch=master)

 * process a potentially unbounded number of elements
 * in sequence
 * asynchronously passing elements between components
 * with mandatory non-blocking back-pressure

```java
@FunctionalInterface   
public static interface Flow.Publisher<T> {  
    public void    subscribe(Flow.Subscriber<? super T> subscriber);  
}   
  
public static interface Flow.Subscriber<T> {  
    public void    onSubscribe(Flow.Subscription subscription);  
    public void    onNext(T item) ;  
    public void    onError(Throwable throwable) ;  
    public void    onComplete() ;  
}   
  
public static interface Flow.Subscription {  
    public void    request(long n);  
    public void    cancel() ;  
}   
  
public static interface Flow.Processor<T,R>  extends Flow.Subscriber<T>, Flow.Publisher<R> {  
}  

```
