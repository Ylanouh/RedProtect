// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipException;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.FileWriter;
import org.yaml.snakeyaml.Yaml;
import java.util.Map;
import java.io.File;
import org.bukkit.Location;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.World;
import java.util.HashMap;

public class WorldFlatFileRegionManager implements WorldRegionManager
{
    HashMap<Long, LargeChunkObject> regions;
    World world;
    
    public WorldFlatFileRegionManager(final World world) {
        this.regions = new HashMap<Long, LargeChunkObject>(100);
        this.world = world;
    }
    
    @Override
    public void add(final Region r) {
        final int cMaxX = LargeChunkObject.convertBlockToLCO(r.getMaxMbrX());
        final int cMaxZ = LargeChunkObject.convertBlockToLCO(r.getMaxMbrZ());
        final int cMinX = LargeChunkObject.convertBlockToLCO(r.getMinMbrX());
        final int cMinZ = LargeChunkObject.convertBlockToLCO(r.getMinMbrZ());
        for (int xl = cMinX; xl <= cMaxX; ++xl) {
            for (int zl = cMinZ; zl <= cMaxZ; ++zl) {
                LargeChunkObject lco = this.regions.get(Location2I.getXZLong(xl, zl));
                if (lco == null) {
                    lco = new LargeChunkObject();
                }
                lco.addRegion(r);
                this.regions.put(Location2I.getXZLong(xl, zl), lco);
            }
        }
        this.save();
    }
    
    @Override
    public void remove(final Region r) {
        final int cMaxX = LargeChunkObject.convertBlockToLCO(r.getMaxMbrX());
        final int cMaxZ = LargeChunkObject.convertBlockToLCO(r.getMaxMbrZ());
        final int cMinX = LargeChunkObject.convertBlockToLCO(r.getMinMbrX());
        final int cMinZ = LargeChunkObject.convertBlockToLCO(r.getMinMbrZ());
        for (int xl = cMinX; xl <= cMaxX; ++xl) {
            for (int zl = cMinZ; zl <= cMaxZ; ++zl) {
                final LargeChunkObject lco = this.regions.get(Location2I.getXZLong(xl, zl));
                if (lco != null) {
                    lco.removeRegion(r);
                }
            }
        }
        this.save();
    }
    
    @Override
    public boolean canBuild(final Player p, final Block b) {
        final int bx = b.getX();
        final int bz = b.getZ();
        final LargeChunkObject lco = this.regions.get(LargeChunkObject.getBlockLCOLong(bx, bz));
        if (lco == null) {
            return true;
        }
        if (lco.regions == null) {
            return true;
        }
        for (final Region poly : lco.regions) {
            if (poly.inBoundingRect(bx, bz) && poly.intersects(bx, bz)) {
                return poly.canBuild(p);
            }
        }
        return true;
    }
    
    @Override
    public Set<Region> getRegions(final Player p) {
        return this.getRegions(p.getName());
    }
    
    @Override
    public Set<Region> getRegions(final String p) {
        final Set<Region> ls = new HashSet<Region>();
        for (final LargeChunkObject lco : this.regions.values()) {
            if (lco == null) {
                continue;
            }
            if (lco.regions == null) {
                continue;
            }
            for (final Region r : lco.regions) {
                if (r.getCreator().equalsIgnoreCase(p)) {
                    ls.add(r);
                }
            }
        }
        return ls;
    }
    
    @Override
    public boolean regionExists(final Block b) {
        return this.regionExists(b.getX(), b.getZ());
    }
    
