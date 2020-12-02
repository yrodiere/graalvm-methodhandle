package org.hibernate.testing.graalvm.methodhandle;

public class Main {

    public static void main(String[] args) {
        // This will initialize the static final variables in Invoker
        try {
            Object result = Invoker.invoke();
            System.out.println(result);
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
    }
}
