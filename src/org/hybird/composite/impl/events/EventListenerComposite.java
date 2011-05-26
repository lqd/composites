package org.hybird.composite.impl.events;

import java.lang.reflect.InvocationTargetException;
import java.util.EventListener;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import net.sf.cglib.proxy.MethodProxy;

import org.hybird.composite.IComposite;
import org.hybird.composite.impl.BaseComposite;

public class EventListenerComposite<T extends EventListener> extends BaseComposite<T>
{
    private boolean invokeOnEDT;
    private EventListenerList list;

    public EventListenerComposite (Class<T> klass)
    {
        this (klass, false);
    }

    public EventListenerComposite (Class<T> klass, boolean invokeOnEDT)
    {
        super (klass);

        this.invokeOnEDT = invokeOnEDT;

        list = new EventListenerList ();
    }

    @Override
    protected Object invokeSingleComponent (final MethodProxy method, final Object [] args,
            final Object component) throws IllegalAccessException, InvocationTargetException
    {
        if (invokeOnEDT)
        {
            SwingUtilities.invokeLater (new Runnable ()
            {
                @Override
                public void run ()
                {
                    try
                    {
                        doInvokeSingleComponent (method, args, component);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace ();
                    }
                }
            });
            
            return null;
        }
        else
        {
            return super.invokeSingleComponent (method, args, component);
        }
    }

    @Override
    public IComposite<T> add (T t)
    {
        list.add (klass, t);
        return super.add (t);
    }

    @Override
    public IComposite<T> remove (T t)
    {
        list.remove (klass, t);
        return super.remove (t);
    }

    @Override
    protected Object [] entries ()
    {
        return list.getListeners (klass);
    }
}