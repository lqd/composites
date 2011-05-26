package org.hybird.composite.impl.collections;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedList;

import org.hybird.composite.impl.WeakReferenceCollectionComposite;

public class WeakLinkedListComposite<T> extends WeakReferenceCollectionComposite<T>
{
    public WeakLinkedListComposite (Class<T> klass)
    {
        super (klass);
    }

    @Override
    protected Collection<WeakReference<T>> createCollection ()
    {
        return new LinkedList<WeakReference<T>> ();
    }
}