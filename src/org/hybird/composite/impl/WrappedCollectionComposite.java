package org.hybird.composite.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.hybird.composite.IComposite;

public abstract class WrappedCollectionComposite<WrapperType, ComponentType> extends BaseComposite<ComponentType>
{
    public WrappedCollectionComposite (Class<ComponentType> klass)
    {
        super (klass);
    }

    protected Collection<WrapperType> components;

    @Override
    public IComposite<ComponentType> add (ComponentType t)
    {
        if (components == null)
            components = createCollection ();

        components.add (wrap (t));

        return super.add (t);
    }

    protected abstract WrapperType wrap (ComponentType t);
    protected abstract ComponentType unwrap (WrapperType t);
    
    protected abstract Collection<WrapperType> createCollection ();

    @Override
    public IComposite<ComponentType> remove (ComponentType t)
    {
        if (components != null)
        {
            for (Iterator<WrapperType> i = components.iterator (); i.hasNext ();)
            {
                WrapperType wrapper = i.next ();
                ComponentType component = unwrap (wrapper);
                if (component == null || component == t || component.equals (t))
                    i.remove ();
            }
        }

        return super.remove (t);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object [] entries ()
    {
        if (components == null)
            return (ComponentType []) Array.newInstance (klass, 0);
        
        Collection<ComponentType> unwrapped = new ArrayList<ComponentType> (components.size());

        for (Iterator<WrapperType> i = components.iterator (); i.hasNext ();)
        {
            WrapperType wrapper = i.next ();
            ComponentType component = unwrap (wrapper);
            if (component != null)
                unwrapped.add (component);
            else
                i.remove ();
        }
        
        return unwrapped.toArray ((ComponentType []) Array.newInstance (klass, unwrapped.size ()));
    }
}