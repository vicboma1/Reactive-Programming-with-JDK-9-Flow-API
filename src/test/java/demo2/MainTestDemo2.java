package demo2;

import base.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class MainTestDemo2 {

    private List<Integer> generator ;

    @Before
    public void setUp() throws Exception {
        generator = new ArrayList();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void main() throws Exception {
        final int MAX_BUFFER_CAPACITY = 128;
        final int PERIOD = 200;
        final int CORE_POOL_SIZE = 1;
        final int MAX_ITEM_TO_PUBLISH = 5;
        final int NUM_FIXED_THREAD_POOL = 2;

        final ExecutorService executor = Executors.newFixedThreadPool(NUM_FIXED_THREAD_POOL);
        final Publisher publisher =  Publisher.create(executor, CORE_POOL_SIZE,MAX_BUFFER_CAPACITY, PERIOD, TimeUnit.MILLISECONDS,MAX_ITEM_TO_PUBLISH);


        Generator.stream()
                .forEach(it -> {
                    generator.add(it);
                    String name = Generator.name(it);
                    final Subscriber subscriber = Subscriber.create( name.toUpperCase(),it);
                    publisher.subscribe(subscriber);
                });

        Assert.assertEquals(generator.size(),publisher.getSubscribers().size());
        Assert.assertEquals(publisher.getAllSubscriber().size(),publisher.getSubscribers().size());

        publisher.getSubscribers().stream()
                .filter(subscriber -> subscriber != null)
                .forEach(subscriber -> {
                    Assert.assertTrue(publisher.getAllSubscriber().contains(subscriber));
                });

        publisher.getAllSubscriber().stream()
            .filter(subscriber -> subscriber != null && publisher.isSubscribed(subscriber))
            .forEach(subscriber -> {
                Assert.assertTrue(publisher.getSubscribers().contains(subscriber));
            });

        publisher.getAllSubscriber().stream()
            .filter(subscriber -> subscriber != null && !publisher.isSubscribed(subscriber))
            .forEach(subscriber -> {
                Assert.assertFalse(publisher.getSubscribers().contains(subscriber));
            });

    }

}