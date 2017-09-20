package demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Finalizer {

    final private CompletableFuture<Void> completable;

    public static Finalizer create(){
        return new Finalizer();
    }

    Finalizer(){
        completable = new CompletableFuture();
    }

    public Void get() throws ExecutionException, InterruptedException {
        return completable.get();
    }

    public Boolean complete(){
        return completable.complete(null);
    }
}
