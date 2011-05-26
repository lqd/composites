package org.hybird.composite.impl.collections;

import java.util.ArrayList;
import java.util.Collection;

import org.hybird.composite.impl.CollectionComposite;

public class ArrayListComposite<T> extends CollectionComposite<T>
{
    private int initialCapacity;
    
    public ArrayListComposite (Class<T> klass)
    {
        this (klass, 10);
    }

    public ArrayListComposite (Class<T> klass, int initialCapacity)
    {
        super (klass);
        this.initialCapacity = initialCapacity;
    }
    
    @Override
    protected Collection<T> createCollection ()
    {
        return new ArrayList<T> (initialCapacity);
    }
}