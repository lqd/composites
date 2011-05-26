package org.hybird.composite.impl.collections;

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.LinkedList;

import org.hybird.composite.impl.SoftReferenceCollectionComposite;

public class SoftLinkedListComposite<T> extends SoftReferenceCollectionComposite<T>
{
    public SoftLinkedListComposite (Class<T> klass)
    {
        super (klass);
    }
    
    @Override
    protected Collection<SoftReference<T>> createCollection ()
    {
        return new LinkedList<SoftReference<T>> ();
    }
}