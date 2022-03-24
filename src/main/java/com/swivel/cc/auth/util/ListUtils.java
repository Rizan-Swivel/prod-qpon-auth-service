package com.swivel.cc.auth.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtils {

    private ListUtils() {}
    /**
     * Remove duplicates in a list
     *
     * @param keyExtractor keyExtractor
     * @param <T>          T
     * @return predicate
     * Copyright {@link "https://www.baeldung.com/java-streams-distinct-by"}
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
