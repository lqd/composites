package org.hybird.composite;

import java.util.EventListener;

import javax.swing.SwingUtilities;

public class CompositeTest
{
    public static interface IListener
    {
        void callback ();
    }

    public static interface ISwingListener extends EventListener
    {
        void callback (String event);
    }

    public static void main (String [] args)
    {
        IComposite<IListener> sink = Composites.createLinkedListComposite (IListener.class).add (new IListener ()
        {
            @Override
            public void callback ()
            {
                System.out.println ("IListener callback ()");
            }
        });
        sink.delegate ().callback ();

        IComposite<ISwingListener> sink2 = Composites.createEventListenerComposite (ISwingListener.class);
        sink2.add (new ISwingListener ()
        {
            @Override
            public void callback (String event)
            {
                System.out.println ("ISwingListener callback ('" + event + "') - EDT: " + SwingUtilities.isEventDispatchThread ());
            }
        });
        sink2.delegate ().callback ("Main thread dispatch test");

        IComposite<ISwingListener> sink3 = Composites.createEventListenerOnEDTComposite (ISwingListener.class).add (new ISwingListener ()
        {
            @Override
            public void callback (String event)
            {
                System.out.println ("ISwingListener callback ('" + event + "') - EDT: " + SwingUtilities.isEventDispatchThread ());
            }
        });
        sink3.delegate ().callback ("EDT dispatch test");
    }
}