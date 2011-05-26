package org.hybird.composite;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.hybird.composite.impl.IProxy;

public class ImageCompositeTest
{
    private static class ImageX extends BufferedImage
    {
        public ImageX (int width, int height, int imageType)
        {
            super (width, height, imageType);
        }

        private int id;
        
        public ImageX id (int id)
        {
            this.id = id;
            return this;
        }
        
        public int id ()
        {
            return id;
        }
        
        @Override
        public String toString ()
        {
            return "ImageX [id=" + id + "] - " + super.toString ();
        }
    }
    
    public static class Resource
    {
        private int id;
        
        public Resource ()
        {
        }
        
        public Resource (int id)
        {
            this.id = id;
        }

        public Image image()
        {
            return new ImageX (64, 48, BufferedImage.TYPE_INT_ARGB).id (id);
        }
    }
    
    public static void main (String [] args)
    {
        IComposite<Resource> resources = Composites.createArrayListComposite (Resource.class);
        for (int i = 0; i < 10; ++i)
            resources.add (new Resource (i));

        Image images = resources.delegate ().image ();
        
        System.out.println ("images: " + images);
        System.out.println ("images instanceof IProxy: " + IProxy.class.isInstance (images));
        
        IProxy proxy = (IProxy) images;
        Object [] components = proxy.org_hybird_composite_impl_components ();
        
        System.out.println ("Component count: " + components.length);
        
        Image[] array = (Image []) components;
        
        for (Image i : array)
        {
            ImageX x = (ImageX) i;
            System.out.println ("ImageX: " + x.id ());
        }
    }
}
