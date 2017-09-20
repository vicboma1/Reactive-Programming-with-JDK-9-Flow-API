package demo2;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Generator {

    private static final Random random = new Random();
    private static List<String> listName = List.of("A","B","C","D","E","F","G","H","I","J","K");

    public static Boolean nextBoolean(){
        return random.nextBoolean();
    }

    public static List<Integer> stream(){
        final Set<Integer> set  = IntStream.range(0, 1 + (int)(Math.random() * 10) ).boxed().collect(Collectors.toSet());
        final List<Integer> listShuffle = new ArrayList<>(set);
        Collections.shuffle(listShuffle);
        return listShuffle;
    }

    public static String name(int it){
        return listName.get(it);
    }
}
