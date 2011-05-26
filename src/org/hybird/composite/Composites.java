package org.hybird.composite;

import java.util.Collection;
import java.util.EventListener;
import java.util.concurrent.Callable;

import org.hybird.composite.impl.CollectionComposite;
import org.hybird.composite.impl.TypeReference;
import org.hybird.composite.impl.collections.*;
import org.hybird.composite.impl.events.EventListenerComposite;

public final class Composites
{
    private Composites ()
    {
    }

    
    public static <T> IComposite<T> createLinkedListComposite (TypeReference<T> ref)
    {
        return createLinkedListComposite (ref.getRawType ());
    }
    
    public static <T> IComposite<T> LinkedListComposite (Class<T> klass)
    {
        return createLinkedListComposite (klass);
    }
    
    public static <T> IComposite<T> createLinkedListComposite (Class<T> klass)
    {
        return new LinkedListComposite<T> (klass);
    }

    
    public static <T> IComposite<T> createWeakLinkedListComposite (TypeReference<T> ref)
    {
        return createWeakLinkedListComposite (ref.getRawType ());
    }
    
    public static <T> IComposite<T> WeakLinkedListComposite (Class<T> klass)
    {
        return createWeakLinkedListComposite (klass);
    }
    
    public static <T> IComposite<T> createWeakLinkedListComposite (Class<T> klass)
    {
        return new WeakLinkedListComposite<T> (klass);
    }
    
    
    public static <T> IComposite<T> createSoftLinkedListComposite (TypeReference<T> ref)
    {
        return createSoftLinkedListComposite (ref.getRawType ());
    }
    
    public static <T> IComposite<T> SoftLinkedListComposite (Class<T> klass)
    {
        return createSoftLinkedListComposite (klass);
    }
    
    public static <T> IComposite<T> createSoftLinkedListComposite (Class<T> klass)
    {
        return new SoftLinkedListComposite<T> (klass);
    }
    
    
    public static <T> IComposite<T> createArrayListComposite (TypeReference<T> ref)
    {
        return createArrayListComposite (ref.getRawType ());
    }
    
    public static <T> IComposite<T> ArrayListComposite (Class<T> klass)
    {
        return createArrayListComposite (klass);
    }
    
    public static <T> IComposite<T> createArrayListComposite (Class<T> klass)
    {
        return new ArrayListComposite<T> (klass);
    }
    
    public static <T> IComposite<T> createArrayListComposite (Class<T> klass, int initialCapacity)
    {
        return new ArrayListComposite<T> (klass, initialCapacity);
    }
    
    
    public static <T> IComposite<T> createWeakArrayListComposite (TypeReference<T> ref)
    {
        return createWeakArrayListComposite (ref.getRawType ());
    }
    
    public static <T> IComposite<T> WeakArrayListComposite (Class<T> klass)
    {
        return createWeakArrayListComposite (klass);
    }
    
    public static <T> IComposite<T> createWeakArrayListComposite (Class<T> klass)
    {
        return new WeakArrayListComposite<T> (klass);
    }
    
    
    public static <T> IComposite<T> createSoftArrayListComposite (TypeReference<T> ref)
    {
        return createSoftArrayListComposite (ref.getRawType ());
    }
    
    public static <T> IComposite<T> SoftArrayListComposite (Class<T> klass)
    {
        return createSoftArrayListComposite (klass);
    }
    
    public static <T> IComposite<T> createSoftArrayListComposite (Class<T> klass)
    {
        return new SoftArrayListComposite<T> (klass);
    }

    /**
     *  Creates a composite which will put its components into the specified collection with calls to IComposite.add().
     *  If the collection contains elements, they will be used as components, just as you would expect.
     */
    public static <T> IComposite<T> createCollectionComposite (final Collection<T> collection, TypeReference<T> ref)
    {
        return createCollectionComposite (collection, ref.getRawType ());
    }
    
    /**
     *  Creates a composite which will put its components into the specified collection.
     *  If the collection contains elements, they will be used as components, just as you would expect.
     */
    public static <T> IComposite<T> createCollectionComposite (final Collection<T> collection, Class<T> klass)
    {
        CollectionComposite<T> composite = new CollectionComposite<T> (klass)
        {
            @Override
            protected Collection<T> createCollection ()
            {
                return collection;
            }
        };
        
        if (collection.isEmpty () == false)
            composite.prepareCollection ();
        
        return composite;
    }

    public static <T> IComposite<T> createCollectionComposite (final Callable<Collection<T>> builder, TypeReference<T> ref)
    {
        return createCollectionComposite (builder, ref.getRawType ());
    }
    
    /**
     *  Creates a composite which will put its components into the collection the builder will return.
     *  The builder should not return a collection that contains elements or they'll be lost if no IComposite.add() calls are made.
     */
    public static <T> IComposite<T> createCollectionComposite (final Callable<Collection<T>> builder, Class<T> klass)
    {
        return new CollectionComposite<T> (klass)
        {
            @Override
            protected Collection<T> createCollection ()
            {
                try
                {
                    return builder.call ();
                }
                catch (Exception e)
                {
                    throw new IllegalArgumentException ("Exception while creating collection with the specified builder " + builder + ": " + e);
                }
            }
        };
    }

    public static <T extends EventListener> IComposite<T> createEventListenerComposite (TypeReference<T> ref)
    {
        return createEventListenerComposite (ref.getRawType ());
    }
    
    public static <T extends EventListener> IComposite<T> createEventListenerComposite (Class<T> klass)
    {
        return new EventListenerComposite<T> (klass);
    }

    public static <T extends EventListener> IComposite<T> createEventListenerOnEDTComposite (TypeReference<T> ref)
    {
        return createEventListenerOnEDTComposite (ref.getRawType ());
    }
    
    public static <T extends EventListener> IComposite<T> createEventListenerOnEDTComposite (Class<T> klass)
    {
        return new EventListenerComposite<T> (klass, true);
    }
}