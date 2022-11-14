// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

public class Flags
{
    static boolean pvp;
    static boolean chest;
    static boolean lever;
    static boolean button;
    static boolean door;
    static boolean mobs;
    static boolean passive;
    
    static {
        Flags.pvp = false;
        Flags.chest = false;
        Flags.lever = true;
        Flags.button = true;
        Flags.door = false;
        Flags.mobs = true;
        Flags.passive = false;
    }
}
