// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import org.bukkit.Location;
import org.bukkit.block.Block;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import java.util.Iterator;
import org.bukkit.Bukkit;
import java.util.HashMap;
import org.bukkit.World;
import java.util.Map;

public class RegionManager
{
    Map<World, WorldRegionManager> regionManagers;
    
    public RegionManager() {
        this.regionManagers = new HashMap<World, WorldRegionManager>();
    }
    
    public void loadAll() throws Exception {
        for (final World w : Bukkit.getWorlds()) {
            if (this.regionManagers.containsKey(w)) {
                continue;
            }
            WorldRegionManager mgr;
            if (RedProtect.fileType == RedProtect.FILE_TYPE.mysql) {
                mgr = new WorldMySQLRegionManager(w);
            }
            else {
                mgr = new WorldFlatFileRegionManager(w);
            }
            mgr.load();
            this.regionManagers.put(w, mgr);
        }
    }
    
    public void load(final World w) throws Exception {
        if (this.regionManagers.containsKey(w)) {
            return;
        }
        WorldRegionManager mgr;
        if (RedProtect.fileType == RedProtect.FILE_TYPE.mysql) {
            mgr = new WorldMySQLRegionManager(w);
        }
        else {
            mgr = new WorldFlatFileRegionManager(w);
        }
        mgr.load();
        this.regionManagers.put(w, mgr);
    }
    
    public void unload(final World w) {
        if (!this.regionManagers.containsKey(w)) {
            return;
        }
        final WorldRegionManager mgr = this.regionManagers.get(w);
        mgr.save();
        this.regionManagers.remove(w);
    }
    
    public void saveAll() {
        final Iterator<WorldRegionManager> rms = this.regionManagers.values().iterator();
        while (rms.hasNext()) {
            rms.next().save();
        }
    }
    
    public void save(final World w) {
        this.regionManagers.get(w).save();
    }
    
    public Region getRegion(final String name, final World w) {
        return this.regionManagers.get(w).getRegion(name);
    }
    
    public int getTotalRegionSize(final String name) {
        int ret = 0;
        final Iterator<WorldRegionManager> rms = this.regionManagers.values().iterator();
        while (rms.hasNext()) {
            ret += rms.next().getTotalRegionSize(name);
        }
        return ret;
    }
    
    public Set<Region> getWorldRegions(final Player player, final World w) {
        return this.regionManagers.get(w).getRegions(player);
    }
    
    public Set<Region> getRegions(final String player) {
        final Set<Region> ret = new HashSet<Region>();
        final Iterator<WorldRegionManager> rms = this.regionManagers.values().iterator();
        while (rms.hasNext()) {
            ret.addAll(rms.next().getRegions(player));
        }
        return ret;
    }
    
    public Set<Region> getRegions(final Player player) {
        return this.getRegions(player.getName());
    }
    
    public Set<Region> getRegionsNear(final Player player, final int i, final World w) {
        return this.regionManagers.get(w).getRegionsNear(player, i);
    }
    
    public Set<Region> getRegions(final String string, final World w) {
        return this.regionManagers.get(w).getRegions(string);
    }
    
    public Region getRegion(final Player player, final World w) {
        return this.regionManagers.get(w).getRegion(player);
    }
    
    public void add(final Region region, final World w) {
        this.regionManagers.get(w).add(region);
    }
    
    public void remove(final Region reg) {
        final Iterator<WorldRegionManager> rms = this.regionManagers.values().iterator();
        while (rms.hasNext()) {
            rms.next().remove(reg);
        }
    }
    
    public boolean canBuild(final Player p, final Block b, final World w) {
        return this.regionManagers.get(w).canBuild(p, b);
    }
    
    public boolean isSurroundingRegion(final Region rect, final World w) {
        return this.regionManagers.get(w).isSurroundingRegion(rect);
    }
    
    public boolean regionExists(final Block block, final World w) {
        return this.regionManagers.get(w).regionExists(block);
    }
    
    public boolean regionExists(final int x, final int z, final World w) {
        return this.regionManagers.get(w).regionExists(x, z);
    }
    
    public Region getRegion(final Location location) {
        return this.regionManagers.get(location.getWorld()).getRegion(location);
    }
    
    public Set<Region> getPossibleIntersectingRegions(final Region r, final World w) {
        return this.regionManagers.get(w).getPossibleIntersectingRegions(r);
    }
    
    public void rename(final Region rect, final String name, final World world) {
        final WorldRegionManager rm = this.regionManagers.get(world);
        if (!rm.regionExists(rect)) {
            return;
        }
        rm.setRegionName(rect, name);
    }
    
    public void setFlag(final Region rect, final int flag, final boolean value, final World world) {
        final WorldRegionManager rm = this.regionManagers.get(world);
        if (!rm.regionExists(rect)) {
            return;
        }
        rm.setFlagValue(rect, flag, value);
    }
}
