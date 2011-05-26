package org.hybird.composite;

/* pvr enregistrer des equivalences ?
ex: pour des properties, je ne veux pas creer des composites des classes derivant des types de properties. Si par ex, une property est un Int
j'aimerais creer un composite sur IProperty<Integer> et non pas Int ou une classe en derivant.
Les equivalences, ou alors une classe/interface composite particulière pour une classe donnée pourrait remedier a ce problème
*/

public interface IComposite <T>
{
   IComposite<T> add (T component);
   IComposite<T> remove (T component);
   
   T[] components();
   
   T delegate();
   <U extends T> U as (Class<U> klass);
   
   Class<T> delegateClass();
   
   void setListener (Listener<T> listener);
   
   public static interface Listener<T>
   {
       void delegateCreated (T delegate);
   }
}