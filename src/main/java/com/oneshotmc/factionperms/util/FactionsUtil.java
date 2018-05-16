package com.oneshotmc.factionperms.util;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import org.bukkit.Location;

public class FactionsUtil {

    public static boolean isInTerritory(Location location){
        FLocation fLoc = new FLocation(location);
        return Board.getInstance().getFactionAt(fLoc).isNormal();
    }

}
