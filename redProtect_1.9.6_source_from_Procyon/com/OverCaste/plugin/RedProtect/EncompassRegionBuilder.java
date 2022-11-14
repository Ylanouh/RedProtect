// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.World;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.LinkedList;
import org.bukkit.event.block.SignChangeEvent;

public class EncompassRegionBuilder extends RegionBuilder
{
    public EncompassRegionBuilder(final SignChangeEvent e) {
        final String owner1 = e.getLine(2).toLowerCase();
        final String owner2 = e.getLine(3).toLowerCase();
        final Block b = e.getBlock();
        final World w = b.getWorld();
        final Player p = e.getPlayer();
        final String pName = p.getName().toLowerCase();
        Block last = b;
        Block current = b;
        Block next = null;
        Block first = null;
        String regionName = e.getLine(1);
        final List<Integer> px = new LinkedList<Integer>();
        final List<Integer> pz = new LinkedList<Integer>();
        Block bFirst1 = null;
        Block bFirst2 = null;
        final List<Block> redstone = new LinkedList<Block>();
        int oldFacing = 0;
        int curFacing = 0;
        if (regionName.equals("")) {
            int i = 0;
            while (true) {
                if (pName.length() > 13) {
                    regionName = String.valueOf(pName.substring(0, 13)) + "_" + i;
                }
                else {
                    regionName = String.valueOf(pName) + "_" + i;
                }
                if (RedProtect.rm.getRegion(regionName, w) == null) {
                    break;
                }
                ++i;
            }
            if (regionName.length() > 16) {
                this.setErrorSign(e, "Couldn't generate automatic region name, please name it yourself.");
                return;
            }
        }
        if (RedProtect.rm.getRegion(regionName, w) != null) {
            this.setErrorSign(e, "That name is already taken, please choose another one.");
            return;
        }
        if (regionName.length() < 2 || regionName.length() > 16) {
            this.setErrorSign(e, "Invalid name, place a 2-16 character name in the 2nd row.");
            return;
        }
        if (regionName.contains(" ")) {
            this.setErrorSign(e, "The name of the region can't have a space in it.");
            return;
        }
        for (int i = 0; i < RedProtect.maxScan; ++i) {
            int nearbyCount = 0;
            final int x = current.getX();
            final int y = current.getY();
            final int z = current.getZ();
            int blockSize = 6;
            Block[] block;
            if (RedProtect.blockID == 55) {
                block = new Block[12];
                blockSize = 12;
                block[0] = w.getBlockAt(x + 1, y, z);
                block[1] = w.getBlockAt(x - 1, y, z);
                block[2] = w.getBlockAt(x, y, z + 1);
                block[3] = w.getBlockAt(x, y, z - 1);
                block[4] = w.getBlockAt(x + 1, y + 1, z);
                block[5] = w.getBlockAt(x - 1, y + 1, z);
                block[6] = w.getBlockAt(x, y + 1, z + 1);
                block[7] = w.getBlockAt(x, y + 1, z - 1);
                block[8] = w.getBlockAt(x + 1, y - 1, z);
                block[9] = w.getBlockAt(x - 1, y - 1, z);
                block[10] = w.getBlockAt(x, y - 1, z + 1);
                block[11] = w.getBlockAt(x, y - 1, z - 1);
            }
            else if (RedProtect.blockID == 85 || RedProtect.blockID == 101 || RedProtect.blockID == 113) {
                block = new Block[6];
                blockSize = 6;
                block[0] = w.getBlockAt(x + 1, y, z);
                block[1] = w.getBlockAt(x - 1, y, z);
                block[2] = w.getBlockAt(x, y, z + 1);
                block[3] = w.getBlockAt(x, y, z - 1);
                block[4] = w.getBlockAt(x, y - 1, z);
                block[5] = w.getBlockAt(x, y + 1, z);
            }
            else {
                block = new Block[6];
                blockSize = 6;
                block[0] = w.getBlockAt(x + 1, y, z);
                block[1] = w.getBlockAt(x - 1, y, z);
                block[2] = w.getBlockAt(x, y, z + 1);
                block[3] = w.getBlockAt(x, y, z - 1);
                block[4] = w.getBlockAt(x, y - 1, z);
                block[5] = w.getBlockAt(x, y + 1, z);
            }
            for (int bi = 0; bi < blockSize; ++bi) {
                boolean validBlock = false;
                validBlock = ((RedProtect.blockID == 85 && (block[bi].getType().equals((Object)Material.FENCE) || block[bi].getType().equals((Object)Material.FENCE_GATE))) || block[bi].getTypeId() == RedProtect.blockID);
                if (validBlock && !block[bi].getLocation().equals((Object)last.getLocation())) {
                    ++nearbyCount;
                    next = block[bi];
                    curFacing = bi % 4;
                    if (i == 1) {
                        if (nearbyCount == 1) {
                            bFirst1 = block[bi];
                        }
                        if (nearbyCount == 2) {
                            bFirst2 = block[bi];
                        }
                    }
                }
            }
            if (nearbyCount == 1) {
                if (i != 0) {
                    redstone.add(current);
                    if (current.equals(first)) {
                        final LinkedList<String> owners = new LinkedList<String>();
                        owners.add(pName);
                        if (!owner1.equals("")) {
                            if (owner1.contains(" ")) {
                                this.setErrorSign(e, "The first sign owner's name is invalid.");
                                return;
                            }
                            if (owner1.equals(pName)) {
                                p.sendMessage(ChatColor.YELLOW + "[RP]: You don't need to enter your name manually, it's added automatically.");
                            }
                            else {
                                owners.add(owner1);
                            }
                        }
                        if (!owner1.equals("")) {
                            if (owner2.contains(" ")) {
                                this.setErrorSign(e, "The second sign owner's name is invalid.");
                                return;
                            }
                            if (owner2.equals(pName)) {
                                p.sendMessage(ChatColor.YELLOW + "[RP]: You don't need to enter your name manually, it's added automatically.");
                            }
                            else {
                                owners.add(owner2);
                            }
                        }
                        final int[] rx = new int[px.size()];
                        final int[] rz = new int[pz.size()];
                        int bl = 0;
                        for (final int bx : px) {
                            rx[bl] = bx;
                            ++bl;
                        }
                        bl = 0;
                        for (final int bz : pz) {
                            rz[bl] = bz;
                            ++bl;
                        }
                        for (final Block ib : redstone) {
                            final Region other = RedProtect.rm.getRegion(ib.getLocation());
                            if (other != null) {
                                this.setErrorSign(e, "You're overlapping another region. (" + other.getName() + ")");
                                return;
                            }
                        }
                        final Region region = new Region(regionName, owners, rx, rz);
                        if (RedProtect.rm.isSurroundingRegion(region, w)) {
                            this.setErrorSign(e, "You're completely surrounding another region.");
                            return;
                        }
                        final int pLimit = RedProtect.ph.getPlayerLimit(p);
                        final boolean areaUnlimited = RedProtect.ph.hasPerm(p, "RedProtect.unlimited");
                        final int totalArea = RedProtect.rm.getTotalRegionSize(p.getName());
                        if (pLimit >= 0 && totalArea + region.getArea() > pLimit && !areaUnlimited) {
                            this.setErrorSign(e, "You can't make any more regions because you've reached the maximum area alotted per player.");
                            return;
                        }
                        p.sendMessage(ChatColor.AQUA + "Your area used: " + ChatColor.GOLD + (totalArea + region.getArea()) + ChatColor.AQUA + ", left: " + ChatColor.GOLD + (areaUnlimited ? "unlimited" : Integer.valueOf(pLimit - (totalArea + region.getArea()))) + ChatColor.AQUA + ".");
                        if (RedProtect.dropType == RedProtect.DROP_TYPE.drop) {
                            b.breakNaturally();
                            for (final Block rb : redstone) {
                                rb.breakNaturally();
                            }
                        }
                        else if (RedProtect.dropType == RedProtect.DROP_TYPE.remove) {
                            b.setTypeId(0);
                            for (final Block rb : redstone) {
                                rb.setTypeId(0);
                            }
                        }
                        super.r = region;
                        return;
                    }
                }
            }
            else if (i == 1 && nearbyCount == 2) {
                redstone.add(current);
                first = current;
                final int x2 = bFirst1.getX();
                final int z2 = bFirst1.getZ();
                final int x3 = bFirst2.getX();
                final int z3 = bFirst2.getZ();
                final int distx = Math.abs(x2 - x3);
                final int distz = Math.abs(z2 - z3);
                if ((distx != 2 || distz != 0) && (distz != 2 || distx != 0)) {
                    px.add(current.getX());
                    pz.add(current.getZ());
                }
            }
            else if (i != 0) {
                this.setErrorSign(e, "Error in your area at: (x: " + current.getX() + ", y: " + current.getY() + ", z: " + current.getZ() + "). Press f3 to look there for those coordinates and make sure there isn't 3 blocks touching.");
                return;
            }
            if (oldFacing != curFacing && i > 1) {
                px.add(current.getX());
                pz.add(current.getZ());
            }
            last = current;
            if (next == null) {
                this.setErrorSign(e, "Put your sign next to the block you want. There is no viable block next to your sign.");
                return;
            }
            current = next;
            oldFacing = curFacing;
        }
        this.setErrorSign(e, "That area is too big!");
    }
}
