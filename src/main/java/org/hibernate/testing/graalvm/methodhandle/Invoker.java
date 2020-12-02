package org.hibernate.testing.graalvm.methodhandle;

import java.lang.invoke.MethodHandle;

public class Invoker {
    private static final MethodHandle MH;

    static {
        MH = Collector.get(0);
    }

    public static Object invoke(Target target) throws Throwable {
        return MH.invoke(target);
    }
}
