// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import org.bukkit.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RedefineRegionBuilder extends RegionBuilder
{
    public RedefineRegionBuilder(final Player p, final Region old, final Location l1, final Location l2) {
        if (l1 == null || l2 == null) {
            this.setError(p, "One or both of your selection positions aren't set!");
            return;
        }
        final World w = p.getWorld();
        int minX;
        int maxX;
        if (l2.getBlockX() < l1.getBlockX()) {
            minX = l2.getBlockX();
            maxX = l1.getBlockX();
        }
        else {
            maxX = l2.getBlockX();
            minX = l1.getBlockX();
        }
        int minZ;
        int maxZ;
        if (l2.getBlockZ() < l1.getBlockZ()) {
            minZ = l2.getBlockZ();
            maxZ = l1.getBlockZ();
        }
        else {
            maxZ = l2.getBlockZ();
            minZ = l1.getBlockZ();
        }
        for (int xl = minX; xl <= maxX; ++xl) {
            if (RedProtect.rm.regionExists(xl, minZ, w) || RedProtect.rm.regionExists(xl, maxZ, w)) {
                p.sendMessage(ChatColor.RED + "You're overlapping another region.");
                return;
            }
        }
        for (int zl = minZ; zl <= maxZ; ++zl) {
            if (RedProtect.rm.regionExists(minX, zl, w) || RedProtect.rm.regionExists(maxX, zl, w)) {
                this.setError(p, "You're overlapping another region.");
                return;
            }
        }
        final Region r = new Region(old.getName(), old.getOwners(), new int[] { l1.getBlockX(), l1.getBlockX(), l2.getBlockX(), l2.getBlockX() }, new int[] { l1.getBlockZ(), l1.getBlockZ(), l2.getBlockZ(), l2.getBlockZ() });
        if (RedProtect.rm.isSurroundingRegion(r, w)) {
            this.setError(p, "You're overlapping another region.");
            return;
        }
        r.f = old.f.clone();
        super.r = r;
    }
}
