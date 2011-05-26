package org.hybird.composite.impl;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class Proxy
{
    private Proxy ()
    {
    }

    public static Object newProxyInstance (Class<?> klass, MethodInterceptor interceptor)
    {
        Enhancer enhancer = new Enhancer();
        
        if (klass.isInterface ())
        {
            enhancer.setInterfaces (new Class<?> [] {IProxy.class, klass});
        }
        else
        {
            enhancer.setSuperclass (klass);
            enhancer.setInterfaces (new Class<?> [] {IProxy.class});
        }
        
        enhancer.setCallback (interceptor);
        
        try
        {
            return enhancer.create ();
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException ("The class " + klass.getName () + " doesn't provide a default constructor, a composite can't be created", e);
        }
    }
}