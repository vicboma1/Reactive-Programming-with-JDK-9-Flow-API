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
 

```
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

Demo 2 
 * SubmissionPublisher ( Integer ) <-- Random --> List( Subscriber) ( Object )

```
demo2.Subscriber E -> [pool-1-thread-2] Subscribed...
demo2.Subscriber E -> [pool-1-thread-2] request new 4 items...
demo2.Subscriber B -> [pool-1-thread-2] Subscribed...
demo2.Subscriber B -> [pool-1-thread-2] request new 1 items...
demo2.Subscriber A -> [pool-1-thread-1] Subscribed...
demo2.Subscriber H -> [pool-1-thread-2] Subscribed...
demo2.Subscriber A -> [pool-1-thread-1] request new 0 items...
demo2.Subscriber H -> [pool-1-thread-2] request new 7 items...
demo2.Subscriber F -> [pool-1-thread-2] Subscribed...
demo2.Subscriber F -> [pool-1-thread-2] request new 5 items...
demo2.Subscriber I -> [pool-1-thread-2] Subscribed...
demo2.Subscriber I -> [pool-1-thread-2] request new 8 items...
demo2.Publisher ----> [pool-2-thread-1] publishing item: 1 ...
demo2.Subscriber C -> [pool-1-thread-2] Subscribed...
demo2.Subscriber C -> [pool-1-thread-2] request new 2 items...
demo2.Publisher ----> [pool-2-thread-1] estimateMaximumLag: 1
demo2.Subscriber C -> [pool-1-thread-2] itemValue: 1
demo2.Subscriber A -> [pool-1-thread-1] Error: non-positive subscription request
demo2.Subscriber D -> [pool-1-thread-1] Subscribed...
demo2.Subscriber D -> [pool-1-thread-1] request new 3 items...
demo2.Subscriber D -> [pool-1-thread-1] itemValue: 1
demo2.Subscriber J -> [pool-1-thread-1] Subscribed...
demo2.Subscriber J -> [pool-1-thread-1] request new 9 items...
demo2.Subscriber G -> [pool-1-thread-2] Subscribed...
demo2.Subscriber J -> [pool-1-thread-1] itemValue: 1
demo2.Subscriber G -> [pool-1-thread-2] request new 6 items...
demo2.Subscriber G -> [pool-1-thread-2] itemValue: 1
demo2.Subscriber B -> [pool-1-thread-2] itemValue: 1
demo2.Subscriber E -> [pool-1-thread-1] itemValue: 1
demo2.Publisher ----> [pool-2-thread-1] estimateMinimumDemand: -1
demo2.Publisher ----> [pool-2-thread-1] publishing item: 2 ...
demo2.Publisher ----> [pool-2-thread-1] estimateMaximumLag: 2
demo2.Publisher ----> [pool-2-thread-1] estimateMinimumDemand: -1
demo2.Publisher ----> [pool-2-thread-1] publishing item: 3 ...
demo2.Publisher ----> [pool-2-thread-1] estimateMaximumLag: 3
demo2.Publisher ----> [pool-2-thread-1] estimateMinimumDemand: -2
demo2.Publisher ----> [pool-2-thread-1] publishing item: 4 ...
demo2.Subscriber B -> [pool-1-thread-2] Cancel subscribe...  
demo2.Publisher ----> [pool-2-thread-1] estimateMaximumLag: 4
demo2.Publisher ----> [pool-2-thread-1] estimateMinimumDemand: -3
demo2.Publisher ----> [pool-2-thread-1] publishing item: 5 ...
demo2.Subscriber H -> [pool-1-thread-2] itemValue: 1
demo2.Publisher ----> [pool-2-thread-1] estimateMaximumLag: 5
demo2.Publisher ----> [pool-2-thread-1] estimateMinimumDemand: -3
demo2.Subscriber E -> [pool-1-thread-1] itemValue: 2
shutting down...
demo2.Subscriber E -> [pool-1-thread-1] itemValue: 3
demo2.Subscriber H -> [pool-1-thread-2] itemValue: 2
demo2.Subscriber H -> [pool-1-thread-2] itemValue: 3
demo2.Subscriber E -> [pool-1-thread-1] itemValue: 4
demo2.Subscriber H -> [pool-1-thread-2] itemValue: 4
demo2.Subscriber E -> [pool-1-thread-1] Cancel subscribe...  
demo2.Subscriber F -> [pool-1-thread-1] itemValue: 1
demo2.Subscriber H -> [pool-1-thread-2] itemValue: 5
demo2.Subscriber I -> [pool-1-thread-2] itemValue: 1
demo2.Subscriber F -> [pool-1-thread-1] itemValue: 2
demo2.Subscriber I -> [pool-1-thread-2] itemValue: 2
demo2.Subscriber F -> [pool-1-thread-1] itemValue: 3
demo2.Subscriber F -> [pool-1-thread-1] itemValue: 4
demo2.Subscriber I -> [pool-1-thread-2] itemValue: 3
demo2.Subscriber I -> [pool-1-thread-2] itemValue: 4
demo2.Subscriber F -> [pool-1-thread-1] itemValue: 5
demo2.Subscriber F -> [pool-1-thread-1] Cancel subscribe...  
demo2.Subscriber I -> [pool-1-thread-2] itemValue: 5
demo2.Subscriber D -> [pool-1-thread-2] itemValue: 2
demo2.Subscriber C -> [pool-1-thread-1] itemValue: 2
demo2.Subscriber D -> [pool-1-thread-2] itemValue: 3
demo2.Subscriber D -> [pool-1-thread-2] Cancel subscribe...  
demo2.Subscriber C -> [pool-1-thread-1] request new 2 items...
demo2.Subscriber C -> [pool-1-thread-1] itemValue: 3
demo2.Subscriber J -> [pool-1-thread-2] itemValue: 2
demo2.Subscriber C -> [pool-1-thread-1] itemValue: 4
demo2.Subscriber C -> [pool-1-thread-1] request new 2 items...
demo2.Subscriber C -> [pool-1-thread-1] itemValue: 5
demo2.Subscriber J -> [pool-1-thread-2] itemValue: 3
demo2.Subscriber G -> [pool-1-thread-1] itemValue: 2
demo2.Subscriber J -> [pool-1-thread-2] itemValue: 4
demo2.Subscriber J -> [pool-1-thread-2] itemValue: 5
demo2.Subscriber G -> [pool-1-thread-1] itemValue: 3
demo2.Subscriber G -> [pool-1-thread-1] itemValue: 4
demo2.Subscriber G -> [pool-1-thread-1] itemValue: 5
demo2.Publisher ----> [pool-2-thread-1] Subscriber J isSubscribed(): true
demo2.Publisher ----> [pool-2-thread-1] Subscriber F isSubscribed(): false
demo2.Publisher ----> [pool-2-thread-1] Subscriber G isSubscribed(): true
demo2.Publisher ----> [pool-2-thread-1] Subscriber A isSubscribed(): false
demo2.Publisher ----> [pool-2-thread-1] Subscriber E isSubscribed(): false
demo2.Publisher ----> [pool-2-thread-1] Subscriber H isSubscribed(): true
demo2.Publisher ----> [pool-2-thread-1] Subscriber D isSubscribed(): false
demo2.Publisher ----> [pool-2-thread-1] Subscriber B isSubscribed(): false
demo2.Publisher ----> [pool-2-thread-1] Subscriber I isSubscribed(): true
demo2.Publisher ----> [pool-2-thread-1] Subscriber C isSubscribed(): true
Finalize
demo2.Subscriber H -> [pool-1-thread-2] Complete! 
demo2.Subscriber I -> [pool-1-thread-1] Complete! 
demo2.Subscriber C -> [pool-1-thread-2] Complete! 
demo2.Subscriber G -> [pool-1-thread-2] Complete! 
demo2.Subscriber J -> [pool-1-thread-1] Complete! 
```

References :
* https://community.oracle.com/docs/DOC-1006738
* http://javasampleapproach.com
