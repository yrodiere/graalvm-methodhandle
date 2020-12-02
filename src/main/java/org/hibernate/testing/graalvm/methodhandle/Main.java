package org.hibernate.testing.graalvm.methodhandle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Main {
    // Simulate static init
    static {
        MethodHandle methodHandle;
        try {
            methodHandle = MethodHandles.lookup().findVirtual(Target.class, "foo", MethodType.methodType(Object.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
        // Collect method handles as part of static init
        Collector.collect(methodHandle);
    }

    public static void main(String[] args) {
        // This will initialize the static final variables in Invoker
        try {
            Object result = Invoker.invoke(new Target());
            System.out.println(result);
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
    }

    public static Object foo() {
        return "Big success!";
    }
}
