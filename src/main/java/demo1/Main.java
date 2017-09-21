package demo1;

import base.Logger;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static Long ITERATIONS = 5L;
    public static Boolean DEBUG = true;

    public static void main (String args []) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(()->{

            Logger.printf("Hello Reactive World!\n" );

            final ExecutorService executor = Executors.newFixedThreadPool(2);

            final Publisher publisher = Publisher.create(executor);
            final Subscriber subscriber = Subscriber.create(4);
            final Processor processor = Processor.create(executor,ITERATIONS);

            publisher.subscribe(processor);
            processor.subscribe(subscriber);

            try {
                publisher.exitAsync();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            finally {
                Logger.printf("Finalize Reactive World!\n");
            }

        });

    }
}
