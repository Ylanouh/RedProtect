// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

public class LargeChunkObject
{
    Set<Region> regions;
    
    public LargeChunkObject(final HashMap<String, Region> regionValues, final Set<String> values) {
        this.regions = new HashSet<Region>(values.size());
        for (final String s : values) {
            this.regions.add(regionValues.get(s));
        }
    }
    
    public LargeChunkObject() {
        this.regions = new HashSet<Region>();
    }
    
    public void addRegion(final Region r) {
        if (this.regions == null) {
            this.regions = new HashSet<Region>(10);
        }
        this.regions.add(r);
    }
    
    public void removeRegion(final Region r) {
        if (this.regions == null) {
            return;
        }
        this.regions.remove(r);
        if (this.regions.size() <= 0) {
            this.regions = null;
        }
    }
    
    public boolean isNull() {
        return this.regions == null;
    }
    
    public static int convertBlockToLCO(final int i) {
        int ie = i / 512;
        if (ie < 0) {
            --ie;
        }
        return ie;
    }
    
    public static long getBlockLCOLong(final int x, final int z) {
        int xe = x / 512;
        if (xe < 0) {
            --xe;
        }
        int ze = z / 512;
        if (ze < 0) {
            --ze;
        }
        return Location2I.getXZLong(xe, ze);
    }
    
    public static long getChunkLCOLong(final int x, final int z) {
        int xe = x / 32;
        if (xe < 0) {
            --xe;
        }
        int ze = x / 32;
        if (ze < 0) {
            --ze;
        }
        return Location2I.getXZLong(xe, ze);
    }
}
