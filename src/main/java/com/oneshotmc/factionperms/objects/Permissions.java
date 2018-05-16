package com.oneshotmc.factionperms.objects;

import com.oneshotmc.factionperms.enums.InteractionType;
import org.bukkit.Material;

import java.util.*;

public abstract class Permissions {

    private final UUID id;
    private boolean placeCore = false;
    private boolean useGenBucket = false; //TODO: implement

    private List<Material> interactList = new ArrayList<>();
    private List<Material> placeList = new ArrayList<>();
    private List<Material> breakList = new ArrayList<>();

    public Permissions(UUID id){
        this.id = id;
    }

    public UUID getId(){
        return id;
    }

    public boolean canUseGenBucket() {
        return useGenBucket;
    }

    public void setUseGenBucket(boolean useGenBucket) {
        this.useGenBucket = useGenBucket;
    }

    public void setPlaceCore(boolean canPlaceCore){
        this.placeCore = canPlaceCore;
    }

    public boolean canPlaceCore(){
        return placeCore;
    }

    public List<Material> getList(InteractionType interaction){
        switch(interaction){
            case INTERACT:
                return interactList;
            case PLACE:
                return placeList;
            case BREAK:
                return breakList;
            default:
                return null;
        }
    }

    public boolean isBlacklisted(InteractionType interaction, Material material){
      return getList(interaction).contains(material);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permissions that = (Permissions) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
