package org.hibernate.testing.graalvm.methodhandle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Invoker {
    private static final MethodHandle MH;
    private static final Target target = new Target();
    static {
        try {
            MH = MethodHandles.lookup().findVirtual(Target.class, "foo", MethodType.methodType(Object.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    public static Object invoke() throws Throwable {
        return MH.invoke(target);
    }
}
