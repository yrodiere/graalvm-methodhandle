package org.hibernate.testing.graalvm.methodhandle;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final MethodHandle MH1;
    private static final MethodHandle MH2;

    static {
        try {
            MH1 = MethodHandles.lookup().findVirtual(Target1.class, "foo", MethodType.methodType(Object.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
        try {
            MH2 = MethodHandles.lookup().findVirtual(Target2.class, "bar", MethodType.methodType(Object.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            Target1 target1 = new Target1();
            Target2 target2 = new Target2();

            List<Callable<Void>> tasks = new ArrayList<>();

            for (int i = 0; i < 100; i++) {
                if (i % 2 == 0) {
                    tasks.add(() -> {
                        invokeManyTimes(MH1, target1);
                        return null;
                    });
                } else {
                    tasks.add(() -> {
                        invokeManyTimes(MH2, target2);
                        return null;
                    });
                }
            }
            executor.invokeAll(tasks);
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } finally {
            executor.shutdownNow();
        }
    }

    private static void invokeManyTimes(MethodHandle methodHandle, Object target) {
        try {
            int count = 10000;
            for (int j = 0; j < count; j++) {
                try {
                    methodHandle.invoke(target);
                } catch (Throwable throwable) {
                    throw new IllegalStateException("Exception while invoking '" + methodHandle
                            + "' on '" + target + "': " + throwable.getMessage(), throwable);
                }
            }
            System.out.println("Invoked '" + methodHandle + "' " + count + " times without any problem.");
        } catch (Throwable throwable) {
            StringWriter writer = new StringWriter();
            throwable.printStackTrace(new PrintWriter(writer));
            System.out.println(writer);
        }
    }
}
