package org.hybird.composite.impl;

import java.lang.ref.WeakReference;

public abstract class WeakReferenceCollectionComposite<T> extends WrappedCollectionComposite <WeakReference<T>, T>
{
    public WeakReferenceCollectionComposite (Class<T> klass)
    {
        super (klass);
    }

    @Override
    protected WeakReference<T> wrap (T t)
    {
        return new WeakReference<T> (t);
    }
    
    @Override
    protected T unwrap (WeakReference<T> t)
    {
        return t.get ();
    }
}