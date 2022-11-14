// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import java.sql.DriverManager;
import org.bukkit.World;
import java.sql.Connection;

public class WorldMySQLRegionManager implements WorldRegionManager
{
    static String url;
    static final String baseurl = "jdbc:mysql://";
    static final String driver = "com.mysql.jdbc.Driver";
    static String dbname;
    static boolean dbexists;
    Connection dbcon;
    
    static {
        WorldMySQLRegionManager.url = "jdbc:mysql://localhost/";
        WorldMySQLRegionManager.dbexists = false;
    }
    
    public WorldMySQLRegionManager(final World w) throws Exception {
        this.dbcon = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e2) {
            RedProtect.logger.severe("Couldn't find the driver for MySQL! com.mysql.jdbc.Driver.");
            RedProtect.plugin.disable();
            return;
        }
        WorldMySQLRegionManager.dbname = String.valueOf(RedProtect.mysqlDatabaseName) + "_" + w.getName();
        Statement st = null;
        try {
            if (!this.checkDBExists()) {
                Connection con = DriverManager.getConnection(WorldMySQLRegionManager.url, RedProtect.mysqlUserName, RedProtect.mysqlUserPass);
                st = con.createStatement();
                st.executeUpdate("CREATE DATABASE " + WorldMySQLRegionManager.dbname);
                RedProtect.logger.info("Created database \"" + WorldMySQLRegionManager.dbname + "\"!");
                st.close();
                st = null;
                con = DriverManager.getConnection(String.valueOf(WorldMySQLRegionManager.url) + WorldMySQLRegionManager.dbname, RedProtect.mysqlUserName, RedProtect.mysqlUserPass);
                st = con.createStatement();
                st.executeUpdate("CREATE TABLE Region(uid int AUTO_INCREMENT PRIMARY KEY, name varchar(16), creator varchar(16), maxMbrX int, minMbrX int, maxMbrZ int, minMbrZ int, centerX int, centerZ int, pvp boolean, chest boolean, lever boolean, button boolean, door boolean, mobs boolean, animals boolean)");
                st.close();
                st = null;
                RedProtect.logger.info("Created table: 'Region'!");
                st = con.createStatement();
                st.executeUpdate("CREATE TABLE Owner(uid int AUTO_INCREMENT PRIMARY KEY, name varchar(16))");
                st.close();
                st = null;
                RedProtect.logger.info("Created table: 'Owner'!");
                st = con.createStatement();
                st.executeUpdate("CREATE TABLE Member(uid int AUTO_INCREMENT PRIMARY KEY, name varchar(16))");
                st.close();
                st = null;
                RedProtect.logger.info("Created table: 'Member'!");
                st = con.createStatement();
                st.executeUpdate("CREATE TABLE Region_Members(region_uid int, member_uid int)");
                st.close();
                st = null;
                RedProtect.logger.info("Created table: 'Region_Members'!");
                st = con.createStatement();
                st.executeUpdate("CREATE TABLE Region_Owners(region_uid int, owner_uid int)");
                st.close();
                st = null;
                RedProtect.logger.info("Created table: 'Region_Owners'!");
                st = con.createStatement();
                st.executeUpdate("CREATE TABLE Region_Points(region_uid int, x int, z int, seq_no int)");
                st.close();
                RedProtect.logger.info("Created table: 'Region_Points'!");
            }
            this.dbcon = DriverManager.getConnection(String.valueOf(WorldMySQLRegionManager.url) + WorldMySQLRegionManager.dbname, RedProtect.mysqlUserName, RedProtect.mysqlUserPass);
        }
        catch (CommunicationsException e3) {
            RedProtect.logger.severe("Couldn't connect to mysql! Make sure you have mysql turned on and installed properly, and the service is started.");
            throw new Exception("Couldn't connect to mysql!");
        }
        catch (SQLException e) {
            e.printStackTrace();
            RedProtect.logger.severe("There was an error while parsing SQL, redProtect will shut down to avoid further damage.");
            throw new Exception("SQLException!");
        }
        finally {
            if (st != null) {
                st.close();
            }
        }
        if (st != null) {
            st.close();
        }
    }
    
    private boolean checkDBExists() throws SQLException {
        if (WorldMySQLRegionManager.dbexists) {
            return true;
        }
        final Connection con = DriverManager.getConnection(WorldMySQLRegionManager.url, RedProtect.mysqlUserName, RedProtect.mysqlUserPass);
        final DatabaseMetaData meta = con.getMetaData();
        final ResultSet rs = meta.getCatalogs();
        while (rs.next()) {
            final String listOfDatabases = rs.getString("TABLE_CAT");
            if (listOfDatabases.equalsIgnoreCase(WorldMySQLRegionManager.dbname)) {
                return WorldMySQLRegionManager.dbexists = true;
            }
        }
        return false;
    }
    
    int getRegionUID(final String region) {
        int ret = -1;
        try {
            final Statement stmt = this.dbcon.createStatement();
            final ResultSet rs = stmt.executeQuery("SELECT uid from Region where name = '" + region + "'");
            int i = 0;
            while (rs.next()) {
                if (i != 0) {
                    RedProtect.logger.warning("Several columns with the same region name detected!");
                }
                ret = rs.getInt("uid");
                ++i;
            }
            stmt.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    int getMemberUID(final String member) {
        int ret = -1;
        try {
            final Statement stmt = this.dbcon.createStatement();
            final ResultSet rs = stmt.executeQuery("SELECT uid from Member where name = '" + member + "'");
            int i = 0;
            while (rs.next()) {
                if (i != 0) {
                    RedProtect.logger.warning("Several columns with the same member detected!");
                }
                ret = rs.getInt("uid");
                ++i;
            }
            stmt.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    String getMemberName(final int uid) {
        String ret = null;
        try {
            final Statement stmt = this.dbcon.createStatement();
            final ResultSet rs = stmt.executeQuery("SELECT name from Member where uid = '" + uid + "'");
            int i = 0;
            while (rs.next()) {
                if (i != 0) {
                    RedProtect.logger.warning("Several members with the same unique identifiers detected!");
                }
                ret = rs.getString("name");
                ++i;
            }
            stmt.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    int getOwnerUID(final String owner) {
        int ret = -1;
        try {
            final Statement stmt = this.dbcon.createStatement();
            final ResultSet rs = stmt.executeQuery("SELECT uid from Owner where name = '" + owner + "'");
            int i = 0;
            while (rs.next()) {
                if (i != 0) {
                    RedProtect.logger.warning("Several columns with the same owner detected!");
                }
                ret = rs.getInt("uid");
                ++i;
            }
            stmt.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    String getOwnerName(final int uid) {
        String ret = null;
        try {
            final Statement stmt = this.dbcon.createStatement();
            final ResultSet rs = stmt.executeQuery("SELECT name from Owner where uid = '" + uid + "'");
            int i = 0;
            while (rs.next()) {
                if (i != 0) {
                    RedProtect.logger.warning("Several owners with the same unique identifiers detected!");
                }
                ret = rs.getString("name");
                ++i;
            }
            stmt.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    void addOwner(final String owner) {
        if (owner == null) {
            return;
        }
        if (this.getOwnerUID(owner) != -1) {
            return;
        }
        try {
            final Statement st = this.dbcon.createStatement();
            st.executeUpdate("INSERT INTO Owner (name) values (\"" + owner + "\")");
            st.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    void addMember(final String member) {
        if (this.getMemberUID(member) != -1) {
            return;
        }
        try {
            final Statement st = this.dbcon.createStatement();
            st.executeUpdate("INSERT INTO Member (name) values (\"" + member + "\")");
            st.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void add(final Region r) {
        if (!this.regionExists(r)) {
            try {
                int uid = -1;
                Statement st = this.dbcon.createStatement();
                st.executeUpdate("INSERT INTO Region (name,creator,maxMbrX,minMbrX,maxMbrZ,minMbrZ,centerX,centerZ,pvp,chest,lever,button,door,mobs,animals) VALUES (\"" + r.getName() + "\", " + "\"" + r.getCreator() + "\", " + r.getMaxMbrX() + ", " + r.getMinMbrX() + ", " + r.getMaxMbrZ() + ", " + r.getMinMbrZ() + ", " + r.getCenterX() + ", " + r.getCenterZ() + ", " + r.getFlag(0) + ", " + r.getFlag(1) + ", " + r.getFlag(2) + ", " + r.getFlag(3) + ", " + r.getFlag(4) + ", " + r.getFlag(5) + ", " + r.getFlag(6) + ")", 1);
                final ResultSet rs = st.getGeneratedKeys();
                if (!rs.next()) {
                    RedProtect.logger.warning("Couldn't generate Primary Key for SQLManager.add(Region r). Region " + r.getName() + " will not be saved.");
                    return;
                }
                uid = rs.getInt(1);
                st.close();
                rs.close();
                if (r.getX() != null) {
                    for (int size = r.getX().length, i = 0; i < size; ++i) {
                        st = this.dbcon.createStatement();
                        st.executeUpdate("INSERT INTO Region_Points VALUES (" + uid + "," + r.getX()[i] + ", " + r.getZ()[i] + ", " + i + ")");
                        st.close();
                    }
                }
                for (final String member : r.getMembers()) {
                    this.addMember(member);
                    final int muid = this.getMemberUID(member);
                    st = this.dbcon.createStatement();
                    st.executeUpdate("INSERT INTO Region_Members (region_uid,member_uid) VALUES (" + uid + "," + muid + ")");
                    st.close();
                }
                for (final String owner : r.getOwners()) {
                    this.addOwner(owner);
                    final int ouid = this.getOwnerUID(owner);
                    st = this.dbcon.createStatement();
                    st.executeUpdate("INSERT INTO Region_Owners (region_uid,owner_uid) VALUES (" + uid + "," + ouid + ")");
                    st.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void remove(final Region r) {
        if (this.regionExists(r)) {
            final int uid = this.getRegionUID(r.getName());
            try {
                Statement st = this.dbcon.createStatement();
                st.executeUpdate("DELETE FROM Region_Points WHERE region_uid = " + uid);
                st.close();
                st = this.dbcon.createStatement();
                st.executeUpdate("DELETE FROM Region_Owners WHERE region_uid = " + uid);
                st.close();
                st = this.dbcon.createStatement();
                st.executeUpdate("DELETE FROM Region_Members WHERE region_uid = " + uid);
                st.close();
                st = this.dbcon.createStatement();
                st.executeUpdate("DELETE FROM Region WHERE name = \"" + r.getName() + "\"");
                st.close();
                for (final String member : r.getMembers()) {
                    if (this.getTotalMemberRegionSize(member) == 0) {
                        final Statement rst = this.dbcon.createStatement();
                        rst.executeUpdate("DELETE FROM Member WHERE name = \"" + member + "\"");
                    }
                }
                for (final String owner : r.getOwners()) {
                    if (this.getTotalOwnerRegionSize(owner) == 0) {
                        final Statement rst = this.dbcon.createStatement();
                        rst.executeUpdate("DELETE FROM Owner WHERE name = \"" + owner + "\"");
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Set<Region> getRegionsIntersecting(final int bx, final int bz) {
        final Set<Region> ret = new HashSet<Region>();
        try {
            final Statement st = this.dbcon.createStatement();
            final ResultSet rs = st.executeQuery("SELECT name FROM Region WHERE " + bx + "<=maxMbrX AND " + bx + ">=minMbrX AND " + bz + "<=maxMbrZ AND" + bz + ">=minMbrZ");
            while (rs.next()) {
                final String name = rs.getString("name");
                ret.add(this.getRegion(name));
            }
            rs.close();
            st.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        RedProtect.logger.debug("Rects intersecting " + bx + ", " + bz + ": ");
        for (final Region r : ret) {
            RedProtect.logger.debug(String.valueOf(r.getName()) + r.info());
        }
        return ret;
    }
    
    @Override
    public boolean canBuild(final Player p, final Block b) {
        final int bx = b.getX();
        final int bz = b.getZ();
        for (final Region poly : this.getRegionsIntersecting(bx, bz)) {
            if (poly.intersects(bx, bz)) {
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
        try {
            final Statement st = this.dbcon.createStatement();
            final ResultSet rs = st.executeQuery("SELECT name from Region where creator = \"" + p + "\"");
            while (rs.next()) {
                ls.add(this.getRegion(rs.getString("name")));
            }
            st.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ls;
    }
    
    @Override
    public boolean regionExists(final Block b) {
        return this.regionExists(b.getX(), b.getZ());
    }
    
    @Override
    public boolean regionExists(final int x, final int z) {
        for (final Region poly : this.getRegionsIntersecting(x, z)) {
            if (poly.intersects(x, z)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean regionExists(final String name) {
        int total = 0;
        try {
            final Statement st = this.dbcon.createStatement();
            final ResultSet rs = st.executeQuery("SELECT COUNT(*) from Region where name = \"" + name + "\"");
            if (rs.next()) {
                total = rs.getInt("COUNT(*)");
            }
            st.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return total > 0;
    }
    
    @Override
    public boolean regionExists(final Region region) {
        return this.regionExists(region.getName());
    }
    
    @Override
    public Region getRegion(final Location l) {
        final int x = new Double(l.getX() - 1.0).intValue();
        final int z = new Double(l.getZ() - 1.0).intValue();
        return this.getRegion(x, z);
    }
    
    private Region getRegion(final int x, final int z) {
        for (final Region poly : this.getRegionsIntersecting(x, z)) {
            if (poly.intersects(x, z)) {
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
    public Region getRegion(final String rname) {
        Region ret = null;
        if (!this.regionExists(rname)) {
            return null;
        }
        final int regionUID = this.getRegionUID(rname);
        final LinkedList<String> owners = new LinkedList<String>();
        final List<String> members = new ArrayList<String>();
        int maxMbrX = 0;
        int minMbrX = 0;
        int maxMbrZ = 0;
        int minMbrZ = 0;
        final boolean[] flags = new boolean[7];
        String creator = null;
        try {
            Statement st = this.dbcon.createStatement();
            final ArrayList<Integer> xa = new ArrayList<Integer>();
            final ArrayList<Integer> za = new ArrayList<Integer>();
            ResultSet rs = st.executeQuery("SELECT x, z, seq_no FROM Region_Points WHERE region_uid = '" + regionUID + "'");
            while (rs.next()) {
                final int rsx = rs.getInt("x");
                final int rsz = rs.getInt("z");
                final int rssq = rs.getInt("seq_no");
                xa.add(rssq, rsx);
                za.add(rssq, rsz);
            }
            int[] x;
            if (xa.isEmpty()) {
                x = null;
            }
            else {
                x = RPUtil.toIntArray(xa);
            }
            int[] z;
            if (za.isEmpty()) {
                z = null;
            }
            else {
                z = RPUtil.toIntArray(za);
            }
            rs.close();
            st.close();
            st = this.dbcon.createStatement();
            rs = st.executeQuery("SELECT owner_uid FROM Region_Owners WHERE region_uid = '" + regionUID + "'");
            while (rs.next()) {
                owners.add(this.getOwnerName(rs.getInt("owner_uid")));
            }
            rs.close();
            rs = st.executeQuery("SELECT member_uid FROM Region_Members WHERE region_uid = '" + regionUID + "'");
            while (rs.next()) {
                members.add(this.getMemberName(rs.getInt("member_uid")));
            }
            rs.close();
            st.close();
            st = this.dbcon.createStatement();
            rs = st.executeQuery("SELECT creator, maxMbrX, minMbrX, maxMbrZ, minMbrZ, pvp, chest, lever, button, door, mobs, animals from Region WHERE uid = '" + regionUID + "'");
            int i = 0;
            boolean regionValuesSet = false;
            while (rs.next()) {
                if (i != 0) {
                    RedProtect.logger.warning("Several columns with the same region name detected! (getRegion.1)");
                }
                creator = rs.getString("creator");
                maxMbrX = rs.getInt("maxMbrX");
                minMbrX = rs.getInt("minMbrX");
                maxMbrZ = rs.getInt("maxMbrZ");
                minMbrZ = rs.getInt("minMbrZ");
                flags[0] = rs.getBoolean("pvp");
                flags[1] = rs.getBoolean("chest");
                flags[2] = rs.getBoolean("lever");
                flags[3] = rs.getBoolean("button");
                flags[4] = rs.getBoolean("door");
                flags[5] = rs.getBoolean("mobs");
                flags[6] = rs.getBoolean("animals");
                regionValuesSet = true;
                ++i;
            }
            st.close();
            rs.close();
            if (regionValuesSet) {
                ret = new Region(x, z, rname, owners, members, creator, maxMbrX, minMbrX, maxMbrZ, minMbrZ, flags);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    @Override
    public int getTotalRegionSize(final String p) {
        if (p == null) {
            return 0;
        }
        int total = 0;
        try {
            final Statement st = this.dbcon.createStatement();
            final ResultSet rs = st.executeQuery("SELECT COUNT(*) from Region where creator = \"" + p + "\"");
            if (rs.next()) {
                total = rs.getInt("COUNT(*)");
            }
            st.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
    public int getTotalMemberRegionSize(final String p) {
        if (p == null) {
            return 0;
        }
        int total = 0;
        final int pid = this.getMemberUID(p);
        try {
            final Statement st = this.dbcon.createStatement();
            final ResultSet rs = st.executeQuery("SELECT COUNT(*) from Region_Members where member_uid = " + pid);
            if (rs.next()) {
                total = rs.getInt("COUNT(*)");
            }
            st.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
    public int getTotalOwnerRegionSize(final String p) {
        if (p == null) {
            return 0;
        }
        int total = 0;
        final int pid = this.getOwnerUID(p);
        try {
            final Statement st = this.dbcon.createStatement();
            final ResultSet rs = st.executeQuery("SELECT COUNT(*) from Region_Owners where owner_uid = " + pid);
            if (rs.next()) {
                total = rs.getInt("COUNT(*)");
            }
            st.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
    @Override
    public boolean isSurroundingRegion(final Region p) {
        return false;
    }
    
    @Override
    public void load() {
    }
    
    @Override
    public void save() {
    }
    
    @Override
    public Set<Region> getRegionsNear(final Player player, final int radius) {
        final int px = (int)player.getLocation().getX();
        final int pz = (int)player.getLocation().getZ();
        final Set<Region> ret = new HashSet<Region>();
        try {
            final Statement st = this.dbcon.createStatement();
            final ResultSet rs = st.executeQuery("SELECT name FROM Region where ABS(centerX-" + px + ")<" + radius + 1 + " AND ABS(centerZ-" + pz + ")<" + radius + 1);
            while (rs.next()) {
                ret.add(this.getRegion(rs.getString("name")));
            }
            st.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    @Override
    public void setFlagValue(final Region region, final int flag, final boolean value) {
        region.setFlag(flag, value);
    }
    
    @Override
    public void setRegionName(final Region rect, final String name) {
    }
    
    @Override
    public Set<Region> getPossibleIntersectingRegions(final Region r) {
        return null;
    }
}
