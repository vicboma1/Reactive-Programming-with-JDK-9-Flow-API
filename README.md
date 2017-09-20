# Reactive Programming with JDK 9 Flow API

![](https://travis-ci.org/vicboma1/Reactive-Programming-with-JDK-9-Flow-API.svg?branch=master)

 * process a potentially unbounded number of elements
 * in sequence
 * asynchronously passing elements between components
 * with mandatory non-blocking back-pressure

```java
@FunctionalInterface   
public static interface Flow.demo1.Publisher<T> {  
    public void    subscribe(Flow.demo1.Subscriber<? super T> subscriber);  
}   
  
public static interface Flow.demo1.Subscriber<T> {  
    public void    onSubscribe(Flow.Subscription subscription);  
    public void    onNext(T item) ;  
    public void    onError(Throwable throwable) ;  
    public void    onComplete() ;  
}   
  
public static interface Flow.Subscription {  
    public void    request(long n);  
    public void    cancel() ;  
}   
  
public static interface Flow.demo1.Processor<T,R>  extends Flow.demo1.Subscriber<T>, Flow.demo1.Publisher<R> {  
}  

```

Demo1 
 * Publisher ( Integer ) <-> [ Processor { Integer , String } ] <-> Subscriber ( String )
 

```java
Hello Reactive World!
demo1.Processor --> [AWT-EventQueue-0] Subscribed...
demo1.Processor --> [AWT-EventQueue-0] Requesting 5 new items...
demo1.Publisher --> [pool-1-thread-1] publish item: [0] ...
demo1.Publisher --> [pool-1-thread-2] publish item: [1] ...
demo1.Processor --> [pool-1-thread-2] processing item: [1] ...
demo1.Publisher --> [pool-1-thread-2] publish item: [2] ...
demo1.Processor --> [pool-1-thread-2] processing item: [2] ...
demo1.Publisher --> [pool-1-thread-2] publish item: [3] ...
demo1.Processor --> [pool-1-thread-2] processing item: [3] ...
demo1.Publisher --> [pool-1-thread-2] publish item: [4] ...
demo1.Processor --> [pool-1-thread-2] processing item: [4] ...
demo1.Publisher --> [pool-1-thread-2] publish item: [5] ...
demo1.Processor --> [pool-1-thread-2] processing item: [5] ...
demo1.Processor --> [pool-1-thread-1] processing item: [0] ...
demo1.Subscriber -> [AWT-EventQueue-0] Subscribed
demo1.Subscriber -> [AWT-EventQueue-0] Requesting 5 new items...
demo1.Subscriber -> [pool-1-thread-1] item value = 0 after processing
demo1.Subscriber -> [pool-1-thread-1] item value = 20 after processing
demo1.Subscriber -> [pool-1-thread-1] item value = 30 after processing
demo1.Subscriber -> [pool-1-thread-1] item value = 40 after processing
demo1.Subscriber -> [pool-1-thread-2] item value = 10 after processing
demo1.Subscriber -> [pool-1-thread-2] Cancelling subscription...
demo1.Processor --> [pool-1-thread-2] Shutdown executor...
demo1.Processor --> [pool-1-thread-2] Shutdown complete.
demo1.Publisher --> [pool-1-thread-2] Shutdown executor...
demo1.Processor --> [AWT-EventQueue-0] Remaining 1 items to be published to demo1.Subscriber!
demo1.Publisher --> [ForkJoinPool.commonPool-worker-1] Shutdown complete.
Finalize Reactive World!

```


References :
* https://community.oracle.com/docs/DOC-1006738
* http://javasampleapproach.com/java/java-9-flow-api-example-processor#1_Create_implementation_of_Publisher
