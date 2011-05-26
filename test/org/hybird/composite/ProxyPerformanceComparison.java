package org.hybird.composite;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyPerformanceComparison {

  @SuppressWarnings("unchecked")
public static void main(String[] args) throws Exception {
    Callable<Integer> jdkProxy = (Callable<Integer>) Proxy.newProxyInstance(
        ClassLoader.getSystemClassLoader(),
        new Class[] { Callable.class },
        new JdkHandler(new Counter())
    );

    Enhancer enhancer = new Enhancer();
    enhancer.setCallback(new CglibInterceptor(new Counter()));
    enhancer.setInterfaces(new Class[] { Callable.class });
    Callable<Integer> cglibProxy = (Callable<Integer>) enhancer.create();


    for (int i2 = 0; i2 < 10; i2++) {
      iterate(jdkProxy,    "JDK Proxy: ");
      iterate(cglibProxy,  "CGLIB:     ");

      System.err.println();
    }
  }

  static final DecimalFormat format = new DecimalFormat();

  static void iterate(Callable<Integer> callable, String label)
      throws Exception {
    int count = 10000000;
    long time = System.currentTimeMillis();
    int total = 0;
    for (int i = 0; i < count; i++) {
      total += callable.call();
    }
    time = System.currentTimeMillis() - time;
    System.err.println(label
        + format.format(count * 1000 / time) + " calls/s");
  }

  static class JdkHandler implements InvocationHandler {

    final Object delegate;

    JdkHandler(Object delegate) {
      this.delegate = delegate;
    }

    public Object invoke(Object object, Method method, Object[] objects)
        throws Throwable {
      return method.invoke(delegate, objects);
    }
  }

  static class CglibInterceptor implements MethodInterceptor {

    final Object delegate;

    CglibInterceptor(Object delegate) {
      this.delegate = delegate;
    }

    public Object intercept(Object object, Method method, Object[] objects,
        MethodProxy methodProxy) throws Throwable {
      return methodProxy.invoke(delegate, objects);
    }
  }

  static class Counter implements Callable<Integer> {
    int count = 0;
    public Integer call() throws Exception {
      return count++;
    }
  }
}