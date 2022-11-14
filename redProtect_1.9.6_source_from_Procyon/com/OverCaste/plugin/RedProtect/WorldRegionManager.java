// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import org.bukkit.Location;
import org.bukkit.block.Block;
import java.util.Set;
import org.bukkit.entity.Player;

public interface WorldRegionManager
{
    void load();
    
    void save();
    
    Region getRegion(final String p0);
    
    int getTotalRegionSize(final String p0);
    
    Set<Region> getRegions(final Player p0);
    
    Set<Region> getRegionsNear(final Player p0, final int p1);
    
    Set<Region> getRegions(final String p0);
    
    Region getRegion(final Player p0);
    
    void add(final Region p0);
    
    void remove(final Region p0);
    
    boolean canBuild(final Player p0, final Block p1);
    
    boolean isSurroundingRegion(final Region p0);
    
    boolean regionExists(final Block p0);
    
    Region getRegion(final Location p0);
    
    boolean regionExists(final Region p0);
    
    void setFlagValue(final Region p0, final int p1, final boolean p2);
    
    void setRegionName(final Region p0, final String p1);
    
    boolean regionExists(final int p0, final int p1);
    
    Set<Region> getPossibleIntersectingRegions(final Region p0);
}
