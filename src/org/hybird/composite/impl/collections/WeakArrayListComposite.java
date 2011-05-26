package org.hybird.composite.impl.collections;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;

import org.hybird.composite.impl.WeakReferenceCollectionComposite;

public class WeakArrayListComposite<T> extends WeakReferenceCollectionComposite<T>
{
    public WeakArrayListComposite (Class<T> klass)
    {
        super (klass);
    }

    @Override
    protected Collection<WeakReference<T>> createCollection ()
    {
        return new ArrayList<WeakReference<T>> ();
    }
}