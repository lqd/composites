package org.hybird.composite.impl.collections;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;

import org.hybird.composite.impl.SoftReferenceCollectionComposite;

public class SoftArrayListComposite<T> extends SoftReferenceCollectionComposite<T>
{
    public SoftArrayListComposite (Class<T> klass)
    {
        super (klass);
    }
    
    @Override
    protected Collection<SoftReference<T>> createCollection ()
    {
        return new ArrayList<SoftReference<T>> ();
    }
}