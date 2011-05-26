package org.hybird.composite.impl;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.hybird.composite.Composites;
import org.hybird.composite.IComposite;

@SuppressWarnings("unchecked")
public abstract class BaseComposite<T> implements IComposite<T>
{
    protected Class<T> klass;

    public BaseComposite (Class<T> klass)
    {
//        if (klass.isInterface () == false)
//            throw new IllegalArgumentException ("Unable to create composite, this class is not an interface: " + klass.getName ());
        
        this.klass = klass;
        
        if (klass.isPrimitive ())
        {
            throw new IllegalArgumentException ("Unable to create composite, this class is a primitive type: " + klass.getName ());
            
//            this.klass = replacePrimitiveClass (klass);
//            
//            System.out.println ("yoopsy: " + this.klass);
        }
    }
    
    @Override
    public Class<T> delegateClass ()
    {
        return klass;
    }
    
    protected Class<T> replacePrimitiveClass (Class<T> klass)
    {
        Class<?> [] primitiveClasses = {Double.class, Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class};
        
        for (int i = 0; i < primitiveClasses.length; ++i)
        {
            try
            {
                if (primitiveClasses [i].getField ("TYPE").get (primitiveClasses [i]) == klass)
                    return (Class<T>) primitiveClasses [i];
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return klass;
    }
    
    private T delegate;

    public IComposite<T> add (T t)
    {
        delegate = null;
        return this;
    }
    
    public IComposite<T> remove (T t)
    {
        delegate = null;
        return this;
    }
    
    @Override
    public T delegate ()
    {
        if (delegate == null)
        {
            Object [] components = entries ();
            if (components.length == 1)
                delegate = (T) components [0];
            else
                delegate = createDelegate (components);
                
            if (listener != null)
                listener.delegateCreated (delegate);
        }
        return delegate;
    }
    
    private Listener<T> listener;
    
    @Override
    public void setListener (Listener<T> listener)
    {
        this.listener = listener;
    }
    
    protected abstract Object [] entries ();
    
    @Override
    public T [] components ()
    {
        return (T []) entries();
    }
    
    @Override
    public <U extends T> U as (Class<U> klass)
    {
//        System.out.println ("BaseComposite.as(), components: " + components().length + ", filtered: " + filterComponents (klass).length);
        U asDelegate = createDelegate (klass, filterComponents (klass));
        if (listener != null)
            listener.delegateCreated (asDelegate);
        return asDelegate;
    }
    
    protected Object [] filterComponents (Class<?> klass)
    {
        Collection<Object> filteredComponents = new ArrayList <Object> ();
        
        for (Object component : entries())
        {
            if (klass.isInstance (component))
                filteredComponents.add (component);
        }
     
//            System.out.println ("total: " + components().length + " - as " + klass.getName () + ": " + filteredComponents.size ());
        
        return filteredComponents.toArray ();
    }
    
    protected T createDelegate (final Object [] components)
    {
        return createDelegate (klass, components);
    }
    
    protected <V> V createDelegate (final Class<V> delegateClass, final Object [] components)
    {
        return (V) Proxy.newProxyInstance (delegateClass, new MethodInterceptor()
        {
            @Override
            public Object intercept (Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
            {
                if (method.getDeclaringClass () == IProxy.class
                        && "org_hybird_composite_impl_components".equals (method.getName ()))
                    return components;
                
                if (method.isAccessible () == false)
                    method.setAccessible (true);
                
                Class<?> returnType = method.getReturnType ();
                if (returnType != Void.TYPE)
                {
                    if (returnType.isPrimitive () || Modifier.isFinal (returnType.getModifiers ()))
                    {
                        // oops
                        // System.out.println ("Oops - method can't be wrapped yet: " + method + " - invoking super");
                        
                        return methodProxy.invokeSuper (object, args);
                    }
                    
                    if (components.length == 1)
                    {
                        Object result = invokeSingleComponent (methodProxy, args, components[0]);
//                        System.out.println ("method: " + method + ", result: " + result);
                        return result;
                    }
                    else
                    {
                        if (returnType != method.getGenericReturnType ()) // if (method.getGenericReturnType () instanceof TypeVariable)
                            returnType = findActualType (delegateClass, method);
                        
                        IComposite composite = Composites.createArrayListComposite (returnType, components.length);
                        loopBackwards (methodProxy, args, components, composite);
                        
                        try
                        {
                            return composite.delegate ();
                        }
                        catch (Exception e)
                        {
                            throw new IllegalStateException ("Unexpected exception caught while trying to create Composite for method " + method, e);
                        }
                    }
                }
                else
                {
                    loopBackwards (methodProxy, args, components, null);
                    return null;
                }
            }

            
        });
    }

    protected void loopBackwards (MethodProxy method, Object [] args, Object [] components, IComposite resultCollector)
    {
        for (int i = components.length; (i--) > 0;)
        {
            Object component = components[i];
            try
            {
                Object result = invokeSingleComponent (method, args, component);
                
                if (resultCollector != null)
                    resultCollector.add (result);
            }
            catch (Exception e)
            {
                System.out.println ("Unexpected exception caught while invoking method " + method + ": " + e);
                e.printStackTrace ();

                // we might remove the faulty element from the list at this
                //  point, for instance
            }
        }
    }

    protected Object invokeSingleComponent (MethodProxy method, Object [] args, Object component)
            throws IllegalAccessException, InvocationTargetException
    {
        return doInvokeSingleComponent (method, args, component);
    }

    protected Object doInvokeSingleComponent (MethodProxy method, Object [] args, Object component)
            throws IllegalAccessException, InvocationTargetException
    {
        if (component != null)
        {
            try
            {
                return method.invoke (component, args);
            }
            catch (Throwable e)
            {
                return new InvocationTargetException (e);
            }
        }
        
        return null;
    }
    
    private <V> Class<?> findActualType (final Class<V> delegateClass, Method method)
    {
        TypeVariable rt = (TypeVariable) method.getGenericReturnType ();
        TypeVariable<?> [] typeParameters = rt.getGenericDeclaration ().getTypeParameters ();
        
        int index = -1;
        for (int i = 0; i < typeParameters.length; ++i)
        {
            TypeVariable t = typeParameters [i];

            if (rt == t)
            {
                index = i;
                break;
            }
        }
        
        if (index == -1)
            throw new IllegalStateException ("Pb finding the generic type");
        
        if (delegateClass.getGenericSuperclass () instanceof ParameterizedType == false)
            return delegateClass;
        
        ParameterizedType genericSuperclass = (ParameterizedType) delegateClass.getGenericSuperclass ();
        Type actualType = genericSuperclass.getActualTypeArguments () [index];
        
        if (actualType instanceof Class == false)
            throw new IllegalStateException ("WTF - actual type is not a class");
        
        return (Class<?>) actualType;
    }
}