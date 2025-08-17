package com.algovis.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class AlgorithmRegistry {

    public record Metadata(String displayName, String category,
                           boolean supportsPrev, String bigOAvg, String bigOWorst, String space) {}

    private static final Map<AlgorithmId, Supplier<Algorithm<?>>> factories = new HashMap<>();
    private static final Map<AlgorithmId, Metadata> meta = new HashMap<>();

    public static void register(AlgorithmId id, Supplier<Algorithm<?>> factory, Metadata m) {
        factories.put(id, factory);
        meta.put(id, m);
    }
    public static Algorithm<?> create(AlgorithmId id) {
        Supplier<Algorithm<?>> s = factories.get(id);
        if (s == null) throw new IllegalStateException("No factory for " + id);
        return s.get();
    }
    public static Metadata metadata(AlgorithmId id) { return meta.get(id); }

    private AlgorithmRegistry() {}
}