package demo2;

import demo2.subscriber.Subscriber;
import demo2.subscriber.SubscriberImpl;
import demo2.publisher.Publisher;
import demo2.publisher.PublisherImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

public class MainTestDemo2 {

    private List<Integer> generator ;
    private CompletableFuture<Void> lock;
    private List<Boolean> listAssert;
    private Publisher publisherImpl;
    private  List<Flow.Subscriber<? super Integer>> allSubscribers;

    @Before
    public void setUp() throws Exception {
        generator = new ArrayList();
        listAssert = new ArrayList<>();
        lock = new CompletableFuture<>();

        final int MAX_BUFFER_CAPACITY = 128;
        final int PERIOD = 200;
        final int CORE_POOL_SIZE = 1;
        final int MAX_ITEM_TO_PUBLISH = 5;
        final int NUM_FIXED_THREAD_POOL = 2;

        final ExecutorService executor = Executors.newFixedThreadPool(NUM_FIXED_THREAD_POOL);
        publisherImpl =  PublisherImpl.create(executor, CORE_POOL_SIZE,MAX_BUFFER_CAPACITY, PERIOD, TimeUnit.MILLISECONDS,MAX_ITEM_TO_PUBLISH);

        Generator.stream()
                .forEach(it -> {
                    generator.add(it);
                    String name = Generator.name(it);
                    final Subscriber subscriber = SubscriberImpl.create( name.toUpperCase(),it);
                    publisherImpl.subscribe(subscriber);
                });

        allSubscribers = new ArrayList(publisherImpl.getAllSubscriber());

    }

    @After
    public void tearDown() throws Exception {
        generator = null;
        lock = null;
        publisherImpl = null;
        listAssert = null;
        allSubscribers = null;
    }

    @Test
    public void allSubscriptorsSize() throws Exception {

        this.execute(it -> {
            try {
                listAssert.add(allSubscribers.size() == generator.size());
            } catch (Exception e) {
                Assert.assertFalse(e.toString(), true);
            } finally {
                lock.complete(null);
            }

            return null;
        },"allSubscriptorsSize");
    }

    @Test
    public void subscribersInAllSubscriptors() throws Exception {

        this.execute(it -> {
                    try {
                        final List<Flow.Subscriber<? super Integer>> subscribers = new ArrayList((List<Flow.Subscriber<? super java.lang.Integer>>)it);

                        //Interseccion Subscriptores en allSubscribers
                        subscribers.stream()
                                .filter(subscriber -> subscriber != null)
                                .forEach(subscriber -> {
                                    listAssert.add(allSubscribers.contains(subscriber));
                                });
                    }
                    catch(Exception e){
                        Assert.assertFalse(e.toString(),true);
                    }
                    finally {
                        lock.complete(null);
                    }

                    return null;
                },"subscribersInAllSubscriptors");
    }

    @Test
    public void subscribersCompleted() throws Exception {

        this.execute(it -> {
            try {
                final List<Flow.Subscriber<? super Integer>> subscribers = new ArrayList((List<Flow.Subscriber<? super java.lang.Integer>>)it);

                //Subscriptores subscritos y completados
                subscribers.stream()
                        .filter(subscriber -> subscriber != null && publisherImpl.isSubscribed(subscriber) && ((Subscriber)subscriber).isCompleted() )
                        .forEach(subscriberCompleted -> {
                            listAssert.add(((Subscriber)subscriberCompleted).isCompleted());
                        });
            }
            catch(Exception e){
                Assert.assertFalse(e.toString(),true);
            }
            finally {
                lock.complete(null);
            }

            return null;
        },"subscribersCompleted");
    }

    @Test
    public void subscribersNoCompleted() throws Exception {

        this.execute(it -> {
            try {
                final List<Flow.Subscriber<? super Integer>> subscribers = new ArrayList((List<Flow.Subscriber<? super java.lang.Integer>>)it);

                //Subscriptores subscritos y NO completados
                subscribers.stream()
                        .filter(subscriber -> subscriber != null && publisherImpl.isSubscribed(subscriber) && !((Subscriber)subscriber).isCompleted() )
                        .forEach(subscriberCompleted -> {
                            listAssert.add(!((Subscriber)subscriberCompleted).isCompleted());
                        });
            }
            catch(Exception e){
                Assert.assertFalse(e.toString(),true);
            }
            finally {
                lock.complete(null);
            }

            return null;
        },"subscribersNoCompleted");
    }

