package demo1;

import static java.lang.Thread.currentThread;

public abstract class Logger {

    public static void printf(String title, String message, Object... args) {
        if(!Main.DEBUG)
            return ;

            String fullMessage = String.format(title, currentThread().getName(), message);
            System.out.printf(fullMessage, args);

    }

    public static void printf(String title) {
       printf(title,"","");
    }
}
