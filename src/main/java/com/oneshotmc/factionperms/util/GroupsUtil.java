package com.oneshotmc.factionperms.util;

import com.oneshotmc.factionperms.objects.Group;
import java.util.Comparator;
import java.util.List;

public class GroupsUtil {

    public static List<Group> sortGroups(List<Group> groupCollection, boolean highToLow){
        List<Group> result = groupCollection;
        if(highToLow)
            result.sort((left, right) -> right.getPriority() - left.getPriority());
        else
            result.sort(Comparator.comparing(Group::getPriority));

        return result;
    }

}
