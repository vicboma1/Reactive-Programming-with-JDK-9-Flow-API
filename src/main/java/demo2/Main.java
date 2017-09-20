package demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {


    public static void main(String[] args) {

        final int MAX_BUFFER_CAPACITY = 128;
        final int PERIOD = 200;
        final int CORE_POOL_SIZE = 1;
        final int MAX_ITEM_TO_PUBLISH = 5;
        final int NUM_FIXED_THREAD_POOL = 2;

        final ExecutorService executor = Executors.newFixedThreadPool(NUM_FIXED_THREAD_POOL);
        final Publisher publisher =  Publisher.create(executor, CORE_POOL_SIZE,MAX_BUFFER_CAPACITY, PERIOD, TimeUnit.MILLISECONDS,MAX_ITEM_TO_PUBLISH);

        Generator.stream()
                 .forEach(it -> {
                     String name = Generator.name(it);
                     publisher.subscribe(Subscriber.create( name.toUpperCase(),it));
                 });
    }

}
