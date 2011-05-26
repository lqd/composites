package org.hybird.composite.impl.collections;

import java.util.Collection;
import java.util.LinkedList;

import org.hybird.composite.impl.CollectionComposite;

public class LinkedListComposite<T> extends CollectionComposite<T>
{
    public LinkedListComposite (Class<T> klass)
    {
        super (klass);
    }

    @Override
    protected Collection<T> createCollection ()
    {
        return new LinkedList<T> ();
    }
}