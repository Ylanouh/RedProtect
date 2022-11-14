// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import java.util.List;
import java.io.Serializable;

public class Region implements Serializable
{
    private static final long serialVersionUID = 3904371508520551177L;
    private int[] x;
    private int[] z;
    private int minMbrX;
    private int maxMbrX;
    private int minMbrZ;
    private int maxMbrZ;
    private String name;
    private List<String> owners;
    private List<String> members;
    private String creator;
    protected boolean[] f;
    
    public void setFlag(final int flag, final boolean value) {
        if (flag > this.f.length) {
            return;
        }
        this.f[flag] = value;
    }
    
    public void setX(final int[] x) {
        this.x = x;
    }
    
    public void setZ(final int[] z) {
        this.z = z;
    }
    
    public void setOwners(final List<String> owners) {
        this.owners = owners;
    }
    
    public void setMembers(final List<String> members) {
        this.members = members;
    }
    
    public void setCreator(final String s) {
        this.creator = s;
    }
    
    public int[] getX() {
        return this.x;
    }
    
    public int[] getZ() {
        return this.z;
    }
    
    public String getCreator() {
        return this.creator;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<String> getOwners() {
        return this.owners;
    }
    
    public List<String> getMembers() {
        return this.members;
    }
    
    public int getCenterX() {
        return (this.minMbrX + this.maxMbrX) / 2;
    }
    
    public int getCenterZ() {
        return ((this.minMbrZ + this.maxMbrZ) / 2.0).intValue();
    }
    
    public int getMaxMbrX() {
        return this.maxMbrX;
    }
    
    public int getMinMbrX() {
        return this.minMbrX;
    }
    
    public int getMaxMbrZ() {
        return this.maxMbrZ;
    }
    
    public int getMinMbrZ() {
        return this.minMbrZ;
    }
    
    public String info() {
        String ownerstring = "";
        String memberstring = "";
        for (int i = 0; i < this.owners.size(); ++i) {
            ownerstring = String.valueOf(ownerstring) + ", " + this.owners.get(i);
        }
        for (int i = 0; i < this.members.size(); ++i) {
            memberstring = String.valueOf(memberstring) + ", " + this.members.get(i);
        }
        if (this.owners.size() > 0) {
            ownerstring = ownerstring.substring(2);
        }
        else {
            ownerstring = "None";
        }
        if (this.members.size() > 0) {
            memberstring = memberstring.substring(2);
        }
        else {
            memberstring = "None";
        }
        return ChatColor.AQUA + "Name: " + ChatColor.GOLD + this.name + ChatColor.AQUA + ", Creator: " + ChatColor.GOLD + this.creator + ChatColor.AQUA + ", Center: [" + ChatColor.GOLD + this.getCenterX() + ChatColor.AQUA + ", " + ChatColor.GOLD + this.getCenterZ() + ChatColor.AQUA + "], Owners: [" + ChatColor.GOLD + ownerstring + ChatColor.AQUA + "], Members: [" + ChatColor.GOLD + memberstring + ChatColor.AQUA + "].";
    }
    
    public Region(final int[] x, final int[] z, final String name, final LinkedList<String> owners, final List<String> members, final String creator, final int maxMbrX, final int minMbrX, final int maxMbrZ, final int minMbrZ, final boolean[] flags) {
        this.minMbrX = 0;
        this.maxMbrX = 0;
        this.minMbrZ = 0;
        this.maxMbrZ = 0;
        this.creator = "";
        this.f = new boolean[] { Flags.pvp, Flags.chest, Flags.lever, Flags.button, Flags.door, Flags.mobs, Flags.passive };
        this.x = x;
        this.z = z;
        this.maxMbrX = maxMbrX;
        this.minMbrX = minMbrX;
        this.maxMbrZ = maxMbrZ;
        this.minMbrZ = minMbrZ;
        this.name = name;
        this.owners = owners;
        this.members = members;
        this.creator = creator;
        this.f = flags;
    }
    
    public Region(final String name, final List<String> list, final int[] x, final int[] z) {
        this.minMbrX = 0;
        this.maxMbrX = 0;
        this.minMbrZ = 0;
        this.maxMbrZ = 0;
        this.creator = "";
        this.f = new boolean[] { Flags.pvp, Flags.chest, Flags.lever, Flags.button, Flags.door, Flags.mobs, Flags.passive };
        final int size = x.length;
        if (size != z.length) {
            throw new Error("The X & Z arrays are different sizes!");
        }
        this.x = x;
        this.z = z;
        if (size < 4) {
            throw new Error("You can't generate a polygon with less then 4 points!");
        }
        if (size == 4) {
            this.x = null;
            this.z = null;
        }
        this.owners = list;
        this.members = new ArrayList<String>();
        this.name = name;
        this.creator = list.get(0);
        this.maxMbrX = x[0];
        this.minMbrX = x[0];
        this.maxMbrZ = z[0];
        this.minMbrZ = z[0];
        for (int i = 0; i < x.length; ++i) {
            if (x[i] > this.maxMbrX) {
                this.maxMbrX = x[i];
            }
            if (x[i] < this.minMbrX) {
                this.minMbrX = x[i];
            }
            if (z[i] > this.maxMbrZ) {
                this.maxMbrZ = z[i];
            }
            if (z[i] < this.minMbrZ) {
                this.minMbrZ = z[i];
            }
        }
    }
    
    public void delete() {
        RedProtect.rm.remove(this);
    }
    
    public int getArea() {
        if (this.x == null) {
            return (this.maxMbrX - this.minMbrX) * (this.maxMbrZ - this.minMbrZ);
        }
        int area = 0;
        for (int i = 0; i < this.x.length; ++i) {
            final int j = (i + 1) % this.x.length;
            area += this.x[i] * this.z[j] - this.z[i] * this.x[j];
        }
        area = Math.abs(area / 2);
        return area;
    }
    
    public boolean inBoundingRect(final int bx, final int bz) {
        return bx <= this.maxMbrX && bx >= this.minMbrX && bz <= this.maxMbrZ && bz >= this.minMbrZ;
    }
    
    public boolean inBoundingRect(final Region other) {
        return other.maxMbrX >= this.minMbrX && other.maxMbrZ >= this.minMbrZ && other.minMbrX <= this.maxMbrX && other.minMbrZ <= this.maxMbrZ;
    }
    
    public boolean intersects(final int bx, final int bz) {
        if (this.x == null) {
            return true;
        }
        boolean ret = false;
        int i = 0;
        int j = this.x.length - 1;
        while (i < this.x.length) {
            if (((this.z[i] <= bz && bz < this.z[j]) || (this.z[j] <= bz && bz < this.z[i])) && bx < (this.x[j] - this.x[i]) * (bz - this.z[i]) / (this.z[j] - this.z[i]) + this.x[i]) {
                ret = !ret;
            }
            j = i++;
        }
        return ret;
    }
    
    public boolean isOwner(String p) {
        p = p.toLowerCase();
        return this.owners.contains(p);
    }
    
    public boolean isOwner(final Player player) {
        return this.owners.contains(player.getName().toLowerCase());
    }
    
    public boolean isMember(String p) {
        p = p.toLowerCase();
        return this.members.contains(p);
    }
    
    public boolean isMember(final Player player) {
        return this.members.contains(player.getName().toLowerCase());
    }
    
    public void addMember(String p) {
        p = p.toLowerCase();
        if (!this.members.contains(p) && !this.owners.contains(p)) {
            this.members.add(p);
        }
    }
    
    public void addOwner(String p) {
        p = p.toLowerCase();
        if (this.members.contains(p)) {
            this.members.remove(p);
        }
        if (!this.owners.contains(p)) {
            this.owners.add(p);
        }
    }
    
    public void removeMember(String p) {
        p = p.toLowerCase();
        if (this.members.contains(p)) {
            this.members.remove(p);
        }
        if (this.owners.contains(p)) {
            this.owners.remove(p);
        }
    }
    
    public void removeOwner(String p) {
        p = p.toLowerCase();
        if (this.owners.contains(p)) {
            this.owners.remove(p);
        }
    }
    
    public boolean getFlag(final int flag) {
        return this.f[flag];
    }
    
    public boolean canBuild(final Player p) {
        return p.getLocation().getY() < RedProtect.heightStart || this.isOwner(p) || this.isMember(p) || RedProtect.ph.hasPerm(p, "redprotect.bypass");
    }
    
    public boolean canPVP(final Player p) {
        return this.f[0] || RedProtect.ph.hasPerm(p, "redprotect.bypass");
    }
    
    public boolean canChest(final Player p) {
        return this.f[1] || this.isOwner(p) || this.isMember(p) || RedProtect.ph.hasPerm(p, "redprotect.bypass");
    }
    
    public boolean canLever(final Player p) {
        return this.f[2] || this.isOwner(p) || this.isMember(p) || RedProtect.ph.hasPerm(p, "redprotect.bypass");
    }
    
    public boolean canButton(final Player p) {
        return this.f[3] || this.isOwner(p) || this.isMember(p) || RedProtect.ph.hasPerm(p, "redprotect.bypass");
    }
    
    public boolean canDoor(final Player p) {
        return this.f[4] || this.isOwner(p) || this.isMember(p) || RedProtect.ph.hasPerm(p, "redprotect.bypass");
    }
    
    public boolean canMobs() {
        return this.f[5];
    }
    
    public boolean canHurtPassives(final Player p) {
        return this.f[6] || this.isOwner(p) || this.isMember(p) || RedProtect.ph.hasPerm(p, "redprotect.bypass");
    }
    
    public int ownersSize() {
        return this.owners.size();
    }
    
    public String getFlagInfo() {
        return ChatColor.AQUA + "Player vs Player: " + ChatColor.GOLD + this.f[0] + ChatColor.AQUA + ", Chest opening: " + ChatColor.GOLD + this.f[1] + ChatColor.AQUA + ", Lever flipping: " + ChatColor.GOLD + this.f[2] + ChatColor.AQUA + ", Button pushing: " + ChatColor.GOLD + this.f[3] + ChatColor.AQUA + ", Door toggling: " + ChatColor.GOLD + this.f[4] + ChatColor.AQUA + ", Monster spawning: " + ChatColor.GOLD + this.f[5] + ChatColor.AQUA + ", Passive entity hurting: " + ChatColor.GOLD + this.f[6];
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
