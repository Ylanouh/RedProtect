// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

public class Location2I
{
    final int x;
    final int z;
    
    public Location2I(final int x, final int z) {
        this.x = x;
        this.z = z;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash *= 17 + this.x;
        hash *= 29 + this.z;
        return hash;
    }
    
    public long longValue() {
        return (long)this.x << 16 | (long)this.z;
    }
    
    public static long getXZLong(final int x, final int z) {
        return (long)x << 16 | (long)z;
    }
    
    public Location2I getLocationFromLong(final long l) {
        return new Location2I((int)(l >> 16), (int)(l & 0x7FFFFFFFL));
    }
}