    @Test
    public void allSubscribersCompleted() throws Exception {

        this.execute(it -> {
            try {
                final List<Flow.Subscriber<? super Integer>> subscribers = new ArrayList((List<Flow.Subscriber<? super java.lang.Integer>>)it);

                //Todos los subscriptores subscritos y completados
                allSubscribers.stream()
                        .filter(subscriber -> subscriber != null && publisherImpl.isSubscribed( subscriber) && ((Subscriber)subscriber).isCompleted() )
                        .forEach(subscriber -> {
                            listAssert.add(subscribers.contains(subscriber));
                        });
            }
            catch(Exception e){
                Assert.assertFalse(e.toString(),true);
            }
            finally {
                lock.complete(null);
            }

            return null;
        },"allSubscribersCompleted");
    }

    @Test
    public void allSubscribersNoCompleted() throws Exception {

        this.execute(it -> {
            try {
                final List<Flow.Subscriber<? super Integer>> subscribers = new ArrayList((List<Flow.Subscriber<? super java.lang.Integer>>)it);

                //Todos los subscriptores subscritos y No completados
                allSubscribers.stream()
                        .filter(subscriber -> subscriber != null && publisherImpl.isSubscribed( subscriber) && !((Subscriber)subscriber).isCompleted())
                        .forEach(subscriberCanceled -> {
                            listAssert.add(subscribers.contains(subscriberCanceled));
                        });
            }
            catch(Exception e){
                Assert.assertFalse(e.toString(),true);
            }
            finally {
                lock.complete(null);
            }

            return null;
        },"allSubscribersNoCompleted");
    }

    @Test
    public void main() throws Exception {

        this.execute(it -> {
              try {

                  final List<Flow.Subscriber<? super Integer>> subscribers = new ArrayList((List<Flow.Subscriber<? super java.lang.Integer>>)it);

                  listAssert.add(allSubscribers.size() == generator.size());

                 //Interseccion Subscriptores en allSubscribers
                  subscribers.stream()
                          .filter(subscriber -> subscriber != null)
                          .forEach(subscriber -> {
                              listAssert.add(allSubscribers.contains(subscriber));
                          });

                  //Subscriptores subscritos y completados
                  subscribers.stream()
                          .filter(subscriber -> subscriber != null && publisherImpl.isSubscribed(subscriber) && ((Subscriber)subscriber).isCompleted() )
                          .forEach(subscriberCompleted -> {
                              listAssert.add(((Subscriber)subscriberCompleted).isCompleted());
                          });

                  //Subscriptores subscritos y NO completados
                  subscribers.stream()
                          .filter(subscriber -> subscriber != null && publisherImpl.isSubscribed(subscriber) && !((Subscriber)subscriber).isCompleted() )
                          .forEach(subscriberCompleted -> {
                              listAssert.add(!((Subscriber)subscriberCompleted).isCompleted());
                          });

                  //Todos los subscriptores subscritos y completados
                  allSubscribers.stream()
                          .filter(subscriber -> subscriber != null && publisherImpl.isSubscribed( subscriber) && ((Subscriber)subscriber).isCompleted() )
                          .forEach(subscriber -> {
                              listAssert.add(subscribers.contains(subscriber));
                          });

                  //Todos los subscriptores subscritos y No completados
                  allSubscribers.stream()
                          .filter(subscriber -> subscriber != null && publisherImpl.isSubscribed( subscriber) && !((Subscriber)subscriber).isCompleted())
                          .forEach(subscriberCanceled -> {
                              listAssert.add(subscribers.contains(subscriberCanceled));
                          });

              }
              catch(Exception e){
                  Assert.assertFalse(e.toString(),true);
              }
              finally {
                  lock.complete(null);
              }

              return null;
        }, "Main Test");

       this.lockMethodAndAssert("Main tests");
    }

    public void execute(Function function, String message) throws Exception {

        publisherImpl.isClosing()
                .thenApplyAsync(it -> {
                    try {
                        function.apply(null);
                    }
                    catch(Exception e){
                        Assert.assertFalse(e.toString(),true);
                    }
                    finally {
                        lock.complete(null);
                    }

                    return null;
                });

        lockMethodAndAssert(message);
    }
    private void lockMethodAndAssert(String message) throws Exception {

        System.out.println(message);

        lock.get();
        System.out.println("FinalizeAsync");

        listAssert.stream()
                .forEach(it -> {
                    System.out.println(it);
                    Assert.assertTrue(it);
                });

        System.out.println("Asserts Ok!");

    }

}