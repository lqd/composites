package org.hybird.composite.impl;

import java.lang.reflect.Array;
import java.util.Collection;

import org.hybird.composite.IComposite;

public abstract class CollectionComposite<T> extends BaseComposite<T>
{
    public CollectionComposite (Class<T> klass)
    {
        super (klass);
    }

    protected Collection<T> components;

    @Override
    public IComposite<T> add (T t)
    {
        prepareCollection ();

        components.add (t);

        return super.add (t);
    }

    public void prepareCollection ()
    {
        if (components == null)
            components = createCollection ();
    }

    protected abstract Collection<T> createCollection ();

    @Override
    public IComposite<T> remove (T t)
    {
        if (components != null)
            components.remove (t);

        return super.remove (t);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object [] entries ()
    {
        int size = components == null ? 0 : components.size ();
        T [] array = (T []) Array.newInstance (klass, size);
        return components == null ? array : components.toArray (array);
    }
}