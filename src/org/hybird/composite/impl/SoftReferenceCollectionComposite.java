package org.hybird.composite.impl;

import java.lang.ref.SoftReference;

public abstract class SoftReferenceCollectionComposite<T> extends WrappedCollectionComposite <SoftReference<T>, T>
{
    public SoftReferenceCollectionComposite (Class<T> klass)
    {
        super (klass);
    }

    @Override
    protected SoftReference<T> wrap (T t)
    {
        return new SoftReference<T> (t);
    }
    
    @Override
    protected T unwrap (SoftReference<T> t)
    {
        return t.get ();
    }
}