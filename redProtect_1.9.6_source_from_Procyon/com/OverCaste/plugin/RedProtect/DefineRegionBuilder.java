// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import java.util.List;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DefineRegionBuilder extends RegionBuilder
{
    public DefineRegionBuilder(final Player p, final Location loc1, final Location loc2, String regionName, String creator) {
        final String pName = p.getName().toLowerCase();
        creator = creator.toLowerCase();
        if (regionName.equals("")) {
            int i = 0;
            while (true) {
                if (p.getName().length() > 13) {
                    regionName = String.valueOf(p.getName().substring(0, 13)) + "_" + i;
                }
                else {
                    regionName = String.valueOf(p.getName()) + "_" + i;
                }
                if (RedProtect.rm.getRegion(regionName, p.getWorld()) == null) {
                    break;
                }
                ++i;
            }
            if (regionName.length() > 16) {
                p.sendMessage(ChatColor.RED + "Couldn't generate automatic region regionName, please regionName it yourself.");
                return;
            }
        }
        if (loc1 == null || loc2 == null) {
            p.sendMessage(ChatColor.RED + "One or both of your selection positions aren't set!");
            return;
        }
        if (RedProtect.rm.getRegion(regionName, p.getWorld()) != null) {
            p.sendMessage(ChatColor.RED + "That regionName is already taken, please choose another one.");
            return;
        }
        if (regionName.length() < 2 || regionName.length() > 16) {
            p.sendMessage(ChatColor.RED + "Invalid regionName, place a 2-16 character regionName in the 2nd row.");
            return;
        }
        int minX;
        int maxX;
        if (loc2.getBlockX() < loc1.getBlockX()) {
            minX = loc2.getBlockX();
            maxX = loc1.getBlockX();
        }
        else {
            maxX = loc2.getBlockX();
            minX = loc1.getBlockX();
        }
        int minZ;
        int maxZ;
        if (loc2.getBlockZ() < loc1.getBlockZ()) {
            minZ = loc2.getBlockZ();
            maxZ = loc1.getBlockZ();
        }
        else {
            maxZ = loc2.getBlockZ();
            minZ = loc1.getBlockZ();
        }
        for (int xl = minX; xl <= maxX; ++xl) {
            if (RedProtect.rm.regionExists(xl, minZ, p.getWorld()) || RedProtect.rm.regionExists(xl, maxZ, p.getWorld())) {
                this.setError(p, ChatColor.RED + "You're overlapping another region.");
                return;
            }
        }
        for (int zl = minZ; zl <= maxZ; ++zl) {
            if (RedProtect.rm.regionExists(minX, zl, p.getWorld()) || RedProtect.rm.regionExists(maxX, zl, p.getWorld())) {
                this.setError(p, ChatColor.RED + "You're overlapping another region.");
                return;
            }
        }
        final LinkedList<String> owners = new LinkedList<String>();
        owners.add(creator);
        if (!pName.equals(creator)) {
            owners.add(pName);
        }
        final Region r = new Region(regionName, owners, new int[] { loc1.getBlockX(), loc1.getBlockX(), loc2.getBlockX(), loc2.getBlockX() }, new int[] { loc1.getBlockZ(), loc1.getBlockZ(), loc2.getBlockZ(), loc2.getBlockZ() });
        if (RedProtect.rm.isSurroundingRegion(r, p.getWorld())) {
            p.sendMessage(ChatColor.RED + "You're overlapping another region.");
            return;
        }
        super.r = r;
    }
}
