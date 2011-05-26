package org.hybird.composite;

public class FinalClassesTest
{
    private static interface I
    {
        void f();
    }
    
    private static final class A implements I
    {
        public A ()
        {
        }
        
        @Override
        public void f ()
        {
            System.out.println ("A.f()");
        }
    }
    
    private static class B implements I
    {
        public B ()
        {
        }
        
        @Override
        public final void f ()
        {
            System.out.println ("B.f()");
        }
    }
    
    public static void main (String [] args)
    {
        try
        {
            System.out.println ("Creating a proxy on A");
            IComposite<A> a = Composites.createArrayListComposite (A.class);
            
            a.add (new A());
            a.add (new A());
            
            a.delegate ().f (); // will throw an exception
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println ("Creating a proxy on B");
        IComposite<B> b = Composites.createArrayListComposite (B.class);
        
        b.add (new B());
        b.add (new B());
        
        b.delegate ().f (); // will only print one "B.f()" where you should see 2
    }
}
