package org.hybird.composite;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.hybird.composite.impl.TypeReference;

public class CompositeAPITest
{
    public static void main (String [] args)
    {
        IComposite<JComponent> composite = Composites.createArrayListComposite (JComponent.class);
        
        composite.add (new JLabel ());
        composite.add (new JPanel ());
        
        composite.delegate ().setName ("name");
        
        for (JComponent c : composite.components ())
            System.out.println ("c: " + c.getName ());
        
    }
    
    public static interface NodeRef
    {
        void callback();
    }
    
    public static interface NodeRef2 extends NodeRef
    {
        void callback2();
    }
    
    public static void main1 (String [] args)
    {
        List<NodeRef> nodes = new ArrayList<NodeRef>();
        nodes.add (new NodeRef ()
        {

            @Override
            public void callback ()
            {
                System.out.println ("1.callback()");
            }
        });
        nodes.add (new NodeRef ()
        {
            @Override
            public void callback ()
            {
                System.out.println ("2.callback()");
            }
        });
        
        IComposite<NodeRef> composites = Composites.createCollectionComposite (nodes, NodeRef.class);
        
        System.out.println ("Nb of entries: " + composites.components ().length);
        
        composites.delegate ().callback ();
    }
    
    public static void main2 (String [] args)
    {
        IComposite<NodeRef> composites = Composites.createWeakArrayListComposite (NodeRef.class);
        composites.add (new NodeRef ()
        {

            @Override
            public void callback ()
            {
                System.out.println (".callback()");
            }
        });
        composites.add (new NodeRef2 ()
        {
            @Override
            public void callback ()
            {
                System.out.println (".callback()");
            }
            
            @Override
            public void callback2 ()
            {
                System.out.println (".callback2()");
            }
        });
        
        System.gc ();
        
        System.out.println ("Nb of entries: " + composites.components ().length);
        
        composites.delegate ().callback ();
        
        composites.as (NodeRef2.class).callback2 ();
    }
    
    public static interface Z <T>
    {
        void callback (T t);
        
        public static abstract class Impl<T> implements Z<T>
        {
            
        }
    }
    
    public static void main3 (String [] args)
    {
        IComposite<Z <String>> c1 = Composites.createWeakArrayListComposite (new TypeReference<Z <String>> () {});
        
        c1.add (new Z.Impl <String>()
        {
            @Override
            public void callback (String t)
            {
                System.out.println ("String callback(): " + t);
            }
        });
        
        System.gc ();
        
        c1.delegate ().callback ("test");
        
        IComposite<Z <Integer>> c2 = Composites.createWeakArrayListComposite (new TypeReference<Z <Integer>> () {});
        c2.add (new Z.Impl <Integer>()
        {
            @Override
            public void callback (Integer t)
            {
                System.out.println ("Integer callback(): " + t);
            }
        });
        
        System.gc ();
        c2.delegate ().callback (12);
    }
    
    public static interface HierarchicalCompositeLeaf
    {
        void callback();
    }
    
    public static interface HierarchicalCompositeNode
    {
        HierarchicalCompositeLeaf leaf();
    }
    
    public static interface HierarchicalCompositeRoot
    {
        HierarchicalCompositeNode node();
    }
    
    public static void main4 (String [] args)
    {
        IComposite<HierarchicalCompositeRoot> c = Composites.ArrayListComposite (HierarchicalCompositeRoot.class);
        
        c.add (new HierarchicalCompositeRoot()
        {
            @Override
            public HierarchicalCompositeNode node ()
            {
                System.out.println ("1.node()");
                return new HierarchicalCompositeNode()
                {
                    @Override
                    public HierarchicalCompositeLeaf leaf ()
                    {
                        System.out.println ("1.leaf()");
                        return new HierarchicalCompositeLeaf()
                        {
                            @Override
                            public void callback ()
                            {
                                System.out.println ("1.callback()");
                            }
                        };
                    }                    
                };
            }
        });

        c.add (new HierarchicalCompositeRoot()
        {
            @Override
            public HierarchicalCompositeNode node ()
            {
                System.out.println ("2.node()");
                return new HierarchicalCompositeNode()
                {
                    @Override
                    public HierarchicalCompositeLeaf leaf ()
                    {
                        System.out.println ("2.leaf()");
                        return new HierarchicalCompositeLeaf()
                        {
                            @Override
                            public void callback ()
                            {
                                System.out.println ("2.callback()");
                            }
                        };
                    }                    
                };
            }
        });
        
       c.delegate ().node ().leaf ().callback ();
    }
}
