package org.hibernate.testing.graalvm.methodhandle;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

public class Collector {
    private static final List<MethodHandle> collected = new ArrayList<>();

    static MethodHandle get(int i) {
        return collected.get(i);
    }

    public static void collect(MethodHandle methodHandle) {
        collected.add(methodHandle);
    }
}