    @Override
    public boolean regionExists(final int x, final int z) {
        final LargeChunkObject lco = this.regions.get(LargeChunkObject.getBlockLCOLong(x, z));
        if (lco == null || lco.regions == null) {
            return false;
        }
        if (lco != null) {
            for (final Region poly : lco.regions) {
                if (poly.inBoundingRect(x, z) && poly.intersects(x, z)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public Region getRegion(final Location l) {
        final int x = l.getBlockX();
        final int z = l.getBlockZ();
        return this.getRegion(x, z);
    }
    
    private Region getRegion(final int x, final int z) {
        final LargeChunkObject lco = this.regions.get(LargeChunkObject.getBlockLCOLong(x, z));
        if (lco == null || lco.regions == null) {
            return null;
        }
        for (final Region poly : lco.regions) {
            if (poly.inBoundingRect(x, z) && poly.intersects(x, z)) {
                return poly;
            }
        }
        return null;
    }
    
    @Override
    public Region getRegion(final Player p) {
        return this.getRegion(p.getLocation());
    }
    
    @Override
    public Region getRegion(final String name) {
        if (name == null) {
            return null;
        }
        for (final LargeChunkObject lco : this.regions.values()) {
            if (lco != null) {
                if (lco.regions == null) {
                    continue;
                }
                for (final Region r : lco.regions) {
                    if (r.getName().equals(name)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public void save() {
        try {
            RedProtect.logger.debug("RegionManager.Save(): File type is " + RedProtect.fileType.toString());
            final String world = this.getWorld().getName();
            final File datf = new File(RedProtect.pathData, "data_" + world + ".regions");
            if (!datf.exists()) {
                datf.createNewFile();
            }
            final HashMap<Long, Set<String>> lcos = new HashMap<Long, Set<String>>(this.regions.size());
            for (final Map.Entry<Long, LargeChunkObject> e : this.regions.entrySet()) {
                if (e != null && e.getValue() != null) {
                    if (e.getValue().regions == null) {
                        continue;
                    }
                    final HashSet<String> newRegions = new HashSet<String>();
                    for (final Region r : e.getValue().regions) {
                        newRegions.add(r.getName());
                    }
                    lcos.put(e.getKey(), newRegions);
                }
            }
            final HashMap<String, Region> newRegions2 = new HashMap<String, Region>();
            for (final LargeChunkObject lco : this.regions.values()) {
                if (lco.regions == null) {
                    continue;
                }
                for (final Region r : lco.regions) {
                    newRegions2.put(r.getName(), r);
                }
            }
            final Yaml yml;
            final Yaml ymlgz;
            final ObjectOutputStream oos;
            final ObjectOutputStream oosgz;
            switch (RedProtect.fileType) {
                case yml: {
                    this.backupRegions(datf);
                    yml = new Yaml();
                    yml.dump((Object)newRegions2, (Writer)new FileWriter(datf));
                    break;
                }
                case ymlgz: {
                    this.backupRegions(datf);
                    ymlgz = new Yaml();
                    ymlgz.dump((Object)this.regions, (Writer)new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(datf))));
                    break;
                }
                case oos: {
                    this.backupRegions(datf);
                    oos = new ObjectOutputStream(new FileOutputStream(datf));
                    oos.writeObject(lcos);
                    oos.writeObject(newRegions2);
                    oos.close();
                    break;
                }
                case oosgz: {
                    this.backupRegions(datf);
                    oosgz = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(datf)));
                    oosgz.writeObject(lcos);
                    oosgz.writeObject(newRegions2);
                    oosgz.close();
                    break;
                }
            }
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        catch (Exception e4) {
            e4.printStackTrace();
        }
    }
    
    private void backupRegions(final File datf) {
        if (!RedProtect.backup) {
            return;
        }
        final File dataBackup = new File(RedProtect.pathData, "data_" + this.getWorld().getName() + ".regions.backup");
        dataBackup.delete();
        datf.renameTo(dataBackup);
        try {
            datf.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int getTotalRegionSize(String p) {
        if (p == null) {
            return 0;
        }
        p = p.toLowerCase();
        int total = 0;
        final Set<Region> regions = new HashSet<Region>();
        for (final LargeChunkObject lco : this.regions.values()) {
            if (lco.regions == null) {
                continue;
            }
            for (final Region r : lco.regions) {
                regions.add(r);
            }
        }
        for (final Region r2 : regions) {
            if (r2.getCreator().equals(p)) {
                total += r2.getArea();
            }
        }
        return total;
    }
    
    @Override
    public boolean isSurroundingRegion(final Region r) {
        if (r == null) {
            return false;
        }
        for (final LargeChunkObject lco : this.getRegionLcos(r)) {
            if (lco != null) {
                if (lco.regions == null) {
                    continue;
                }
                for (final Region other : lco.regions) {
                    if (other != null && r.inBoundingRect(other.getCenterX(), other.getCenterZ()) && r.intersects(other.getCenterX(), other.getCenterZ())) {
                        System.out.println("Intersecting!: " + other.info());
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void load() {
        final String world = this.getWorld().getName();
        this.load(String.valueOf(RedProtect.pathData) + "data_" + world + ".regions");
    }
    
    private void load(final String path) {
        final String world = this.getWorld().getName();
        final String datbackf = String.valueOf(RedProtect.pathData) + "data_" + world + ".regions.backup";
        final File f;
        if (!(f = new File(path)).exists()) {
            try {
                f.createNewFile();
                RedProtect.logger.info("Created new world region file: (" + path + ").");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (!RPUtil.isFileEmpty(path)) {
                ObjectInputStream ois = null;
                final Yaml yml;
                final Yaml ymlgz;
                switch (RedProtect.fileType) {
                    case yml: {
                        RedProtect.logger.debug("RegionManager.load() File type: yml");
                        yml = new Yaml();
                        final Object oyml;
                        if ((oyml = yml.load((InputStream)new FileInputStream(path))) instanceof HashMap) {
                            this.regions = (HashMap<Long, LargeChunkObject>)oyml;
                            break;
                        }
                        break;
                    }
                    case ymlgz: {
                        RedProtect.logger.debug("RegionManager.load() File type: ymlgz");
                        ymlgz = new Yaml();
                        final Object oymlgz;
                        if ((oymlgz = ymlgz.load((InputStream)new GZIPInputStream(new FileInputStream(path)))) instanceof HashMap) {
                            this.regions = (HashMap<Long, LargeChunkObject>)oymlgz;
                            break;
                        }
                        break;
                    }
                    case oosgz: {
                        ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(path)));
                    }
                    case oos: {
                        RedProtect.logger.debug("RegionManager.load() File type: oos");
                        if (ois == null) {
                            ois = new ObjectInputStream(new FileInputStream(path));
                        }
                        Object oois = ois.readObject();
                        HashMap<Long, Set<String>> lcos;
                        if (oois instanceof HashMap) {
                            lcos = (HashMap<Long, Set<String>>)oois;
                        }
                        else {
                            lcos = null;
                        }
                        oois = ois.readObject();
                        HashMap<String, Region> newRegions;
                        if (oois instanceof HashMap) {
                            newRegions = (HashMap<String, Region>)oois;
                        }
                        else {
                            newRegions = null;
                        }
                        ois.close();
                        this.regions = new HashMap<Long, LargeChunkObject>(lcos.size());
                        for (final Map.Entry<Long, Set<String>> ss : lcos.entrySet()) {
                            this.regions.put(ss.getKey(), new LargeChunkObject(newRegions, ss.getValue()));
                        }
                        break;
                    }
                }
            }
            else {
                if (RedProtect.backup && this.backupExists() && !path.equalsIgnoreCase(datbackf)) {
                    this.load(datbackf);
                    RedProtect.logger.info("Main data file is blank, Reading from backup.");
                    return;
                }
                RedProtect.logger.info("Creating a new data file.");
                this.regions = new HashMap<Long, LargeChunkObject>();
            }
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (ZipException e5) {
            if (RedProtect.backup && this.backupExists() && !path.equalsIgnoreCase(datbackf)) {
                this.load(datbackf);
                RedProtect.logger.info("The data file is corrupt. Loading from backup.");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e3) {
            e3.printStackTrace();
        }
        catch (Exception e4) {
            e4.printStackTrace();
        }
    }
    
    private boolean backupExists() {
        final String world = this.getWorld().getName();
        final String datbackf = String.valueOf(RedProtect.pathData) + "data_" + world + ".regions.backup";
        return new File(datbackf).exists();
    }
    
    @Override
    public Set<Region> getRegionsNear(final Player player, final int area) {
        final int px = player.getLocation().getBlockX();
        final int pz = player.getLocation().getBlockZ();
        final int cmaxX = LargeChunkObject.convertBlockToLCO(px + area);
        final int cmaxZ = LargeChunkObject.convertBlockToLCO(pz + area);
        final int cminX = LargeChunkObject.convertBlockToLCO(px - area);
        final int cminZ = LargeChunkObject.convertBlockToLCO(pz - area);
        final Set<Region> ret = new HashSet<Region>();
        for (int xl = cminX; xl <= cmaxX; ++xl) {
            for (int zl = cminZ; zl <= cmaxZ; ++zl) {
                final LargeChunkObject lco = this.regions.get(Location2I.getXZLong(xl, zl));
                if (lco != null) {
                    if (lco.regions != null) {
                        for (final Region poly : lco.regions) {
                            if (Math.abs(poly.getCenterX() - px) <= area && Math.abs(poly.getCenterZ() - pz) <= area) {
                                ret.add(poly);
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }
    
    @Override
    public boolean regionExists(final Region region) {
        if (region == null) {
            return false;
        }
        for (final LargeChunkObject lco : this.regions.values()) {
            if (lco.regions == null) {
                continue;
            }
            for (final Region r : lco.regions) {
                if (r != null && r.getName().equalsIgnoreCase(region.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    @Override
    public void setFlagValue(final Region region, final int flag, final boolean value) {
        region.setFlag(flag, value);
    }
    
    @Override
    public void setRegionName(final Region region, final String name) {
        region.setName(name);
    }
    
    @Override
    public Set<Region> getPossibleIntersectingRegions(final Region r) {
        final Set<Region> ret = new HashSet<Region>();
        if (r == null) {
            return ret;
        }
        final int cmaxX = LargeChunkObject.convertBlockToLCO(r.getMaxMbrX());
        final int cmaxZ = LargeChunkObject.convertBlockToLCO(r.getMaxMbrZ());
        final int cminX = LargeChunkObject.convertBlockToLCO(r.getMinMbrX());
        final int cminZ = LargeChunkObject.convertBlockToLCO(r.getMinMbrZ());
        for (int xl = cminX; xl <= cmaxX; ++xl) {
            for (int zl = cminZ; zl <= cmaxZ; ++zl) {
                final LargeChunkObject lco = this.regions.get(Location2I.getXZLong(xl, zl));
                if (lco != null) {
                    if (lco.regions != null) {
                        for (final Region other : lco.regions) {
                            if (r.inBoundingRect(other)) {
                                ret.add(other);
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }
    
    public List<LargeChunkObject> getRegionLcos(final Region r) {
        final List<LargeChunkObject> ret = new LinkedList<LargeChunkObject>();
        final int cmaxX = LargeChunkObject.convertBlockToLCO(r.getMaxMbrX());
        final int cmaxZ = LargeChunkObject.convertBlockToLCO(r.getMaxMbrZ());
        final int cminX = LargeChunkObject.convertBlockToLCO(r.getMinMbrX());
        final int cminZ = LargeChunkObject.convertBlockToLCO(r.getMinMbrZ());
        for (int xl = cminX; xl <= cmaxX; ++xl) {
            for (int zl = cminZ; zl <= cmaxZ; ++zl) {
                final LargeChunkObject lco = this.regions.get(Location2I.getXZLong(xl, zl));
                if (lco != null) {
                    if (lco.regions != null) {
                        ret.add(lco);
                    }
                }
            }
        }
        return ret;
    }
}
