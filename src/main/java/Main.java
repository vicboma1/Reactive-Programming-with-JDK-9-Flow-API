import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main (String args []) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(()->{
            System.out.print("Hello Reactive World!");

            final Publisher1 publisher = Publisher1.create();

            final Subscriber1 subscriber = Subscriber1.create();
            subscriber.setDEMAND(4);

            final Processor1 processor = Processor1.create();
            processor.setDEMAND(5);

            publisher.subscribe(processor);
            processor.subscribe(subscriber);

            try {
                publisher.waitUntilTerminated();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

    }
}
