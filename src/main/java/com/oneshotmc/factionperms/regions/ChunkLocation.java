package com.oneshotmc.factionperms.regions;


import org.bukkit.Location;

public class ChunkLocation {

    private int x;
    private int z;

    public ChunkLocation(int x, int z){
        this.x = x;
        this.z = z;
    }

    public ChunkLocation(Location location){
        this.x = (int) location.getX() >> 4;
        this.z = (int) location.getZ() >> 4;
    }


    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChunkLocation chunk = (ChunkLocation) o;

        if (x != chunk.x) return false;
        return z == chunk.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + z;
        return result;
    }

    @Override
    public String toString() {
        return "ChunkLocation{" +
                "x=" + x +
                ", z=" + z +
                '}';
    }
}
